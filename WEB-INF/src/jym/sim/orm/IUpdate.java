// CatfoOD 2010-4-16 下午04:44:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

/**
 * 可执行insert delete update方法<br>
 * 部分操作依赖于主键的有效性,且主键的有效区间x>=0
 */
public interface IUpdate<T> {
	/**
	 * 在数据库中插入一行数据,成功返回true
	 */
	public boolean add(T model);
	
	/**
	 * 删除数据库中数据,如果是数据库错误返回-1
	 */
	public int delete(T model);
	
	/**
	 * 更新数据库中的数据,如果是数据库错误返回-1<br>
	 * 主键的值不会改变,并且更新条件只是主键
	 */
	public int update(T model);
}
