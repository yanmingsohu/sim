// CatfoOD 2009-12-14 下午08:25:03

package jym.sim.tags.template;

import java.util.List;

/**
 * 结果集表格的回调函数
 */
public interface IResultCallback<E> {
	/**
	 * 返回表格列数
	 */
	int getColumn();
	
	/**
	 * 分页后的总页数
	 */
	int getTotalPage();
	
	/**
	 * 分页后的当前页
	 */
	int getCurrentPage();
	
	/**
	 * 返回页码超链接url模式字符串%page转换为页数
	 */
	String getUrlPattern();
	
	/**
	 * 返回列名，作为表头
	 * @param column - 列的索引
	 * @return 列值
	 */
	String getColumnName(int column);
	
	/**
	 * 映射行数据
	 * 从source对象取出属性，并依序放入List中
	 * ResultTableTemplate遍历每行数据，并在每行中调用这个函数
	 * 
	 * @param target - 行的结果值
	 * @param source - 数据来源对象
	 */
	void mappingRowValue(List<String>target, E source);
}
