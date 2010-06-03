// CatfoOD 2010-6-3 上午10:18:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * sql的where子句中列于值的判断方法
 */
public interface IWhere {
	/**
	 * 返回where的一个判断子句
	 * @param columnName - 列的名字
	 * @param value - 列的值
	 * @return 如何判断columnName的sql
	 */
	public String w(String columnName, Object value);
}
