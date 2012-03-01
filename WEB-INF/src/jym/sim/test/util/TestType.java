// CatfoOD 2011-7-4 下午02:45:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.util;

import jym.sim.util.Tools;


public class TestType {

	public static void main(String[] args) {
		Object o = 1;
		boolean c1 = Integer.class.isAssignableFrom(o.getClass());
		boolean c2 = String.class.isAssignableFrom(o.getClass());
		Tools.pl(c1, c2);
	}

}
