// CatfoOD 2010-7-12 上午08:12:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.sql.IWhere;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;

public class DefinitionLogic implements IWhere, ISkipValueCheck {
	
	private static Pattern exp = Pattern.compile(
			"(.*?)\\$\\{([a-zA-Z][a-zA-Z_0-9]*)\\}((?:(?!\\$\\{[a-zA-Z][a-zA-Z_0-9]*\\}).)*)");

	private List<String> deflist;
	
	public DefinitionLogic(String str) {
		deflist = new ArrayList<String>();
		Matcher m = exp.matcher(str);
		boolean nofind = true;
		
		while (m.find()) {
			add( m.group(1) );
			add( m.group(2) );
			add( m.group(3) );
			nofind = false;
		}
		
		if (nofind) {
			throw new IllegalArgumentException("参数不是有效的表达式");
		}
	}
	
	private void add(String s) {
		if (s.length()<1) s = null;
		deflist.add(s);
	}
	
	public String w(String columnName, Object v, Object model) {
		StringBuilder out = new StringBuilder();
		
		Iterator<String> it = deflist.iterator();
		Object fvalue = null;
		
		while (it.hasNext()) {
			
		fvalue = it.next();
			if (fvalue!=null) {
				out.append( fvalue );
			}
			
		fvalue = getValue(model, it.next());
			if (fvalue==SKIP_WHERE_SUB) {
				return SKIP_WHERE_SUB;
			}
			out.append( fvalue );
			
		fvalue = it.next();
			if (fvalue!=null) {
				out.append( fvalue );
			}
		}
		
		return out.toString();
	}
	
	private Object getValue(Object model, String fieldn) {
		Object value = null;
		
		if (fieldn!=null && model!=null) {
			String mname = BeanUtil.getGetterName(fieldn);
			
			try {
				value = BeanUtil.invoke(model, mname, new Object[0]);
				
			} catch (Exception e) {
				Tools.plerr(e);
			}
		}
		return value;
	}
	
}

