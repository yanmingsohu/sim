package jym.sim.orm;

import java.util.List;

/**
 * 可以执行sql-select命令
 */
public interface ISelecter<T> {
	
	/**
	 * 使用model中的有效属性查询结果集
	 * 
	 * @param model - bean对象,如果所有的属性都无效则无条件返回所有数据行
	 * @param join - where子句每个逻辑的连接方式 and/or
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
	public Class<T> getModelClass();
	
}
