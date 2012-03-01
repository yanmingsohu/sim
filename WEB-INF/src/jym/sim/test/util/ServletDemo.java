// CatfoOD 2011-7-12 下午02:28:51 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.util;

import jym.sim.base.HttpBase;
import jym.sim.base.IHttpData;
import jym.sim.util.Tools;


public class ServletDemo extends HttpBase<UserBean> {

	private static final long serialVersionUID = 3128609912045893646L;

	@Override
	public Object execute(IHttpData<UserBean> data) throws Exception {
		Tools.pl(data.getFormObj().getName());
		return "/WEB-INF/test/hello.jsp";
	}

	@Override
	protected Class<UserBean> getBeanClass() {
		return UserBean.class;
	}
}
