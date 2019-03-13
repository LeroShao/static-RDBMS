package util;

import net.sf.jsqlparser.statement.Statement;
import operators.physical.*;

import java.util.ArrayList;

/**
 * parse the select statement, build operator tree
 * Created by s on 2/24/19
 **/
public class TreeBuilder {
    public Operator root;
    public QueryParser qp;

    public TreeBuilder(Statement stmt) {
        qp = new QueryParser(stmt);

        Operator curNode = new ScanOperator(qp.getTable(0));
        if(qp.getSelectCond(0) != null)
            curNode = new SelectOperator((ScanOperator) curNode, qp.getSelectCond(0));

        for (int i = 1; i < qp.froms.size(); i++) {
            Operator newOp = new ScanOperator(qp.getTable(i));
            if(qp.getSelectCond(i) != null)
                newOp = new SelectOperator((ScanOperator) newOp, qp.getSelectCond(i));
            curNode = new TNLJ(curNode, newOp, qp.getJoinCond(i));
        }

        if(qp.selectItems != null)
            curNode = new ProjectOperator(curNode, qp.selectItems);
        if(qp.orderByElements != null)
            curNode = new InMemSortOperator(curNode, qp.orderByElements);
        if(qp.distinct != null) {
            if(qp.orderByElements == null)
                curNode = new InMemSortOperator(curNode, new ArrayList<>());

            curNode = new DuplicateEliminationOperator(curNode);
        }

        root = curNode;
    }
}
