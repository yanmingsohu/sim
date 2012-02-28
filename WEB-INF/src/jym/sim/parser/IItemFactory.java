// CatfoOD 2012-2-28 上午08:38:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;


public abstract class IItemFactory {
	
	/**
	 * 客户通过该方法创建 IItem, 可以抛出null异常
	 */
	public final IItem create(Type type) throws NullPointerException {
		IItem item = _create(type);
		if (item == null) {
			throw new NullPointerException("无法创建指定的类型: " + type);
		}
		return item;
	}
	
	/**
	 * 子类必须重新该方法, 如果该方法返回null
	 * 则客户调用处会抛出null异常
	 */
	protected abstract IItem _create(Type type);
	
}
