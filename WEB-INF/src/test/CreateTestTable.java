// CatfoOD 2010-8-20 上午08:44:23 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import jym.sim.sql.ISql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.compile.ReadAndComplie;
import jym.sim.util.Tools;


@SuppressWarnings("deprecation")
public class CreateTestTable {
	
	public static void main(String[] s) throws SQLException, IOException {
		
		final ReadAndComplie sr = new ReadAndComplie("/test/create_test_data.sql");
		JdbcTemplate jdbc = TestJdbcTemplate.createJdbc();
		
		jdbc.query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				stm.execute(sr.getResultSql());
				Tools.pl("数据库未出现异常");
			}
		});
	}
	
}
