// CatfoOD 2012-2-27 下午01:09:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.reader;


/**
 * 读取的文件必须使用 UTF-8 编码
 */
public interface ISqlReader {
	
	String SQL_FILE_CODE = "UTF-8";
	
	/**
	 * 设置sql文件中的${..}变量的值,变量名必须符合Java属性命名规范
	 * 
	 * @param name - 被替换的变量名
	 * @param value - 替换后的值
	 * @throws NoSuchFieldException - 找不到name指定的变量
	 */
	public void set(String name, Object value) throws 
			NoSuchFieldException;
	
	/**
	 * 返回拼装好的sql,其中的变量使用set()提前设置好
	 */
	public String getResultSql();
	
	/**
	 * 把sql打印到控制台
	 */
	public void showSql();
	
}
