package jym.sim.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetFilter implements Filter {

	String encode;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		request.setCharacterEncoding(encode);
		response.setCharacterEncoding(encode);
		
		chain.doFilter(request, response);
	}

	public void init(FilterConfig init) throws ServletException {
		encode = init.getInitParameter("encode");
	}

}
