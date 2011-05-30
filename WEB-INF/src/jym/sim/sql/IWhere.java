// CatfoOD 2010-6-3 上午10:18:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import jym.sim.orm.ISqlLogic;

/**
 * sql的where子句中列值的判断方法
 */
public interface IWhere extends ISqlLogic {
	
	/**
	 * 如果要忽略此where子句则在w()中返回SKIP_WHERE_SUB
	 */
	public static final String SKIP_WHERE_SUB = null;
	
	/**
	 * 返回where的一个判断子句
	 * 
	 * @param columnName - 数据库列的名字
	 * @param value - 对应数据库列名的数据实体属性的值
	 * @param model - 执行查询的数据对象,条件放于其中
	 * 
	 * @return 如何判断columnName的sql,如果返回null则忽略此子句的判断
	 */
	public String w(String columnName, Object value, Object model);
}
