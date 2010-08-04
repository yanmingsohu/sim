// CatfoOD 2010-8-4 上午11:05:53 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;


/**
 * 转换字符串,防止sql语句注入
 */
public class SafeSql {
	
	private final static char TAG = '\'';
	
	public static final String transformValue(Object o) {
		String s = o.toString();
		// String.indexOf方法使用内部数组效率高
		if (s.indexOf(TAG)>=0) {
			s = to(s);
		}
		return s;
	}
	
	private static final String to(String a) {
		char[] ch = a.toCharArray();
		char[] nch = new char[ch.length * 2];
		int ni = 0;
		
		for (int i=0; i<ch.length; ++i) {
			if (ch[i]==TAG) {
				nch[ni++] = TAG;
			}
			nch[ni++] = ch[i];
		}
		
		return new String(nch, 0, ni);
	}
}
