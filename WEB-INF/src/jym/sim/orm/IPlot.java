package jym.sim.orm;

import jym.sim.sql.IOrder;

/**
 * 实体属性与数据库列映射策略
 */
public interface IPlot {
	
	/**
	 * 把需要映射的实体属性名fieldName与数据库列名colname进行匹配<br>
	 * 未映射的实体属性使用表格名匹配<br>
	 * <br>
	 * <b>如果fieldName不是简单变量类型:</b>则必须调用三个参数的fieldPlot方法<br>
	 * <br><br>
	 * 
	 * logics参数的说明:(如果参数中类型重复则靠后的参数会覆盖之前的参数)<br><br>
	 * 
	 * <b>select语句使用IWhere接口:</b><br>
	 * 	当使用实体做参数执行select时, colname列的逻辑方法<br>
	 *  如果fieldName=='value', log==Logic.EQ, 则where语句为<br>
	 * 	<code>where colname = 'value'</code>
	 * <br><br>
	 * 
	 * <b>update语句使用IUpdateLogic接口:</b><br>
	 *  当实体做参数执行updata时, colname列如何拼装为sql, 该接口为更新为null值提供支持<br>
	 *  如果未指定行为,默认使用全局有效检查策略,否则全局策略会被覆盖
	 * <br><br>
	 * 
	 * <b>select语句使用ISelectJoin接口:(当前只支持一个外表)</b><br>
	 *  当使用实体做参数执行select时, 使用colname列与另一个表进行级联查询<br>
	 *  fieldName属性的值不影响也不参与生成的join语句
	 * <br>
	 * 
	 * @param fieldName -- 属性名,必须有相应的setter方法,不区分大小写
	 * @param colname -- 列名,不区分大小写
	 * @param logics -- 属性值开始拼装sql时,不同的sql语句有不同的逻辑方式
	 */
	public void fieldPlot(String fieldName, String colname, ISqlLogic ...logics);
	
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
	 * 外键映射<br>
	 * 把需要映射的实体属性名fieldName与数据库列名colname进行匹配<br>
	 * 未映射的实体属性使用表格名匹配<br>
	 * <br>
	 * 如果找到匹配, 创建<?>类型对象, 把pkname指定的属性用colname取得值初始化<br>
	 * 然后调用getter.select(<?>), 把返回的集合放入fieldName属性中
	 * 
	 * @param fieldName -- 属性名,必须有相应的setter方法,不区分大小写
	 * @param colname -- 列名,不区分大小写
	 * @param getter -- 创建fieldName类型对象需要的ISelecter对象,
	 * 					如果为null则和调用两个参数的方法效果相同
	 * @param pkname -- 外键对象的主键属性名
	 * 
	 * @deprecated 该方法尚未详细测试
	 */
	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter, String pkname);
	
	/**
	 * 取得排序接口
	 */
	public IOrder order();
}
