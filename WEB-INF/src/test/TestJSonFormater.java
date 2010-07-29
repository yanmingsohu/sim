// CatfoOD 2010-7-29 上午09:48:27 yanming-sohu@sohu.com/@qq.com

package test;

import jym.sim.json.JSonFormater;
import jym.sim.util.UsedTime;

public class TestJSonFormater {
	
	public static void main(String[] s) {
		String a = "dir {\n String s	= \"fdsafdsaf\"; \n}";
		StringBuilder buff = new StringBuilder();
		
		for (int i=0; i<30000; i++) {
			buff.append(a);
		}
		
		a = buff.toString();
		
		System.out.println("字符串长度" + a.length());
//		System.out.println(a);
		
		for (int i=0; i<9; ++i) {
			System.out.println("---");
			UsedTime.start("使用Strng.replaceAll");
			formatJson2(a);
			UsedTime.endAndPrint();
			
			UsedTime.start("使用JSonFormater");
			formatJson(a);
			UsedTime.endAndPrint();
			System.out.println("---");
		}
	}
	
	
	protected static String formatJson2(String str) {
		return str.replaceAll("\"", "\\\\\"")
		.replaceAll("\n", "\\\\n")
		.replaceAll("\r", "\\\\r");
	}
	
	
	protected static String formatJson(String str) {
		return	JSonFormater.frm(str);
	}
	
}
