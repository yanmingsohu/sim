// CatfoOD 2010-5-24 上午09:30:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;


/**
 * oracle数据库的分页方法
 */
public class OraclePagination implements IPage {

	public String select(PaginationParam parm) {

		String tableName = parm.getTableName();
		String whereSub = parm.getWhereSub();
		String order = parm.getOrder();
		PageBean page = parm.getPage();
		
		boolean needPage = page.getOnesize() != Integer.MAX_VALUE;
		int s = (page.getCurrent() - 1) * page.getOnesize() + 1;
		int e = s + page.getOnesize() - 1;
		
		StringBuilder buff = new StringBuilder();
	if (needPage) {
		buff.append( "select * from ( "											);
		
		buff.append( 	"select rownum sim__row__num, sim__in__table.* from ("	);
	}
		buff.append( 		"select " ).append( tableName ).append( ".* from "	);
		buff.append(		 tableName											);
		buff.append(		 BLANK												);
		buff.append(		 parm.getJoin()										);
		buff.append( 		 BLANK 												);
		buff.append( 		 whereSub 											);
		buff.append( 		 BLANK 												);
		buff.append(		 order 												);
	if (needPage) {	
		buff.append( 	" ) sim__in__table "  									);
		buff.append(	"where rownum <= "										);
		buff.append(	e														);
		/*
		buff.append( " ),( " 													);
		
		buff.append(	"select count(1) " ).append( TOTAL_COLUMN_NAME 			);
		buff.append(	" from " 												);
		buff.append( 	 tableName												);
		buff.append(	 BLANK													);
		buff.append(	 parm.getJoin()											);
		buff.append( 	 BLANK													);
		buff.append( 	 whereSub												);
		*/
		buff.append( " ) "														);
		buff.append( "where sim__row__num >= "									);
		buff.append(  s															);
	}
//	Tools.pl("oracle 分页sql: " + buff.toString());
		
		return buff.toString();
	}

	public String selectTotalPage(PaginationParam parm) {
		StringBuilder buff = new StringBuilder();
		
		buff.append(	"select count(1) " ).append( TOTAL_COLUMN_NAME 			);
		buff.append(	" from " 												);
		buff.append( 	 parm.getTableName()									);
		buff.append(	 BLANK													);
		buff.append(	 parm.getJoin()											);
		buff.append( 	 BLANK													);
		buff.append( 	 parm.getWhereSub()										);
		
		return buff.toString();
//		return null;
	}

}
