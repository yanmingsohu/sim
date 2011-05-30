// CatfoOD 2009-12-21 下午08:50:28

package jym.sim.tags;

import java.io.PrintWriter;

/**
 * 可以输出对象
 */
public interface IPrinter {
	
	/**
	 * 当前对象内容写出到out<br>
	 * 由内部实现调用
	 * 
	 * @param out - 向out开炮~
	 */
	void printout(PrintWriter out);
}
