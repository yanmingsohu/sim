// CatfoOD 2010-6-17 上午08:43:30 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;

public class DateRange implements IWhere, ISkipValueCheck {
	
	private SimpleDateFormat sqlDateFormat = 
			new SimpleDateFormat(JdbcTemplate.DEFAULT_DATE_FORMAT);
	
	private String adf;
	private String bdf;
	
	
	public DateRange(String afterDateFieldName, String beforeDateFieldName) {
		adf = afterDateFieldName;
		bdf = beforeDateFieldName;
	}

	public String w(String columnName, Object value, Object model) {
		StringBuilder where = new StringBuilder();
		
		Object after = getValue(model, adf);
		Object before = getValue(model, bdf);
		
		if (after==null && before==null) {
			return SKIP_WHERE_SUB;
		}
		
		where.append('\'').append(format(after)).append('\'').append(" <= ").append(columnName);
		where.append(" and ");
		where.append(columnName).append(" < ").append('\'').append(format(before)).append('\'');
		
		return where.toString();
	}
	
	private String format(Object obj) {
		if (obj==null) {
			obj = new Date();
		}
		
		if (obj instanceof Date) {
			return sqlDateFormat.format(obj);
		}
		return obj.toString();
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
		Tools.pl("DateRange error: " + msg);
	}
}
