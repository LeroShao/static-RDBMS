package operators;

import util.Table;
import util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class ScanOperator extends Operator{	
	Table table;
	
	/**
	 * construct a scan operator for a table
	 */
	public ScanOperator(Table table) {
		this.table = table;
		schema = new ArrayList<String>();
		if (table == null || table.schema == null){
			System.out.println("table / schema empty!");
		}
		for (String col : table.schema) {
			schema.add(table.name + '.' + col);
		}
	}

	@Override
	public Tuple getNextTuple() {
		return table.nextTuple();
	}

	@Override
	public void reset() {
		table.reset();
	}

	@Override
	public List<String> schema() {
		return schema;
	}
}