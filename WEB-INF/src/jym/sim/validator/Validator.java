// CatfoOD 2010-4-21 上午09:14:14 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jym.sim.util.Tools;

/**
 * 对象验证器,缓存前一个对象的相关消息<br>
 * 验证对象时使用同一个Validator对象更有效率
 */
public class Validator {
	
	private Class<?> classkey;
	private Field fieldkey;
	private List<Field> cachedfields;
	private List<IVerify> cachedverifys;

	
	public VerifyMessage validate(Object o) {
		VerifyMessage msg = new VerifyMessage();
		
		Class<?> cd = o.getClass();
		Iterator<Field> it = getAllFields(cd);
		
		while (it.hasNext()) {
			vfield(o, it.next(), msg);
		}
		
		return msg;
	}
	
	private Iterator<Field> getAllFields(final Class<?> clazz) {
		List<Field> flist = null;
		
		if (clazz==classkey && cachedfields!=null) {
			flist = cachedfields;
		} 
		else {
			flist = new ArrayList<Field>();
			Class<?> superclass = clazz;
			
			while (superclass!=null) {
				if (superclass != this.getClass()) {
					Collections.addAll(flist, superclass.getDeclaredFields());
				}
				superclass = superclass.getSuperclass();
			}
			// 缓存
			classkey = clazz;
			cachedfields = flist;
		}
		return flist.iterator();
	}
	
	private Iterator<IVerify> getFieldVerifys(Field fd) {
		List<IVerify> list = null;
		
		if (fd==fieldkey && cachedverifys!=null) {
			list = cachedverifys;
		}
		else {
			list = Verifys.getVerify(fd);
			cachedverifys = list;
			fieldkey = fd;
		}
		return list.iterator();
	}

	private void vfield(Object obj, Field field, VerifyMessage msg) {
		// 突破访问限制
		field.setAccessible(true);
		Iterator<IVerify> ite = getFieldVerifys(field);
		
		try {
			Object fieldvalue = field.get(obj);
			
			while (ite.hasNext()) {
				IVerify iv = ite.next();
				try {
					String mstr = iv.getMessage(field, fieldvalue);
					if ( mstr!=null ) {
						msg.putMsg(field, mstr);
					}
				} catch (Exception e) {
				}
			}
			
		} catch (Exception e) {
			Tools.pl("Validator访问属性时错误:" + e.getMessage());
		}
	}
}
