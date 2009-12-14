// CatfoOD 2009-11-10 下午01:39:13

package jym.base.jstags;

import jym.base.tags.ITag;

/**
 * 给表格标签附加颜色效果
 */
public class TableMouseOverColor extends InnerScript {
	private String fc;
	private String sc;
	private String mc;
	
	/**
	 * 使用默认颜色初始化
	 */
	public TableMouseOverColor() {
		this("#ddd", "#f0f0f0", "#faa");
	}
	
	/**
	 * 表格颜色初始化,颜色字符串是有效的css颜色
	 * 
	 * @param fcolor - 奇数行的颜色
	 * @param scolor - 偶数行的颜色
	 * @param mcolor - 鼠标悬停的颜色
	 */
	public TableMouseOverColor(String fcolor, String scolor, String mcolor) {
		super("/jym/javascript/tablecolor.js");
		fc = fcolor;
		sc = scolor;
		mc = mcolor;
	}

	@Override
	public void setTarget(ITag tag) {
		callMethodString("changeTableColor", tag.getID(), fc, sc, mc);
		tag.append(this);
	}
}
