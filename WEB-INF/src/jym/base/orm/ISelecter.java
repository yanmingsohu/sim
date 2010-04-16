package jym.base.orm;

import java.util.List;

/**
 * 可以执行sql-select命令
 */
public interface ISelecter<T> {
	
	/**
	 * 执行select查询, 并把sql语句中的$where替换为where中的条件
	 * 
	 * @param where - where查询条件, 可以不含where关键字
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(String where);
	
	/**
	 * 使用model中的有效属性查询结果集
	 * 
	 * @param model - bean对象
	 * @param join - where子句每个逻辑的连接方式 and/or/not
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
}
