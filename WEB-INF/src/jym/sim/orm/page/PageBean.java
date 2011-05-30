// CatfoOD 2010-5-24 上午09:44:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

/**
 * 存储分页数据的类
 */
public class PageBean {
	
	public final static int DEFAULT_CURRENT_PAGE = 1;
	public final static int DEFAULT_TOTAL_PAGE = 1;
	public final static int DEFAULT_ONE_SIZE = 20;
	
	// 当前页,从1开始
	private int current;
	// 页码总数
	private int total;
	// 一页显示的数量
	private int onesize;
	
	/**
	 * 默认值: current = 1, total = 1, onesize = 20
	 */
	public PageBean() {
		current = DEFAULT_CURRENT_PAGE;
		total = DEFAULT_TOTAL_PAGE;
		onesize = DEFAULT_ONE_SIZE;
	}

	/**
	 * 当前页的有效值1~*
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * 总页数的有效值 1~*
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 当前页的有效值1~*
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

	/**
	 * 总页数的有效值 1~*<br>
	 * 如果total<=0则置1
	 */
	public void setTotal(int total) {
		if (total<=0) total = 1;
		this.total = total;
	}

	/**
	 * 默认值20
	 */
	public int getOnesize() {
		return onesize;
	}

	public void setOnesize(int onesize) {
		this.onesize = onesize;
	}

	/**
	 * 返回分页后数据行的起始索引, 第一行数据的索引号为<b>1</b><br>
	 * <br>
	 * <b>当前页(current)的值应该已经被设置, 单页数据长度(onesize)也必须有效</b>
	 */
	public int getStartPosition() {
		return (getCurrent() - 1) * getOnesize() + 1;
	}
	
	/**
	 * 设置整个结果集的数据行数<br>
	 * 此结果将转换为总页数<br>
	 * <br>
	 * <b>单页数据长度(onesize)也必须有效<b/>
	 */
	public void setTotalRow(int rownum) {
		int tp = rownum / getOnesize();
		int tc = rownum % getOnesize();
		if (tc>0) tp++;
		setTotal(tp);
	}
	
	/**
	 * 返回分页后数据行的结束索引, 第一行数据的索引号为<b>1</b><br>
	 * <br>
	 * <b>当前页(current)的值应该已经被设置, 单页数据长度(onesize)也必须有效</b>
	 */
	public int getEndPosition() {
		return getStartPosition() + getOnesize() - 1;
	}
}
