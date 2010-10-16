// CatfoOD 2010-10-16 下午07:55:49

package jym.sim.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;

public class FromBean extends Json {
	
	private Object bean;
	
	public FromBean(Object targetBean) {
		if (targetBean==null) {
			throw new NullPointerException("bean对象不能为null");
		}
		bean = targetBean;
	}

	public void go(Appendable out) throws IOException {
		Class<?> clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length>0) {
			for (int i=0; i<fields.length; ++i) {
				String fname = fields[i].getName();
				String geterName = BeanUtil.getGetterName(fname);
				
				try {
					Method m = clazz.getDeclaredMethod(geterName);
					Object result = m.invoke(bean);

					super.setOrPrimitive(fname, result);
				} catch (Exception e) {
					Tools.pl(e);
				}
			}
		}
		
		super.go(out);
	}
}
