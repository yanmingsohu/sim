// CatfoOD 2009-12-21 下午08:34:38

package jym.sim.css;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jym.sim.tags.HtmlTagBase;
import jym.sim.tags.ITag;
import jym.sim.util.ResourceLoader;

public final class InnerCss extends HtmlTagBase {
	private List<Css> cs;

	public InnerCss() {
		super("style");
		cs = new ArrayList<Css>();
	}
	
	/**
	 * 从外部文件中载入样式表
	 * @param fromClassPath - 样式表所在的路径,路径以 '/' 开始
	 */
	public InnerCss(String fromClassPath) {
		this();
		
		URL url = ResourceLoader.getUrl(fromClassPath);
		if (url!=null) {
			PrintWriter out = this.createText();
			ResourceLoader.urlWriteOut(url, out);
		} else {
			System.out.println("InnerCss: 找不到url ("+fromClassPath+")");
		}
	}

	@Override
	public void printout(PrintWriter out) {
		printCss(out);
		super.printout(out);
	}
	
	private void printCss(PrintWriter out) {
		Iterator<Css> it = cs.iterator();
		while (it.hasNext()) {
			it.next().printout(out);
		}
	}

	/**
	 * 直接向样式表中添加文本，注意格式
	 */
	@Override
	public boolean append(String text) {
		return super.append(text);
	}
	
	/**
	 * 向样式表中添加样式
	 */
	public boolean append(Css css) {
		return cs.add(css);
	}
	
	/**
	 * 直接向样式表中添加文本，注意格式
	 */
	@Override
	public PrintWriter createText() {
		return super.createText();
	}

	// ------------------------------------- 不支持的方法
	
	private void unsupport() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean append(ITag tag) {
		unsupport();
		return false;
	}

	@Override
	public ITag create(String newtagname) {
		unsupport();
		return null;
	}
	
	@Override
	public void addAttribute(String name, String value) {
		unsupport();
		super.addAttribute(name, value);
	}
}
