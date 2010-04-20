// CatfoOD 2010-4-16 ÏÂÎç02:43:57 yanming-sohu@sohu.com/@qq.com

package test;

import java.sql.Statement;

import javax.sql.DataSource;

import jym.base.sql.ISql;
import jym.base.sql.JdbcTemplate;
import jym.base.util.Tools;

public class testJdbcTemplate {

	final private static String url = "jdbc:oracle:thin:@192.168.0.68:1521:RMCSH";
	final private static String user = "LWBS_ROOT";
	final private static String pwd = "dl20100325";
	
	
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
