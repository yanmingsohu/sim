// CatfoOD 2012-2-28 上午10:00:15 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
	 * 以此规则解析o : 返回o的names[0 + beginIndex]的属性(或方法),
	 * 	保存的对象的names[1 + beginIndex]的属性(或方法),
	 * 	保存的对象的names[n + beginIndex]的属性(或方法)保存的对象<br>
	 * 如果此过程中某个属性(或方法)为null则返回null, 如果o为null也返回null<br>
	 * 如果names[n]为null则会跳过并解析names[n+1]
	 * 
	 * @param names 中的元素不可有空格
	 */
	public static Object get(Object o, String[] names, int beginIndex) {
		Object tar = o;
		if (o != null) {
			try {
				for (int i = beginIndex; i < names.length; ++i) {
					if (names[i] != null) {
						final Class<?> cl = tar.getClass();
						final String name = names[i];
						final String mname = getMethodName(name);
						
						if (mname != null) {
							Method method = cl.getMethod(mname);
							tar = method.invoke(tar);
						} else {
							Field field = getField(cl, name);
							if (field == null) {
								tar = "找不到属性: " + name;
								break;
							}
							field.setAccessible(true);
							tar = field.get(tar);
						}
						
						if (tar == null) break;
					}
				}
			} catch(Exception e) {
				tar = "错误: " + e;
			}
		}
		return tar;
	}
	
	private static String getMethodName(String name) {
		int i = name.lastIndexOf("()");
		if (i >= 0) {
			return name.substring(0, i);
		}
		return null;
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
