// CatfoOD 2009-10-20 上午03:10:47

package jym.sim.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

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
import jym.sim.util.Tools;

/**
 * 默认使用中文编码<br>
 * 当前支持get/post<br>
 * 
 * 请求的URL格式: <servlet>?[do=<method>]<br>
 * 		如果有do参数，则执行相对的方法
 */
public abstract class HttpBase<BEAN> extends HttpServlet {
	
	private static final long serialVersionUID = 4056930472082034056L;
	private static final String PARM_CLASSNAME = "bean-class";
	private static final String PARM_CHARSET = "charset";
	private static final String PARM_METHOD = "do";
	private static final String DEFAULT_METHOD = "execute";
	
	private static String charset = null;
	private BeanUtil<BEAN> bean = null;
	
	
	/**
	 * 从InitParameter中读取beanclass的值
	 * 用post来的数据初始化这个bean
	 */
	@Override
	public final void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		Class<BEAN> bc = getBeanClass();
		
		if (bc!=null) {
			bean = new BeanUtil<BEAN>(bc);
		} else {
			String bclass = config.getInitParameter(PARM_CLASSNAME);
			
			if (bclass!=null) {
				bean = new BeanUtil<BEAN>(bclass);
			} else {
				Tools.pl(PARM_CLASSNAME + " init-param not set, or getBeanClass() return NULL");
			}
		}
		
		if (charset==null) {
			charset = config.getServletContext().getInitParameter(PARM_CHARSET);
			try {
				Charset _cs = Charset.forName(charset);
				charset = _cs.name();
			} catch(Exception e) {
				Tools.pl(PARM_CHARSET + " context-param not set." + e);
				charset = Charset.defaultCharset().name();
			}
		}
		
		init2(config);
	}
	
	/**
	 * 子类重写在init方法之后被调用
	 * @param config
	 * @throws ServletException
	 */
	protected void init2(ServletConfig config) throws ServletException {
	}
	
	/**
	 * 返回实体类的类型, 默认返回null, 此时需要在web.xml中配置实体类型<br>
	 * 推荐使用该方法配置类型, 并且该方法的优先级更高
	 */
	protected Class<BEAN> getBeanClass() {
		return null;
	}
	
	/**
	 * 被doGet/doPost..包装,其中data对象包含的formbean
	 * 已经被保存在HttpServletRequest,用小写类名（不含包名）引用,<br>
	 * 如果请求uri没有指定方法,则该方法被调用,该方法默认不执行操作
	 * 
	 * @param data - HttpServlet数据对象
	 * @return	如果返回String类型，则String为有效的mapping路径<br>
	 * 			如果返回IPrinter类型，则打印他，并返回null路径<br>
	 * 			如果返回其他类型，则直接把toString的结果输出到客户端，
	 * 			并返回null路径<br>
	 * @throws Exception
	 */
	public Object execute(IHttpData<BEAN> data) throws Exception {
		Tools.pl(getClass() + " servlet do nothing, " +
				"must rewrite execute method.");
		return null;
	}
	
	/**
	 * 在方法被调用前插入操作,如果抛出异常或返回false则不再执行后继方法<br>
	 * 默认总是返回true
	 * @param methodName - 请求的方法,不会为null
	 */
	public boolean before(IHttpData<BEAN> data, String methodName) throws Exception {
		return true;
	}
	
	
	/** 不要直接覆盖这个方法 */
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/** 不要直接覆盖这个方法 */
	protected final void doPost(final HttpServletRequest req, 
			final HttpServletResponse resp)
			throws ServletException, IOException {

		charCoding(req, resp);
		BEAN formbean = packBean(req, resp);
		HttpData data = new HttpData(req, resp, formbean);
		
		String methodName = data.getParameter(PARM_METHOD);
		if (methodName==null) methodName = DEFAULT_METHOD;
		
		try {
			if ( !before(data, methodName) ) return;
			
			final Object obj = callFunction(data);
			
			if (obj!=null) {
				ForwardProcess.exec(data, obj, new ICallBack() {
					public void back() throws Exception {
						forward(req, resp, (String)obj);
					}
				});
			}
			
		} catch (ServletException se) {
			throw se;
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			throw new ServletException("servlet execute error", e);
		}
	}
	
	protected Object callFunction(IHttpData<BEAN> data) throws Exception {
		
		String methodName = data.getParameter(PARM_METHOD);
		Object result = null;
		
		if (methodName!=null) {
			Method method = getClass().getMethod(methodName, IHttpData.class);
			result = method.invoke(this, data);
		} else {
			result = execute(data);
		}
		return result;
	}
	
	private BEAN packBean(HttpServletRequest req, HttpServletResponse resp) {
		BEAN formbean = null;
		if (bean!=null) {
			formbean = bean.creatBean(req);
			req.setAttribute(bean.getBeanName(), formbean);
		}
		return formbean;
	}
	
	private void charCoding(HttpServletRequest req, HttpServletResponse resp){
		resp.setContentType("text/html; charset=" + charset);
		resp.setCharacterEncoding(charset);
		try {
			req.setCharacterEncoding(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
	
	private class HttpData extends ServletData implements IHttpData<BEAN> {
		private BEAN fb;

		public HttpData(HttpServletRequest request, HttpServletResponse response,
				BEAN formbean) throws IOException {
			
			super(request, response);
			fb = (BEAN) formbean;
		}

		public BEAN getFormObj() {
			return fb;
		}
	}
}
