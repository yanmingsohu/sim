package jym.sim.orm;

import java.util.List;

import jym.sim.orm.page.IPage;
import jym.sim.orm.page.PageBean;

/**
 * 可以执行sql-select命令
 */
public interface ISelecter<T> {
	
	/**
	 * 使用model中的有效属性查询结果集,不分页<br>
	 * 该方法返回的List会找查询需求从数据库中提取数据,而不是一次全部提取<br>
	 * 此方法在动态提取时会有性能损失,不消耗内存,但会占用数据库连接对象<br><br>
	 * <b>由于从返回List中提取的数据都是临时的,所以对他们的修改都是无效的</b>
	 * 
	 * @param model - bean对象,如果所有的属性都无效则无条件返回所有数据行
	 * @param join - where子句每个逻辑的连接方式 and/or
	 * @return T对象列表 -- 如果没有对象返回,则返回空的List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
	/**
	 * 使用model中的有效属性查询结果集,分页<br/>
	 * 此方法返回后,page.total属性将被设置为总页数<br>
	 * 该方法认为分页已经合理设置,并一次性把所需要的全部数据保存在List中<br>
	 * 此方法在查询结束后数据库连接对象已经释放,但如果数据过多则消耗内存
	 * (或内存溢出)因为List需要保存所有查询到的数据<br>
	 * 
	 * @param model -  bean对象,如果所有的属性都无效则无条件返回所有数据行
	 * @param join - where子句每个逻辑的连接方式 and/or
	 * @param page - 分页数据,不能为null,否则会抛出异常
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
