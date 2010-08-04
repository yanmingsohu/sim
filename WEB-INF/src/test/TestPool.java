// CatfoOD 2010-8-4 ионГ09:12:34 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jym.sim.pool.PoolFactory;

public class TestPool {

	public static void main(String[] args) throws IOException, SQLException {
		PoolFactory pool = new PoolFactory("/test/datasource.conf");
		
		Connection c = pool.getDataSource().getConnection();
		c.createStatement().execute("select * from xxx");
	}

}
