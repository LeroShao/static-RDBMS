package operators;

import net.sf.jsqlparser.statement.select.OrderByElement;
import util.Helper;
import util.Tuple;

import java.util.*;

/**
 * Created by s on 2/26/19
 **/
public class SortOperator extends UnaryOperator {
    public List<Tuple> tuples;
    public List<Integer> orders;
    private int curIdx;

    public SortOperator(Operator child, List<OrderByElement> orders) {
        super(child);
        tuples = new ArrayList<>();
        this.orders = new ArrayList<>();

        Tuple tp;
        while ((tp = child.getNextTuple()) != null) {
            tuples.add(tp);
        }

        for(OrderByElement order : orders) {
            this.orders.add(Helper.getAttrIdx(order.toString(), child.schema()));
        }

        Collections.sort(tuples, new tupleComp(this.orders));
    }

    public class tupleComp implements Comparator<Tuple> {
        List<Integer> orders;
        HashSet<Integer> set;

        public tupleComp(List<Integer> orders) {
            this.orders = orders;
            set = new HashSet<>(orders);
        }

        @Override
        public int compare(Tuple tp1, Tuple tp2) {
            for(int i : orders) {
                int cmp = tp1.get(i) - tp2.get(i);
                if(cmp != 0) return cmp;
            }

            for(int i = 0; i < tp1.length(); i++) {
                if(set.contains(i)) continue;
                int cmp = tp1.get(i) - tp2.get(i);
                if(cmp != 0) return cmp;
            }
            return 0;
        }
    }

    /*
    * since whole table is buffered in memory
    * we can keep track of the next index to be read
    * */
    @Override
    public Tuple getNextTuple() {
        if(curIdx >= tuples.size())
            return null;
        return tuples.get(curIdx++);
    }

    @Override
    public void reset() {
        curIdx = 0;
    }
}
