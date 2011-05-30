// CatfoOD 2010-9-9 下午03:40:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import jym.sim.sql.IQuery;
import jym.sim.sql.IResultSql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.ResultSetList;
import jym.sim.util.Tools;


/**
 * 该类的对象<b>应该被缓存</b>,以便提高效率
 */
public class SqlReader implements ResultSetList.IGetBean<Object[]> {
	
	private Exception lastErr;
	private Info inf;
	private Class<?> sqlclz;
	private Object instance;
	
	
	/**
	 * 初始化sql读取器<br>
	 * <br>
	 * 主文件名必须符合类名的命名规范,文件中的${..}将被参数化<br>
	 * 参数化的变量通过方法set()来设置值,变量名必须符合Java属性名命名规范<br>
	 * sql文件必须使用<b>UTF-8</b>编码<br>,并且是单条语句
	 * 
	 * @param filename - 从classpath中读取sql文件的完整路径,<br>
	 * 如<code>/jym/sim/sql/complie/x.sql</code>
	 */
	public SqlReader(String filename) throws IOException {
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
			long lm = (Long) f.get(instance);
			
			if (inf.lastModified()!=lm) {
				compileSql();
				if (!loadClass()) {
					Tools.pl(inf.getSqlFileName() + " 已经修改并重新编译,但重新加载时失败: " + lastErr);
				}
			} else {
				inf.clear();
			}
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
			Field f = sqlclz.getField(FileParse.VAR_PREFIX + name);
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
	 * 执行指定文件中的sql语句,该文件中的变量应该已经用set()替换
	 * 返回的Statement需要自行关闭
	 * 
	 * @param conn - 与数据库的连接,返回之后需自行关闭
	 * @return java.sql.Statement
	 * @throws SQLException
	 */
	public Statement execute(Connection conn) throws SQLException {
		Statement s = conn.createStatement();
		s.execute(instance.toString());
		return s;
	}
	
	/**
	 * 在IQuery中执行sql文件中的sql语句,语句的类型只能为DDL,DML
	 * @param iq - 一般传递JdbcTemplate
	 * @return 返回语句影响的行数，如果返回-1则说明sql语句不符合规定
	 */
	public int executeUpdate(IQuery iq) {
		Object r = iq.query(new IResultSql() {
			public Object exe(Statement stm) throws Throwable {
				return stm.executeUpdate(instance.toString());
			}
		});
		
		if (r!=null) {
			return (Integer)r;
		} else {
			return -1;
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
}
