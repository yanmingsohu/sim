package jym.base.orm;

import jym.base.sql.Logic;

/**
 * 实体属性与数据库列映射策略
 */
public interface IPlot {
	
	/**
	 * 把需要映射的实体属性名fieldName与数据库列名colname进行匹配<br>
	 * 未映射的实体属性使用表格名匹配<br>
	 * <br>
	 * <b>如果fieldName不是简单变量类型:</b>则必须调用三个参数的fieldPlot方法
	 * 
	 * @param fieldName -- 属性名,必须有相应的setter方法,不区分大小写
	 * @param colname -- 列名,不区分大小写
	 * @param log -- 当使用实体做参数执行select时, colname列的逻辑方法<br>
	 * 				如果fieldName=='value', log==Logic.EQ, 则where语句为<br>
	 * 				<code>where colname = 'value'</code>
	 */
	public void fieldPlot(String fieldName, String colname, Logic log);
	
	/**
	 * 把需要映射的实体属性名fieldName与数据库列名colname进行匹配<br>
	 * 未映射的实体属性使用表格名匹配<br>
	 * 当使用实体做参数执行select时, colname列的逻辑方法默认使用 Login.EQ
	 * <br>
	 * <b>如果fieldName不是简单变量类型:</b>则必须调用三个参数的fieldPlot方法
	 * 
	 * @param fieldName -- 属性名,必须有相应的setter方法,不区分大小写
	 * @param colname -- 列名,不区分大小写
	 */
	public void fieldPlot(String fieldName, String colname);
	
	/**
	 * 把需要映射的实体属性名fieldName与数据库列名colname进行匹配<br>
	 * 未映射的实体属性使用表格名匹配<br>
	 * <br>
	 * 如果找到匹配,则用getter创建fieldName类型的对象,
	 * <b>并把colname的列值放到getter.select(...)函数的第一个参数中</b>
	 * 
	 * @param fieldName -- 属性名,必须有相应的setter方法,不区分大小写
	 * @param colname -- 列名,不区分大小写
	 * @param getter -- 创建fieldName类型对象需要的ISelecter对象,
	 * 					如果为null则和调用两个参数的方法效果相同
	 */
	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter);
}
