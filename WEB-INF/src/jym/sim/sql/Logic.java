// CatfoOD 2010-4-16 上午11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * 数据库查询where字句中逻辑判断策略
 */
public class Logic implements IWhere {
	
	/** 等于 */
		public static final Logic EQ = new Logic("%1$s='%2$s'");
		
	/** 不等于 */
		public static final Logic NE = new Logic("%1$s!='%2$s'");
		
	/** 小于 */
		public static final Logic LT = new Logic("%1$s<'%2$s'");
		
	/** 小于等于 */
		public static final Logic LE = new Logic("%1$s<='%2$s'");
		
	/** 大于 */
		public static final Logic GT = new Logic("%1$s>'%2$s'");
		
	/** 大于等于 */
		public static final Logic GE = new Logic("%1$s>='%2$s'");
		
	/** like查询 */
		public static final Logic LIKE = new Logic("%1$s like '%2$s'");
		
	/** 包含查询; 如果结果中含有子串则为true */
		public static final Logic INCLUDE = new Logic("%1$s like '%%%2$s%%'");
		
	/** 排除查询; 如果结果中不含有子串则为true */
		public static final Logic EXCLUDE = new Logic("%1$s not like '%%%2$s%%'");
		
	/** 日期查询,精确到日 */
		public static final Logic DATE = new Logic("to_char(%1$s, 'yyyy-mm-dd') = '%2$s'");
	
	////////////////////// ----------------------------------
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};

	public String w(String columnName, Object value) {
		return String.format(format, columnName, value);
	}
	
}
