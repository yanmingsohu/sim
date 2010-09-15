// CatfoOD 2010-9-9 下午03:40:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jym.sim.sql.IQuery;
import jym.sim.sql.ISql;
import jym.sim.util.Tools;


/**
 * 该类的对象<b>应该被缓存</b>,以便提高效率
 */
public class SqlReader {
	
	private Exception lastErr;
	private Info inf;
	private Class<?> sqlclz;
	private Object instance;
	
	
	/**
	 * 初始化sql读取器<br>
	 * <br>
	 * 主文件名必须符合类名的命名规范,文件中的${..}将被参数化<br>
	 * 参数化的变量通过方法set()来设置值,变量名必须符合Java属性名命名规范<br>
	 * sql文件必须使用<b>UTF-8</b>编码
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
	 * 在IQuery中执行sql文件中的sql语句,每条sql的结果存放在List元素中<br>
	 * 如果是更新语句,则元素是一个Integer类型,否则该元素是另一个List,<br>
	 * 该List中每个元素是一个数组,数组的长度等于结果集的列长,第0个元素<br>
	 * 存放表头的名字,之后的元素是结果集的行数据<br>
	 * <br>
	 * 通常,调用此函数前已经清楚sql的类型,并能做正确的转换<br>
	 * 
	 * @param iq - 一般传递JdbcTemplate
	 * @return List<Integer or List<Object[]>> 返回查询的结果集
	 */
	public List<Object> execute(IQuery iq) {
		final List<Object> rel = new ArrayList<Object>();
		
		iq.query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				do {
					boolean isSet = stm.execute(instance.toString());
					int uc = stm.getUpdateCount();
					
					if (isSet) {
						ResultSet rs = stm.getResultSet();
						ResultSetMetaData smd = rs.getMetaData();
						int cc = smd.getColumnCount();
						
						List<Object[]> resultSet = new ArrayList<Object[]>();
						Object[] row = new Object[cc];
						
						for (int i=0; i<cc; ++i) {
							row[i] = smd.getColumnLabel(i+1);
						}
						resultSet.add(row);
						
						while (rs.next()) {
							row = new Object[cc];
							for (int i=0; i<cc; ++i) {
								row[i] = rs.getObject(i+1);
							}
							resultSet.add(row);
						}
						rel.add(resultSet);
					}
					else if (uc>0) {
						rel.add(uc);
					}
				} while(stm.getMoreResults());
			}
		});
		
		return rel;
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
}
