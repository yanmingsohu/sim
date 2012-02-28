// CatfoOD 2012-2-27 下午01:29:41 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.el.FileParse;
import jym.sim.util.Tools;


/**
 * 读取sql, 使用拼接字符串的方式取得结果sql
 */
public class SqlLink implements ISqlReader {
	
	private Map<String, IItem> vars;
	private FileParse parse;
	private String fname;
	private URL url;
	
	
	public SqlLink(String filename) throws IOException {
		url = getClass().getResource(filename);
		if (url == null) {
			throw new IOException("找不到文件: " + filename);
		}
		
		fname = filename;
		checkAndRead();
	}
	
	private void checkAndRead() throws IOException {
		int i = fname.lastIndexOf('/');
		fname = fname.substring(i + 1);
		
		parse = new FileParse(fname, 
				new InputStreamReader(url.openStream(), SQL_FILE_CODE), 
				new LinkFactory() );
		
		vars = parse.getVariables();
	}

	public String getResultSql() {
		StringBuilder sql = new StringBuilder();
		Iterator<IItem> itr = parse.getItems();
		
		while (itr.hasNext()) {
			sql.append(itr.next().filter());
		}
		
		return sql.toString();
	}

	public void set(String name, Object value) throws NoSuchFieldException {
		IItem item = vars.get(name);
		if (item == null) {
			throw new NoSuchFieldException("找不到变量: " + name);
		}
		item.init(null, value);
	}

	public void showSql() {
		Tools.plsql(getResultSql());
	}
}
