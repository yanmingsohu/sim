// CatfoOD 2010-7-28 下午02:20:32 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.io.IOException;


/**
 * json对象
 */
public interface IjSon {

	/**
	 * 立即把json字符串输出到Appendable(StringBuilder, PrintWriter)中,只包含子变量,无父变量
	 * @param json - 要输出的StringBuilder对象
	 */
	public void go(Appendable json) throws IOException;
	/**
	 * 设置一个变量的名字-值对
	 * @param name - 变量的名字
	 * @param value - 变量的值,可以是另一个IjSon作为数组
	 */
	public void set(Object name, Object value);
	/**
	 * 创建一个name名字的数组变量,如果该数组已经存在则返回它
	 * 如果名字指定的对象不是IjSon返回null
	 * @return - json对象
	 */
	public IjSon createSub(Object name);
	
}
