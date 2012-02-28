// CatfoOD 2010-9-9 下午02:04:39 yanming-sohu@sohu.com/@qq.com

package test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
		UsedTime.start("总共");
		final StringBuilder out = new StringBuilder();
		final StringBuilder res = new StringBuilder();

		UsedTime.start("\n连接数据库");
		JdbcTemplate jdbc = TestJdbcTemplate.createJdbc();
		UsedTime.endAndPrint(out);

		UsedTime.start("\n生成sql");
//		final ISqlReader sr2  = genSql( new ReadAndComplie("/test/complier.sql") );
		final ISqlReader sr = genSql( new SqlLink("/test/complier.sql") );
		UsedTime.endAndPrint(out);
		sr.showSql();
		
		// 判断两种方法生成的字符串是否相同
//		boolean yy = sr.getResultSql().equals( sr2.getResultSql() );
//		Tools.pl("无二义性: " + yy);
		
		jdbc.query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				ResultSet rs = stm.executeQuery(sr.getResultSql());
				UsedTime.start("\n打印");
				print(rs, res);
				UsedTime.endAndPrint(out);
			}
		});

		Tools.pl(res);
		Tools.pl(out);
		UsedTime.printAll();
	}
	
	public static void print(ResultSet rs, StringBuilder out) throws Exception {
		out.append('\n');
		
		ResultSetMetaData rsmd = rs.getMetaData();
		final int cc = rsmd.getColumnCount();
		
		for (int i=1; i<=cc; ++i) {
			out.append(rsmd.getColumnName(i));
			out.append('\t');
		}
		out.append('\n');
		
		while (rs.next()) {
			for (int i=1; i<=cc; ++i) {
				out.append(rs.getObject(i));
				out.append('\t');
			}
			out.append('\n');
		}
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
	
	static class Data {
		int areaSn;
		String kindId;
		String dates;
	}
}
