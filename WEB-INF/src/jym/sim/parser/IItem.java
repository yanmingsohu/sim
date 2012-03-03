// CatfoOD 2012-2-27 上午11:16:40 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;

/**
 * 解析器执行分析后, 取出的元素, 必须有默认构造函数
 */
public interface IItem extends IComponent {
	
	/**
	 * 初始化对象使用的数据<br><br>
	 * 
	 * <b>变量的初始化约定:</b><br>
	 * 参数1 : 变量名,可以通过改变名字来引用该元素的子属性<br>
	 * 参数2 : 变量值<br>
	 * 参数3 : 引用另一个 IItem 的 value
	 */
	public void init(Object... datas);

	/**
	 * 元素类型
	 */
	public Type getType();
	
	/**
	 * 不同的实现有不同的过滤方式<br>
	 * 如果是变量, 通过init()改变变量名, 可以引用变量的属性
	 */
	public String filter();
	
	/**
	 * 返回的文本是解析到的原始字符串
	 */
	public String getText();
	
	/**
	 * 返回使用init(..)设置的原始对象
	 */
	public Object originalObj();
	
	/**
	 * 创建一个与当前实现相同的对象
	 */
	public IItem newInstance();
}
