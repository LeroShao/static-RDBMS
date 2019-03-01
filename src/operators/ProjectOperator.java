package operators;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import util.Catlog;
import util.Helper;
import util.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s on 2/21/19
 **/
public class ProjectOperator extends UnaryOperator {
    /*
     * child could be either ScanOperator || SelectOperator, depending on whether the SQL query has a WHERE clause
     *
     * get the projection columns from the selectItems field of PlainSelect
     * selectItems is a list of SelectItems
     * where each one is either AllColumns (for a SELECT * ) or a SelectExpressionItem
     * Expression in a SelectExpressionItem will always be a Column.
     * @param operator the child
     * @param selectItems the list of selected columns
     * */
    public ProjectOperator(Operator operator, List<SelectItem> selectItems) {
        super(operator);
        List<String> childSchema = child.schema();
        schema = new ArrayList<>();
        for (SelectItem si : selectItems) {
            if (si instanceof AllColumns) {
                schema = childSchema;
                return;
            }
            Column col = (Column) ((SelectExpressionItem) si).getExpression();
            String tableName = col.getTable().getName();
            Catlog.getCatlog();
            if(Catlog.schemas.containsKey(tableName)) {
                schema.add(tableName + "." + col.getColumnName());
            } else {
                String colName = col.getColumnName();
                for (String tabCol : childSchema) {
                    if (Helper.getColName(tabCol).equals(colName)) {
                        schema.add(tabCol);
                    }
                }
            }
        }
    }

    @Override
    public Tuple getNextTuple() {
        Tuple tuple = child.getNextTuple();
        if (tuple == null) return null;

        int[] cols = new int[schema.size()];
        int i = 0;
        for (String attr : schema) {
            Long val = Helper.getAttrVal(tuple, attr, child.schema());
            cols[i++] = val.intValue();
        }
        return new Tuple(cols);
    }
}