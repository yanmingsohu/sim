// CatfoOD 2012-3-1 下午02:31:38 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.parse;

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
//		
//		test("1");
//		test("1*1");
//		test("1+1+1");
//		test("1+2-4");
//		test("1+2-3+2");
//		test("1 + 2 * 3 ");
//		test("2 + 2 - 3 + 6 / 3 *2.3+ 6 / 2 + 2"); 
//		test("1*2*3");
//		test("10/3");
//		
//		test("a + b * 2");
		
		test("1+(1+2)*3");
		test("1+2*(3/(4 -1) +3)");
	}

	public static void test(String str) throws ExprException {
		Expression exp = new Expression(str, vmap);
		Tools.pl(str, "\t\t::", exp.compute());
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
