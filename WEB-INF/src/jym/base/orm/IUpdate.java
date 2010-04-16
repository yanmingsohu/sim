// CatfoOD 2010-4-16 обнГ04:44:50 yanming-sohu@sohu.com/@qq.com

package jym.base.orm;

public interface IUpdate<T> {
	public int add(T model);
	public int update(T model);
	public int delete(T model);
}
