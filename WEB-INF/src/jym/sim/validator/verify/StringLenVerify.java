// CatfoOD 2010-4-21 обнГ12:45:29 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Stringlen;

public class StringLenVerify implements IVerify {

	public Class<? extends Annotation> getAnnoClass() {
		return Stringlen.class;
	}

	public String getMessage(Field field, Object value) {
		if (value==null || !(value instanceof String)) return ERROR;
		
		Stringlen anno = (Stringlen) field.getAnnotation(getAnnoClass());
		String str = value.toString();
		int len = str.getBytes().length;
		
		if (len>=anno.min() && len<=anno.max()) {
			return SUCCESS;
		}
		else {
			return anno.msg();
		}
	}

}
