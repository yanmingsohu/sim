// CatfoOD 2010-8-19 下午12:46:30 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter.sql;

import java.util.HashMap;
import java.util.Map;

import jym.sim.filter.SimFilterException;

/**
 * 把从数据库输出的字符串，装换为在HTML页面上显示时不会出错的字符串
 */
public class HtmlDisplayFilter implements ISqlOutputParamFilter<String> {
	
	private Map<Character, String> map;
	
	public HtmlDisplayFilter() {
		map = new HashMap<Character, String>();
		
		map.put(' ',	"&#160;");
		map.put('<', 	"&#60;"	);
		map.put('>', 	"&#62;"	);
		map.put('\'',	"&#39;"	);
		map.put('"',	"&#34;"	);
		map.put('\\',	"\\\\"	);
	}

	public String see(String src) throws SimFilterException {
		char[] chs = src.toCharArray();
		for (int i=0; i<chs.length; ++i) {
			if (map.get(chs[i])!=null) {
				return fix(chs, i);
			}
		}
		return src;
	}

	private String fix(char[] chs, int sIndex) {
		StringBuilder buff = new StringBuilder(chs.length * 2);
		buff.append(chs, 0, sIndex);
		
		for (int i=sIndex; i<chs.length; ++i) {
			String s = map.get(chs[i]);
			if (s==null) {
				buff.append(chs[i]);
			} else {
				buff.append(s);
			}
		}
		return buff.toString();
	}
}
