// CatfoOD 2010-8-20 上午10:48:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.sql.IWhere;

/**
 * 固定的sql where子句,总是返回已经定义的sql
 */
public class FixedLogic implements IWhere, ISkipValueCheck {
	
	private final String sql;
	
	public FixedLogic(String _sql) {
		sql = _sql;
	}

	public String w(String columnName, Object value, Object model) {
		return sql;
	}

}
