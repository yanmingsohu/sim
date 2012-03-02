// CatfoOD 2010-4-16 下午02:43:57 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.sql;

import java.io.IOException;
import java.sql.Statement;

import jym.sim.sql.ISql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.util.Tools;

public class TestJdbcTemplate {
	
	public static void main(String[] s) throws Exception {
		JdbcTemplate jt = createJdbc();
		
		jt.query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				Tools.pl( stm.execute("select * from ba_brongth") );
			}
		});
	}
	
	public static JdbcTemplate createJdbc() throws IOException {
		return new JdbcTemplate(TestDBPool.getDataSource());
	}
	
}