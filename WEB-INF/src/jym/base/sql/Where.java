package jym.base.sql;

import java.util.HashMap;
import java.util.Map;

public class Where implements ISqlsub {
	
	private Map<String, Object> vars;
	private StringBuilder where;
	
	/**
	 * where子句,可以是全部,也可以是部分
	 */
	public Where(String wh) {
		vars  = new HashMap<String, Object>();
		where = new StringBuilder();
		add(wh);
	}
	
	/**
	 * 把条件加入where子句,prere中可以有$开始的变量变量以$开始空格结束<br>
	 * 格式: and {prere} 
	 * 
	 * @param prere - where子句,可以有变量
	 * @return Where对象,便于书写
	 */
	public Where and(String prere) {
		where.append(" and ");
		add(prere);
		return this;
	}
	
	/**
	 * 把条件加入where子句,prere中可以有$开始的变量变量以$开始空格结束<br>
	 * 格式: and {prere} 
	 * 
	 * @param prere - where子句,可以有变量
	 * @return Where对象,便于书写
	 */
	public Where or(String prere) {
		where.append(" or ");
		add(prere);
		return this;
	}
	
	private void add(String p) {
		where.append('(');
		where.append(p);
		where.append(')');
	}
	
	/**
	 * 用value替换where子句中的变量占位符
	 * @param variableName -- 变量名
	 * @param value -- 替换的值
	 */
	public Where set(String variableName, Object value) {
		vars.put(variableName, value);
		return this;
	}

	@Override
	public String get() {
		return null;
	}
}
