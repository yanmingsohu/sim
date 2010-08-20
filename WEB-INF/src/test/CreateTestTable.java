// CatfoOD 2010-8-20 上午08:44:23 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import jym.sim.util.Tools;


public class CreateTestTable {
	
	@SuppressWarnings("unused")
	public static void main(String[] s) throws SQLException, IOException {
		
		String ct = "CREATE TABLE ba_brongth (brongthname varchar(50), brongthid varchar(50), brongthsn varchar(50))";
		
		String dt = "DROP TABLE ba_brongth";
		
		Connection con = TestDBPool.getDataSource().getConnection();
		Statement st = con.createStatement();
		
		st.executeUpdate(ct);
		
		con.close();
		
		Tools.pl("数据库未出现异常");
	}
	
}
