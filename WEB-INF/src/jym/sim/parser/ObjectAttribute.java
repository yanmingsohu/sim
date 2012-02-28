// CatfoOD 2012-2-28 上午10:00:15 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;

import java.lang.reflect.Field;

/**
 * 对象属性解析器
 */
public final class ObjectAttribute {

	/**
	 * @see #get(Object, String[], int)
	 */
	public static Object get(Object o, String[] names) {
		return get(o, names, 0);
	}
	
	/**
	 * 以此规则解析o : 返回o的names[0 + beginIndex]的属性,
	 * 	保存的对象的names[1 + beginIndex]的属性,
	 * 	保存的对象的names[n + beginIndex]的属性保存的对象<br>
	 * 如果此过程中某个属性为null则返回null, 如果o为null也返回null<br>
	 * 如果names[n]为null则会跳过并解析names[n+1]
	 */
	public static Object get(Object o, String[] names, int beginIndex) {
		Object tar = o;
		if (o != null) {
			try {
				for (int i = beginIndex; i < names.length; ++i) {
					if (names[i] != null) {
						Class<?> cl = tar.getClass();
						Field field = getField(cl, names[i]);
						field.setAccessible(true);
						tar = field.get(tar);
						if (tar == null) break;
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return tar;
	}
	
	/**
	 * 寻找cl类中的fname实例属性, 会遍历到父类
	 */
	public static Field getField(Class<?> cl, String fname) {
		Field field = null;
		while (cl != null) {
			try {
				field = cl.getDeclaredField(fname);
				break;
			} catch (Exception e) {
				cl = cl.getSuperclass();
			}
		}
		return field;
	}
}
