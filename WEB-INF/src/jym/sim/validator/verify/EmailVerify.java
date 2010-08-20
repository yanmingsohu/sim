// CatfoOD 2010-4-21 ÏÂÎç02:37:35 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Email;

public class EmailVerify implements IVerify {
	
	private Pattern p;
	
	private static final String exp = 
			"\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	
	public EmailVerify() {
		p = Pattern.compile(exp);
	}

	public Class<? extends Annotation> getAnnoClass() {
		return Email.class;
	}

	public String getMessage(Field field, Object value) {
		if (value==null && !(value instanceof String)) return SKIP;
		
		String email = (String) value;
		if (p.matcher(email).matches()) {
			return SUCCESS;
		}
		
		return field.getAnnotation(Email.class).msg();
	}

}
