// CatfoOD 2009-10-28 ÏÂÎç09:16:22

package jym.base.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
					System.out.println("³õÊ¼»¯"+e);
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
}
