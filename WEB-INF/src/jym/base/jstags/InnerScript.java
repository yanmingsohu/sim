// CatfoOD 2009-11-10 上午09:31:24

package jym.base.jstags;

import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jym.base.tags.HtmlTagBase;
import jym.base.tags.ITag;

/**
 * 嵌入html的脚本
 */
public class InnerScript extends HtmlTagBase implements IJavaScript {
	private Map<Object, URL> jss;
	
	/**
	 * 创建一个空的内部脚本
	 */
	public InnerScript() {
		super("script");
		this.addAttribute("language", "JavaScript");
		jss = new HashMap<Object, URL>();
	}
	
	/**
	 * 创建一个内部脚本，并加载fromfile制定的js文件到标记中
	 * 
	 * @param fromfile - js文件路径，从Class.getResource()中加载文件
	 */
	public InnerScript(String fromfile) {
		this();
		readJavaScript(fromfile);
	}
	
	public void readJavaScript(String fromfile) {
		URL url = null;
		Class<?> c = this.getClass();
		
		while (url==null) {
			url = c.getResource(fromfile);
			ClassLoader loader = c.getClassLoader();
			
			if (loader!=null) {
				c = loader.getClass();
			} else {
				break;
			}
		}
		
		readJavaScript(url);
	}
	
	public void readJavaScript(URL fromurl) {
		if (fromurl!=null) {
			jss.put(fromurl, fromurl);
		} else {
			throw new IllegalArgumentException("cannot find file: " + fromurl);
		}
	}
	
	public Map<Object, URL> getInnerJs() {
		return jss;
	}
	
	/**
	 * 调用js函数,全部参数都是字符串，自动用引号包围<br/>
	 * 其实质把格式化好的函数附加到标记体中
	 * 
	 * @param method - js函数名，不带括号
	 * @param args - 参数列表，自动用引号包围
	 */
	public void callMethodString(String method, Object ...args) {
		JSFunction jsf = new JSFunction(method);
		for (int i=0; i<args.length; ++i) {
			jsf.addString(args[i]);
		}
		callMethod(jsf);
	}
	
	/**
	 * 调用js函数
	 * 
	 * @param func
	 */
	public void callMethod(JSFunction func) {
		append(func.getCallString());
	}
	
	/**
	 * 调用js函数,注意字符串参数应该用引号包围<br/>
	 * 其实质把格式化好的函数附加到标记体中
	 * 
	 * @param method - js函数名，不带括号
	 * @param args - 参数列表
	 */
	public void callMethod(String method, Object ...args) {
		JSFunction jsf = new JSFunction(method);
		for (int i=0; i<args.length; ++i) {
			jsf.add(args[i]);
		}
		callMethod(jsf);
	}

	/**
	 * 添加脚本字符串
	 */
	@Override
	public boolean append(String text) {
		return super.append(text);
	}
	
	/**
	 * 向脚本中写代码
	 */
	@Override
	public PrintWriter creatText() {
		return super.creatText();
	}

	/**
	 * 不支持的方法
	 */
	@Override
	public ITag creat(String newtagname) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 不支持的方法
	 */
	@Override
	public boolean append(ITag tag) {
		throw new UnsupportedOperationException();
	}

	public void setTarget(ITag tag) {
	}
}
