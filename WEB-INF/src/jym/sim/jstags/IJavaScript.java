// CatfoOD 2009-11-10 下午01:46:53

package jym.sim.jstags;

import java.net.URL;
import java.util.Map;

import jym.sim.tags.ITag;

/**
 * js脚本标签
 */
public interface IJavaScript extends ITag {
	
	/**
	 * 将js应用到目标标签中，否则js标签不起作用
	 * 
	 * @param tag - 目标标签
	 */
	void setTarget(ITag tag);
	
	/**
	 * 加载fromfile制定的js文件到标记中<br/>
	 * fromfile为标准外部js文件
	 * 
	 * @param fromfile - js文件路径，从Class.getResource()中加载文件
	 */
	void readJavaScript(String fromfile);
	
	/**
	 * 加载外部js到标签中
	 * 
	 * @param fromurl - js文件的url
	 */
	void readJavaScript(URL fromurl);
	
	/**
	 * 返回从文件加载的脚本URL
	 * Object 是脚本的标识，如果标识重复，则脚本只被输出一次
	 */
	Map<Object, URL> getInnerJs();
}
