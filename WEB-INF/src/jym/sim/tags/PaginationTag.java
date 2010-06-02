// CatfoOD 2009-12-14 下午09:33:05

package jym.sim.tags;

import java.io.PrintWriter;

public class PaginationTag extends HtmlTagBase {
	/** tag元素class属性的前缀 */
	public final static String CSS_CLASS_NAME = "sim_pagination";
	public final static String CSS_CLASS_FIRSTPAGE = "sim_pagination_firstpage";
	public final static String CSS_CLASS_LASTPAGE = "sim_pagination_lastpage";
	public final static String CSS_CLASS_JUMPPAGE = "sim_pagination_page_jump";
	public final static String CLASS_ATTR = "class";
	public final static String HREF_ATTR = "href";
	public final static String TAG_A = "a";
	
	private int current;
	private int total;
	private int dispsize;
	private String url;
	
	/**
	 * 生成分页html
	 */
	public PaginationTag() {
		super("div");
		super.addAttribute(CLASS_ATTR, CSS_CLASS_NAME);
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
	public void setUrlPattern(String _url) {
		url = _url; //up.replaceAll("%page", "%d");
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
		
	if (start+1>end) return;
		
		ITag stag = new HtmlTagBase(TAG_A);
		stag.addAttribute(HREF_ATTR, getUrl(1));
		stag.addAttribute(CLASS_ATTR, CSS_CLASS_FIRSTPAGE);
		stag.append("首页");
		append(stag);
		
		for (int i=start; i<=end; ++i) {
			appendSpace();
			ITag atag = new HtmlTagBase(TAG_A);
			atag.addAttribute(CLASS_ATTR, CSS_CLASS_JUMPPAGE);
			if (i!=current) {
				atag.addAttribute(HREF_ATTR, getUrl(i));
				atag.append(String.valueOf(i));
			} else {
				atag.append("["+ (i) +"]");
			}
			append(atag);
			appendSpace();
		}
		
		ITag etag = new HtmlTagBase(TAG_A);
		etag.addAttribute(HREF_ATTR, getUrl(total));
		etag.addAttribute(CLASS_ATTR, CSS_CLASS_LASTPAGE);
		etag.append("末页");
		append(etag);
	}
	
	private void appendSpace() {
		append("&nbsp;");
	}
	
	private String getUrl(int page) {
		return url.replaceAll("%page", Integer.toString(page));
	}
}
