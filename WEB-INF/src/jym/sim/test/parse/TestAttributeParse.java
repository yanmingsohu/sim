// CatfoOD 2012-2-28 上午10:17:41 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.parse;

import jym.sim.parser.ObjectAttribute;
import jym.sim.util.Tools;


public class TestAttributeParse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		A a = new A();
		Object o = ObjectAttribute.get(a, new String[] {"b", "c", "test()", "hashCode()"});
		
		Tools.pl(o);
	}
	
	static class sA {
		B b = new B();
	}

	static class A extends sA {
	}
	
	static class B {
		C c = new C();
	}
	
	public static class sC {
		public String test() {
			return "ok";
		}
	}
	
	static class C extends sC {
		String str = "hello";
	}
}
