// CatfoOD 2010-4-16 上午11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.base.sql;

public enum Logic {
	
	/** 等于 */
		EQ("='%1$s'"),
		
	/** 不等于 */
		NE("!='%1$s'"),
		
	/** 小于 */
		LT("<'%1$s'"),
		
	/** 小于等于 */
		LE("<='%1$s'"),
		
	/** 大于 */
		GT(">'%1$s'"),
		
	/** 大于等于 */
		GE(">='%1$s'"),
		
	/** like查询 */
		LIKE("like '%1$s'"),
		
	/** 包含查询, 如果结果中含有子串则为true */
		INCLUDE("like '%%%1$s%%'"),
		
	/** 排除查询, 如果结果中不含有子串则为true */
		EXCLUDE("not like '%%%1$s%%'"),
	;
	
	////////////////////// ----------------------------------
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};
	
	/**
	 * 把parm作为参数与逻辑方式结合成字符串
	 */
	public String in(Object parm) {
		return String.format(format, parm);
	}
	
}
