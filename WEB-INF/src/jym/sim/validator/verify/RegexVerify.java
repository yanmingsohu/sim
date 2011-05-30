// CatfoOD 2010-4-21 下午02:23:03 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jym.sim.util.Tools;
import jym.sim.validator.IVerify;
import jym.sim.validator.annotation.Regex;

public class RegexVerify implements IVerify {

	public Class<? extends Annotation> getAnnoClass() {
		return Regex.class;
	}

	public String getMessage(Field field, Object value) {
		if (value==null || !(value instanceof String)) return SKIP;
		
		String str = (String) value;
		Regex anno = field.getAnnotation(Regex.class);
		
		try {
			if ( Pattern.matches(anno.exp(), str) ) {
				return SUCCESS;
			}
		} catch (PatternSyntaxException e) {
			Tools.pl("验证属性(" + field + ")时正则表达式错误:" + e.getMessage() );
			return ERROR;
		}
		
		return anno.msg();
	}

}
