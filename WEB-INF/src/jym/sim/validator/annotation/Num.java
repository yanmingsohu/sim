// CatfoOD 2010-4-21 下午01:27:04 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 对数字类型的测试, 被验证的值x须符合 max()<=x<=min()
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Num {
	/**
	 * 出错后的消息
	 */
	String msg();
	/**
	 * 最大值
	 */
	double max();
	/**
	 * 最小值
	 */
	double min();
}
