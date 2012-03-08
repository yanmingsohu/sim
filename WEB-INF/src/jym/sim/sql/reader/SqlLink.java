// CatfoOD 2012-2-27 下午01:29:41 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.Type;
import jym.sim.parser.el.FileParse;
import jym.sim.util.Tools;


/**
 * 读取sql模板文件, 处理动态变量, 解析表达式和命令, 返回处理后的sql
 * 
 * @see jym.sim.parser.cmd.ICommand 文件中可以有命令
 * @see jym.sim.parser.el.ParseCore 文件解析器核心, 如何处理动态变量
 */
public class SqlLink implements ISqlReader {
	
	private IItemFactory itemFact;
	private long protectKey;
	private Map<String, IItem> vars;
	private FileParse parse;
	private String fname;
	private URL url;
	
	/**
	 * filename 指定的文件与 Object 类处于同一个目录(或包)
	 * @throws IOException 
	 */
	public SqlLink(Object usePackage, String filename) throws IOException {
		this( usePackage.getClass().getResource(filename) );
	}
	
	/**
	 * filename 指定的文件与 cl 的类处于同一个目录(或包)
	 * @throws IOException 
	 */
	public SqlLink(Class<?> cl, String filename) throws IOException {
		this( cl.getResource(filename) );
	}
	
	/**
	 * filename 必须是绝对路径使用 '/../../...' 格式
	 * @throws IOException 
	 */
	public SqlLink(String filename) throws IOException {
		this( SqlLink.class.getResource(filename) );
	}
	
	public SqlLink(URL file) throws IOException {
		if (file == null) {
			throw new IOException("找不到文件");
		}
		protectKey	= 0l;
		fname		= file.getFile();
		url			= file;
		itemFact	= new LinkFactory();
		checkAndRead();
	}
	
	private void checkAndRead() throws IOException {
		int i = fname.lastIndexOf('/');
		fname = fname.substring(i + 1);
		
		parse = new FileParse(fname, 
				new InputStreamReader(url.openStream(), SQL_FILE_CODE), 
				itemFact );
		
		vars = parse.getVariables();
	}

	public String getResultSql() {
		if (protectKey == 0) {
			Tools.pl(url, "文件的锁定码为: ", parse.getCrc() + "L");
			
		} else if (protectKey != parse.getCrc()) {
			throw new AccessControlException(fname + "(" +parse.getCrc() + 
					"L) 文件校验码不符, bad:" + protectKey + "L, 读取sql文件失败.");
		}
		
		StringBuilder sql = new StringBuilder();
		Iterator<IItem> itr = parse.getItems();
		
		while (itr.hasNext()) {
			sql.append(itr.next().filter());
		}
		
		return sql.toString();
	}

	public void set(String name, Object value) {
		IItem item = vars.get(name);
		if (item == null) {
			item = itemFact.create(Type.VAR);
			vars.put(name, item);
		}
		item.init(null, value);
	}
	
	public void lockFile(long protectKey) {
		this.protectKey = protectKey;
	}

	public void showSql() {
		Tools.pl(getResultSql());
	}
}
