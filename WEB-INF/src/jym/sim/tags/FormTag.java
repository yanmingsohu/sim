// CatfoOD 2010-9-7 下午03:44:48 yanming-sohu@sohu.com/@qq.com

package jym.sim.tags;

public class FormTag extends HtmlTagBase {

	public FormTag() {
		super("form");
	}

	/**
	 * 表单递交的uri
	 */
	public void setAction(String uri) {
		super.addAttribute("action", uri);
	}
	
	/**
	 * 添加一个表单域
	 * 
	 * @param type - 表单的类型
	 * @param name - 表单的名字
	 * @return 返回这个表单对象
	 */
	public ITag addInput(String type, String name) {
		ITag div = super.create("div");
		
		ITag lable = div.create("span");
		lable.append(name);
		
		ITag input = div.create("input");
		input.addAttribute("type", type);
		input.addAttribute("name", name);
		
		return input;
	}
	
	/**
	 * 添加一个单行文本表单
	 * 
	 * @param name - 表单的名字
	 * @return 返回这个表单对象
	 */
	public ITag addInput(String name) {
		return addInput("text", name);
	}
	
	/**
	 * 创建递交按钮
	 * @param label - 递交按钮的字面值
	 * @return 返回这个按钮
	 */
	public ITag addSubmit(String label) {
		ITag submit = addInput("submit", "");
		submit.addAttribute("value", label);
		return submit;
	}
}
