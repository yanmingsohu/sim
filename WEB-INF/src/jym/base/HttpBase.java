// CatfoOD 2009-10-20 上午03:10:47

package jym.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.base.util.BeanUtil;

/**
 * 默认使用中文编码<br>
 * 当前支持get/post
 */
public abstract class HttpBase extends HttpServlet {
	
	private static final long serialVersionUID = 4056930472082034056L;
	private BeanUtil bean = null;
	
	/**
	 * 从InitParameter中读取beanclass的值
	 * 用post来的数据初始化这个bean
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String bclass = config.getInitParameter("beanclass");
		if (bclass!=null) {
			bean = new BeanUtil(bclass);
		}
	}

	/** 不要直接覆盖这个方法 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Object formbean = common(req, resp);
		String path = get(req, resp, formbean);
		forward(req, resp, path);
	}

	/** 不要直接覆盖这个方法 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Object formbean = common(req, resp);
		String path = post(req, resp, formbean);
		forward(req, resp, path);
	}
	
	private Object common(HttpServletRequest req, HttpServletResponse resp) {
		charencoding(req,resp);
		Object formbean = null;
		if (bean!=null) {
			formbean = bean.creatBean(req);
			req.setAttribute(bean.getBeanName(), formbean);
		}
		return formbean;
	}
	
	private void charencoding(HttpServletRequest req, HttpServletResponse resp){
		if (req.getLocale().equals(Locale.CHINA)) {
			resp.setContentType("text/html; charset=gbk");
			resp.setCharacterEncoding("gbk");
			try {
				req.setCharacterEncoding("gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void forward(HttpServletRequest req, HttpServletResponse resp, String path) 
			throws ServletException, IOException {
		
		if (path!=null) {
			RequestDispatcher rd = req.getRequestDispatcher(path);
			if (rd!=null) {
				rd.forward(req, resp);
			}
		}
	}
	
	/**
	 * 被doGet包装,其中formbean已经被保存在HttpServletRequest,用小写类名（不含包名）引用
	 * 
	 * @param req - 请求
	 * @param resp - 响应
	 * @param formbean - web.xml配置中beanclass属性的对象，使用post/get参数初始化
	 * @return String - 转发的路径，可以为null
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String get(HttpServletRequest req, HttpServletResponse resp, Object formbean)
		throws ServletException, IOException {
		return null;
	}
	
	/**
	 * 被doPost包装,其中formbean已经被保存在HttpServletRequest,用小写类名（不含包名）引用
	 * 
	 * @param req - 请求
	 * @param resp - 响应
	 * @param formbean - web.xml配置中beanclass属性的对象，使用post/get参数初始化
	 * @return String - 转发的路径，可以为null
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String post(HttpServletRequest req, HttpServletResponse resp, Object formbean)
		throws ServletException, IOException {
		return null;
	}
}
