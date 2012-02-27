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

/**
 * 文件解析器，读取文件中的${varname}表达式
 * varname必须符合Java变量命名规范,如果想要输出$则需要$$
 */
public class FileParse {
	
	private static final Pattern exp = Pattern.compile("^[_A-Za-z][\\$_A-Za-z0-9]*");
	public static final String VAR_PREFIX = "__";
	
	private Set<String> variables;
	private List<String> texts;
	private List<String> items;
	private int line = 1;
	private String filename;
	
	
	public FileParse(Info inf) throws IOException {
		this(inf.getSqlFileName(), inf.openSqlInputStream());
	}
	
	/**
	 * 开始解析sql文件，并生成相关的信息
	 * 
	 * @param _filename - 被解析的文件名，用于输出错误信息，可以为null
	 * @param reader - 从reader中读取目标文件
	 * @throws IOException - 如果解析失败，抛出异常
	 */
	public FileParse(String _filename, Reader reader) throws IOException {
		Reader in = new BufferedReader(reader);
		filename = _filename;
		init();
		
	try {
		StringBuilder buff = new StringBuilder();
		int ch = ' ';
		boolean isVar = false;
		
		while (true) {
			ch = in.read();

			if (ch=='\r') continue;
			
			if (ch=='\n') {	
				line++;	
				buff.append('\\').append('n');
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
				
				if (ch=='$') continue;
				
				if (ch=='{') {
					addItem(texts, buff);
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
		addItem(texts, buff);
		
	}
	finally {
		try {if (in!=null) in.close();} catch(Exception e) {} 
		}
	}
	
	private void addItem(Collection<String> point, StringBuilder str) throws IOException {
		String s = str.toString().trim();
		
		if (s.length()>0) {
			if (point==texts) {
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
				msg + ", 在文件 " + filename + 
				" 第" + line + "行" );
	}
	
	private void init() {
		variables	= new HashSet<String>();
		texts		= new ArrayList<String>();
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
	 * 其中，文本已经用双引号包围，而变量则直接返回
	 */
	public Iterator<String> getItems() {
		return items.iterator();
	}
	
	/**
	 * 返回文件中的文本元素的迭代器，元素被分开的原因
	 * 可能是中间的变量
	 */
	public Iterator<String> getTexts() {
		return texts.iterator();
	}
}
