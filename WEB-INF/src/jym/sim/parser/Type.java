// CatfoOD 2012-2-27 上午11:12:27 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;


public final class Type {
	
	/** 变量类型, 通常需要被实际的参数替换 */
	public final static Type VAR = new Type("variable");
	
	/** 静态文本 */
	public final static Type STR = new Type("string");
	
	/** 换行 */
	public final static Type ENT = new Type("enter");
	
/** ------------------------------------------------------ */
	
	private Type(String name) {
		this.name = name;
	}
	public String toString() {
		return name;
	}
	private String name;
}
