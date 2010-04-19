// CatfoOD 2009-10-28 下午09:16:22

package jym.base.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class BeanUtil {
	
	private Class<?> beanclass;
	private String m_beanname;
	
	public BeanUtil(String beanname) throws ServletException {
		try {
			beanclass = Class.forName(beanname);
			m_beanname = beanclass.getSimpleName().toLowerCase();
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}
	
	public String getBeanName() {
		return m_beanname;
	}
	
	public Object creatBean(HttpServletRequest req) {
		Object bean = null;
		
		try {
			bean = beanclass.newInstance();
			Field[] fields = beanclass.getDeclaredFields();
			for (int i=0; i<fields.length; ++i) {
				try {
					Field f = fields[i];					
					String name = f.getName();
					Class<?> paramclass = f.getType();
					
					String methodname = "set" + firstUp(name);
					Method method = beanclass.getDeclaredMethod(methodname, paramclass);
					String value = req.getParameter(name);
					
					method.invoke(bean, transitionType(value, paramclass) );
					
				} catch (Exception e) {
					System.out.println("初始化"+e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	private Object transitionType(String value, Class<?> type) 
	throws SecurityException, NoSuchMethodException, 
		IllegalArgumentException, InstantiationException, 
		IllegalAccessException, InvocationTargetException 
	{
		Object o = null;
		if (String.class.isAssignableFrom(type)) {
			o = value;
		}
		else if (Integer.class.isAssignableFrom(type)) {
			o = new Integer(value);
		}
		else if (Double.class.isAssignableFrom(type)) {
			o = new Double(value);
		}
		else if (Float.class.isAssignableFrom(type)) {
			o = new Float(value);
		}
		else if (Long.class.isAssignableFrom(type)) {
			o = new Long(value);
		}
		else if (Short.class.isAssignableFrom(type)) {
			o = new Short(value);
		}
		else {
			Constructor<?> cons = type.getConstructor(String.class);
			o = cons.newInstance(value);
		}
		return o;
	}
	
	private String firstUp(String s) {
		char[] cs = s.toCharArray();
		cs[0] = Character.toUpperCase(cs[0]);
		return new String(cs);
	}
	
	/**
	 * 如果fieldname为null, 抛出异常
	 */
	public static String getSetterName(String fieldname) {
		return getXetName(fieldname, "set");
	}
	
	/**
	 * 如果fieldname为null, 抛出异常
	 */
	public static String getGetterName(String fieldname) {
		return getXetName(fieldname, "get");
	}
	
	private static String getXetName(String fieldname, String xxx) {
		char[] fns = fieldname.toCharArray();
		StringBuilder buff = new StringBuilder();
		buff.append(xxx);
		buff.append( Character.toTitleCase(fns[0]) );
		buff.append(fns, 1, fns.length-1);
		return buff.toString();
	}
	
	public static Method[] getGetterMethods(Class<?> clazz) {
		ArrayList<Method> ms = new ArrayList<Method>();
		Method[] all = clazz.getMethods();
		for (int i=0; i<all.length; ++i) {
			String mname = all[i].getName();
			if (mname.startsWith("get")) {
				ms.add(all[i]);
			}
		}
		return ms.toArray(new Method[0]);
	}
	
	/**
	 * 创建clazz类的对象
	 */
	public static Object creatBean(Class<?> clazz, Object...params) throws Exception {
		Class<?>[] cls = new Class[params.length];
		for (int i=0; i<cls.length; ++i) {
			cls[i] = params[i].getClass();
		}
		Constructor<?> cons = clazz.getConstructor(cls);
		return cons.newInstance(params);
	}
	
	/**
	 * 执行targer对象的method方法, 参数是params
	 */
	public static Object invoke(Object targer, String method, Object...params) 
	throws Exception, NoSuchMethodException {
		Class<?> clazz = targer.getClass();
		Class<?>[] cls = new Class[params.length];
		for (int i=0; i<cls.length; ++i) {
			cls[i] = params[i].getClass();
		}
		Method m = clazz.getMethod(method, cls);
		return m.invoke(targer, params);
	}
	
	/**
	 * 测试obj是否有效
	 * 
	 * @param obj 
	 * @return 如果obj不为null, 并且如果obj是Number类型则不为0, 
	 * 			并且obj是String类型长度>0 则返回true, 否则返回false;
	 */
	public static boolean isValid(Object obj) {
		boolean r = false;
		if (obj!=null) {
			r = true;
			if (obj instanceof Number) {
				Number num = (Number) obj;
				r = num.doubleValue()!=0;
			}
			if (obj instanceof String) {
				String str = obj.toString();
				r = str.trim().length()>0;
			}
		}
		
		return r;
	}
}
