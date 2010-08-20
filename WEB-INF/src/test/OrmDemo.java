// CatfoOD 2010-4-16 ÏÂÎç01:42:14 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.Iterator;
import java.util.List;

import jym.sim.orm.IOrm;
import jym.sim.orm.IPlot;
import jym.sim.orm.ISelecter;
import jym.sim.orm.IUpdate;
import jym.sim.orm.OrmTemplate;
import jym.sim.sql.Logic;

@SuppressWarnings("unused")
public class OrmDemo {
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		OrmTemplate<UserBean> orm = new OrmTemplate<UserBean>(TestDBPool.getDataSource(), new IOrm<UserBean>() {

			public Class<UserBean> getModelClass() {
				return UserBean.class;
			}

			public void mapping(IPlot plot) {
				plot.fieldPlot("brongthname", "brongthname", Logic.INCLUDE);
				plot.fieldPlot("brongthid", "brongthid");
				plot.fieldPlot("brongthsn", "brongthsn");
			}

			public String getKey() {
				return "brongthsn";
			}

			public String getTableName() {
				return "ba_brongth";
			}
			
		});
		
		checkDelete(orm);
//		checkUpdate(orm);
//		checkInsert(orm);
		
		checkSelect(orm);
		
		// list do something...
	}
	
	private static void checkDelete(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("rename2 " + i);
			
			orm.delete(user);
		}
	}
	
	private static void checkUpdate(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("rename2 " + i);
			user.setBrongthsn(""+i);
			user.setBrongthid("i" + i);
			
			orm.update(user);
		}
	}
	
	
	private static void checkInsert(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("test " + i);
			user.setBrongthsn(""+i);
			user.setBrongthid("i" + i);
			orm.add(user);
		}
	}

	private static void checkSelect(ISelecter<UserBean> orm) {
		UserBean user = new UserBean();
		List<?> list = orm.select(user, "or");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
