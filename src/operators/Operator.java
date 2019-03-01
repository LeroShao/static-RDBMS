package operators;

import util.Tuple;

import java.io.PrintStream;
import java.util.List;

public abstract class Operator {

    /**
     * return the next tuple or null if no tuple left
     *
     * @return the next tuple
     */
    public abstract Tuple getNextTuple();

    /**
     * reset the operator to the start
     */
    public abstract void reset();

    protected List<String> schema;

    public abstract List<String> schema();

    /**
     * repeatedly calls getNextTuple until the next tuple is null
     * write each tuple to a suitable PrintStream
     *
     * @param ps the print stream
     */
    public void dump(PrintStream ps) {
        Tuple tp = null;
        while ((tp = getNextTuple()) != null) {
            ps.println(tp.toString());
        }
    }

}
