/* CatfoOD 2011-11-25 下午07:19:02 yanming-sohu@sohu.com Q.412475540 */

package jym.sim.orm.page;



public class MysqlPagiation implements IPage {

	public String select(PaginationParam parm) {

		String tableName = parm.getTableName();
		String whereSub = parm.getWhereSub();
		String order = parm.getOrder();
		PageBean page = parm.getPage();
		
		StringBuilder buff = new StringBuilder();
		
		buff.append( 		"select " ).append( tableName ).append( ".* from "	);
		buff.append(		 tableName											);
		buff.append(		 BLANK												);
		buff.append(		 parm.getJoin()										);
		buff.append( 		 BLANK 												);
		buff.append( 		 whereSub 											);
		buff.append( 		 BLANK 												);
		buff.append(		 order 												);
	if ( page.getOnesize() < 1000 && page.getCurrent() > 0 ) {
		buff.append(		" LIMIT "											);
		buff.append( 		(page.getCurrent()-1) * page.getOnesize()			);
		buff.append(		","													);
		buff.append( 		 page.getOnesize()									);
		}
	
		return buff.toString();
	}

	public String selectTotalPage(PaginationParam parm) {
		return "select count(*) " 
				+ TOTAL_COLUMN_NAME 
				+ " from " 
				+ parm.getTableName() 
				+ BLANK
				+ parm.getJoin() 
				+ BLANK
				+ parm.getWhereSub();
	}

}
