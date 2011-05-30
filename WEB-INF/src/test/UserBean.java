// CatfoOD 2010-4-19 上午09:44:55 yanming-sohu@sohu.com/@qq.com

package test;


public class UserBean {
	
	private String name;
	private String id;
	private String sn;
	

	public String getName() {
		return name;
	}
	public void setName(String brongthname) {
		this.name = brongthname;
	}
	public String getId() {
		return id;
	}
	public void setId(String brongthid) {
		this.id = brongthid;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String brongthsn) {
		this.sn = brongthsn;
	}
	
	public String toString() {
		return	"name: " + name
				+ "\tid: " + id
				+ "\tsn: " + sn;
	}
}