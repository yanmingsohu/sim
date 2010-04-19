// CatfoOD 2010-4-19 ÉÏÎç09:44:55 yanming-sohu@sohu.com/@qq.com

package test;


public class UserBean {
	
	private String brongthname;
	private String brongthid;
	private String brongthsn;
	

	public String getBrongthname() {
		return brongthname;
	}
	public void setBrongthname(String brongthname) {
		this.brongthname = brongthname;
	}
	public String getBrongthid() {
		return brongthid;
	}
	public void setBrongthid(String brongthid) {
		this.brongthid = brongthid;
	}
	public String getBrongthsn() {
		return brongthsn;
	}
	public void setBrongthsn(String brongthsn) {
		this.brongthsn = brongthsn;
	}
	
	public String toString() {
		return	"name: " + brongthname
				+ "\tid: " + brongthid
				+ "\tsn: " + brongthsn;
	}
}