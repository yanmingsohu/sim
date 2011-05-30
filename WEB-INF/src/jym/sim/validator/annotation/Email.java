// CatfoOD 2010-4-21 下午02:35:57 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 电子邮件地址效验器
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Email {
	/**
	 * 效验错误时的信息
	 */
	String msg();
}
