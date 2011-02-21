// CatfoOD 2010-5-11 下午02:28:03 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import jym.sim.util.Tools;

public class EnumTag extends SelectTag {

	private static final long serialVersionUID = 4858481581469242308L;
	private static final String UNKONW = "未知值";
	private String key;

	public int doStartTag() throws JspException {
		super.init();
		try {
			do_enum();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	private void do_enum() throws IOException {
		Map<String, String> obj = super.getEnumMap();
		
		if (obj==null) {
			print("服务器错误");
			return;
		}
	
		String v = obj.get(key);
		if (v!=null) {
			print(v);
		} else {
			print(UNKONW+key);
			Tools.pl("EnumTag: key属性值无效:" + key + " 在:" + getEnumObjName());
		}
	}
	
	public void setName(String name) {
		super.setName(name);
	}
	
	public void setClazz(String name) {
		super.setClazz(name);
	}
	
	public void setField(String name) {
		super.setField(name);
	}
	
	public void setKey(String k) {
		key = k;
	}
}
