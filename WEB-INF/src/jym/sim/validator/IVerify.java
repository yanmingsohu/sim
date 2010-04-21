// CatfoOD 2010-4-21 上午09:20:02 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 验证注解的实现接口
 */
public interface IVerify {
	
	/**
	 * 验证成功
	 */
	public final String SUCCESS = null;
	
	/**
	 * 验证过程中出现错误
	 */
	public final String ERROR = null;
	
	/**
	 * 返回验证器要验证的'注解'的class
	 */
	public Class<? extends Annotation> getAnnoClass();
	
	/**
	 * 按照指定的规则验证field的值value,<br>
	 * 如果符合规则(验证成功)返回null,<br>
	 * 否则返回错误字符串<br>
	 * <b>如果value值无法验证返回null</b>
	 * 
	 * @param field - 要验证的字段
	 * @param value - 字段所属对象的值
	 */
	public String getMessage(Field field, Object value);
}
