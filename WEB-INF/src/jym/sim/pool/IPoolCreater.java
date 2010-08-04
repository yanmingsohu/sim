// CatfoOD 2010-8-4 上午09:24:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import java.io.IOException;

import javax.sql.DataSource;

/**
 * 不同数据池的创建类,实现这个接口<br>
 * 并且有无参构造函数
 */
public interface IPoolCreater {
	
	/**
	 * 从配置中创建连接池对象, <b>不能返回null</b>
	 * 
	 * @param conf - 数据库连接配置
	 * @return 数据池
	 * @exception 无效的配置,或数据库连接错误
	 */
	DataSource create(PoolConf conf) throws IOException;
}
