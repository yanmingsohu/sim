// CatfoOD 2010-8-20 上午08:29:10 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;

import javax.sql.DataSource;

import jym.sim.pool.PoolFactory;

/**
 * 测试数据源工厂，同时可以得到数据源
 */
public class TestDBPool {
	
	private static PoolFactory pool;
	private static PoolFactory mysql;
	
	public static void main(String s[]) throws IOException {
		getDataSource();
	}
	
	public static DataSource getDataSource() throws IOException {
		if (pool==null) {
			pool = new PoolFactory("/test/datasource.conf");
		}
		return pool.getDataSource();
	}
	
	public static DataSource mySqlSource() throws IOException {
		if (mysql==null) {
			mysql = new PoolFactory("/test/mysql_datasource.conf");
		}
		return mysql.getDataSource();
	}
}
