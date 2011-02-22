// CatfoOD 2011-2-22 ÉÏÎç09:34:03

package test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import jym.sim.exception.BeanException;
import jym.sim.util.LoopBeanField;
import jym.sim.util.Tools;
import jym.sim.util.LoopBeanField.ILoader;
import jym.sim.util.LoopBeanField.ISaver;
import jym.sim.util.LoopBeanField.ITransForGet;
import jym.sim.util.LoopBeanField.ITransForSet;

public class TestLoopBeanField {

	public static void main(String[] args) {
		TestBean A = new TestBean();
		testload(A);
		testsave(A);
	}
	
	
	private static void testload(TestBean A) {
		LoopBeanField<TestBean, String> lbf = 
			new LoopBeanField<TestBean, String>(TestBean.class, transSet());
		
		try {
			lbf.save(A, new ILoader<String>() {
				public String load(String fieldName) {

					if (fieldName.endsWith("a")) {
						return "22";
					}
					else if (fieldName.endsWith("b")) {
						return "2.3";
					}
					else if (fieldName.endsWith("c")) {
						return "string";
					}
					else if (fieldName.endsWith("e")) {
						return "true";
					}
					return "1";
				}
			});
		} catch (BeanException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void testsave(TestBean A) {
		LoopBeanField<TestBean, String> lbf = 
			new LoopBeanField<TestBean, String>(TestBean.class, transGet());
		
		try {
			lbf.load(A, new ISaver<String>() {
				public void save(String data, String fieldName) {
					Tools.pl(fieldName + " = " + data);
				}
			});
		} catch (BeanException e) {
			e.printStackTrace();
		}
	}
	
	
	private static ITransForSet<String> transSet() {
		return new ITransForSet<String>() {

			public Boolean from0(String value) {
				return Boolean.parseBoolean(value);
			}

			public String fromS(String value) {
				return value;
			}

			public Short fromH(String value) {
				return Short.parseShort(value);
			}

			public Integer fromI(String value) {
				return Integer.parseInt(value);
			}

			public Double fromD(String value) {
				return Double.parseDouble(value);
			}

			public Float fromF(String value) {
				return Float.parseFloat(value);
			}

			public Long fromL(String value) {
				return Long.parseLong(value);
			}

			public Timestamp fromT(String value) {
				long t = Long.parseLong(value);
				return new Timestamp(t);
			}

			public Date fromA(String value) {
				long t = Long.parseLong(value);
				return new Date(t);
			}

			public BigDecimal fromB(String value) {
				return new BigDecimal(value);
			}

			public Character fromC(String value) {
				return value!=null ? value.charAt(0) : ' ';
			}

			public Object last(String value, Class<?> type) {
				return null;
			}
			
		};
	}

	private static ITransForGet<String> transGet() {
		return new ITransForGet<String>() {

			public String from(String value) {
				return value;
			}

			public String from(Short value) {
				return String.valueOf(value);
			}

			public String from(Integer value) {
				return String.valueOf(value);
			}

			public String from(Double value) {
				return String.valueOf(value);
			}

			public String from(Float value) {
				return String.valueOf(value);
			}

			public String from(Long value) {
				return String.valueOf(value);
			}

			public String from(Timestamp value) {
				return String.valueOf(value);
			}

			public String from(Date value) {
				return String.valueOf(value);
			}

			public String from(BigDecimal value) {
				return String.valueOf(value);
			}

			public String from(Boolean value) {
				return String.valueOf(value);
			}

			public String from(Character value) {
				return String.valueOf(value);
			}

			public String last(Object value, Class<?> type) {
				return String.valueOf(value);
			}
			
		};
	}
}
