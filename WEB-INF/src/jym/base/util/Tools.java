// CatfoOD 2009-10-26 обнГ09:15:11

package jym.base.util;

import javax.servlet.http.HttpServletResponse;

public class Tools {
	private static int id = 0;
	
	public static void nocatch(HttpServletResponse resp) {
		resp.setHeader("cache-control", "no-cache");
	}
	
	public static void setAjaxXmlHeader(HttpServletResponse resp) {
		nocatch(resp);
		resp.setContentType("text/xml");
	}
	
	public static String creatTagID(String tagname) {
		int _id;
		synchronized (Tools.class) {
			_id = id++;
		}
		return tagname + _id;
	}
	
	public static void pl(Object o) {
		System.out.println(o);
	}
}
