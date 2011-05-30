// CatfoOD 2010-1-4 上午10:17:32

package jym.sim.util;

import java.io.PrintWriter;

import jym.sim.json.IjSon;
import jym.sim.tags.IPrinter;

public final class ForwardProcess {
	
	/**
	 * 对exec返回类型的通用处理器
	 * 
	 * @param data - 数据对象
	 * @param obj - 如果是String类型，则String为有效的mapping路径<br>
	 * 			如果是IPrinter类型，则打印他，并返回null路径<br>
	 * 			如果是其他类型，则直接把toString的结果输出到客户端
	 * 
	 * @param back - 如果obj为String类型，则调用bak.back()
	 * @throws Exception
	 */
	public static void exec(IServletData data, Object obj, ICallBack back)
	 throws Exception {
		
		PrintWriter out = data.getHttpServletResponse().getWriter();
		
		if (obj instanceof String) {
			back.back();
		}
		else if (obj instanceof IPrinter) {
			IPrinter prt = (IPrinter) obj;
			prt.printout(out);
		}
		else if (obj instanceof IjSon) {
			IjSon json = (IjSon) obj;
			json.go(out);
		}
		else {
			out.print(obj);
		}
	}
	
}
