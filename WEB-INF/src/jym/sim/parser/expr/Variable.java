// CatfoOD 2012-3-1 上午10:10:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;
import java.util.Map;

import jym.sim.parser.IItem;
import jym.sim.parser.ObjectAttribute;


/**
 * 变量
 */
public class Variable implements IVal {

	private final Map<String, IItem> var_map;
	private final String var_name;
	
	/**
	 * 创建变量, 引用全局变量表 
	 * @param v_name
	 * @param vmap
	 * @throws IllegalArgumentException - 变量名无效, 或无效的变量
	 */
	public Variable(String v_name, Map<String, IItem> vmap) throws IllegalArgumentException {
		ObjectAttribute.checkVarName(v_name);
		var_map = vmap;
		
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
		try {
			return new BigDecimal( var_map.get(var_name).filter() );
		} catch(Exception e) {}
		
		return BigDecimal.ZERO;
	}

	public void set(BigDecimal v) {
		var_map.get(var_name).init(null, v);
	}

}
