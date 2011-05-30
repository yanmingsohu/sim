// CatfoOD 2010-8-19 上午11:03:41 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

import jym.sim.filter.SimFilterException;

/**
 * 日期转换为sql字符串
 */
public class SqlDateFilter implements ISqlInputParamFilter<Date> {
	
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String ORACLE_DEFAULT_FMT = "dd-M月 -yy";

	private SimpleDateFormat sqlDateFormat;
	
	
	/**
	 * 使用默认格式拼装sql日期
	 */
	public SqlDateFilter() {
		this(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 使用指定的格式拼装sql日期
	 * @param format - 日期格式
	 */
	public SqlDateFilter(String format) {
		this(new SimpleDateFormat(format));
	}
	
	public SqlDateFilter(SimpleDateFormat df) {
		sqlDateFormat = df;
	}
	
	public Date see(final Date date) throws SimFilterException {
		return new Date(date.getTime()) {
			private static final long serialVersionUID = 8902062727482387855L;

			public String toString() {
				return sqlDateFormat.format(date);
			}
		};
	}
	
}
