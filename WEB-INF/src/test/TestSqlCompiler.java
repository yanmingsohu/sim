// CatfoOD 2010-9-9 ÏÂÎç02:04:39 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.Iterator;
import java.util.List;

import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.compile.SqlReader;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;


public class TestSqlCompiler extends com.sun.tools.javac.Main {
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] sa) throws Exception {
		JdbcTemplate jdbc = testJdbcTemplate.createJdbc();
		UsedTime.start();
		
		for (int i=0; i<1; ++i) {
			SqlReader sr = new SqlReader("/test/complier.sql");
			
			sr.set("tbname", "ba_brongth");
			List<Object> result = sr.execute(jdbc);
			
			Iterator<Object> it = result.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof List) {
					List<Object[]> rows = (List<Object[]>) o;
					for (int r = 0; r<rows.size(); ++r) {
						Object[] row = rows.get(r);
						for (int c=0; c<row.length; ++c) {
							Tools.p(row[c]+"\t");
						}
						Tools.pl();
					}
				}
			}
		}
		
		UsedTime.endAndPrint();
	}
}
