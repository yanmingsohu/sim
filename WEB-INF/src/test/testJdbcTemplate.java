// CatfoOD 2010-4-16 ÏÂÎç02:43:57 yanming-sohu@sohu.com/@qq.com

package test;

import java.sql.Statement;

import javax.sql.DataSource;

import jym.sim.sql.ISql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.util.Tools;

public class testJdbcTemplate {

	final private static String url = "jdbc:oracle:thin:@xxx:xxx";
	final private static String user = "";
	final private static String pwd = "";
	
	
	public static void main(String[] s) throws Exception {
		Class<?> dsclazz = Class.forName("oracle.jdbc.pool.OracleDataSource");
		
		DataSource ds = (DataSource) dsclazz.newInstance();
		
		dsclazz.getMethod("setURL", String.class).invoke(ds, url);
		dsclazz.getMethod("setUser", String.class).invoke(ds, user);
		dsclazz.getMethod("setPassword", String.class).invoke(ds, pwd);
		
		JdbcTemplate jt = new JdbcTemplate(ds);
		
		jt.query(new ISql() {

			public void exe(Statement stm) throws Throwable {
				Tools.pl( stm.execute("select * from ba_brongth") );
			}
			
		});
	}
	
}
