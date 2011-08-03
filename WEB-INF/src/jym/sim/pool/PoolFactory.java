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
	
	private static final long RETRY_TIME = 8000;
	private static final char BEEP = 7;
	
	private boolean waitSuccess;
	private DataSource _ds;
	
	
	/**
	 * 不管数据库是否能连接,立即返回
	 * @param fromfile
	 * @throws IOException
	 */
	public PoolFactory(String fromfile) throws IOException {
		this(fromfile, false);
	}
	
	/**
	 * 创建连接池工厂,配置从文件classpath/[fromfile]中读取
	 * 
	 * @param fromfile 配置文件的路径,配置文件的格式参考 test/datasource.conf
	 * @param waitSuccess 是否阻塞操作直到数据库正常使用才返回, 默认false
	 * @throws IOException - 配置文件读取错误,连接数据库错误
	 */
	public PoolFactory(String fromfile, boolean waitSuccess) throws IOException {
		this.waitSuccess = waitSuccess;
		
		Properties prop = new Properties();
		readFromFile(prop, fromfile);
		PoolConf pc = createConfig(prop);
		
		try {
			_ds = createFromJndi(pc);
			success("使用服务器在jndi中提供的数据源, jndi: [" + pc.getJndiName() + "]");
		} catch (Exception e) {
			_ds = createFromLocal(pc);
			success("创建了独立的数据源, url: [" + pc.getUrl() + "]");	
		}
	}
	
	public DataSource getDataSource() {
		return _ds;
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
		} catch (Exception e) {
			error("错误", conf);
		}
		
		return null;
	}
	
	private DataSource createFromJndi(PoolConf conf) throws IOException, NamingException {
		String name = conf.getJndiName();
		if (Tools.isNull(name)) {
			throw new NamingException("null的jndi name");
		}
        
		InitialContext cxt = new InitialContext();

		DataSource ds = (DataSource) cxt.lookup(name);
		if (ds == null) {
			throw new NamingException("DataSource为空, poolname指定的name无效: " + name);
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
	
	private void success(String msg) {
		
		while (waitSuccess) {
			try {
				_ds.setLogWriter(new LogStream().getOut());
				Tools.pl("PoolFactory : 数据源连接池创建完成," + msg);
				break;
				
			} catch (SQLException e) {
				Tools.pl("PoolFactory : 数据源连接错误, 始化失败:" + e.getMessage());
				
				if (waitSuccess) {
					Tools.pl("PoolFactory : 重试..." + BEEP + BEEP);
					
					try {
						Thread.sleep(RETRY_TIME);
					} catch (InterruptedException e1) {}
				}
			}
		}
	}
	
	private int toInt(String i) {
		return Integer.parseInt(i);
	}
	
	private void error(String msg, PoolConf conf) throws IOException {
		throw new IOException("创建连接池失败(" + conf.getPoolClassName() + ")");
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
