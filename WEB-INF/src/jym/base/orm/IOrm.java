package jym.base.orm;

/**
 * orm映射时使用的实体映射策略,
 * 
 * @param <T> - 数据实体
 */
public interface IOrm<T> {
	
	/**
	 * 返回T类型的class
	 * 
	 * @return T类型的class
	 */
	public Class<T> getModelClass();
	
	/**
	 * 返回特殊的sql命令
	 * 
	 * @return 
	 * 		特殊格式的sql命令,其中的where子句使用$where代替,
	 * 		以便动态替换成特定的条件,例如<br>
	 * 		原句: <code>select * from table_name where col1 > 1;</code><br>
	 * 		替换: <code>select * from table_name $where; </code>
	 */
	public String getSimSql();
	
	/**
	 * 数据与实体的映射策略
	 * 在plot中放入实体属性与数据库列名的对应关系
	 */
	public void mapping(IPlot plot);
	
}
