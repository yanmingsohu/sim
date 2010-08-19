// CatfoOD 2010-8-19 上午11:18:29 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter.sql;

import jym.sim.filter.SimFilterException;

/**
 * 把字符串转换为不会在sql中产成其他语义的字符串
 */
public class SafeSqlStringFilter implements ISqlInputParamFilter<String> {
	
	private final static char TAG = '\'';
	
	public String see(String s) throws SimFilterException {
		if (s!=null) {
			// String.indexOf方法使用内部数组效率高
			// 而toCharArray方法需要复制
			if (s.indexOf(TAG)>=0) {
				s = to(s);
			}
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
