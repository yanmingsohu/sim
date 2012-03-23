// CatfoOD 2011-7-25 下午03:03:31 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;


public class SqlFormat {
	
	private final static int NOT = 0;
	private final static int ONLY_ENTER = 1;
	private final static int ENTER_AND_SPACE = 2;
	

	/** 全小写 */
	private final static char[][] key = {
		 {'s', 'e', 'l', 'e', 'c', 't', ' '} // 0
		,{'w', 'h', 'e', 'r', 'e',      ' '}
		,{'f', 'r', 'o', 'm',           ' '} // 2
		,{'o', 'n',                     ' '}
		,{'g', 'r', 'o', 'u', 'p',      ' '} // 4
		,{'o', 'r', 'd', 'e', 'r',      ' '}
		,{'j', 'o', 'i', 'n',           ' '} // 6
		,{'h', 'a', 'v', 'i', 'n', 'g', ' '}
		,{'w', 'h', 'e', 'n',			' '}
		,{'t', 'h', 'e', 'n',			' '}
		,{'e', 'n', 'd', 				' '}
		,{'a', 'n', 'd', 				' '}
	};
	
	private final static String SPACE = "  ";
	

	public static String format(final String sql) {
		int tab = 0;
		StringBuilder out = new StringBuilder();
		
		final int len = sql.length();
		boolean enter = false;
		int i = 0;
		char c, pc = 0;
		
		while (i<len) {
			c = sql.charAt(i++);
			
			if (c=='\n') {
				continue;
			}
			
			if (c==' ' && (enter || pc==' ')) {
				pc = c;
				continue;
			}
			
			enter = false;
			
			if (c==')') {
				tab--;
				out.append('\n');
				tab(out, tab);
			}
			
			out.append(c);
			
			int need = needEnter(sql, i);
			if (need!=NOT) {
				enter = true;
			}
			if (c=='(') {
				enter = true;
				tab++;
			}
			if (enter) {
				out.append('\n');
				tab(out, tab);
			}
			if (need==ENTER_AND_SPACE) {
				out.append(' ');
			}
			
			pc = c;
		}
		
		return out.toString();
	}
	
	private static int needEnter(String sql, int index) {
		
		if (index+1<sql.length() && (sql.charAt(index)==' ' || sql.charAt(index)=='\n')) {
			boolean has = false;
			boolean find = false;
			int klen = 0;
			
			for (int i=index+1; i<sql.length(); ++i) {
				has = false;
				int j;
				
				for (j=0; j<key.length; ++j) {
					char[] i_k = key[j];
					
					if (klen<i_k.length && i_k[klen]==Character.toLowerCase( sql.charAt(i) )) {
						has = true;
						find = (klen + 1 == i_k.length);
						break;
					}
				}
				
				if (!has) break;
				if (find) {
					if (j==3 || j==7) return ENTER_AND_SPACE;
					return ONLY_ENTER;
				}
				klen++;
			}
		}
		return NOT;
	}
	
	private static void tab(StringBuilder out, int count) {
		for (int i=0; i<count; ++i) {
			out.append(SPACE);
		}
	}
}
