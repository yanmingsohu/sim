// CatfoOD 2010-4-19 上午10:59:36 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.sql.SQLException;

import javax.sql.DataSource;

import jym.sim.sql.IQuery;

/**
 * 实体对象映射
 */
public class OrmTemplate<T> 
	extends UpdateTemplate<T> 
	implements IQuery, ISelecter<T>, IUpdate<T> {
	

	/**
	 * orm模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param ds - 数据源
	 * @param modelclass - 数据模型的class类
	 * @param tablename - 数据库表名
	 * @param priKey - 主键名
	 */
	public OrmTemplate(DataSource ds, Class<T> modelclass, String tablename,
			String priKey) {
		super(ds, modelclass, tablename, priKey);
	}

	/**
	 * jdbc模板构造函数,默认每次连接不会自动关闭连接
	 * 
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public OrmTemplate(DataSource ds, IOrm<T> orm) {
		super(ds, orm);
	}
	
}
