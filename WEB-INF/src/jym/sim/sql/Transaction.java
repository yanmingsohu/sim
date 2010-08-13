// CatfoOD 2010-8-13 下午02:09:12 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import jym.sim.util.Tools;


/**
 * 封装数据库事务操作
 */
public class Transaction {
	
	private IJsGeter getter;
	
	/**
	 * 为IQuery接口提供事务支持
	 */
	public Transaction(final IQuery iq) {		
		this(new IJsGeter() {
			public IJdbcSession get() throws Exception {
				if (iq==null) {
					throw new NullPointerException("Transaction 初始化时IQuery不能为null");
				}
				return iq.getSession();
			}
		});
	}
	
	/**
	 * 通过IJsGeter接口提供事务支持
	 */
	public Transaction(IJsGeter g) {
		Tools.check(g, "Transaction 初始化失败，参数不能为null");
		getter = g;
	}
	
	/**
	 * 开始一个事务
	 * 
	 * @param handle - 处理事务的对象
	 * @return 如果事务成功并递交返回null, 否则返回事务失败的原因
	 */
	public Exception start(ITransactionHanle handle) {
		IJdbcSession js = null;
		Exception ex = null;
		
		try {
			js = getter.get();
			js.setCommit(false);
			
			handle.start();
			
			if (!js.commit()) {
				throw new Exception("Transaction 递交失败，未知的错误.");
			}
			
		} catch(Exception e) {
			if (js!=null) {
				js.rollback();
			}
			ex = e;
			
		} finally {
			if (js!=null) {
				js.setCommit(true);
			}
		}
		
		return ex;
	}
	
	/**
	 * 提供IJdbcSession的类
	 */
	public interface IJsGeter {
		IJdbcSession get() throws Exception;
	}
}
