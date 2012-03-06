// CatfoOD 2010-9-9 下午03:40:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.ResultSetList;
import jym.sim.sql.SqlReaderFactory;
import jym.sim.sql.reader.ISqlReader;
import jym.sim.util.Tools;


/**
 * 读取sql并编译为class文件<br/>
 * 该类的对象<b>可以被缓存(单线程)</b>,以便提高效率<br/>
 * 线程不安全
 * @deprecated 因为依赖JDK所以不推荐使用, 该类将不对新的功能提供支持, 用jym.sim.reader.SqlLink替换
 */
public class ReadAndComplie implements ISqlReader, ResultSetList.IGetBean<Object[]> {
	
	private Exception lastErr;
	private Class<?> sqlclz;
	private Object instance;
	private Info inf;
	
	
	/**
	 * @see SqlReaderFactory#get(String)
	 */
	public ReadAndComplie(String filename) throws IOException {
		inf = new Info(filename);
		URL sqlfile = getClass().getResource(filename);

		if (loadClass()) {
			if (sqlfile!=null) {
				inf.setUrl(sqlfile);
				checkLastModify();
			}
			// else {加载类成功，但sql文件不存在，则认为不需要更新}
		} else {
			if (sqlfile==null) {
				throw new IOException("找不到文件: " + filename);
			}
			inf.setUrl(sqlfile);
			
			compileSql();

			if (!loadClass()) {
				throw new IOException("初始化类文件[" + inf.getFullClassName() + "]失败" + lastErr);
			}
		}
	}
	
	private void checkLastModify() throws IOException {
		try {
			Field f = sqlclz.getField(Compiler.MODIFY_FIELD);
			final long lm = (Long) f.get(instance);
			final long m = inf.lastModified();
			
			if (m > 0) {
				if (m != lm) {
					compileSql();
					if (!loadClass()) {
						Tools.pl(inf.getSqlFileName() + " 已经修改并重新编译,但重新加载时失败: " + lastErr);
					}
				} else {
					inf.clear();
				}
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("读取最后修改日期失败: " + e);
		}
	}
	
	private void compileSql() throws IOException {
		Compiler c = new Compiler(inf);
		
		if (c.start()) {
			inf.clear();
		} else {
			throw new IOException(inf.getSqlFileName() + "转换为java文件后编译失败" + lastErr);
		}
	}
	
	/**
	 * 设置sql文件中的${..}变量的值,变量名必须符合Java属性命名规范
	 * 
	 * @param name - 被替换的变量名
	 * @param value - 替换后的值
	 * @throws NoSuchFieldException - 找不到name指定的变量
	 */
	public void set(String name, Object value) throws 
			NoSuchFieldException {
		
		try {
			Field f = sqlclz.getField(ComplieFactory.VAR_PREFIX + name);
			f.set(instance, value);
			
		} catch(SecurityException se) {
			lastErr = se;
		} catch(IllegalAccessException ae) {
			lastErr = ae;
		} catch(IllegalArgumentException ae) {
			lastErr = ae;
		}
	}
	
	/**
	 * 在IQuery中执行sql文件中的sql语句,该语句只能是单条查询语句<br><br>
	 * 返回的List中每行元素是一个数组,数组的长度等于结果集的列长,<br>
	 * List<b>负值索引</b>存放表头名字在数组中的位置,之后的元素是结果集的行数据<br>
	 * 
	 * @param jdbc
	 * @return List<Object[]> 返回查询的结果集,如果出现错误则返回null
	 */
	public List<Object[]> executeQuery(JdbcTemplate jdbc) {
		List<Object[]> list = null;
		try {
			list = new ResultSetList<Object[]>(instance.toString(), jdbc, this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 返回拼装好的sql,其中的变量使用set()提前设置好
	 */
	public String getResultSql() {
		return instance.toString();
	}
	
	/**
	 * 把sql打印到控制台
	 */
	public void showSql() {
		Tools.pl(instance);
	}

	private boolean loadClass() {
		try {
			DynClassLoader loader = new DynClassLoader();
			sqlclz = loader.reLoadClass(inf.getFullClassName());
			instance = sqlclz.newInstance();
			return true;
		} catch (Exception e) {
			lastErr = e;
			return false;
		}
	}

	public Object[] fromRowData(String[] columnNames, ResultSet rs, int rowNum)
			throws Exception {
		
		Object[] r = new Object[columnNames.length];
		if (rowNum>=0) {
			for (int i=1; i<=columnNames.length; ++i) {
				//XXX 不同的实现下，不同的getXxx()方法效率差异很大
				r[i-1] = rs.getObject(i);
			}
		} else {
			for (int i=0; i<columnNames.length; ++i) {
				r[i] = columnNames[i];
			}
		}
		return r;
	}

	/** 编译的文件不需要锁定 */
	public void lockFile(long protectKey) {
	}
}
