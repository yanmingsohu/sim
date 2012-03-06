// CatfoOD 2012-2-27 下午01:09:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.reader;

import java.security.AccessControlException;


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
	 * <b>如果变量没有改变, 不要频繁调用该方法, 而是缓存结果</b>
	 * @throws AccessControlException - 如果文件校验错误抛出改异常
	 */
	public String getResultSql() throws AccessControlException;
	
	/**
	 * 把sql打印到控制台
	 */
	public void showSql();
	
	/**
	 * 设置文件锁定参数, 如果文件经过校验算法后与该值不同, getResultSql()会抛出异常
	 * @param protectKey - 文件校验码
	 */
	public void lockFile(long protectKey);
}
