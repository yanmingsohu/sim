package jym.base.orm;

import java.util.List;

/**
 * 可以执行sql-select命令
 */
public interface ISelecter<T> {
	
	/**
	 * 执行select查询
	 * 
	 * @param params -- setPrepareSql()时sql语句中?的参数
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(Object ...params);
	
	/**
	 * 使用model中的有效属性查询结果集
	 * 
	 * @param model - bean对象
	 * @param join - where子句的连接方式 and/or/not
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
}
