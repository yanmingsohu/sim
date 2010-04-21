// CatfoOD 2010-4-21 ионГ09:52:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Notnull;

public class NotNullVerify implements IVerify {

	public Class<? extends Annotation> getAnnoClass() {
		return Notnull.class;
	}
	
	public String getMessage(Field field, Object value) {
		if (value==null) {
			Notnull nn = (Notnull) field.getAnnotation(getAnnoClass());
			return nn.msg();
		}
		return SUCCESS;
	}

}
