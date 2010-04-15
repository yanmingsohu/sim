package jym.base.orm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import jym.base.orm.JdbcTemplate.ITransition;

class MethodMapping {
	
	private Method m;
	private ITransition it;
	private ISelecter<?> objcrt;
	
	/**
	 * 抛出异常说明方法不符合要求
	 */
	MethodMapping(Method md, ISelecter<?> is) 
		throws SecurityException, IllegalArgumentException, NoSuchMethodException, 
		InstantiationException, IllegalAccessException, InvocationTargetException 
	{
		m = md;
		objcrt = is;
		
		Class<?>[] pt0 = md.getParameterTypes();
		if (pt0.length==1) {
			it = getTransitionType(pt0[0]);
		} else {
			warnning(md.getName() + " 实体方法参数数量不匹配");
		}
	}
	
	/** 
	 * 转化来自数据库中的数据，setter到实体中
	 * model.setXxx(sqldata);
	 */
	public void invoke(ResultSet rs, int col, Object model)
		throws IllegalArgumentException, IllegalAccessException, 
		InvocationTargetException, SQLException
	{
		Object data = it.trans(rs, col);
		m.invoke(model, data);
	}
	
	public String getName() {
		return m.getName();
	}
	
	private ITransition getTransitionType(final Class<?> type) 
	throws SecurityException, NoSuchMethodException, IllegalArgumentException, 
		InstantiationException,	IllegalAccessException, InvocationTargetException 
	{
		ITransition it;
		
		if (String.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getString(col);
				}
			};
		}
		else if (Integer.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getInt(col);
				}
			};
		}
		else if (Double.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getDouble(col);
				}
			};
		}
		else if (Float.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getFloat(col);
				}
			};
		}
		else if (Long.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getLong(col);
				}
			};
		}
		else if (Short.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getShort(col);
				}
			};
		}
		else if (Date.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getDate(col);
				}
			};
		}
		else if (BigDecimal.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return new BigDecimal(rs.getString(col));
				}
			};
		}
		else if (Collection.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return objcrt.select(rs.getString(col));
				}
			};
		}
		else {
			it = getObjTransition(type);
		}
		
		return it;
	}
	
	private ITransition getObjTransition(final Class<?> type) {
		return new ITransition() {
			public Object trans(ResultSet rs, int col) throws SQLException {
				Constructor<?> cons;
				try {
					cons = type.getConstructor(String.class);
					return cons.newInstance(rs.getString(col));
					
				} catch (Exception e) {
					warnning(type + "类型没有(String)构造函数." + e.getMessage());
				}
				return null;
			}
		};
	}
	
	private void warnning(String msg) {
		System.out.println("警告: (MethodMapping): " + msg);
	}
}
