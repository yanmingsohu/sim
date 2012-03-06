// CatfoOD 2010-9-10 上午08:35:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.el;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ILineCounter;

/**
 * 文件解析器，读取文件中的${varname}表达式
 * varname必须符合Java变量命名规范,如果想要输出$则需要$$
 */
public class FileParse implements ILineCounter {
	
	private ParseCore core;
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
		
		Reader in = new BufferedReader(reader);
		
		try {
			filename = _filename;
			core = new ParseCore(in, fact, this);
			
		} catch(IOException e) {
			
			throw new IOException(
					e.getMessage() + ", 在文件 " + filename + 
					" 第" + line + "行" );
			
		} finally {
			
			try { if (in != null) in.close(); } 
			catch(Exception e) { } 
		}
	}

	public Map<String, IItem> getVariables() {
		return core.getVariables();
	}
	
	/**
	 * 如果文件中有该变量返回true
	 */
	public boolean hasVariable(String vname) {
		return core.hasVariable(vname);
	}
	
	/**
	 * 返回解析后的sql文件中元素的迭代器
	 */
	public Iterator<IItem> getItems() {
		return core.getItems();
	}
	
	/**
	 * @see jym.sim.parser.el.ParseCore#getCrc
	 */
	public long getCrc() {
		return core.getCrc();
	}

	public void add() {
		++line;
	}
}
