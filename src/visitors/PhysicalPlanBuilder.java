package visitors;

import operators.logic.*;
import operators.physical.*;

/**
 * Created by s on 3/10/19
 **/
public class PhysicalPlanBuilder {
    private Operator pop;

    public Operator getPhysicalOperator() {
        return pop;
    }

    public void getChild(LogicUnaryOperator lop) {
        pop = null;
        lop.child.accept(this);
    }

    public Operator[] getLR(LogicBinaryOperator lop) {
        Operator[] LR = new Operator[2];

        pop = null;
        lop.left.accept(this);
        LR[0] = pop;

        pop = null;
        lop.right.accept(this);
        LR[1] = pop;

        return LR;
    }

    public void visit(LogicScanOperator lop) {
        pop = new ScanOperator(lop.table);
    }

    public void visit(LogicSelectOperator lop) {
        getChild(lop);
        pop = new SelectOperator((ScanOperator) pop, lop.expression);
    }

    public void visit(LogicProjectOperator lop) {
        getChild(lop);
        pop = new ProjectOperator(pop, lop.selectItems);
    }

    public void visit(LogicSortOperator lop) {
        getChild(lop);
        // TODO determine which sort method to use

    }

    public void visit(LogicDuplicateEliminationOperator lop) {
        getChild(lop);
        pop = new DuplicateEliminationOperator(pop);
    }

    public void visit(LogicJoinOperator lop) {
        Operator[] LR = getLR(lop);
        // TODO determine which join method to use
    }
}
