package jym.sim.orm;

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
	 * 返回数据库表名
	 */
	public String getTableName();
	
	/**
	 * 数据与实体的映射策略
	 * 在plot中放入实体属性与数据库列名的对应关系
	 */
	public void mapping(IPlot plot);
	
	/**
	 * 返回表格的主键列的列名子
	 */
	public String getKey();
	
}
