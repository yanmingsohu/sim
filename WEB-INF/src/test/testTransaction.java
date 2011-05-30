// CatfoOD 2010-8-13 下午02:15:21 yanming-sohu@sohu.com/@qq.com

package test;

import java.sql.Savepoint;

import jym.sim.sql.IJdbcSession;
import jym.sim.sql.ITransactionHanle;
import jym.sim.sql.Transaction;
import jym.sim.util.Tools;

public class TestTransaction {

	
	public static void main(String[] args) {
		
		final Point p = new Point(0);
		Transaction t = new Transaction(p);
		
		p.set(10);
		System.out.println(p);
		
		Exception e = t.start(new ITransactionHanle() {
			public void start() throws Exception {
				p.set(1);
				System.out.println(p);
				throw new Exception("测试事务错误");
			}
		});
		
		if (e!=null) {
			e.printStackTrace();
		}
		
		System.out.println(p);
	}

	
	public static class Point implements Transaction.IJsGeter {
		
		private Object tv = null;
		private Object no = null;
		private boolean auto = true;
		
		private Point(Object o) {
			tv = o;
		}
		
		public void set(Object o) {
			no = o;
			if (auto) {
				tv = no;
			}
		}
		
		public String toString() {
			return String.valueOf(tv);
		}

		public IJdbcSession get() throws Exception {
			return new IJdbcSession() {
				
				public boolean commit() {
					Tools.pl("事务已递交");
					tv = no;
					return true;
				}

				public boolean isAutoCommit() {
					return false;
				}

				public boolean releaseSavepoint(Savepoint savepoint) {
					return false;
				}

				public boolean rollback() {
					Tools.pl("出错,事务回滚");
					return true;
				}

				public boolean rollback(Savepoint savepoint) {
					return false;
				}

				public void setCommit(boolean isAuto) {
					Tools.pl("set " + (isAuto?"auto":"manual") + " commit.");
					auto = isAuto;
				}

				public Savepoint setSavepoint() {
					return null;
				}

				public Savepoint setSavepoint(String name) {
					return null;
				}
				
			};
		}
	}
}
