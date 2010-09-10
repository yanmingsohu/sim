// CatfoOD 2010-9-10 上午08:35:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FileParse {
	
	private static final Pattern exp = Pattern.compile("^[_A-Za-z][\\$_A-Za-z0-9]*");
	public static final String VAR_PREFIX = "__";
	
	private Set<String> variables;
	private List<String> sqls;
	private List<String> items;
	private int line = 1;
	private Info info;
	
	
	public FileParse(Info inf) throws IOException {
		
		Reader in = new BufferedReader(inf.openSqlInputStream());
	try {
		info = inf;
		init();
		
		StringBuilder buff = new StringBuilder();
		int ch = ' ';
		boolean isVar = false;
		
		while (true) {
			ch = in.read();

			if (ch=='\r') continue;
			
			if (ch=='\n') {	
				line++;	
				buff.append("\\n");
				continue;
			}
			
			if (ch=='"') {
				invalid("不能有双引号");
				continue;
			}
			
			if (isVar) {
				if (ch=='}') {
					addItem(variables, buff);
					isVar = false;
					continue;
				}
			}
			else if (ch=='$') {
				in.mark(3);
				ch = (char) in.read();
				
				if (ch=='{') {
					addItem(sqls, buff);
					isVar = true;
					continue;
				} else {
					in.reset();
				}
			}

			if (ch>=0) {
				buff.append((char)ch);
			} else {
				break;
			}
		}
		addItem(sqls, buff);
		
	}
	finally {
		try {if (in!=null) in.close();} catch(Exception e) {} 
		}
	}
	
	private void addItem(Collection<String> point, StringBuilder str) throws IOException {
		String s = str.toString();
		
		if (s.trim().length()>0) {
			if (point==sqls) {
				str.insert(0, '"').append('"');
				s = str.toString();
			} else {
				checkVarName(s);
				s = VAR_PREFIX + s;
			}
			
			items.add(s);
			point.add(s);
			str.setLength(0);
		}
	}
	
	private void checkVarName(String name) throws IOException {
		if (!exp.matcher(name).matches()) {
			invalid("无效的变量名:[" + name + "]");
		}
	}
	
	private void invalid(String msg) throws IOException {
		throw new IOException(
				msg + ", 在文件 " + info.getSqlFileName() + 
				" 第" + line + "行" );
	}
	
	private void init() {
		variables	= new HashSet<String>();
		sqls		= new ArrayList<String>();
		items		= new ArrayList<String>();
	}
	
	/**
	 * 返回变量名的迭代器
	 */
	public Iterator<String> getVariableNames() {
		return variables.iterator();
	}
	
	/**
	 * 返回解析后的sql文件中元素的迭代器
	 */
	public Iterator<String> getItems() {
		return items.iterator();
	}
}
