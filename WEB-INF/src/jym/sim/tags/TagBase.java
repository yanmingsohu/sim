// CatfoOD 2009-10-20 下午08:21:54

package jym.sim.tags;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.sim.util.Tools;

/**
 * 标准xml标记,不支持内嵌的js <br/>
 * 如果需要内嵌js，需要使用HtmlTagBase
 */
public class TagBase implements ITag {
	
	private String stag;
	private String etag;
	private String tagname;
	private ArrayList<Object> subtags;
	private Map<String,String> attrib;
	private TagBase parent = null;
	/** 是否是自终结标记 */
	private boolean isSelf;
	
	
	/**
	 * 标准xml标记,不支持内嵌的js <br/>
	 * 如果需要内嵌js，需要使用HtmlTagBase
	 */
	public TagBase(String tagname) {
		this(tagname, false);
	}
	
	public TagBase(String tagname, boolean endself) {
		isSelf = endself;
		this.tagname = tagname;
		stag = '<' + tagname;
		etag = "</"+ tagname + '>';
		subtags = new ArrayList<Object>();
		attrib = new HashMap<String,String>();
	}
	
	public String getID() {
		String id = attrib.get("id");
		if (id==null) {
			id = Tools.creatTagID(tagname);
			addAttribute("id", id);
		}
		return id;
	}
	
	public ITag create(String newtagname) {
		TagBase tag = null;
		if (!isSelf) {
			tag = new TagBase(newtagname);
			subtags.add(tag);
		}
		return tag;
	}
	
	public PrintWriter createText() {
		PrintWriter out = null;
		if (!isSelf) {
			CharArrayWriter buff = new CharArrayWriter();
			out = new PrintWriter(buff);
			subtags.add(buff);
		}
		return out;
	}
	
	public boolean append(String text) {
		if (!isSelf) {
			if (text!=null) {
				subtags.add(text);
				return true;
			}
		}
		return false;
	}
	
	public boolean append(ITag tag) {
		if (!isSelf) {
			if (tag!=null) {
				subtags.add(tag);
				
				if (tag instanceof TagBase) {
					TagBase tb = (TagBase) tag;
					tb.setParent(this);
				}
				return true;
			}
		}
		return false;
	}
	
	public void addAttribute(String name, String value) {
		if (name!=null && value!=null) {
			attrib.put(name, value);
		}
	}
	
	public void printout(PrintWriter out) {
		out.print(getAttribs());

		if (!isSelf) {
			printOut(subtags, out);
			out.print(etag);
		}
	}
	
	private void printOut(ArrayList<?> list, PrintWriter out) {
		for (int i=0; i<list.size(); ++i) {
			Object o = list.get(i);
			
			if (o instanceof TagBase) {
				TagBase tag = (TagBase) o;
				tag.printout(out);
			}
			else {
				out.print(o);
			}
		}	
	}
	
	private String getAttribs() {
		StringBuilder buff = new StringBuilder();
		buff.append(stag);
		Iterator<String> it = attrib.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			String valu = attrib.get(name);
			buff.append(' ');
			buff.append(name);
			buff.append("=\"");
			buff.append(valu);
			buff.append("\"");
		}
		if (isSelf) {
			buff.append('/');
		}
		buff.append('>');
		
		return buff.toString();
	}
	
	public boolean isEndSelf() {
		return isSelf;
	}
	
	protected void setParent(TagBase p) {
		parent = p;
	}
	
	protected TagBase getParent() {
		return parent;
	}
	
	/**
	 * 返回跟节点，如果返回null说明当前节点为无上层节点
	 */
	protected ITag getRoot() {
		TagBase tag = (TagBase) getParent();
		ITag tp = null;
		
		while (tag!=null) {
			tp = tag;
			tag = (TagBase) tag.getParent();
		}
		
		if (tp==null) tp = this;
		
		return tp;
	}
	
	public String toString() {
		CharArrayWriter buff = new CharArrayWriter();
		PrintWriter out = new PrintWriter(buff);
		printout(out);
		out.flush();
		return buff.toString();
	}

	public String getTagName() {
		return tagname;
	}

}
