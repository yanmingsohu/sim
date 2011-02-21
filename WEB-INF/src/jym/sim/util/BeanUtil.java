// CatfoOD 2009-10-28 下午09:16:22

package jym.sim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class BeanUtil<TYPE> {
	
	public static final String DATE_FMT_TIME = "yyyy-MM-dd kk:mm:ss";
	
	private Class<TYPE> beanclass;
	private String m_beanname;
	
	
	@SuppressWarnings("unchecked")
	public BeanUtil(String beanname) throws ServletException {
		try {
			beanclass = (Class<TYPE>) Class.forName(beanname);
			m_beanname = beanclass.getSimpleName().toLowerCase();
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}
	
	public BeanUtil(Class<TYPE> cl) throws ServletException {
		beanclass = cl;
		m_beanname = beanclass.getSimpleName().toLowerCase();
	}
	
	public String getBeanName() {
		return m_beanname;
	}
	
	public TYPE creatBean(HttpServletRequest req) {
		TYPE bean = null;
		
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
		else if (Timestamp.class.isAssignableFrom(type)) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_TIME);
			try {
				o = new Timestamp( sdf.parse(value).getTime() );
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else if (Date.class.isAssignableFrom(type)) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_TIME);
			try {
				o = sdf.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else if (BigDecimal.class.isAssignableFrom(type)) {
			o = new BigDecimal(value);
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
	 * 创建clazz类的对象,params是构造函数的参数
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public static Object creatBean(Class<?> clazz, Object...params) 
			throws SecurityException, NoSuchMethodException, 
			IllegalArgumentException, InstantiationException, 
			IllegalAccessException, InvocationTargetException  {
		
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
	public static Object invoke(Object target, String method, Object...params) 
	throws Exception, NoSuchMethodException {
		
		Method m = getMethod(target, method, params);
		return m.invoke(target, params);
	}
	
	/**
	 * 取得targer对象的method方法, 参数是params
	 */
	public static Method getMethod(Object target, String method, Object...params) 
	throws SecurityException, NoSuchMethodException {
		
		Class<?> clazz = target.getClass();
		Class<?>[] cls = null;
		
		if (params!=null) {
			cls = new Class[params.length];
			for (int i=0; i<cls.length; ++i) {
				cls[i] = params[i].getClass();
			}
		}
		
		return clazz.getMethod(method, cls);
	}
	
	/**
	 * 测试obj是否有效
	 * 
	 * @param obj 
	 * @return 如果obj不为null, 并且如果obj是Number类型则>=0, 
	 * 			并且obj是String类型长度>0 则返回true, 否则返回false;
	 */
	public static boolean isValid(Object obj) {
		boolean r = false;
		if (obj!=null) {
			r = true;
			if (obj instanceof Number) {
				Number num = (Number) obj;
				r = num.doubleValue()>=0;
			}
			if (obj instanceof String) {
				String str = obj.toString();
				r = str.trim().length()>0;
			}
		}
		
		return r;
	}
	
	/**
	 * 取得get/set方法对应的属性,属性的类型和带参数方法的参数类型可能不同
	 * 
	 * @param m - 方法
	 * @return 属性,没有返回null
	 */
	public static Field getMethodTargetField(Method m) {
		String name = m.getName();
		Field f = null;
		
		if (name.length()>3 && (name.startsWith("set") || name.startsWith("get")) ) {
			char[] ch = name.toCharArray();
			ch[3] = Character.toLowerCase(ch[3]);
			name = new String(ch, 3, ch.length-3);
			try {
				f = m.getDeclaringClass().getField(name);
			} catch (Exception e) {
			}
		}
		return f;
	}
	
	/**
	 * 通过bean对象fieldname属性对应的getter方法取得该属性的值
	 * 
	 * @param bean - 数据对象, 如果为null, 会抛出异常
	 * @param fieldname - 属性名, 如果为null, 则返回null
	 * @return 属性的值
	 * 
	 * @throws Exception - 出现了其他的错误
	 * @throws NoSuchMethodException - 找不到属性对应的getter方法
	 */
	public static Object getFieldValue(Object bean, String fieldname) 
	throws Exception, NoSuchMethodException {
		
		Object value = null;
		
		if (fieldname!=null) {
			String fieldGetMethod = getGetterName(fieldname);
			if (fieldGetMethod!=null) {
				value = invoke(bean, fieldGetMethod, new Object[0]);	
			}
		}
		return value;
	}
}
