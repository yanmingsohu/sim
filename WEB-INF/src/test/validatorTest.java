// CatfoOD 2010-4-21 上午08:06:24 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import jym.sim.util.UsedTime;
import jym.sim.validator.Validator;
import jym.sim.validator.VerifyMessage;
import jym.sim.validator.VerifyMessage.Msg;
import jym.sim.validator.annotation.Daterange;
import jym.sim.validator.annotation.Email;
import jym.sim.validator.annotation.Notnull;
import jym.sim.validator.annotation.Num;
import jym.sim.validator.annotation.Stringlen;


/**
 * 测试字段验证器的效率
 */
@SuppressWarnings("unused")
public class validatorTest {

	public static void main(String[] args) throws Throwable {
		pressure();
		//test();
	}
	
	private static void pressure() {
		User2 u = new User2();
		
		Validator v = new Validator();
		
		UsedTime.start("相当50000个字段的验证");
		
		for (int i=0; i<10000; ++i) {
			VerifyMessage vm = v.validate(u);
//			showMsg( vm ); // 显示会占用时间
		}
		UsedTime.endAndPrint();
	}
	
	private static void test() {
		User2 u = new User2();
		u.setUsername("1");
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2101);
		u.setStart(c.getTime());
		
		u.setEmail("f@sohu.f");
		
		Validator v = new Validator();
		VerifyMessage msg = v.validate(u);
		showMsg(msg);
		
		p("over.");
	}
	
	private static void showMsg(VerifyMessage msg) {
		if (msg.isSuccess()) {
			p("没有错误");
		}
		else {
			Iterator<Msg> itr = msg.getMessages();
			while (itr.hasNext()) {
				Msg m = itr.next();
				p(m.getFieldName() + ":" + m.getMessage());
			}
		}
	}
	
	public static void p(Object o) {
		System.out.println(o);
	}
}

class User {
	
	@Notnull(msg="用户名不能为空")
	@Stringlen(max=10, min=3, msg="用户名长度3-10位")
	private String username;

	@Stringlen(max=10, min=3, msg="密码长度3-10位")
	@Notnull(msg="密码不能为空")
	private String password;
	
	@Num(msg="ID有效值在10~2000", min=10, max=2000)
	private int id;
	
	@Notnull(msg="日期不能为空")
	@Daterange(msg="日期超出范围", min="1980-1-1", max="2100-12-30")
	private Date start;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

class User2 extends User {
	
	@Notnull(msg="邮箱不能为空")
	@Email(msg="邮箱格式无效")
	private String email;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}

