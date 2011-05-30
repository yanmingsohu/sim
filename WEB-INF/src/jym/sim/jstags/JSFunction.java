// CatfoOD 2009-11-10 下午02:10:56

package jym.sim.jstags;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装javaScript方法
 */
public class JSFunction {
	private String fname;
	private List<String> args;
	
	/**
	 * 封装一个js函数调用
	 * 
	 * @param funcname - js函数名
	 */
	public JSFunction(String funcname) {
		fname = funcname;
		args = new ArrayList<String>();
	}
	
	/**
	 * 把arg作为参数添加到js函数参数列表的末尾<br/>
	 * 如果arg不为null,arg的取值为Object.toString(),否则为null<br/>
	 * 参数作为变量被传递（不以引号包围）
	 * 
	 * @param arg - 参数
	 */
	public void add(Object arg) {
		String s = "null";
		if (arg!=null) {
			s = arg.toString();
		}
		args.add(s);
	}
	
	/**
	 * 把arg作为参数添加到js函数参数列表的末尾<br/>
	 * 如果arg不为null,arg的取值为Object.toString(),否则为"null"<br/>
	 * 参数作为字符串被传递（用引号包围）
	 * 
	 * @param arg - 参数
	 */
	public void addString(Object arg) {
		add("\"" + arg + "\"");
	}
	
	public String toString() {
		return getCallString();
	}
	
	/**
	 * 返回调用js函数的表达式字符串
	 */
	public String getCallString() {
		StringBuilder buff = new StringBuilder();
		buff.append(fname);
		buff.append('(');
		int size = args.size();
		for (int i=0; i<size; ++i) {
			buff.append(args.get(i));
			if (i<size-1) {
				buff.append(',');
			}
		}
		buff.append(')');
		buff.append(';');
		return buff.toString();
	}
	
	public static void main(String[] s) {
		JSFunction js = new JSFunction("func");
		js.add("ssss");
		System.out.println(js);
	}
}
