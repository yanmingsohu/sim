// CatfoOD 2009-10-20 下午08:09:12

package jym.base;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import jym.base.tags.HtmlTagBase;
import jym.base.tags.IPrinter;
import jym.base.tags.ITag;
import jym.base.tags.TagBase;

public class HtmlPack implements IPrinter {
	private PrintWriter out;
	private CharArrayWriter buff;
	private ITag head;
	private ITag body;
	private ITag title;
	private TagBase html;
	private boolean closed;
	
	public HtmlPack() {
		buff = new CharArrayWriter();
		out = new PrintWriter(buff);
		
		html = new HtmlTagBase("html");
		head = html.creat("head");
		title= head.creat("title");
		body = html.creat("body");
		closed = false;
	}
	
	public ITag getHead() {
		return head;
	}
	
	public ITag getBody() {
		return body;
	}
	
	public void setTitle(String titlename) {
		title.creatText().print(titlename);
	}
	
	/**
	 * 关闭并写出
	 */
	public void close() {
		checkclosed();
		closed = true;
		html.printout(out);
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
		html.printout(out);
	}
}
