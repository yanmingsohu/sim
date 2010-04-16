// CatfoOD 2010-4-16 ÏÂÎç01:42:14 yanming-sohu@sohu.com/@qq.com

package test;

import javax.sql.DataSource;

import jym.base.orm.IOrm;
import jym.base.orm.IPlot;
import jym.base.orm.OrmTemplate;
import jym.base.sql.Logic;

public class OrmDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DataSource ds = null; // from anywhere...
		
		OrmTemplate<User> orm = new OrmTemplate<User>(ds, new IOrm<User>() {

			public Class<User> getModelClass() {
				return User.class;
			}

			public String getSimSql() {
				return "select * from users $where";
			}

			public void mapping(IPlot plot) {
				plot.fieldPlot("brongthname", "brongth_name", Logic.INCLUDE);
				plot.fieldPlot("brongthid", "brongth_id");
				plot.fieldPlot("brongthsn", "brongth_sn");
			}
			
		});
		
		User user = new User();
		orm.select(user, "or");
		// list do something...
	}

}

class User {
	private String name;
	private String pass;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
}