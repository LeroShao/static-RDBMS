package operators.physical;

import java.util.List;

/**
 * Created by s on 2/23/19
 **/
public abstract class BinaryOperator extends Operator {
    public Operator left;
    public Operator right;

    public BinaryOperator(Operator left, Operator right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void reset() {
        left.reset();
        right.reset();
    }

    /*
    * binary operator generates a new concatenated schema
    * */
    @Override
    public List<String> schema() {
        return schema;
    }
}
