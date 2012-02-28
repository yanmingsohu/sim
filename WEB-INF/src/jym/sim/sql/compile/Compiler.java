// CatfoOD 2010-9-9 下午03:44:13 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import jym.sim.parser.IItem;
import jym.sim.parser.el.FileParse;

public class Compiler {

	public static final String MODIFY_FIELD = "_lastModify";
	public static final String END = ";\n";
	public static final String JOIN_STR = "+\t\n";
	public static final int EXIT_OK = 0;
	
	private Info info;
	

	/**
	 * 编译sql文件到java格式
	 * 
	 * @param inf - 保存了文件的相关信息
	 * @throws IOException - 编译失败抛出异常
	 */
	public Compiler(Info inf) throws IOException {
		
		info = inf;
		FileParse parse = new FileParse(inf.getSqlFileName(), inf.openSqlInputStream(), new ComplieFactory());
		
		PrintWriter javaout = inf.openJavaOutputStream();
		
	try {
		javaout.print("package ");
		javaout.print(inf.getPackageName());
		javaout.print(END);
		
		javaout.print("\n\npublic class ");
		javaout.print(inf.getClassName());
		javaout.print(" {\n\n");
		
			writeModifyTime(javaout);
			writeVars(javaout, parse);
			writeConstruction(javaout);
			writeStringFunc(javaout, parse);
			
		javaout.print("\n}");
		
		} 
	finally {
		try {
		if (javaout!=null) 	javaout.close(); } catch (Exception e) {}
		}
	}
	
	/**
	 * 编译已经转换为Java文件的sql为class文件,成功返回true
	 */
	public boolean start() {
		return EXIT_OK == com.sun.tools.javac.Main.compile(new String[] {
				"-g:none",
				info.getJavaFileName()
		});
	}
	
	private void writeModifyTime(PrintWriter out) {
		out.print("\tpublic long ");
		out.print(MODIFY_FIELD);
		out.print(" = ");
		out.print(info.lastModified());
		out.print('l');
		out.print(END);
	}
	
	private void writeVars(PrintWriter out, FileParse parse) {
		Iterator<IItem> vars = parse.getVariables().values().iterator();
		while (vars.hasNext()) {
			out.append("\tpublic Object ");
			out.append(vars.next().filter());
			out.append(END);
		}
	}
	
	private void writeConstruction(PrintWriter out) {
		out.append("\n\tpublic ");
		out.append(info.getClassName());
		out.append("() {}\n");
	}
	
	private void writeStringFunc(PrintWriter out, FileParse parse) {
		out.append("\n\tpublic String toString() {\n\t\treturn "); 
		Iterator<IItem> items = parse.getItems();
		
		if (items.hasNext()) {
			out.append(items.next().filter());
			
			while (items.hasNext()) {
				out.append(JOIN_STR);
				out.append("\t\t\t");
				out.append(items.next().filter());
			}
		} else {
			out.append("\"\"");
		}
		
		out.append(";\n\t} ");
	}
}
