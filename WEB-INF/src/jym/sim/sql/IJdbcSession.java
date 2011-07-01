// CatfoOD 2010-6-8 下午03:21:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Savepoint;

/**
 * 一个数据库会话,该对象由创建的类维护
 */
public interface IJdbcSession {
	/**
	 * 是否在命令执行后自动递交
	 */
	public boolean isAutoCommit();
	/**
	 * 设置是否自动递交,<b>事务结束可能需要把状态改回</b>
	 */
	public void setCommit(boolean isAuto);
	/**
	 * 在当前事务中创建一个未命名的保存点 (savepoint)，并返回表示它的新 Savepoint 对象。 
	 * 如果在活动事务范围之外调用 setSavepoint，则将在新创建的保存点上启动事务。
	 * 
	 * @return - 如果创建保存点失败,在关闭的连接上调用此方法,或者当前处于自动提交模式下 返回null
	 */
	public Savepoint setSavepoint(); 
	/**
	 * 在当前事务中创建一个具有给定名称的保存点，并返回表示它的新 Savepoint 对象。 
	 * 如果在活动事务范围之外调用 setSavepoint，则将在新创建的保存点上启动事务。 
	 * 
	 * @return - 如果创建保存点失败,在关闭的连接上调用此方法,或者当前处于自动提交模式下 返回null
	 */
	public Savepoint setSavepoint(String name);
	/**
	 * 从当前事务中移除指定的 Savepoint 和后续 Savepoint 对象。
	 * 在已移除保存点之后，对该保存点的任何引用都会失败
	 * 
	 * @param savepoint - 将移除的 Savepoint 对象
	 * @return - 成功返回true
	 */
	public boolean releaseSavepoint(Savepoint savepoint);
	/**
	 * 取消在当前事务中进行的所有更改，并释放此 Connection 对象当前持有的所有数据库锁。
	 * 此方法只应该在已禁用自动提交模式时使用。
	 * 
	 * @return - 回滚成功返回true.
	 * 		如果发生数据库访问错误，在参与分布式事务的同时调用此方法，
	 * 		在关闭的连接上调用此方法，或者此 Connection 对象处于自动提交模式则返回false
	 */
	public boolean rollback();
	/**
	 * 取消所有设置给定 Savepoint 对象之后进行的更改。 
	 * 此方法只应该在已禁用自动提交时使用。.
	 * 
	 * @param savepoint - 要回滚到的 Savepoint 对象
	 * @return - 回滚成功返回true.
	 * 		如果发生数据库访问错误，在参与分布式事务的同时调用此方法，
	 * 		在关闭的连接上调用此方法，或者此 Connection 对象处于自动提交模式则返回false
	 */
	public boolean rollback(Savepoint savepoint);
	/**
	 * 使所有上一次提交/回滚后进行的更改成为持久更改，
	 * 并释放此 Connection 对象当前持有的所有数据库锁。
	 * 此方法只应该在已禁用自动提交模式时使用。
	 * 
	 * @return 递交成功返回true
	 */
	public boolean commit();
	/**
	 * 如有可以释放资源(自动递交),则立即释放,否则该方法什么都不做<br>
	 * 可以在数据库操作完成时调用该方法立即释放资源而不是等待垃圾回收
	 */
	public void close();
}
