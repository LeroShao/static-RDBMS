package operators.physical;

import util.Tuple;

import java.util.List;

/**
 * Created by s on 3/11/19
 **/
public class ExternalSortOperator extends SortOperator{
    // TODO
    public ExternalSortOperator(Operator child, List<?> orders) {
        super(child, orders);

    }

    @Override
    public Tuple getNextTuple() {
        return null;
    }

    @Override
    public void reset(long index) {

    }
}
