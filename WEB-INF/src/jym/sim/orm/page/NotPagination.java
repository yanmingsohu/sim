// CatfoOD 2010-5-24 上午09:55:00 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

/**
 * 此实现类不分页数据,select方法的page参数可以为null
 */
public class NotPagination implements IPage {

	
	public String select(PaginationParam parm) {
		StringBuilder buff = new StringBuilder("select ");
		buff.append(parm.getTableName()).append(".* from ");
		buff.append(parm.getTableName());
		buff.append(BLANK);
		buff.append(parm.getJoin());
		buff.append(BLANK);
		buff.append(parm.getWhereSub());
		buff.append(BLANK);
		buff.append(parm.getOrder());
		
		return buff.toString();
	}

	public String selectTotalPage(PaginationParam parm) {
		return null;
	}

}
