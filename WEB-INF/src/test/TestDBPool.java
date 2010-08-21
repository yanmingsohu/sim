// CatfoOD 2010-8-20 ����08:29:10 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;

import javax.sql.DataSource;

import jym.sim.pool.PoolFactory;

/**
 * ��������Դ������ͬʱ���Եõ�����Դ
 */
public class TestDBPool {
	
	private static PoolFactory pool;
	
	public static void main(String s[]) throws IOException {
		getDataSource();
	}
	
	public static DataSource getDataSource() throws IOException {
		if (pool==null) {
			pool = new PoolFactory("/test/datasource.conf");
		}
		return pool.getDataSource();
	}
}