// CatfoOD 2011-8-22 下午01:41:38 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.sql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;

import jym.sim.sql.IPrepSql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.TransformPrep;
import jym.sim.sql.compile.ReadAndComplie;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;


@SuppressWarnings("deprecation")
public class TestPrepTrans {

	public static final void main(String[] s) throws IOException {
		ReadAndComplie sr = new ReadAndComplie("/jym/sim/test/sql/trans.sql");
		TransformPrep tp = new TransformPrep();
		
		tp.setSql(sr.getResultSql());
		Tools.pl(tp.getSql());
		Tools.pl();
		tp.debug();
		
		//pressure(sr.getResultSql());
	}
	
	public static void excute(final TransformPrep tp) throws IOException {
		JdbcTemplate jdbc = TestJdbcTemplate.createJdbc();
		
		/** 使用jdbc调用转换sql的样例 */
		jdbc.query(new IPrepSql() {
			
			public String getSql() {
				return tp.getSql();
			}
			public void exe(PreparedStatement pstm) throws Throwable {
				tp.exe(pstm);
				
				ResultSetMetaData rsmd = pstm.getMetaData();
				int c = rsmd.getColumnCount();
				
				for (int i=0; i<c; ++i) {
					Tools.pl(i, rsmd.getColumnName(i+1));
				}
			}
		});
	}

	public static void pressure(String sql) {
		TransformPrep tp = new TransformPrep();
		UsedTime.start("2000次转换");
		for (int i=0; i<2000; ++i) {
			tp.setSql(sql);
		}
		UsedTime.endAndPrint();
	}
}
