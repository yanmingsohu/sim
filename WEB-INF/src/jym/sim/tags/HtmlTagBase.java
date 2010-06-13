// CatfoOD 2009-11-10 下午07:53:09

package jym.sim.tags;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.sim.jstags.IJavaScript;
import jym.sim.util.ResourceLoader;

/**
 * html标记
 */
public class HtmlTagBase extends TagBase {
	
	private ArrayList<IJavaScript> innerjs;
	private Map<Object, URL> amassjs;
	private PrintWriter insertText;
	
	
	public HtmlTagBase(String tagname) {
		super(tagname);
		innerjs = new ArrayList<IJavaScript>();
		insertText = super.createText();
	}

	/**
	 * 添加一个IJavaScript的行为与xml标记不同<br/>
	 * 如果tag是一个IJavaScript脚本会被添加在标记结尾的后面
	 */
	@Override
	public boolean append(ITag tag) {
		boolean r;
		if (tag instanceof IJavaScript) {
			r = innerjs.add((IJavaScript) tag);
			send2Root((IJavaScript) tag);
		} else {
			r = super.append(tag);
		}
		return r;
	}
	
	public ITag create(String newtagname) {
		HtmlTagBase tag = null;
		if (!super.isEndSelf()) {
			tag = new HtmlTagBase(newtagname);
			if (!super.append(tag)) {
				tag = null;
			}
		}
		return tag;
	}

	@Override
	public void printout(PrintWriter out) {
		if (amassjs!=null)  {
			printAmassJs();
			clearAmassJs();
		}
		
		super.printout(out);
		
		for (int i=0; i<innerjs.size(); ++i) {
			IJavaScript ijs = innerjs.get(i);
			ijs.printout(out);
		}

	}

	/**
	 * 把外部脚本发送到根元素
	 * @param ijs
	 */
	private void send2Root(IJavaScript ijs) {
		ITag root = super.getRoot();

		if (root!=null) {
			if (root instanceof HtmlTagBase) {
				HtmlTagBase rh = (HtmlTagBase) root;
				rh.amass(ijs);
			} else {
				System.out.println("WARN: ["
						+ root.getTagName() + "] is not HtmlTag");
			}
		}
	}
	
	/**
	 * 打印脚本
	 * @param target - 输出目标
	 * @param jsmap - 脚本容器
	 */
	private void printAmassJs() {		
		insertText.append("<script language='JavaScript'>");
		
		Iterator<URL> it = amassjs.values().iterator();
		while (it.hasNext()) {
			ResourceLoader.urlWriteOut(it.next(), insertText);
		}
		
		insertText.append("</script>");
	}
	
	/**
	 * 清除收集到得脚本，因为脚本已经被写出
	 */
	private void clearAmassJs() {
		amassjs = null;
	}
	
	/** 
	 * 收集ijs中的所有脚本 <br/>
	 * 同时这个方法被调用过，说明当前元素是根元素
	 */
	private void amass(IJavaScript ijs) {
		if (amassjs==null) {
			amassjs = new HashMap<Object, URL>();
		}
		amassjs.putAll(ijs.getInnerJs());
	}
}
