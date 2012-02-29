// CatfoOD 2010-9-9 下午02:04:39 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import jym.sim.sql.ISql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.reader.ISqlReader;
import jym.sim.sql.reader.SqlLink;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;


public class TestSqlReader {
	
	public static void main(String[] sa) throws Exception {
		//loopcreate();
		
		UsedTime.start("总共");
		final StringBuilder out = new StringBuilder();
		final StringBuilder res = new StringBuilder();

		UsedTime.start("\n连接数据库");
		JdbcTemplate jdbc = TestJdbcTemplate.createJdbc();
		UsedTime.endAndPrint(out);

		UsedTime.start("\n生成sql");
//		final ISqlReader sr2  = genSql( new ReadAndComplie("/test/complier.sql") );
		final ISqlReader sr = genSql( new SqlLink(TestSqlReader.class, "TestSqlReader.sql") );
		UsedTime.endAndPrint(out);
		sr.showSql();
		
		// 判断两种方法生成的字符串是否相同
//		boolean yy = sr.getResultSql().equals( sr2.getResultSql() );
//		Tools.pl("无二义性: " + yy);
		
		jdbc.query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				ResultSet rs = stm.executeQuery(sr.getResultSql());
				UsedTime.start("\n打印");
				Tools.pl(rs, res);
				UsedTime.endAndPrint(out);
			}
		});

		Tools.pl(res);
		Tools.pl(out);
		UsedTime.printAll();
	}
	
	public static void loopcreate() {
		StringBuilder out = new StringBuilder();
		UsedTime.start("\n1000 次生成sql ");
		for (int i=0; i<1000; i++) {
			//UsedTime.start("\n生成sql " + i);
			try {
				SqlLink sl = new SqlLink("/test/complier.sql");
				sl.set("data", new Data());
			} catch (Exception e) {
				e.printStackTrace();
			}
			//UsedTime.endAndPrint(out);
		}
		UsedTime.endAndPrint(out);
		Tools.pl(out);
	}
	
	@SuppressWarnings("deprecation")
	private static ISqlReader genSql(ISqlReader sr) throws IOException, NoSuchFieldException {
		
		final SimpleDateFormat DEF_FMT = new SimpleDateFormat("yyyy-MM-dd");
		final Date[] days = new Date[] { new Date(110, 9, 11), new Date() };
		
		StringBuilder SQL = new StringBuilder();
		for (int i=0; i<days.length; ++i) {
			final String day = DEF_FMT.format(days[i]);
			SQL.append("     VI.GETDATE >= to_date('" + day + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
			SQL.append(" and VI.GETDATE <= to_date('" + day + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
			if (i+1 < days.length) {
				SQL.append(" or ");
			}
		}
		
		Data data = new Data();
		data.areaSn = 1;
		data.kindId = "IA_F_RG";
		data.dates = SQL.toString();
		
		sr.set("data", data);

		return sr;
	}
	
	public static class Data {
		int areaSn;
		String kindId;
		String dates;
		
		public int as() { return areaSn; }
	}
}
