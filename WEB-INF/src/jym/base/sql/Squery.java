package jym.base.sql;

public class Squery {
	
	public static Select select(String sql) {
		return new Select(sql);
	}
	
}
