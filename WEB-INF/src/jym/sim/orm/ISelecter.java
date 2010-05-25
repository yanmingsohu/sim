package jym.sim.orm;

import java.util.List;

import jym.sim.orm.page.IPage;
import jym.sim.orm.page.PageBean;

/**
 * 可以执行sql-select命令
 */
public interface ISelecter<T> {
	
	/**
	 * 使用model中的有效属性查询结果集,不分页
	 * 
	 * @param model - bean对象,如果所有的属性都无效则无条件返回所有数据行
	 * @param join - where子句每个逻辑的连接方式 and/or
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
	/**
	 * 使用model中的有效属性查询结果集,分页<br/>
	 * 此方法返回后,page.total属性将被设置为总页数
	 * 
	 * @param model -  bean对象,如果所有的属性都无效则无条件返回所有数据行
	 * @param join - where子句每个逻辑的连接方式 and/or
	 * @param page - 分页数据
	 * @return - T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(T model, String join, PageBean page);
	
	/**
	 * 设置数据库的分页方法,此方法的类在jym.sim.orm.page.*中<br/>
	 * 默认不分页
	 * @param plot - 数据库分页策略类
	 */
	public void setPaginationPlot(IPage plot);
	
	/**
	 * 返回bean对象的class
	 */
	public Class<T> getModelClass();
	
}
