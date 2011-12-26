// CatfoOD 2011-2-21 下午07:37:52

package jym.sim.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jym.sim.exception.BeanException;


/**
 * XXX Beta Version
 * 
 * @param <Bean> - 要操作的类型
 * @param <TARGET_TYPE> - 在读取时是要转换成的目标类型,
 * 在设置时是进入的数据类型
 */
public class LoopBeanField<Bean, TARGET_TYPE> {
	
	private final static String GET_METHOD = "get";
	private final static String SET_METHOD = "set";
	public final static String SKIP = "skip this method";
	private List<MethodPack<Bean>> loop;
	private Class<Bean> bean;
	
	
	public LoopBeanField(Class<Bean> bean) {
		this.bean = bean;
	}
	
	/**
	 * 从bean的属性中取出值,到另一个类型,通常用于持久化,只能使用load方法
	 * 
	 * @param bean
	 * @param it
	 */
	public LoopBeanField(Class<Bean> bean, ITransForGet<TARGET_TYPE> it) {
		this(bean);
		loopfield(GET_METHOD, it);
	}
	
	/**
	 * 从数据源中读出数据并设置到bean中, 只能使用save方法
	 * 
	 * @param bean
	 * @param it
	 */
	public LoopBeanField(Class<Bean> bean, ITransForSet<TARGET_TYPE> it) {
		this(bean);
		loopfield(SET_METHOD, it);
	}
	
	
	@SuppressWarnings("unchecked")
	private void loopfield(String s_method, Object trans) {
		loop = new ArrayList<MethodPack<Bean>>();
		Field[] fields = bean.getDeclaredFields();
		
		for (int i=0; i<fields.length; ++i) {
			
			Field f = fields[i];					
			String name = f.getName();
			String methodname = s_method + BeanUtil.firstUp(name);
			
			Class<?> paramclass = f.getType();
			
			try {
				Method method = null;
				IMethod im = null;
				
				if (s_method==GET_METHOD) {
					method = bean.getDeclaredMethod(methodname);
					im = getTypeTrans(paramclass, 
							(ITransForGet<TARGET_TYPE>) trans, null);
				} 
				else if (s_method==SET_METHOD) {
					method = bean.getDeclaredMethod(methodname, paramclass);
					im = getTypeTrans(paramclass, null,
							(ITransForSet<TARGET_TYPE>) trans);
				}
				
				MethodPack<Bean> p = new MethodPack<Bean>(method, im, name);
				loop.add(p);
				
			} catch (Exception e) {
				Tools.pl("LoopBeanField init warn: " + e);
			}
		}
	}
	
	public void setSaveTrans(ITransForGet<TARGET_TYPE> it) {
		loopfield(GET_METHOD, it);
	}
	
	public void setLoadTrans(ITransForSet<TARGET_TYPE> it) {
		loopfield(SET_METHOD, it);
	}
	
	/**
	 * 读出bean中的属性并转换成TARGET_TYPE推送到saver中
	 * 
	 * @param bean
	 * @param saver
	 * @throws BeanException 
	 */
	public void load(Bean bean, ISaver<TARGET_TYPE> saver) throws BeanException {
		Iterator<MethodPack<Bean>> itr = loop.iterator();
		
		while (itr.hasNext()) {
			MethodPack<Bean> mp = itr.next();
			try {
				TARGET_TYPE data = mp.get(bean);
				saver.save(data, mp.fieldName);
			} catch (Exception e) {
				throw new BeanException(e);
			}
		}
	}
	
	/**
	 * 从loader中读取数据并推入到bean中
	 * 
	 * @param bean
	 * @param loader
	 * @throws BeanException 
	 */
	public void save(Bean bean, ILoader<TARGET_TYPE> loader) throws BeanException {
		Iterator<MethodPack<Bean>> itr = loop.iterator();
		
		while (itr.hasNext()) {
			MethodPack<Bean> mp = itr.next();
			TARGET_TYPE data = loader.load(mp.fieldName);
			
			if (data!=SKIP) {
				try {
					mp.set(bean, data);
				} catch (Exception e) {
					throw new BeanException(e);
				}
			}
		}
	}
	
	private class MethodPack<BEAN> {
		private Method method;
		private IMethod im;
		private String fieldName;
		
		private MethodPack(Method m, IMethod i, String _field) {
			method = m;
			im = i;
			fieldName = _field;
		}

		@SuppressWarnings("unchecked")
		public TARGET_TYPE get(BEAN bean) throws IllegalArgumentException, 
												 IllegalAccessException, 
												 InvocationTargetException {
			Object o = method.invoke(bean);
			o = im.from(o);
			return (TARGET_TYPE) o;
		}
		
		public void set(BEAN bean, Object data) throws IllegalArgumentException, 
													   IllegalAccessException, 
													   InvocationTargetException {
			Object o = im.from(data);
			method.invoke(bean, o);
		}
	}
	
	public interface ISaver<LOAD_TYPE> {
		/**
		 * 从实体中读出数据,并在该方法中保存起来
		 * @param data - 已经转换后属性的值
		 * @param fieldName - 属性名
		 */
		void save(LOAD_TYPE data, String fieldName);
	}
	
