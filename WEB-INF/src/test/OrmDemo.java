// CatfoOD 2010-4-16 ÏÂÎç01:42:14 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import jym.base.orm.IOrm;
import jym.base.orm.IPlot;
import jym.base.orm.ISelecter;
import jym.base.orm.IUpdate;
import jym.base.orm.OrmTemplate;
import jym.base.sql.Logic;
import jym.base.util.BeanUtil;

public class OrmDemo {
	final private static String url = "jdbc:oracle:thin:@192.168.0.68:1521:RMCSH";
	final private static String driver = "oracle.jdbc.pool.OracleDataSource";
	final private static String user = "LWBS_ROOT";
	final private static String pwd = "dl20100325";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		Class<?> clazz = Class.forName(driver);
		DataSource ds = (DataSource) BeanUtil.creatBean(clazz);
		BeanUtil.invoke(ds, "setUser", user);
		BeanUtil.invoke(ds, "setURL", url);
		BeanUtil.invoke(ds, "setPassword", pwd);
		
		OrmTemplate<UserBean> orm = new OrmTemplate<UserBean>(ds, new IOrm<UserBean>() {

			public Class<UserBean> getModelClass() {
				return UserBean.class;
			}

			public void mapping(IPlot plot) {
				plot.fieldPlot("brongthname", "brongth_name", Logic.INCLUDE);
				plot.fieldPlot("brongthid", "brongth_id");
				plot.fieldPlot("brongthsn", "brongth_sn");
			}

			public String getKey() {
				return "brongth_sn";
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
	
	//@SuppressWarnings("unused")
	private static void checkDelete(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("rename2 " + i);
			
			orm.delete(user);
		}
	}
	
	@SuppressWarnings("unused")
	private static void checkUpdate(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("rename2 " + i);
			user.setBrongthsn(""+i);
			user.setBrongthid("i" + i);
			
			orm.update(user);
		}
	}
	
	@SuppressWarnings("unused")
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
