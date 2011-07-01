// CatfoOD 2010-8-13 下午02:15:21 yanming-sohu@sohu.com/@qq.com

package test;

import java.sql.SQLException;
import java.sql.Savepoint;

import jym.sim.sql.IJdbcSession;
import jym.sim.sql.ITransactionHanle;
import jym.sim.sql.Transaction;

public class TestTransaction {

	
	public static void main(String[] args) {
		
		final Point p = new Point(0);
		final Transaction t = new Transaction(p);
		
		p.set(10);
		pl(p);
		
		Exception e = t.start(new ITransactionHanle() {
			public void start() throws Exception {
				p.set(1);
				
				t.start(new ITransactionHanle() {
					public void start() throws Exception {
						pl("嵌套");
						p.set(2);
					}
				});
				p.set(3);
				
				throw new Exception("测试");
			}
		});
		
		if (e!=null) {
			pl("\n回滚原因: " + e);
		}
		
		pl("当前值:" + p);
	}

	
	public static class Point implements Transaction.IJsGeter {
		
		private Object true_value = null;
		private Object cache_value = null;
		private boolean auto = true;
		
		private Point(Object o) {
			cache_value = true_value = o;
		}
		
		public void set(Object o) {
			cache_value = o;
			if (auto) {
				true_value = cache_value;
			} else {
				pl("事务中设置: " + o);
			}
		}
		
		public String toString() {
			return "Point value: " + ( auto ? true_value : cache_value );
		}

		public IJdbcSession get() throws Exception {
			return new IJdbcSession() {
				
				public boolean commit() {
					pl("事务已递交");
					true_value = cache_value;
					return true;
				}

				public boolean isAutoCommit() {
					return false;
				}

				public boolean releaseSavepoint(Savepoint savepoint) {
					return false;
				}

				public boolean rollback() {
					pl("出错,事务回滚");
					return true;
				}

				public boolean rollback(Savepoint savepoint) {
					pl("出错,事务回滚," + savepoint);
					return false;
				}

				public void setCommit(boolean isAuto) {
					pl("set " + (isAuto?"auto":"manual") + " commit.");
					auto = isAuto;
				}

				public Savepoint setSavepoint() {
					return new SavepointImpl();
				}

				public Savepoint setSavepoint(String name) {
					return new SavepointImpl(name);
				}

				public void close() {
				}
			};
		}
	}
	
	private static class SavepointImpl implements Savepoint {
		int id;
		String name = null;
		
		SavepointImpl() {
			id = _id++;
		}
		SavepointImpl(String n) {
			this();
			name = n;
		}
		
		public int getSavepointId() throws SQLException {
			return id;
		}

		public String getSavepointName() throws SQLException {
			return "savepoint:[" + id + ", " + name + "]";
		}
		
		public String toString() {
			try {
				return getSavepointName();
			} catch (SQLException e) {
				return e.getLocalizedMessage();
			}
		}
	}
	
	private static int _id = 0;
	
	private static void pl(Object o) {
		System.out.println(o);
	}
}
