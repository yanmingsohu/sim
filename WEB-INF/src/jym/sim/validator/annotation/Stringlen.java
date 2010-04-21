// CatfoOD 2010-4-21 ����12:43:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ����ַ�������,�������ַ�������x�����: min()<=x<=max()
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Stringlen {
	/**
	 * �ַ�����󳤶�
	 */
	int max();
	/**
	 * �ַ�����С����
	 */
	int min();
	/**
	 * ���������Ϣ
	 */
	String msg();
}