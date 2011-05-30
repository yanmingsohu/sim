// CatfoOD 2009-11-4 下午08:46:38

package jym.sim.tags;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetTable extends TableTag {
	private int column;
	
	public ResultSetTable(ResultSet rs) throws SQLException {
		super(rs.getMetaData().getColumnCount());
		ResultSetMetaData rsm = rs.getMetaData();
		column = rsm.getColumnCount();
		createHead(rsm);
		createBody(rs);
	}
	
	private void createHead(ResultSetMetaData rsmd) throws SQLException {
		for (int i=1; i<=column; ++i) {
			ITag b = new TagBase("b");
			b.append(rsmd.getColumnLabel(i));
			super.appendHead(b);
		}
	}
	
	private void createBody(ResultSet rs) throws SQLException {
		while (rs.next()) {
			for (int i=1; i<=column; ++i) {
				super.append( rs.getString(i) );
			}
		}
	}
}
