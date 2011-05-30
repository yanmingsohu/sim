// CatfoOD 2010-9-9 下午04:09:20 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import jym.sim.util.Tools;


/**
 * 保存要编译sql文件的相关信息
 */
class Info {

	public static final String DEFAULT_PACKAGE = "";
	public static final String JAVA = ".java";
	public static final String CODE = "UTF-8";
	
	private String packageName;
	private String javaFileName;
	private String className;
	private String sqlFileName;
	private String fullClassName;
	private URL srcFile;
	
	private File sql;
	private File java;
	
	
	public Info(String filename, URL sqlFile) {
		this(filename);
		setUrl(sqlFile);
	}
	
	public Info(String filename) {
		sqlFileName = filename;
		parsePackage();
		fullClassName = packageName + "." + className;
	}
	
	public void setUrl(URL sqlFileUrl) {
		srcFile = sqlFileUrl;
		parseJavaFile();
		
		java = new File(javaFileName);
		sql = new File(srcFile.getFile());
	}
	
	private void parsePackage() {
		int endindex = sqlFileName.lastIndexOf('/');
		
		if (endindex>0) {
			char[] src = sqlFileName.toCharArray();
			
			Tools.replaceAll(src, '/', '.');
			packageName = new String(src, 1, endindex-1);
			
			Tools.replaceAll(src, '.', '_');
			className = new String(src, endindex+1, src.length-endindex-1);
		}
	}
	
	private void parseJavaFile() {
		String path = srcFile.getFile();
		int end = path.lastIndexOf('/') + 1;
		path = path.substring(0, end);
		
		javaFileName = path + className + JAVA;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getJavaFileName() {
		return javaFileName;
	}

	public String getClassName() {
		return className;
	}

	public String getSqlFileName() {
		return sqlFileName;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public URL getSrcFile() {
		return srcFile;
	}
	
	/**
	 * 清除中间文件和原始sql文件
	 */
	public void clear() {
		java.delete();
		sql.delete();
	}
	
	public long lastModified() {
		return sql.lastModified();
	}
	
	public InputStreamReader openSqlInputStream() throws IOException {
		return new InputStreamReader(srcFile.openStream(), CODE);
	}
	
	public PrintWriter openJavaOutputStream() throws IOException {
		return new PrintWriter(javaFileName, CODE);
	}
}
