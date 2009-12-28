// CatfoOD 2009-10-20 下午08:44:10

package jym.base.tags;

import java.io.PrintWriter;

/**
 * Xml标记，可以创建子标记与文本节点
 */
public interface ITag extends IPrinter {
	final String SP = "&nbsp;";
	
	/**
	 * 创建一个xml元素节点
	 * @param newtagname - 元素的名称
	 * @return xml元素节点
	 */
	ITag creat(String newtagname);
	
	/**
	 * 创建一个文本节点
	 * @return - 文本节点的输出流
	 */
	PrintWriter creatText();
	
	/**
	 * 添加标签的属性
	 * @param name - 属性的名字
	 * @param value - 属性的值
	 */
	void addAttribute(String name, String value);
	
	/**
	 * 直接添加文本到当前位置，频繁使用影响效率<br/>
	 * 成功返回true
	 */
	boolean append(String text);
	
	/**
	 * 添加标签到当前标签内容的结尾<br/>
	 * 成功返回true
	 */
	boolean append(ITag tag);
	
	/**
	 * 如果是自关闭标记返回true<br>
	 * 自关闭标记不能添加子标记等方法
	 */
	boolean isEndSelf();
	
	/**
	 * 取得标签的ID
	 */
	String getID();
	
	/**
	 * 返回标签名
	 */
	String getTagName();
}
