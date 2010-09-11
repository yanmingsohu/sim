// CatfoOD 2010-9-9 обнГ02:04:39 yanming-sohu@sohu.com/@qq.com

package test;

import jym.sim.sql.compile.SqlReader;
import jym.sim.util.UsedTime;


public class TestSqlCompiler extends com.sun.tools.javac.Main {
	
	static String name = "testjavac";
	
	public static void main(String[] sa) throws Exception {
		UsedTime.start();
		
		for (int i=0; i<1; ++i) {
			SqlReader sr = new SqlReader("/test/complier.sql");
			
			sr.set("where", "xxx");
			sr.set("from", "yyy");
			sr.showSql();
		}
		
		UsedTime.endAndPrint();
	}
}
