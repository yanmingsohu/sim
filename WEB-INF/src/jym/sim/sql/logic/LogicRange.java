// CatfoOD 2011-8-19 上午10:33:46 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.sql.IWhere;
import jym.sim.sql.Logic;

/**
 * 范围查询适合能进行大于,小于比较的字段(含等于)<br>
 * 生成类似于:<br>
 * <code>[relatedSql] <= [maxField] and [relatedSql] >= [minField]<code>
 */
public class LogicRange implements IWhere, ISkipValueCheck {

	private IWhere pack;
	
	/**
	 * 创建一个逻辑
	 * @param relatedSql - 条件中的字段,一般是数据表的列名
	 * @param minField - bean中最小值的字段名字
	 * @param maxField - bean中最大值的字段名字
	 * @param hasEq - 是否在最后进行相等比较
	 */
	public LogicRange(String relatedSql, String minField, String maxField, boolean hasEq) {
		String maxP = relatedSql + " <= " + maxField;
		String minP = relatedSql + " >= " + minField;
		
		pack = Logic.OR(
			Logic.DEF(minP + " and " + maxP),
			Logic.DEF(maxP),
			Logic.DEF(minP),
			(hasEq ? Logic.EQ : null)
		);
	}
	
	/**
	 * 创建一个逻辑,最后进行相等比较
	 * @see LogicRange
	 */
	public LogicRange(String relatedSql, String minField, String maxField) {
		this(relatedSql, minField, maxField, true);
	}

	public String w(String columnName, Object value, Object model) {
		return pack.w(columnName, value, model);
	}
}
