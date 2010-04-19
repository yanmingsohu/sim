// CatfoOD 2010-4-19 上午08:28:23 yanming-sohu@sohu.com/@qq.com

package jym.base.orm;

/**
 * 表格列的值
 */
public interface IColumnValue {
	
	/**
	 * 模板对象通过这个方法把表格列与列值传给实现这个接口的对象
	 * 
	 * @param column - 列名
	 * @param value - 值
	 */
	void set(String column, Object value);
	
}
