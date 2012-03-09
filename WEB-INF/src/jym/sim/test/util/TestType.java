// CatfoOD 2011-7-4 下午02:45:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.util;

import jym.sim.util.Tools;


public class TestType {

	
	public static void main(String[] args) {
		Object o = 1;
		test(Integer.class, o);
		test(String.class, o);
	}

	public static void test(Class<?> clazz, Object test) {
		Tools.pl(test, "is", clazz, "=", clazz.isAssignableFrom(test.getClass()));
	}
	
}
