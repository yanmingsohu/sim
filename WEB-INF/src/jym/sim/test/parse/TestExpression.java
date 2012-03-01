// CatfoOD 2012-3-1 下午02:31:38 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.parse;

import java.util.HashMap;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.expr.ExprException;
import jym.sim.parser.expr.Expression;
import jym.sim.util.Tools;


public class TestExpression {
	
	static Map<String, IItem> vmap;
	

	public static void main(String[] args) throws ExprException {
		vmap = new HashMap<String, IItem>();
		
//		test("1");
//		test("1+2");
//		test("1+2+3");
//		test("1+2*3");
//		test("1+2*3 ");
//		test(" 1 + 2 * 3 ");
		
		test("1*1");
	}

	public static void test(String str) throws ExprException {
		Expression exp = new Expression(str, vmap);
		Tools.pl(str, "::", exp.compute());
	}
}
