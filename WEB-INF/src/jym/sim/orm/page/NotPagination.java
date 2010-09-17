// CatfoOD 2010-5-24 上午09:55:00 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

/**
 * 此实现类不分页数据
 */
public class NotPagination implements IPage {
	
	private final static String SELECT = "select * from ";

	public String select(String tableName, String whereSub, String order, PageBean page) {
		StringBuilder buff = new StringBuilder(SELECT);
		buff.append(tableName);
		buff.append(BLANK);
		buff.append(whereSub);
		buff.append(BLANK);
		buff.append(order);
		
		return buff.toString();
	}

}
