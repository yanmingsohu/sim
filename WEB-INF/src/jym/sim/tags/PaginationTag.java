// CatfoOD 2009-12-14 下午09:33:05

package jym.sim.tags;

import java.io.PrintWriter;

public class PaginationTag extends HtmlTagBase {
	
	public final static String CSS_CLASS_NAME = "sim_pagination";
	
	private int current;
	private int total;
	private int dispsize;
	private String url;
	
	/**
	 * 生成分页html
	 */
	public PaginationTag() {
		super("div");
		super.addAttribute("class", CSS_CLASS_NAME);
		dispsize = 10;
	}

	/**
	 * 当前停留的页码,页码有效值1~*
	 */
	public void setCurrentPage(int p) {
		current = p;
	}
	
	/**
	 * 总页码
	 */
	public void setTotalPage(int t) {
		total = t;
	}
	
	/**
	 * 分页提示的数量
	 */
	public void setDisplaySize(int s) {
		dispsize = s/2;
	}
	
	/**
	 * 设置超链接字符串，字符串中的%page转换为页码值
	 */
	public void setUrlPattern(String up) {
		url = up.replaceAll("%page", "%d");
	}

	@Override
	public void printout(PrintWriter out) {
		createPagination();
		super.printout(out);
	}
	
	private void createPagination() {
		int start = current - dispsize;
		if (start<1) start = 1;
		int end = current + dispsize;
		if (end>total) end = total;
		
	if (start+2>end) return;
		
		ITag stag = new HtmlTagBase("a");
		stag.addAttribute("href", String.format(url, 1));
		stag.append("<<<");
		append(stag);
		
		for (int i=start; i<=end; ++i) {
			appendSpace();
			ITag atag = new HtmlTagBase("a");
			if (i!=current) {
				atag.addAttribute("href", String.format(url, i));
				atag.append(String.valueOf(i));
			} else {
				atag.append("["+ (i) +"]");
			}
			append(atag);
			appendSpace();
		}
		
		ITag etag = new HtmlTagBase("a");
		etag.addAttribute("href", String.format(url, total));
		etag.append(">>>");
		append(etag);
	}
	
	private void appendSpace() {
		append("&nbsp;");
	}
}
