// CatfoOD 2010-6-3 上午10:39:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * 查询结果排序接口,可设置排序的方法
 */
public interface IOrder {
	/**
	 * 反向排序,从大到小
	 * @param columnName - 列的名字
	 */
	public void desc(String columnName);
	/**
	 * 正向排序,从小到大
	 * @param columnName - 列的名字
	 */
	public void asc(String columnName);
}
