package jym.base.sql;

public class Select implements ISqlQuery {
	private String select;
	
	public Select(String sql) {
		select = sql;
	}
}
