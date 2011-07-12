// CatfoOD 2011-7-8 上午10:15:57 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;



/**
 * 查询时使用的join语法, 与另一个表级联查询<br><br>
 * 
 * <b>样例:</b><br>
 * <code>
 * select a.* from a<br>
 * left join b<br>
 * on b.c1 = a.c1<br>
 * where b.operator_sn = 41<br>
 * </code><br>
 * 
 * <b>与之对等的伪sql:</b><br>
 * <code>
 * select a.* from {IOrm.getTableName()} a <br>
 * left   join {tableName} b <br>
 * on     a.{IPlot.fieldPlot[colname]} {onLogic} b.{joinColumn} <br>
 * where  ... and {where} <br>
 * </code>
 * 
 * @see jym.sim.orm.IPlot
 */
public interface ISelectJoin extends ISqlLogic {

	/**
	 * 设置主表的名字, 必须在getJoin(),getWhere()方法调用前调用
	 */
	public void setMainTable(String tableName);
	
	/**
	 * 设置主表的列名, 该列与joinColumn进行管理<br>
	 * 必须在getJoin(),getWhere()方法调用前调用
	 */
	public void setMainColumn(String columnName);
	
	/**
	 * 返回join子句, 两边都含有空字符, 一定返回有效的join子句<br>
	 * 每次返回的sql都是一样的, 所有可以缓存
	 */
	public String getJoin();
	
	/**
	 * 返回where子句, 可以返回null, 则没有where子句
	 * (原因可以是没有设置该参数,或逻辑中的相关字段为null)
	 * 
	 * @param model - 数据bean
	 */
	public String getWhere(Object model);

}
