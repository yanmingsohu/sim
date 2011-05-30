// CatfoOD 2010-4-21 ÏÂÎç01:51:27 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Daterange;

public class DateVerify implements IVerify {
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public Class<? extends Annotation> getAnnoClass() {
		return Daterange.class;
	}

	public String getMessage(Field field, Object value) {
		if (value==null || !(value instanceof java.util.Date)) return SKIP;
		
		java.util.Date date = (java.util.Date) value;
		Daterange anno = (Daterange) field.getAnnotation(getAnnoClass());
		java.util.Date max = format(anno.max());
		java.util.Date min = format(anno.min());
		
		if (max==null && min==null) return SKIP;
		
		if (date.compareTo(max)<=0 && date.compareTo(min)>=0) {
			return SUCCESS;
		}
		
		return anno.msg();
	}

	private java.util.Date format(String s) {
		try {
			return df.parse(s);
		} catch (ParseException e) {
		}
		return null;
	}
}
