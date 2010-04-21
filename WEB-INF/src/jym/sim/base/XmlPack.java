// CatfoOD 2009-10-25 下午11:51:14

package jym.sim.base;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import jym.sim.tags.IPrinter;
import jym.sim.tags.ITag;
import jym.sim.tags.TagBase;

public class XmlPack implements IPrinter {
	public final static String SYSTEM = "SYSTEM";
	public final static String PUBLIC = "PUBLIC";
	
	private String head = "<?xml version=\"1.0\" encoding=\"%1s\"?>";
	private String doctype = "<!DOCTYPE %1s %2s \"%3s\" \"%4s\">";
	
	private CharArrayWriter buff;
	private PrintWriter out;
	
	private boolean closed = false;
	private String rootnode = "xml";
	private String encode = "ISO-8859-1";
	private String dtdlocal = null;
	private String dtdinfo = null;
	private String pub = PUBLIC;
	private TagBase root;
	
	public XmlPack(String root) {
		rootnode = root;
		this.root = new TagBase(root);
		buff = new CharArrayWriter();
		out = new PrintWriter(buff);
	}
	
	public void setEncoding(String ec) {
		encode = ec;
	}
	
	public void setDTDLocal(String dtdfile) {
		dtdlocal = dtdfile;
	}
	
	public void setPublic(String p) {
		pub = p;
	}
	
	public void setDTDInfo(String dtdinfo) {
		this.dtdinfo = dtdinfo;
	}
	
	public ITag getRoot() {
		return root;
	}
	
	private String getHead() {
		return String.format(head, encode);
	}
	
	private String getDoctype() {
		String s = "";
		if (dtdinfo!=null) {
			s = String.format(doctype, rootnode, pub, dtdinfo, dtdlocal);
		}
		return s;
	}
	
	public void close() {
		checkclosed();
		closed = true;
		out.append(getHead());
		out.append(getDoctype());
		root.printout(out);
		out.close();
	}
	
	/**
	 * 这个方法效率太低，应该用IPrinter写出
	 */
	public String toString() {
		if (!closed) close();
		return buff.toString();
	}
	
	private void checkclosed() {
		if (closed) 
			throw new IllegalStateException("已经关闭");
	}

	public void printout(PrintWriter out) {
		root.printout(out);
	}
	
	public static void main(String[] s) {
		XmlPack xml = new XmlPack("web-app");
		ITag root = xml.getRoot();
		root.creat("servlet");
		System.out.println(xml);
	}
}
