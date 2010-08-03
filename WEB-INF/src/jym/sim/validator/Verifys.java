// CatfoOD 2010-4-21 上午09:43:58 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jym.sim.util.Tools;
import jym.sim.validator.verify.DateVerify;
import jym.sim.validator.verify.EmailVerify;
import jym.sim.validator.verify.NotNullVerify;
import jym.sim.validator.verify.NumVerify;
import jym.sim.validator.verify.RegexVerify;
import jym.sim.validator.verify.StringLenVerify;

/**
 * 连接验证注解与验证方法的桥梁
 */
public final class Verifys {
	
	private static Map<Object, IVerify> 
			verifys = new HashMap<Object, IVerify>();
	
	
	// 验证器必须注册才能使用
	static {
		reg( new NotNullVerify()	);
		reg( new StringLenVerify()	);
		reg( new NumVerify()		);
		reg( new DateVerify()		);
		reg( new RegexVerify()		);
		reg( new EmailVerify()		);
	}
	
	
	public static void reg(IVerify iv) {
		verifys.put(iv.getAnnoClass(), iv);
	}
	
	protected static List<IVerify> getVerify(Field f) {
		List<IVerify> ivs = new ArrayList<IVerify>();
		Annotation[] annos = f.getAnnotations();
		
		for (int i=0; i<annos.length; ++i) {
			IVerify iv = verifys.get(annos[i].annotationType());
			
			if (iv!=null) {
				ivs.add(iv);
			} else {
				Tools.pl("Verifys未找到验证规则: " + annos[i]);
			}
		}
		
		return ivs;
	}
	
}
