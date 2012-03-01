// CatfoOD 2010-7-29 上午09:48:27 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.util;

import jym.sim.json.JSonFormater;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;

public class TestJSonFormater {
	
	public static void main(String[] s) {
	//	time();
		test();
	}
	
	public static void test() {
		String a = "dir {\n String s	= \"fdsafdsaf\\\"; \n}";
		// 输入和输出字符串，相同说明算法正确
		Tools.pl( formatJson(a) );
	}
	
	public static void time() {
		String a = "dir {\n String s	= \"fdsafdsaf\"; \n}";
		StringBuilder buff = new StringBuilder();
		
		for (int i=0; i<30000; i++) {
			buff.append(a);
		}
		
		a = buff.toString();
		
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
		System.out.println("字符串长度" + a.length());
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
