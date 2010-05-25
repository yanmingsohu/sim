// CatfoOD 2010-4-16 上午09:38:08 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import jym.sim.exception.OrmException;
import jym.sim.sql.Logic;
import jym.sim.util.BeanUtil;


/**
 * 实体属性与数据库列映射策略实现
 *
 * @param <T> - 实体类型
 */
class Plot<T> implements IPlot {
	
	private final static String PASS_COLM_NAME = "sim__";
	
	/** 使用小写比较String <表列名, 方法封装>*/
	private Map<String, MethodMapping> ormmap;
	/** 使用小写比较String <方法小写名, 方法>*/
	private Map<String, Method> classMethodmap;
	/** 大小写敏感, <方法, 列名> */
	private Map<Method, String> reverse;
	
	private Method[] ms;
	private IOrm<T> orm;
	private boolean usecolnamemap = true;
	
	
	public Plot(IOrm<T> _orm) {
		orm = _orm;
		initMethods();
		initOrm();
	}
	
	private void initMethods() {
		ms = orm.getModelClass().getMethods();
		classMethodmap = new HashMap<String, Method>();
		for (int i=0; i<ms.length; ++i) {
			// 使用小写比较
			classMethodmap.put(ms[i].getName().toLowerCase(), ms[i]);
		}
	}
	
	private void initOrm() {
		ormmap = new HashMap<String, MethodMapping>();
		reverse = new HashMap<Method, String>();
		orm.mapping(this);
	}
	
	public void fieldPlot(String fn, String cn) {
		setMappingPlot(fn, cn, null, null, null);
	}

	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter, String pk) {
		setMappingPlot(fieldName, colname, getter, pk, null);
	}
	
	public void fieldPlot(String fieldName, String colname, Logic log) {
		setMappingPlot(fieldName, colname, null, null, log);
	}
	
	protected void mapping(String colname, int colc, ResultSet rs, T model) {
		colname = colname.toLowerCase();
		if (colname.startsWith(PASS_COLM_NAME)) {
			return;
		}
		
		MethodMapping md = null;

		// 自动使用表格列名进行映射
		if (usecolnamemap && !ormmap.containsKey(colname)) {
			md = setMappingPlot(colname, colname, null, null, null);
			
		} else {
			md = ormmap.get(colname);
		}
		
		if (md!=null) {
			try {
				md.invoke(rs, colc, model);
				
			} catch(Exception e) {
				warnning(model.getClass(), "执行方法 (" 
						+ md.getName() + ") 时错误: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			warnning(model.getClass(), colname+" 指定的数据行没有映射");
		}
	}
	
	/**
	 * 如果filedname的类型不是简单类型,则使用sql创建<br>
	 * sql可以为null
	 */
	protected MethodMapping setMappingPlot(
			String fieldname, String colname, ISelecter<?> is, String pk, Logic log) {

		Method setm = getMethod( BeanUtil.getSetterName(fieldname) );
		Method getm = getMethod( BeanUtil.getGetterName(fieldname) );
		MethodMapping mm = null;
		
		try {
			if (setm==null) {
				throw new OrmException("没有setter方法");
			}
			mm = new MethodMapping(setm, is, pk, log);
			// ormmap.set 的参数变为小写
			ormmap.put(colname.toLowerCase(), mm);
			reverse.put(getm, colname);
			
		} catch (OrmException e) {
			warnning(orm.getModelClass(),
					"映射属性(" + fieldname + ")时错误: " + e.getMessage());
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
	 * 取得指定列的比较方式, 大小写不敏感
	 */
	public Logic getColumnLogic(String colname) {
		Logic log = null;
		MethodMapping mm = ormmap.get(colname.toLowerCase());
		if (mm!=null) {
			log = mm.getColumnLogic();
		}
		return log;
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
		System.out.println("警告:(Plot): (" + beanClass +") " + msg);
	}
	
}
