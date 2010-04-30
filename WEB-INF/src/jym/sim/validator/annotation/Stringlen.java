// CatfoOD 2010-4-21 下午12:43:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检测字符串的字节长度,被检测的字符串字节长度x须符合: min()<=x<=max()
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Stringlen {
	/**
	 * 字符串最大字节长度
	 */
	int max();
	/**
	 * 字符串最小字节长度
	 */
	int min();
	/**
	 * 出错后的消息
	 */
	String msg();
}
