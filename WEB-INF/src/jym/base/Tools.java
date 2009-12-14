// CatfoOD 2009-10-26 обнГ09:15:11

package jym.base;

import javax.servlet.http.HttpServletResponse;

public class Tools {
	private static int id = 0;
	
	public static void nocatch(HttpServletResponse resp) {
		resp.setHeader("cache-control", "no-cache");
	}
	
	public static String creatTagID(String tagname) {
		int _id;
		synchronized (Tools.class) {
			_id = id++;
		}
		return tagname + _id;
	}
	
}
