package operators;

import net.sf.jsqlparser.expression.Expression;
import util.Helper;
import util.Tuple;
import visitors.JoinExpVisitor;

import java.util.ArrayList;

/**
 * Created by s on 2/23/19
 **/
public class JoinOperator extends BinaryOperator{
    public JoinExpVisitor joinExpVisitor;
    public Tuple curLeft;
    public Tuple curRight;
    public Expression expression;

    public JoinOperator(Operator left, Operator right, Expression expression) {
        super(left, right);
        joinExpVisitor = new JoinExpVisitor(left.schema(), right.schema());
        curLeft = left.getNextTuple();
        curRight = right.getNextTuple();
        this.expression = expression;
        schema = new ArrayList<>();

        for (String col : left.schema()) {
            schema.add(col);
        }
        for(String col : right.schema) {
            schema.add(col);
        }
    }

    @Override
    public Tuple getNextTuple() {
        Tuple res = null;
        while (curLeft != null && curRight != null) {
            if(expression == null)
                res = joinTuple(curLeft, curRight);
            else if(Helper.getJoinRes(curLeft, curRight, expression, joinExpVisitor))
                res = joinTuple(curLeft, curRight);

            next();
            if(res != null) return res;
        }
        return null;
    }

    private void next() {
        if(curLeft == null) {
            return;
        }
        if(curRight != null) {
            curRight = right.getNextTuple();
        }

        if(curRight == null) {
            curLeft = left.getNextTuple();
            right.reset();
            curRight = right.getNextTuple();
        }
    }

    private Tuple joinTuple(Tuple tuple1, Tuple tuple2) {
        int[] cols = new int[tuple1.length() + tuple2.length()];
        int i = 0;

        for(int col : tuple1.cols)
            cols[i++] = col;
        for(int col : tuple2.cols)
            cols[i++] = col;

        return new Tuple(cols);
    }

}
