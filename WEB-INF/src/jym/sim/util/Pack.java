// CatfoOD 2012-3-15 上午09:17:41 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;


/**
 * 在函数间传递不可变对象, 使之可以改变值
 */
public class Pack<T> {
	
	public T value;

	
	public Pack() {
	}
	
	public Pack(T v) {
		value = v;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
