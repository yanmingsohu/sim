package jym.base.orm;


public interface IOrm<T> {
	
	/**
	 * 返回T类型的class
	 * 
	 * @return T类型的class
	 */
	public Class<T> getModelClass();
	
	/**
	 * 返回sql命令
	 * 
	 * @param sql -- PreparedStatement格式的sql
	 */
	public String getPrepareSql();
	
	/**
	 * 数据与实体的映射策略
	 * 在plot中放入实体属性与数据库列名的对应关系
	 */
	public void mapping(IPlot plot);
	
}
