// CatfoOD 2010-4-21 下午02:20:45 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 正则表达式验证
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Regex {
	/**
	 * 验证错误时的消息
	 */
	String msg();
	/**
	 * 正则表达式
	 */
	String exp();
}
