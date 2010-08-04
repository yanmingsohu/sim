// CatfoOD 2010-8-4 ÉÏÎç10:23:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import java.io.IOException;

import javax.sql.DataSource;

import snaq.db.DBPoolDataSource;

public class SnaqDBPool implements IPoolCreater {

	public DataSource create(PoolConf conf) throws IOException {
		DBPoolDataSource pool = new DBPoolDataSource();
		
		pool.setUrl(				conf.getUrl()		);
		pool.setDriverClassName(	conf.getDriver()	);
		pool.setUser(				conf.getUser()		);
		pool.setPassword(			conf.getPass()		);
		
		pool.setMaxPool(			conf.getMaxActive()	);
		pool.setMinPool(			conf.getMaxWait()	);
		
		if (conf.isHasValidation()) {
			pool.setValidationQuery(conf.getValidation());
		}

		return pool;
	}

}
