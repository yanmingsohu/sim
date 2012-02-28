// CatfoOD 2010-9-10 上午08:35:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.el;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.Type;

/**
 * 文件解析器，读取文件中的${varname}表达式
 * varname必须符合Java变量命名规范,如果想要输出$则需要$$
 */
public class FileParse {
	
	private static final Pattern exp = Pattern.compile("^[_A-Za-z][\\$_A-Za-z0-9]*");
	
	private IItemFactory factory;
	private Map<String, IItem> variables;
	private List<IItem> texts;
	private List<IItem> items;
	private String filename;
	private int line = 1;
	
	
	/**
	 * 开始解析sql文件，并生成相关的信息
	 * 
	 * @param _filename - 被解析的文件名，用于输出错误信息，可以为null
	 * @param reader - 从reader中读取目标文件
	 * @throws IOException - 如果解析失败，抛出异常
	 */
	public FileParse(String _filename, Reader reader, IItemFactory fact) throws IOException {
		if (fact == null) throw new NullPointerException();
		
		factory = fact; 
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
				addText(buff);
				items.add(factory.create(Type.ENT));
				continue;
			}
			
			if (ch=='"') {
				invalid("不能有双引号");
				continue;
			}
			
			if (isVar) {
				if (ch=='}') {
					addVar(buff);
					isVar = false;
					continue;
				}
			}
			else if (ch=='$') {
				in.mark(3);
				ch = (char) in.read();
				
				if (ch=='$') continue;
				
				if (ch=='{') {
					addText(buff);
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
		addText(buff);
		
	}
	finally {
		try {if (in!=null) in.close();} catch(Exception e) {} 
		}
	}
	
	private void addText(StringBuilder str) throws IOException {
		if (str.length() > 0) {
			IItem item = factory.create(Type.STR);
			item.init(str.toString());
			texts.add(item);
			items.add(item);
			str.setLength(0);
		}
	}
	
	private void addVar(StringBuilder str) throws IOException {
		String varname = str.toString().trim();
		
		if (varname.length() > 0) {
			checkVarName(varname);
			IItem item = factory.create(Type.VAR);
			item.init(varname);
			variables.put(varname, item);
			items.add(item);
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
		variables	= new HashMap<String, IItem>();
		texts		= new ArrayList<IItem>();
		items		= new ArrayList<IItem>();
	}

	public Map<String, IItem> getVariables() {
		return variables;
	}
	
	/**
	 * 如果文件中有该变量返回true
	 */
	public boolean hasVariable(String vname) {
		return variables.containsKey(vname);
	}
	
	/**
	 * 返回解析后的sql文件中元素的迭代器
	 * 其中，文本已经用双引号包围，而变量则直接返回
	 */
	public Iterator<IItem> getItems() {
		return items.iterator();
	}
	
	/**
	 * 返回文件中的文本元素的迭代器，元素被分开的原因
	 * 可能是中间的变量
	 */
	public Iterator<IItem> getTexts() {
		return texts.iterator();
	}
}
