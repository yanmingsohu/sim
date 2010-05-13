// CatfoOD 2010-4-16 下午04:49:18 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import jym.sim.sql.ISql;
import jym.sim.util.Tools;

public class UpdateTemplate<T> extends SelectTemplate<T> implements IUpdate<T> {
	
	private final static String INSERT = "INSERT INTO ";
	private final static String DELETE = "DELETE FROM ";
	private final static String UPDATE = "UPDATE ";
	protected static String DATE_FORMAT = "yyyy-MM-dd";
	
	private SimpleDateFormat sqlDateFormat = new SimpleDateFormat(DATE_FORMAT);
	private final String pk;
	

	public UpdateTemplate(DataSource ds, Class<T> modelclass, String tablename, String key) {
		super(ds, modelclass, tablename, key);
		pk = key;
	}

	public UpdateTemplate(DataSource ds, IOrm<T> orm) {
		super(ds, orm);
		pk = orm.getKey();
	}
	
	/**
	 * <b>与oralce的date类型绑定</b>
	 */
	private Object tranValue(Object o) {
		if (o instanceof Date) {
			Date d = (Date)o;
			o = sqlDateFormat.format(d);
		} 
		return o;
	}

	public boolean add(T model) {
		final StringBuilder sql = new StringBuilder(INSERT);
		sql.append(getOrm().getTableName());

		final StringBuilder columns = new StringBuilder();
		final StringBuilder values  = new StringBuilder();
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			public void set(String column, Object value) {
				if (!column.equalsIgnoreCase(pk)) {
					if (first) first = false;
					else {
						columns.append(',');
						values.append(',');
					}
					columns.append(column);
					values.append('\'').append(tranValue(value)).append('\'');
				}
			}			
		});
		
		sql.append('(').append(columns).append(')');
		sql.append("VALUES");
		sql.append('(').append(values).append(')');
		
		final Refer result = new Refer();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				result.b = stm.executeUpdate(sql.toString()) > 0;
			}
		});
		
		return result.b;
	}

	public int delete(T model) {
		final StringBuilder sql = new StringBuilder(DELETE);
		sql.append(getOrm().getTableName());
		sql.append(" WHERE ");
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			
			public void set(String column, Object value) {
				if (first) {
					first = false;
				} else {
					sql.append(" AND ");
				}
				sql.append(column).append('=');
				sql.append('\'').append(tranValue(value)).append('\'');
			}			
		});
		
		final Refer result = new Refer();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				result.i = stm.executeUpdate(sql.toString());
			}
		});
		
		return result.i;
	}

	public int update(final T model) {
		Tools.check(pk, "IOrm.getKey()返回null, update不能执行.");
		
		final Refer result = new Refer();
		
		final StringBuilder sql = new StringBuilder(UPDATE);
		sql.append(getOrm().getTableName()).append(" SET ");
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			
			public void set(String column, Object value) {
				if ( !column.equalsIgnoreCase(pk) ) {
					if (first) {
						first = false;
					} else {
						sql.append(" , ");
					}
					sql.append(column).append('=');
					sql.append('\'').append(tranValue(value)).append('\'');
					
				} else {
					result.value = value;
				}
			}
		});
		
		Tools.check(result.value, "请检查IOrm.getKey()方法是否返回正确的列名:" + pk
				+ " 或主键属性是否返回null值:" + model.getClass() );
		
		sql.append(" WHERE ").append( pk )
				.append("= '").append( result.value ).append("'");
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				result.i = stm.executeUpdate(sql.toString());
			}
		});
		
		return result.i;
	}
	
//	private void warnning(String msg) {
//		System.out.println("警告:(UpdataTemplate): " + msg);
//	}

	private class Refer {
		private boolean b = false;
		private int i = 0;
		private Object value = null;
	}
}
