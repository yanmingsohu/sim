// CatfoOD 2010-4-16 上午10:07:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 类型转换接口
 */
interface ITransition {
	
	/**
	 * 从rs的col列的数据取出转换成指定的类型,返回
	 */
	Object trans(ResultSet rs, int col) throws SQLException;
	
}
