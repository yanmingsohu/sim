// CatfoOD 2012-3-1 上午10:10:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.ObjectAttribute;
import jym.sim.util.Tools;


/**
 * 变量
 */
public class Variable implements IVal {

	private final Map<String, IItem> var_map;
	private final String var_name;
	private final String var_full;
	
	/**
	 * 创建变量, 引用全局变量表 
	 * @param v_name
	 * @param vmap
	 * @throws IllegalArgumentException - 变量名无效, 或无效的变量
	 */
	public Variable(String v_name, Map<String, IItem> vmap) throws IllegalArgumentException {
		ObjectAttribute.checkVarName(v_name);
		var_map = vmap;
		var_full = v_name;
		
		int i = v_name.indexOf('.');
		if (i >= 0) {
			var_name = v_name.substring(0, i);
		} else {
			var_name = v_name;
		}
		
		if (var_map.get(var_name) == null) {
			throw new IllegalArgumentException(v_name + " 指定的变量不存在");
		}
	}

	public BigDecimal get() {
		String val = null;
		try {
			IItem item = var_map.get(var_name);
			item.init(var_full);
			val = item.filter();
			
			return new BigDecimal(val);
			
		} catch(Exception e) {
			Tools.pl(Variable.class, "取变量 [" + var_full + "] 失败:", val);
		}
		
		return BigDecimal.ZERO;
	}

	public void set(BigDecimal v) {
		var_map.get(var_name).init(null, v);
	}

}
