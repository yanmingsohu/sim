// CatfoOD 2010-4-30 上午08:54:36 yanming-sohu@sohu.com/@qq.com

package jym.sim.exception;

public class SimException extends Exception {

	private static final long serialVersionUID = 1474745426650151331L;

	public SimException() {
	}
	
	public SimException(String s) {
		super(s);
	}
	
	public SimException(Throwable e) {
		super(e);
		setStackTrace(e.getStackTrace());
	}
}
