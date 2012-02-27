// CatfoOD 2010-9-9 下午02:04:39 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.compile.SqlReader;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;


public class TestSqlReader {
	
	public static void main(String[] sa) throws Exception {
		UsedTime.start("总共");
		StringBuilder out = new StringBuilder();

		UsedTime.start("\n连接数据库");
			JdbcTemplate jdbc = TestJdbcTemplate.createJdbc();
		UsedTime.endAndPrint(out);

		UsedTime.start("\n生成sql");
			SqlReader sr = genSql();
		UsedTime.endAndPrint(out);
		
		UsedTime.start("\n取数据");
			List<Object[]> result = sr.executeQuery(jdbc);
		UsedTime.endAndPrint(out);
		
		UsedTime.start("\n打印");
		sr.showSql();
		printArr(result.get(-1));
		for (int j=0; j<result.size(); j++) {
			printArr(result.get(j));
		}
		UsedTime.endAndPrint(out);

		Tools.pl(out);
		UsedTime.printAll();
	}
	
	public static void printArr(Object[] a) {
		for (int c=0; c<a.length; ++c) {
			Tools.p(a[c]+"\t");
		}
		Tools.pl();
	}
	
	@SuppressWarnings("deprecation")
	private static SqlReader genSql() throws IOException, NoSuchFieldException {
		SqlReader sr = new SqlReader("/test/complier.sql");
		
		final SimpleDateFormat DEF_FMT = new SimpleDateFormat("yyyy-MM-dd");
		final Date[] days = new Date[] {new Date(110, 9, 11), new Date()};
		
		StringBuilder SQL = new StringBuilder();
		for (int i=0; i<days.length; ++i) {
			final String day = DEF_FMT.format(days[i]);
			SQL.append("     VI.GETDATE >= to_date('" + day + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
			SQL.append(" and VI.GETDATE <= to_date('" + day + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
			if (i+1 < days.length) {
				SQL.append(" or ");
			}
		}
		
		sr.set("areaSn", "1");
		sr.set("kindId", "IA_F_RG");
		sr.set("limit_datas", SQL);

		return sr;
	}
}
