// CatfoOD 2012-2-27 下午02:22:44 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;


public class ParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParserException() {
	}
	
	public ParserException(String message) {
		super(message);
	}
	
	public ParserException(Exception e) {
		super(e);
	}
}
