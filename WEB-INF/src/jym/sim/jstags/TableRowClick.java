// CatfoOD 2009-11-10 下午02:03:27

package jym.sim.jstags;

import java.util.ArrayList;
import java.util.List;

import jym.sim.tags.ITag;

public class TableRowClick extends InnerScript {
	private List<String> funcs;

	public TableRowClick() {
		super("/jym/javascript/tableClick.js");
		funcs = new ArrayList<String>();
	}
	
	/**
	 * 添加一个监听函数到表格行，监听函数的格式：<br>
	 * 		function clickListener(row, rowindex) {}
	 * 
	 * <li>row为当前触发事件的表格行对象</li>
	 * <li>rowindex为表格所在的行的索引，从0开始，包含表格头</li>
	 * 
	 * <br/>监听函数来自jsurl指定的资源
	 * 
	 * @param jsurl - 脚本文件所在的资源url
	 * @param funname - 函数的名字
	 */
	public void appendCall(String jsurl, String funname) {
		readJavaScript(jsurl);
		funcs.add(funname);
	}
	
	/**
	 * 	
	 * 注意顺序，这个方法必须在表格的数据已经加载结束的时候调用
	 *
	 */
	@Override
	public void setTarget(ITag tag) {
		for (int i=0; i<funcs.size(); ++i) {
			JSFunction func = new JSFunction("tableRowMouseOverListener");
			func.addString(tag.getID());
			func.add(funcs.get(i));
			callMethod(func);
			tag.append(this);
		}
	}
}
