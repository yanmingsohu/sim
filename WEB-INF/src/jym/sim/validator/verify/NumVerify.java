// CatfoOD 2010-4-21 обнГ01:31:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Num;

public class NumVerify implements IVerify {

	public Class<? extends Annotation> getAnnoClass() {
		return Num.class;
	}

	public String getMessage(Field field, Object value) {
		if (value==null || !(value instanceof Number) ) return ERROR;
		
		double x = ((Number) value).doubleValue();
		Num anno = (Num) field.getAnnotation(getAnnoClass());
		
		if (x<=anno.max() && x>=anno.min()) {
			return SUCCESS;
		}
		
		return anno.msg();
	}

}
