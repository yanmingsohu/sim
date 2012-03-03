// CatfoOD 2012-3-1 下午02:31:38 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.parse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.ObjectAttribute;
import jym.sim.parser.Type;
import jym.sim.parser.expr.ExprException;
import jym.sim.parser.expr.Expression;
import jym.sim.util.Tools;


public class TestExpression {
	
	static Map<String, IItem> vmap;
	

	public static void main(String[] args) throws ExprException {
		Item item = new Item();
		item.init("a", 1);
		Item item2 = new Item();
		item2.init("b", 2);
		
		vmap = new HashMap<String, IItem>();
		vmap.put("a", item);
		vmap.put("b", item2);
		
		test("1", 1);
		test("1*1", 1*1);
		test("1+1+1", 1+1+1);
		test("1+2-4", 1+2-4);
		test("1+2-3+2", 1+2-3+2);
		test("1 + 2 * 3 ", 1 + 2 * 3);
		test("1*2*3", 1*2*3);
		test("10/3", 10f/3);

		test("2 + 2 - 3 + 6 / 3 *2.3+ 6 / 2 + 2", 2 + 2 - 3 + 6 / 3 * 2.3f + 6 / 2 + 2); 
		test("1 + 2 * 3 + 4 / 5 - 6 + 7 * 8 / 9", 1 + 2 * 3 + 4 / 5f - 6 + 7 * 8 / 9f);
		test("a + b * 2", 1+2*2);
		
		test("(1+2)*4 ", (1+2)*4);
		test("1+((3-4)-1*2)*5/6 + 7131231 ", 1+((3-4)-1*2)*5/6f + 7131231);
		
		test("(((1+1)+1)+1)", 4);
		
		Tools.pl("all over.");
	}

	public static void test(String str, double d) throws ExprException {
		Expression exp = new Expression(str, vmap);
		BigDecimal ret = exp.compute();
		ret = ret.setScale(5, RoundingMode.DOWN);
		
		BigDecimal jj = new BigDecimal(d);
		jj = jj.setScale(5, RoundingMode.DOWN);
		
		if ( ret.compareTo(jj) != 0 ) {
			Tools.pl(str, "\n\t::", ret, "!=", jj);
		}
	}
	
	
	static class Item implements IItem {
		private String[] name;
		private Object value;

		public String filter() {
			return String.valueOf( ObjectAttribute.get(value, name, 1) );
		}

		public String getText() {
			return name[0];
		}

		public Type getType() {
			return Type.VAR;
		}

		public void init(Object... datas) {
			if (datas.length == 1) {
				name = datas[0].toString().split("\\.");
			}
			if (datas.length == 2) {
				value = datas[1];
			}
		}

		public IItem newInstance() {
			return new Item();
		}

		public Object originalObj() {
			return value;
		}
		
	}
}