	public interface ILoader<SAVE_TYPE> {
		/** 
		 * 如果方法返回SKIP,则忽略对该属性的操作 
		 */
		String SKIP = LoopBeanField.SKIP;
		/**
		 * 取得要设置到属性中的数据
		 * @param fieldName - 属性名
		 * @return 返回要设置的数据, 之后会被转换为属性的类型
		 */
		SAVE_TYPE load(String fieldName);
	}

	/**
	 * 与类型绑定的转换函数, 内部依赖
	 */
	public interface IMethod {
		Object from(Object value);
	}
	
	/**
	 * 转换接口,用于把从实体中的属性值转换为一个目标类型值
	 */
	public interface ITransForGet<TARGET_TYPE> {
		TARGET_TYPE from(String		value);
		TARGET_TYPE from(Character	value);
		TARGET_TYPE from(Short		value);
		TARGET_TYPE from(Integer	value);
		TARGET_TYPE from(Double		value);
		TARGET_TYPE from(Float		value);
		TARGET_TYPE from(Long		value);
		TARGET_TYPE from(Timestamp	value);
		TARGET_TYPE from(Date		value);
		TARGET_TYPE from(BigDecimal	value);
		TARGET_TYPE from(Boolean	value);
		TARGET_TYPE last(Object		value, Class<?> type);
	}
	
	/**
	 * 转换接口,用于把一种数据类型转换为实体属性的类型
	 */
	public interface ITransForSet<TARGET_TYPE> {
		String		fromS(TARGET_TYPE value);
		Character	fromC(TARGET_TYPE value);
		Short		fromH(TARGET_TYPE value);
		Integer		fromI(TARGET_TYPE value);
		Double 		fromD(TARGET_TYPE value);
		Float		fromF(TARGET_TYPE value);
		Long 		fromL(TARGET_TYPE value);
		Timestamp 	fromT(TARGET_TYPE value);
		Date 		fromA(TARGET_TYPE value);
		BigDecimal	fromB(TARGET_TYPE value);
		Boolean 	from0(TARGET_TYPE value);
		Object		last (TARGET_TYPE value, Class<?> type);
	}
	
	@SuppressWarnings("unchecked")
	private IMethod getTypeTrans(final Class<?> type, 
								 final ITransForGet<TARGET_TYPE> get,
								 final ITransForSet<TARGET_TYPE> set
								 ) 
						  throws SecurityException, 
					 			 NoSuchMethodException, 
					 			 IllegalArgumentException, 
					 			 InstantiationException, 
					 			 IllegalAccessException, 
					 			 InvocationTargetException {
		IMethod o = null;
		String name = null;
		
		if (type.isPrimitive()) {
			name = type.getName();
		}
		
		if (String.class.isAssignableFrom(type)) {
			o = ( get!=null ) 
				? new IMethod() {
					public Object from(Object value) {
						return get.from((String) value);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.fromS((TARGET_TYPE) value);
					}
				};
		}
		else if (Character.class.isAssignableFrom(type) || 
				(name!=null && name.equals("char")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Character) value);
					}
				}  : new IMethod() {
					public Object from(Object value) {
						return set.fromC((TARGET_TYPE) value);
					}
				};
		}
		else if (Boolean.class.isAssignableFrom(type) || 
				(name!=null && name.equals("boolean")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Boolean) value);
					}
				}  : new IMethod() {
					public Object from(Object value) {
						return set.from0((TARGET_TYPE) value);
					}
				};
		}
		else if (Short.class.isAssignableFrom(type) || 
				(name!=null && name.equals("short")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Short) value);
					}
				}  : new IMethod() {
					public Object from(Object value) {
						return set.fromH((TARGET_TYPE) value);
					}
				};;
		}
		else if (Integer.class.isAssignableFrom(type) || 
				(name!=null && name.equals("int")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Integer) value);
					}
				}  : new IMethod() {
					public Object from(Object value) {
						return set.fromI((TARGET_TYPE) value);
					}
				};
		}
		else if (Long.class.isAssignableFrom(type) || 
				(name!=null && name.equals("long")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Long) value);
					}
				}  : new IMethod() {
					public Object from(Object value) {
						return set.fromL((TARGET_TYPE) value);
					}
				};
		}
		else if (Float.class.isAssignableFrom(type) || 
				(name!=null && name.equals("float")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Float) value);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.fromF((TARGET_TYPE) value);
					}
				};
		}
		else if (Double.class.isAssignableFrom(type) || 
				(name!=null && name.equals("double")) ) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Double) value);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.fromD((TARGET_TYPE) value);
					}
				};
		}
		else if (Timestamp.class.isAssignableFrom(type)) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Timestamp) value);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.fromT((TARGET_TYPE) value);
					}
				};
		}
		else if (Date.class.isAssignableFrom(type)) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((Date) value);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.fromA((TARGET_TYPE) value);
					}
				};
		}
		else if (BigDecimal.class.isAssignableFrom(type)) {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.from((BigDecimal) value);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.fromB((TARGET_TYPE) value);
					}
				};
		}
		else {
			o = ( get!=null )
				? new IMethod() {
					public Object from(Object value) {
						return get.last(value, type);
					}
				} : new IMethod() {
					public Object from(Object value) {
						return set.last((TARGET_TYPE) value, type);
					}
				};
		}
		return o;
	}
}
