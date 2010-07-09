// CatfoOD 2010-7-5 下午04:04:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.util.Collection;
import java.util.Iterator;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;

/**
 * 列表运算符(判断表达式是否为列表中的指定项)：IN (项1,项2……)
 */
public class OperatorIN implements IWhere, ISkipValueCheck {
	
	private String fname;
	
	public OperatorIN(String arrayFieldName) {
		fname = arrayFieldName;
	}

	@SuppressWarnings("unchecked")
	public String w(String columnName, Object value, Object model) {
		Object arr = getValue(model, fname);
		
		while (arr instanceof Collection<?>) {
			
			Collection<Object> c = (Collection<Object>) arr;
			Iterator<Object> it = c.iterator();
			
				if (!it.hasNext()) break;

			StringBuilder out = new StringBuilder();
			out.append(columnName).append(" IN (");
			
				while (it.hasNext()) {
					out.append("'").append(it.next()).append("'");
					if (it.hasNext()) {
						out.append(",");
					}
				}
			
			out.append(")");
			
			return out.toString();
		}
		return null;
	}

	private Object getValue(Object model, String fieldname) {
		String methodName = BeanUtil.getGetterName(fieldname);
		Object value = null;
		
		try {
			value = BeanUtil.invoke(model, methodName, new Object[0]);
			
		} catch (NoSuchMethodException e) {
			error("找不到 "+fieldname+" 的get方法: " + e.getMessage());
			
		} catch (Exception e) {
			error("错误: " + e.getMessage());
		}
		
		return value;
	}
	
	private void error(String msg) {
		Tools.pl("OperatorIN error: " + msg);
	}
}
