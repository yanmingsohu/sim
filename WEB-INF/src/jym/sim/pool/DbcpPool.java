// CatfoOD 2010-8-4 ÉÏÎç09:22:07 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class DbcpPool implements IPoolCreater {

	public DataSource create(PoolConf conf) throws IOException {
		BasicDataSource ds = new BasicDataSource();

		ds.setUsername(			conf.getUser()		);
		ds.setPassword(			conf.getPass()		);
		ds.setUrl(				conf.getUrl()		);
		ds.setDriverClassName(	conf.getDriver()	);
		
		ds.setMaxWait( 			conf.getMaxWait()	);
		ds.setMaxIdle(			conf.getMaxIdle()	);
		ds.setMaxActive(		conf.getMaxActive()	);
		
		ds.setValidationQuery(	conf.getValidation()	);
		ds.setTestWhileIdle(	conf.isHasValidation()	);
		
		return ds;
	}

}
