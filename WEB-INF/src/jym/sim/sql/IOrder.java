// CatfoOD 2010-6-3 上午10:39:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * 查询结果排序接口,可设置排序的方法
 */
public interface IOrder {
	
	/**
	 * 反向排序,从大到小
	 * 
	 * @param columnName - 列的名字
	 * @return 返回的IOrder可以设置下一个排序规则
	 */
	public IOrder desc(String columnName);
	
	/**
	 * 正向排序,从小到大
	 * 
	 * @param columnName - 列的名字
	 * @return 返回的IOrder可以设置下一个排序规则
	 */
	public IOrder asc(String columnName);
	
}
