// CatfoOD 2010-8-20 上午08:44:23 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jym.sim.sql.compile.SqlReader;
import jym.sim.util.Tools;


public class CreateTestTable {
	
	public static void main(String[] s) throws SQLException, IOException {
		
		Connection con = TestDBPool.getDataSource().getConnection();
		SqlReader sr = new SqlReader("/test/create_test_data.sql");
		sr.execute(con);
		
		Tools.pl("数据库未出现异常");
	}
	
}
