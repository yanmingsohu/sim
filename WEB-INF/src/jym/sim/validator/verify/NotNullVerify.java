// CatfoOD 2010-4-21 ÉÏÎç09:52:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Notnull;

public class NotNullVerify implements IVerify {

	public Class<? extends Annotation> getAnnoClass() {
		return Notnull.class;
	}
	
	public String getMessage(Field field, Object value) {
		Notnull nn = field.getAnnotation(Notnull.class);
		
		if (value==null) {
			return nn.msg();
		}
		else if (value instanceof String) {
			if ( ((String)value).trim().length()==0 ) {
				return nn.msg();
			}
		}
		else if (value instanceof Collection<?>) {
			if ( ((Collection<?>)value).size()==0 ) {
				return nn.msg();
			}
		}
		return SUCCESS;
	}

}
