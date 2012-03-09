// CatfoOD 2012-2-28 上午10:00:15 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

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
	 * 从vars中取值, 名字规则为refname
	 * refname: a.b.c().d
	 * @see #get(Object, String[], int)
	 */
	public static Object get(Map<String, IItem> vars, String refname) {
		String[] vs = refname.split("\\.");
		if (vs.length > 0) {
			IItem it = vars.get(vs[0]);
			Object ori = it.originalObj();
			if (vs.length > 1) {
				return get(ori, vs, 1);
			}
			return ori;
		}
		return null;
	}
	
	/**
	 * 以此规则解析o : 返回o的names[0 + beginIndex]的属性(或方法),
	 * 	保存的对象的names[1 + beginIndex]的属性(或方法),
	 * 	保存的对象的names[n + beginIndex]的属性(或方法)保存的对象<br>
	 * 如果此过程中某个属性(或方法)为null则返回null, 如果o为null也返回null<br>
	 * 如果names[n]为null则会跳过并解析names[n+1]
	 * <b>如果发生异常, 则返回描述异常的字符串</b>
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
	

	/**
	 * 检查变量名是否符合java命名规范如:'x.y.z'
	 * @param name
	 * @throws IOException
	 */
	public static void checkVarName(String name) throws IllegalArgumentException {
		char[] ch = name.toCharArray();
		int i = -1;
		int word_len = 0;
		int method = 0;
		
		while (++i < ch.length) {
			if (ch[i] == ' ' || ch[i] == '\t') continue;
			
			if (word_len == 0) {
				if ( !Character.isJavaIdentifierStart(ch[i]) ) {
					throw new IllegalArgumentException("无效的变量名首字母:[" + name + "]");
				}
				
				word_len = 1;
				continue;
			}
			
			if (ch[i] == '(') {
				if (method > 0)
					throw new IllegalArgumentException("不能使用连续的左括号:[" + name + "]");
			
				method = 1;
				continue;
			}

			if (ch[i] == ')') {
				if (method > 1)
					throw new IllegalArgumentException("不能使用连续的右括号:[" + name + "]");
				
				method = 2;
				continue;
			}
			
			if (method == 1) {
				throw new IllegalArgumentException("缺失右括号:[" + name + "]");
			}
			
			if (ch[i] == '.') {
				word_len = 0;
				method = 0;
				continue;
			}

			if (method > 0) {
				throw new IllegalArgumentException("函数调用语法错误:[" + name + "]");
			}
			
			if ( !Character.isJavaIdentifierPart(ch[i]) ) {
				throw new IllegalArgumentException("含有无效的字符:[" + name + "]");
			}
		}	
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
