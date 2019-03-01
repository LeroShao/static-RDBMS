package util;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.*;

public class SelState {
	/**
	 * selects e.g., A, B, C in "SELECT A, B, C"
	 */
	public Select sel;
	public PlainSelect ps;
	public FromItem from;
	public Distinct distinct;
	public List<Join> joins;
	public List<SelectItem> selects;
	public List<OrderByElement> orderBys;
	public Expression where;
	
//	public List<String> froms = new ArrayList<String>();
//	public List<Expression> ands = null;
//	public HashMap<String, List<Expression>> selConds = null, joinConds = null;
//	public HashMap<String, Expression> fnSelCond = null, fnJoinCond = null;
//	
//	public Operator root = null;
}
