// CatfoOD 2011-2-22 上午10:27:38

package jym.sim.base;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jym.sim.exception.BeanException;
import jym.sim.util.LoopBeanField;
import jym.sim.util.Tools;
import jym.sim.util.LoopBeanField.ILoader;
import jym.sim.util.LoopBeanField.ITransForSet;

class BeanBuilder<TYPE> {
	
	public static final String DATE_FMT_TIME = "yyyy-MM-dd kk:mm:ss";
	
	private LoopBeanField<TYPE, String> bf;
	private Class<TYPE> beanclass;
	private String m_beanname;
	
	
	@SuppressWarnings("unchecked")
	public BeanBuilder(String beanname) throws ServletException, ClassNotFoundException {
		this( (Class<TYPE>) Class.forName(beanname) );
	}
	
	public BeanBuilder(Class<TYPE> cl) throws ServletException {
		beanclass = cl;
		m_beanname = beanclass.getSimpleName().toLowerCase();
		bf = new LoopBeanField<TYPE, String>(cl, gettrans());
	}
	
	public String getBeanName() {
		return m_beanname;
	}
	
	public TYPE creatBean(final HttpServletRequest req) throws BeanException {
		try {
			TYPE bean = beanclass.newInstance();
			
			bf.save(bean, new ILoader<String>() {
				public String load(String fieldName) {
					String value = req.getParameter(fieldName);
					if (Tools.isNull(value)) return SKIP;
					
					return value;
				}
			});
			
			return bean;
		} catch (Exception e) {
			throw new BeanException(e);
		}
	}
	
	private static ITransForSet<String> gettrans() {
		return new ITransForSet<String>() {

			public Boolean from0(String value) {
				try { return Boolean.parseBoolean(value);
				} catch (Exception e) { return null; }
			}

			public String fromS(String value) {
				return value;
			}

			public Short fromH(String value) {
				try { return Short.parseShort(value);
				} catch (Exception e) { return null; }
			}

			public Integer fromI(String value) {
				try { return Integer.parseInt(value);
				} catch (Exception e) { return null; }
			}

			public Double fromD(String value) {
				try { return Double.parseDouble(value);
				} catch (Exception e) { return null; }
			}

			public Float fromF(String value) {
				try { return Float.parseFloat(value);
				} catch (Exception e) { return null; }
			}

			public Long fromL(String value) {
				try { return Long.parseLong(value);
				} catch (Exception e) { return null; }
			}

			public Timestamp fromT(String value) {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_TIME);
				try {
					return new Timestamp( sdf.parse(value).getTime() );
				} catch (ParseException e) { return null; }
			}

			public Date fromA(String value) {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_TIME);
				try {
					return sdf.parse(value);
				} catch (ParseException e) { return null; }
			}

			public BigDecimal fromB(String value) {
				try {return new BigDecimal(value);
				} catch (Exception e) { return null; }
			}

			public Object last(String value, Class<?> type) {
				Constructor<?> cons;
				try {
					cons = type.getConstructor(String.class);
					return cons.newInstance(value);
				} catch (Exception e) { return null; }
			}

			public Character fromC(String value) {
				return value!=null ? value.charAt(0) : ' ';
			}
			
		};
	}

}
