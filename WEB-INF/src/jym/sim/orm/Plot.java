// CatfoOD 2010-4-16 上午09:38:08 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.sim.exception.OrmException;
import jym.sim.filter.FilterPocket;
import jym.sim.sql.IOrder;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;


/**
 * 实体属性与数据库列映射策略实现
 *
 * @param <T> - 实体类型
 * @see jym.sim.orm.IOrm
 */
class Plot<T> implements IPlot {
	
	private final static String PASS_COLM_NAME = "sim__";
	
	/** 使用小写比较String <表列名, 方法封装> */
	private Map<String, MethodMapping> ormmap;
	/** 使用小写比较String <方法小写名, 方法> */
	private Map<String, Method> classMethodmap;
	/** 大小写敏感, <方法, 列名> */
	private Map<Method, String> reverse;
	
	private Method[] ms;
	private IOrm<T> orm;
	private boolean usecolnamemap = true;
	private Order order_sub = new Order();
	private FilterPocket outfilter;
	
	
	public Plot(IOrm<T> _orm, FilterPocket _outfilter) {
		orm = _orm;
		outfilter = _outfilter;
		initMethods();
		initOrm();
	}
	
	private void initMethods() {
		ms = orm.getModelClass().getMethods();
		classMethodmap = new HashMap<String, Method>();
		
		for (int i=0; i<ms.length; ++i) {
			// 使用小写比较
			String name = ms[i].getName().toLowerCase();
			
			Method m = classMethodmap.put(name, ms[i]);
			// 如果m非空,则可能有函数重载
			if (m!=null) {
				Field f = BeanUtil.getMethodTargetField(m);
				if (f!=null) {
					Class<?>[] pts = m.getParameterTypes();
					if (pts.length==1) {
						if ( f.getType().equals(pts[0]) ) {
							classMethodmap.put(name, m);
						} 
						// else 属性的类型与方法类型不同
					} 
					// else 函数的参数不合法
				}
			}
		}
	}
	
	private void initOrm() {
		ormmap = new HashMap<String, MethodMapping>();
		reverse = new HashMap<Method, String>();
		orm.mapping(this);
	}
	
	public void fieldPlot(String fn, String cn) {
		setMappingPlot(fn, cn, null, null);
	}

	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter, String pk) {
		setMappingPlot(fieldName, colname, getter, pk);
	}
	
	public void fieldPlot(String fieldName, String colname, ISqlLogic ...logics) {
		setMappingPlot(fieldName, colname, null, null, logics);
	}
	
	protected void mapping(String colname, int colc, ResultSet rs, T model) {
		colname = colname.toLowerCase();
		if (colname.startsWith(PASS_COLM_NAME)) {
			return;
		}
		
		MethodMapping md = null;

		// 自动使用表格列名进行映射
		if (usecolnamemap && !ormmap.containsKey(colname)) {
			md = setMappingPlot(colname, colname, null, null);
			
		} else {
			md = ormmap.get(colname);
		}
		
		if (md!=null) {
			try {
				md.invoke(rs, colc, model);
			} 
			catch(Exception e) {
				warnning(model.getClass(), "执行方法 (" 
						+ md.getName() + ") 时错误: " + e.getMessage());
			}
		} else {
			warnning(model.getClass(), colname + " 指定的数据列名没有映射");
		}
	}
	
	/**
	 * 如果filedname的类型不是简单类型,则使用sql创建<br>
	 * sql可以为null
	 */
	protected MethodMapping setMappingPlot(
			String fieldname, String colname, ISelecter<?> is, String pk, ISqlLogic ...logics) {

		Method setm = getMethod( BeanUtil.getSetterName(fieldname) );
		Method getm = getMethod( BeanUtil.getGetterName(fieldname) );
		MethodMapping mm = null;
		
		try {
			if (setm==null) {
				throw new OrmException("没有setter方法");
			}
			mm = new MethodMapping(setm, is, pk, logics, outfilter);
			// ormmap.set 的参数变为小写
			ormmap.put(colname.toLowerCase(), mm);
			reverse.put(getm, colname);
			
			ISelectJoin join = mm.getLogicPackage().getJoinLogic();
			if (join!=null) {
				join.setMainColumn(colname);
				join.setMainTable(orm.getTableName());
			}
			
		} catch (OrmException e) {
			warnning(orm.getModelClass(), "映射属性("
					+ fieldname + ")时错误: " + e.getMessage());
		}
		
		return mm;
	}
	
	private Method getMethod(String methodname) {
		return classMethodmap.get(methodname.toLowerCase());
	}
	
	protected Method[] getAllMethod() {
		return ms;
	}
	
	/**
	 * 取得指定列where的比较方式, 大小写不敏感<br>
	 * 该方法不会返回null
	 */
	protected LogicPackage getLogicPackage(String colname) {
		LogicPackage log = null;
		MethodMapping mm = ormmap.get(colname.toLowerCase());
		if (mm!=null) {
			log = mm.getLogicPackage();
		} else {
			log = LogicPackage.DEFAULT;
		}
		return log;
	}
	
	/**XXX 如果多个ISelectJoin连接相同的表会出现问题!! */
	protected String getJoinSql() {
		StringBuilder sql = new StringBuilder();
		
		Iterator<MethodMapping> itr = ormmap.values().iterator();
		while (itr.hasNext()) {
			MethodMapping mm = itr.next();
			ISelectJoin sjoin = mm.getLogicPackage().getJoinLogic();
			if (sjoin!=null) {
				sql.append(sjoin.getJoin());
			}
		}
		
		return sql.toString();
	}
	
	protected void stopColnameMapping() {
		usecolnamemap = false;
	}
	
	/**
	 * 取得实体get方法对表名的映射, 区分大小写
	 */
	protected String getColname(Method m) {
		return reverse.get(m);
	}
	
	private void warnning(Class<?> beanClass, String msg) {
		Tools.pl("警告:(Plot): (", beanClass, ") ", msg);
	}

	public IOrder order() {
		return order_sub;
	}
	
	
	private class Order implements IOrder {
		private StringBuilder out;
		private boolean isFirst = false;
		
		private Order() {
			this(new StringBuilder());
			isFirst = true;
		}
		
		private Order(StringBuilder _out) {
			out = _out;
		}

		public IOrder asc(String columnName) {
			return set(columnName, "asc");
		}

		public IOrder desc(String columnName) {
			return set(columnName, "desc");
		}

		private IOrder set(String cn, String o) {
			Tools.check(cn, "排序的列名不能为null");
		if (isFirst) {
				out.append(" ORDER BY ");
				isFirst = false;
			} else {
				out.append(',');
			}
			out.append(cn);
			out.append(' ');
			out.append(o);
			return this;
		}
		
		public String toString() {
			return out.toString();
		}
	}
}
