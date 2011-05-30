// CatfoOD 2010-8-4 上午08:58:28 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.sim.util.ResourceLoader;
import jym.sim.util.Tools;

/**
 * 数据池工厂,应用一般只需要持有此类的一个对象
 */
public class PoolFactory {
	
	private DataSource _ds;
	
	
	/**
	 * 创建连接池工厂,配置从文件classpath/[fromfile]中读取
	 * 
	 * @param fromfile 配置文件的路径,配置文件的格式参考 test/datasource.conf
	 * @throws IOException - 配置文件读取错误,连接数据库错误
	 */
	public PoolFactory(String fromfile) throws IOException {
		Properties prop = new Properties();
		readFromFile(prop, fromfile);
		PoolConf pc = createConfig(prop);
		
		
		if (!Tools.isNull(pc.getJndiName())) {
			try {
				_ds = createFromJndi(pc);
				success("使用服务器在jndi中提供的数据源");
			} catch (Exception e) {
				Tools.pl(e);
			}
		}
		
		if (_ds==null) {
			_ds = createFromLocal(pc);
			success("创建了独立的数据源");
		}
	}
	
	private PoolConf createConfig(Properties prop) {
		PoolConf conf = new PoolConf();
		
		conf.setUser(				prop.getProperty("username")		);
		conf.setPass(				prop.getProperty("password")		);
		conf.setUrl(				prop.getProperty("url")				);
		conf.setDriver(				prop.getProperty("driverClassName")	);
		
		conf.setMaxWait( 	toInt(	prop.getProperty("maxWait")		)	);
		conf.setMaxIdle(	toInt(	prop.getProperty("maxIdle")		)	);
		conf.setMaxActive(	toInt(	prop.getProperty("maxActive")	)	);
		
		conf.setValidation(			prop.getProperty("validation")		);
		conf.setJndiName(			prop.getProperty("poolJndiName")	);
		conf.setPoolClassName(		prop.getProperty("poolClassName")	);
		
		return conf;
	}
	
	private DataSource createFromLocal(PoolConf conf) throws IOException {
		String cname = conf.getPoolClassName();
		
		try {
			Class<?> clazz = Class.forName(cname);
			Object o = clazz.newInstance();
			if (!(o instanceof IPoolCreater)) {
				error("指定的类没有实现 IPoolCreater", conf);
			}
			IPoolCreater creater = (IPoolCreater) o;
			return creater.create(conf);
			
		} catch (ClassNotFoundException e) {
			error("指定的类无效", conf);
			
		} catch (InstantiationException e) {
			error("指定的类初始化失败", conf);
			
		} catch (IllegalAccessException e) {
			error("指定的类的构造方法不可访问", conf);
		}
		
		return null;
	}
	
	private void error(String msg, PoolConf conf) throws IOException {
		throw new IOException("创建连接池失败(" + conf.getPoolClassName() + ")");
	}
	
	private DataSource createFromJndi(PoolConf conf) throws IOException, NamingException {
		String name = conf.getJndiName();
        
		InitialContext cxt = new InitialContext();

		DataSource ds = (DataSource) cxt.lookup(name);
		if (ds == null) {
			throw new NamingException("DataSource为空,poolname指定的name无效:" + name);
		}

		return ds;
	}
	
	private void readFromFile(Properties conf, String fromfile) throws IOException {
		InputStream in = ResourceLoader.getInputStream(fromfile);
		try {
			conf.load(in);
		} finally {
			if (in!=null) in.close();
		}
	}

	public DataSource getDataSource() {
		return _ds;
	}
	
	private int toInt(String i) {
		return Integer.parseInt(i);
	}
	
	private void success(String msg) {
		Tools.pl("PoolFactory : 数据源连接池创建完成," + msg);
		
		try {
			_ds.setLogWriter(new LogStream().getOut());
		} catch (SQLException e) {
			Tools.pl("数据源连接日志初始化失败");
			Tools.plerr(e);
		}
	}
	
	
	private class LogStream extends OutputStream {
		private String NAME = ".:DataPoolMsg: ";
		private PrintWriter out;
		private byte[] buff = new byte[512];
		private int point = 0;
		
		private LogStream() {
			out = new PrintWriter(LogStream.this);
		}

		@Override
		public void write(int b) throws IOException {
			if (b=='\n') {
				flush();
			}
			else {
				buff[point++] = (byte)b;
				if (point >= buff.length) expand();
			}
		}
		
		private void expand() {
			buff = Tools.copyOf(buff, buff.length * 2);
		}
		
		public PrintWriter getOut() {
			return out;
		}
		
		@Override
		public void close() throws IOException {
			flush();
		}

		@Override
		public void flush() throws IOException {
			Tools.pl(NAME + new String(buff, 0, point));
			point = 0;
		}
	}
}
