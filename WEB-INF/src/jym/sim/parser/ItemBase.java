// CatfoOD 2012-2-27 上午11:26:27 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;


public abstract class ItemBase implements IItem {

	public final String toString() {
		return getText();
	}
	
	public Object originalObj() {
		return filter();
	}
	
}
