// CatfoOD 2011-7-8 上午11:14:39 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

import jym.sim.sql.IOrder;


/**
 * 传递需要拼装成sql的部分参数
 */
public class PaginationParam {

	/** 表名 */
	private String tableName;
	/** 查询条件子句,字句中包含where关键字 */
	private String whereSub;
	/** 排序子句, 无需排序时为空字符串 */
	private String order;
	/** 分页数据对象, 可以null */
	private PageBean page;
	/** 多表关联查询, 可以null */
	private String join;
	
	
	public PaginationParam(String tableName, String whereSub, 
			IOrder order, PageBean page, String join) {
		
		this.tableName = tableName;
		this.whereSub = whereSub;
		this.order = order.toString();
		this.page = page;
		this.join = join;
	}
	
	public PaginationParam(String tableName, String whereSub, 
			IOrder order,  String join) {
		
		this.tableName = tableName;
		this.whereSub = whereSub;
		this.order = order.toString();
		this.join = join;
	}
	
	public PaginationParam() {}

	public String getTableName() {
		return tableName;
	}
	
	public String getWhereSub() {
		return whereSub;
	}
	
	public String getOrder() {
		return order;
	}
	
	public PageBean getPage() {
		return page;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void setWhereSub(String whereSub) {
		this.whereSub = whereSub;
	}
	
	public void setOrder(IOrder order) {
		this.order = order.toString();
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	
	public void setPage(PageBean page) {
		this.page = page;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}
}
