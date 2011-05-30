// CatfoOD 2010-10-16 下午08:14:02

package jym.sim.json;

import java.io.IOException;

public interface IGo {
	/**
	 * 立即把json字符串输出到Appendable(StringBuilder, PrintWriter)中,
	 * 只包含子变量,无父变量
	 * 
	 * @param json - 要输出的StringBuilder对象
	 */
	public void go(Appendable json) throws IOException;
}
