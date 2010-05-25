// CatfoOD 2010-5-24 上午09:30:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;


/**
 * oracle数据库的分页方法
 */
public class OraclePagination implements IPage {

	public String select(String tableName, String whereSub, PageBean page) {
		
		int s = (page.getCurrent() - 1) * page.getOnesize() + 1;
		int e = s + page.getOnesize() - 1;
		
		StringBuilder buff = new StringBuilder();
		
		buff.append( "select * from ( " );
		
		buff.append( 	"select rownum sim__row__num, sim__in__table.* from " );
		buff.append( 	 tableName + " sim__in__table" );
		buff.append( 	 BLANK );
		buff.append( 	 whereSub );
		
		buff.append( " ),( " );
		
		buff.append(	"select count(*) " + TOTAL_COLUMN_NAME );
		buff.append(	" from " );
		buff.append( 	 tableName );
		buff.append( 	 BLANK );
		buff.append( 	 whereSub );
		
		buff.append( " ) " );
		buff.append( "where sim__row__num between " );
		buff.append(  s );
		buff.append( " and " );
		buff.append(  e );
		
//	Tools.pl("oracle 分页sql: " + buff.toString());
		
		return buff.toString();
	}

}
