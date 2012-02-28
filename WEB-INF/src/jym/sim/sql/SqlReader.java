// CatfoOD 2012-2-27 下午01:19:08 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.io.IOException;

import jym.sim.sql.compile.ReadAndComplie;
import jym.sim.sql.reader.ISqlReader;
import jym.sim.sql.reader.SqlLink;


/**
 * 尝试把.sql文件编译为.class,
 * 如果失败则使用通常拼接字符串的方式
 * 
 * @deprecated 因为依赖JDK所以不推荐使用; ReadAndComplie 与 SqlLink 实现不一致
 */
public final class SqlReader {
	
	private static final boolean useComple;

	static {
		Class<?> c = null;
		try {
			c = Class.forName("com.sun.tools.javac.Main");
		} catch (ClassNotFoundException e) {
		}
		useComple = (c != null);
	}
	
	/**
	 * 取得sql读取器<br>
	 * <br>
	 * 主文件名必须符合类名的命名规范,文件中的${..}将被参数化<br>
	 * 参数化的变量通过方法set()来设置值,变量名必须符合Java属性名命名规范<br>
	 * sql文件必须使用<b>UTF-8</b>编码<br>,并且是单条语句
	 * 
	 * @param filename - 从classpath中读取sql文件的完整路径,<br>
	 * 如<code>/jym/sim/sql/complie/x.sql</code>
	 * @throws IOException 
	 */
	public static ISqlReader get(String filename) throws IOException {
		if (useComple) {
			return new ReadAndComplie(filename);
		} else {
			return new SqlLink(filename);
		}
	}
	
}
