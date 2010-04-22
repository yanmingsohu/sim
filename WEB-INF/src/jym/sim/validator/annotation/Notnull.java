// CatfoOD 2010-4-21 上午08:06:09 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 非null测试,如果被测试的对象是String,则测试是否0长度字符串
 * 如果对象是集合,则测试集合是否无元素,是则认为null
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Notnull {
	/**
	 * 出错消息
	 */
	String msg();
}
