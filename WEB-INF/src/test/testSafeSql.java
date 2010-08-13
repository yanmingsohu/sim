// CatfoOD 2010-8-10 обнГ01:50:19 yanming-sohu@sohu.com/@qq.com

package test;

import jym.sim.sql.SafeSql;
import jym.sim.util.Tools;

public class testSafeSql {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String a = "fdsafsaf'fdsaffn";
		
		Tools.pl(SafeSql.transformValue(a));
	}

}
