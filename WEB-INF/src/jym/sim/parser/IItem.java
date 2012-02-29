// CatfoOD 2012-2-27 上午11:16:40 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;

/**
 * 解析器执行分析后, 取出的元素, 必须有默认构造函数
 */
public interface IItem extends IComponent {
	
	/**
	 * 初始化对象使用的数据
	 */
	public void init(Object... datas);

	/**
	 * 元素类型
	 */
	public Type getType();
	
	/**
	 * 设置过滤方法使用的数据
	 * @throws ParserException - 不支持该方法或数据不符合要求
	 */
	public void setFilterData(Object data) throws ParserException;
	
	/**
	 * 不同的实现有不同的过滤方式
	 */
	public String filter();
	
	/**
	 * 返回的文本是解析到的原始字符串
	 */
	public String getText();
	
}
