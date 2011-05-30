// CatfoOD 2010-1-4 上午09:43:35

package jym.sim.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ServletData implements IServletData {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession ses;
	private PrintWriter out;
	
	public ServletData(HttpServletRequest request, HttpServletResponse response) 
	 throws IOException {
		req = request;
		resp= response;
		ses = req.getSession();
		out =resp.getWriter();
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return req;
	}

	public HttpServletResponse getHttpServletResponse() {
		return resp;
	}

	public String getParameter(String name) {
		return req.getParameter(name);
	}
	
	public void setAttribute(String name, Object obj) {
		req.setAttribute(name, obj);
	}

	public Object getAttribute(String name) {
		return req.getAttribute(name);
	}
	
	public void setSessionAttribute(String name, Object obj) {
		ses.setAttribute(name, obj);
	}

	public Object getSessionAttribute(String name) {
		return ses.getAttribute(name);
	}
	
	public void print(Object data) {
		out.print(data);
	}
}
