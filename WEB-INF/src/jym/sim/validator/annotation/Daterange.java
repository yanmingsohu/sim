// CatfoOD 2010-4-21 下午01:48:18 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 日期校验器,其中min与max的格式: '年-月-日'<br>
 * 如果校验器格式错误,则认为校验通过
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Daterange {
	/**
	 * 校验错误后的信息
	 */
	String msg();
	/**
	 * 最小的日期值
	 */
	String min();
	/**
	 * 最大的日期值
	 */
	String max();
}
