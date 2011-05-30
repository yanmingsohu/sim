// CatfoOD 2010-9-9 ÏÂÎç02:04:39 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.List;

import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.compile.SqlReader;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;


public class TestSqlReader {
	
	public static void main(String[] sa) throws Exception {
		UsedTime.start("×Ü¹²");
		JdbcTemplate jdbc = TestJdbcTemplate.createJdbc();
		
		for (int i=0; i<1; ++i) {
			SqlReader sr = new SqlReader("/test/complier.sql");
			
			sr.set("tbname", "user_bean");
			List<Object[]> result = sr.executeQuery(jdbc);

			printArr(result.get(-1));
			
			UsedTime.start("list");
			for (int j=0; j<result.size(); j++) {
				printArr(result.get(j));
			}
			
		}
		UsedTime.printAll();
	}
	
	public static void printArr(Object[] a) {
		for (int c=0; c<a.length; ++c) {
			Tools.p(a[c]+"\t");
		}
		Tools.pl();
	}
}
