package jym.sim.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jym.sim.filter.FilterPocket;
import jym.sim.filter.SimFilterException;
import jym.sim.orm.page.IPage;
import jym.sim.orm.page.NotPagination;
import jym.sim.orm.page.PageBean;
import jym.sim.sql.IQuery;
import jym.sim.sql.ISql;
import jym.sim.sql.IWhere;
import jym.sim.sql.JdbcTemplate;
import jym.sim.util.Tools;

/**
 * 数据库实体检索模板
 */
public class SelectTemplate<T> extends JdbcTemplate implements ISelecter<T>, IQuery {

	private Class<T> clazz;
	private IOrm<T> orm;
	private Plot<T> plot;
	private IPage pagePlot;
	private FilterPocket infilter;
	private FilterPocket outfilter;
	private CheckVaildValue vaildChecker;
	
	
	/**
	 * jdbc模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param ds - 数据源
	 * @param modelclass - 数据模型的class类
	 * @param tablename - 数据库表名
	 * @param priKey - 主键名
	 */
	public SelectTemplate(DataSource ds, final Class<T> modelclass, 
			final String tablename, final String priKey) {
		
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public void mapping(IPlot plot) {
			}

			public String getTableName() {
				return tablename;
			}

			public String getKey() {
				return priKey;
			}
		});
	}
	
	/**
	 * jdbc模板构造函数,默认每次连接不会自动关闭连接
	 * 
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public SelectTemplate(DataSource ds, IOrm<T> orm) {
		super(ds);
		this.orm = orm;
		pagePlot = new NotPagination();
		
		check();
		init();
	}
	
	private void init() {
		vaildChecker = new CheckVaildValue();
		infilter = new FilterPocket();
		outfilter = new FilterPocket();
		clazz = orm.getModelClass();
		plot = new Plot<T>(orm, outfilter);
	}
	
	private void check() {
	//	Tools.check(orm.getKey(), 			"getKey()不能返回null"			);
		Tools.check(orm.getModelClass(),	"getModelClass()不能返回null"	);
		Tools.check(orm.getTableName(),		"getTableName()不能返回null"		);
	}
	
	/**
	 * 取得输入数据过滤器设置器，通过FilterBase的实例可以插入新的过滤器<br>
	 * 过滤器的作用是在实体属性拼装为sql之前先转换属性的值
	 */
	public FilterPocket getInputParamPocket() {
		return infilter;
	}
	
	/**
	 * 取得数据库返回数据过滤器设置器，通过在FilterBase的实例可以插入新的过滤器<br>
	 * 过滤器的作用是把从数据库返回的数据，在传给实体属性前进行过滤
	 */
	public FilterPocket getOutputParamPocket() {
		return outfilter;
	}
	
	/**
	 * 为拼装sql时的实体属性有效检查配置策略,<br>
	 * 过滤器只要返回非null,则认为值有效<br>
	 * 有效的属性值将被用来拼装sql(增删改查),无效的值则会被忽略<br>
	 * 默认null值总是认为是无效的
	 */
	public FilterPocket getCheckVaildValue() {
		return vaildChecker;
	}
	
	/**
	 * 从普通对象转换为sql语句字符串
	 */
	protected final Object transformValue(Object o) {
		try {
			return infilter.filter(o);
		} catch (SimFilterException e) {
			warnning("输入参数过滤器转换失败:" + e);
			handleException(e);
		}
		return o;
	}
	
	/**
	 * 该值是否有效,有效性测试在getCheckVaildValue返回的对象中设置<br>
	 * null值总是认为是无效的
	 */
	protected final boolean isValid(Object value) {
		return vaildChecker.isValid(value);
	}
	
	protected void loopMethod2Colume(T model, IColumnValue cv) {
		Method[] ms = plot.getAllMethod();
		
		for (int i=0; i<ms.length; ++i) {
			String colname = plot.getColname(ms[i]);
		
			if (colname!=null) {
				try {
					Tools.check(model, "bean参数不能为null.");
					Object value = ms[i].invoke(model, new Object[0]);
					
					cv.set(colname, value);
					
				} catch (Exception e) {
					warnning("invoke错误: "+ e);
					Tools.plerr(e, "jym.*");
				}
			}
		}
	}
	
	public List<T> select(T model, String join) {
		PageBean p = new PageBean();
		p.setCurrent(1);
		p.setOnesize(Integer.MAX_VALUE);
		p.setTotal(1);
		return select(model, join, p);
	}
	
	public List<T> select(final T model, final String join, final PageBean pagedata) {
		Tools.check(pagedata, "分页数据不能为null");
		final StringBuilder where = new StringBuilder();
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;

			public void set(String column, Object value) {
				IWhere logic = plot.getColumnLogic(column);
				
				if (logic instanceof ISkipValueCheck || isValid(value) ) {
					value = logic.w(column, transformValue( value ), model);

					if (value!=null) {
						if (first) {
							where.append("where");
							first = false;
						} else {
							where.append(join);
						}
						where.append(" (" ).append( value ).append( ") " );
					}
				}
			}
		});
		
		return select(
				pagePlot.select(orm.getTableName(), where.toString(), plot.getOrder(), pagedata),
				pagedata
				);
	}
	
	private List<T> select(final String sql, final PageBean pagedata) {
		final List<T> brs = new ArrayList<T>();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				select( stm.executeQuery(sql), brs, pagedata );
			}
		});
		
		return brs;
	}
	
	private void select(ResultSet rs, List<T> brs, PageBean pagedata) throws Exception {
		if (rs==null) return;
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int col = rsmd.getColumnCount();
			
			if (rs.next()) {
				int total = 1;
			try { 
				// 没有TOTAL_COLUMN_NAME指定的列并不是错误
				total = rs.getInt(IPage.TOTAL_COLUMN_NAME);
				}
			catch(Exception e) {}
				
				pagedata.setTotalRow(total);
				
				do {
					T model = clazz.newInstance();
					
					for (int i=1; i<=col; ++i) {
						// ormmap.set时已经变为小写
						plot.mapping(rsmd.getColumnLabel(i), i, rs, model);
					}
					brs.add(model);
				} while ( rs.next() );
			}
			
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			plot.stopColnameMapping();
		}
	}
	
	private void warnning(String msg) {
		System.out.println("警告:(SelectTemplate): " + msg);
	}
	
	public void setPaginationPlot(IPage plot) {
		if (plot!=null) {
			pagePlot = plot;
		}
	}

	public Class<T> getModelClass() {
		return orm.getModelClass();
	}
	
	protected IOrm<T> getOrm() {
		return orm;
	}
	
	protected Plot<T> getPlot() {
		return plot;
	}
	
}
