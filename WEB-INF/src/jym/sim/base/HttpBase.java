// CatfoOD 2009-10-20 上午03:10:47

package jym.sim.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.sim.util.BeanUtil;
import jym.sim.util.ForwardProcess;
import jym.sim.util.ICallBack;
import jym.sim.util.ServletData;

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
	public final void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String bclass = config.getInitParameter("beanclass");
		if (bclass!=null) {
			bean = new BeanUtil(bclass);
		}
	}
	
	/**
	 * 被doGet/doPost..包装,其中data对象包含的formbean
	 * 已经被保存在HttpServletRequest,
	 * 用小写类名（不含包名）引用
	 * 
	 * @param data - HttpServlet数据对象
	 * @return	如果返回String类型，则String为有效的mapping路径<br>
	 * 			如果返回IPrinter类型，则打印他，并返回null路径<br>
	 * 			如果返回其他类型，则直接把toString的结果输出到客户端，
	 * 			并返回null路径<br>
	 * @throws Exception
	 */
	public abstract Object execute(IHttpData data) throws Exception;
	
	
	/** 不要直接覆盖这个方法 */
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/** 不要直接覆盖这个方法 */
	protected final void doPost(final HttpServletRequest req, 
			final HttpServletResponse resp)
			throws ServletException, IOException {
		
		Object formbean = common(req, resp);
		HttpData data = new HttpData(req, resp, formbean);
		
		try {
			final Object obj = execute(data);
			ForwardProcess.exec(data, obj, new ICallBack() {

				public void back() throws Exception {
					forward(req, resp, (String)obj);
				}
			});
			
		} catch (ServletException se) {
			throw se;
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 设置编码集，返回formbean
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
	
	private class HttpData extends ServletData implements IHttpData {
		private Object fb;

		public HttpData(HttpServletRequest request, HttpServletResponse response,
				Object formbean) throws IOException {
			
			super(request, response);
			fb = formbean;
		}

		public Object getFormObj() {
			return fb;
		}
	}
}
