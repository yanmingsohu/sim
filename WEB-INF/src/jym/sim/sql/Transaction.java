// CatfoOD 2010-8-13 下午02:09:12 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Savepoint;

import jym.sim.util.Tools;


/**
 * 封装数据库事务操作, 事务中不能启动线程, 否则会逃逸事务<br>
 * 事务实例间可以嵌套, 也可以嵌套自己, 该对象线程不安全
 */
public class Transaction {
	
	private static final ThreadLocal<Int> deep = new ThreadLocal<Int>();
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
	
	private Int currDeep() {
		Int i = deep.get();
		if (i==null) {
			i = new Int();
			deep.set(i);
		}
		return i;
	}
	
	private void addDeep() {
		Int i = currDeep();
		i.add();
	}
	
	/** 如果当前事务没有被嵌套, 则返回true */
	private boolean subDeep() {
		Int i = currDeep();
		i.sub();
		return i.i==0;
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
		Savepoint point = null;
		
		try {
			js = getter.get();
			js.setCommit(false);
			point = js.setSavepoint();
			addDeep();
			
			handle.start();
			
			if (currDeep().i<=1 && (!js.commit())) {
				throw new Exception("Transaction 递交失败，未知的错误.");
			}
			
		} catch(Exception e) {
			if (js!=null) {
				js.rollback(point);
			}
			ex = e;
			
		} finally {
			if (subDeep() && js!=null) {
				js.setCommit(true);
				js.close();
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
	
	private class Int {
		int i = 0;
		void add() { ++i; }
		void sub() { --i; if (i<0) i=0; }
	}
}
