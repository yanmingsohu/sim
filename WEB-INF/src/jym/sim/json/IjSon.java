// CatfoOD 2010-7-28 下午02:20:32 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.util.Collection;
import java.util.Map;



/**
 * json对象
 */
public interface IjSon extends IGo {

	/**
	 * 设置一个变量的名字-值对,如果value==null则输出值是"null"含有引号
	 * 
	 * @param name - 变量的名字
	 * @param value - 变量的值,可以是另一个IjSon作为数组
	 */
	public void set(Object name, Object value);
	
	/**
	 * 创建一个name名字的数组变量,如果该数组已经存在则返回它
	 * 如果名字指定的对象不是IjSon返回null
	 * 
	 * @return - json对象
	 */
	public IjSon createSub(Object name);
	
	/**
	 * 设置一个变量的名字-值对,原生类型直接对应js的类型
	 */
	public void set(Object name, boolean b);
	
	/**
	 * 设置一个变量的名字-值对,原生类型直接对应js的类型
	 */
	public void set(Object name, long i);
	
	/**
	 * 设置一个变量的名字-值对,原生类型直接对应js的类型
	 */
	public void set(Object name, double d);
	
	/**
	 * 设置name指定的值来自于一个集合<br>
	 * name : {<br>
	 * 	1 : c[1],<br>
	 * 	n : c[n]<br>
	 * }<br>
	 */
	public void set(Object name, Collection<?> c);
	
	/**
	 * 设置name制定的值来自于一个Map<br>
	 * name : {<br>
	 * 	key : value,<br>
	 * 	key2: value2<br>
	 * }
	 */
	public void set(Object name, Map<?,?> map);
	
	/**
	 * 设置一个变量，名字是name，他的值来自于bean中的属性
	 * 只有有get方法的属性才能输出的json对象中,
	 * bean中继承的属性<b>不能</b>输出到json中
	 */
	public void setBean(Object name, Object bean); 
	
}
