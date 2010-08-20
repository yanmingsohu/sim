// CatfoOD 2010-4-21 上午09:21:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jym.sim.util.Tools;

/**
 * 验证结果集
 */
public class VerifyMessage {
	
	private List<Msg> msgs;
	private String prefix = "";
	private String suffix = "";
	
	/**
	 * 验证消息
	 */
	public VerifyMessage() {
		msgs = new ArrayList<Msg>();
	}
	
	/**
	 * 压入消息
	 */
	protected void putMsg(Field field, String msg) {
		msgs.add(new Msg(field, msg));
	}
	
	/**
	 * 取得错误消息的迭代器,迭代器中的元素可以取得错误详细信息
	 */
	public Iterator<Msg> getMessages() {
		return msgs.iterator();
	}
	
	/**
	 * 如果没有错误消息返回true,否则用getMessages()方法取出错误消息
	 */
	public boolean isSuccess() {
		return msgs.size()==0;
	}
	
	
	public class Msg {
		private Field field;
		private String msg;
		
		public Msg(Field f, String m) {
			field = f;
			msg = m;
		}
		/**
		 * 返回方法的名字,并且加入前缀后缀
		 */
		public String getFieldName() {
			return prefix + field.getName() + suffix;
		}
		public String getMessage() {
			return msg;
		}
		public Field getField() {
			return field;
		}
	}

	/**
	 * 把另一个VerifyMessage中的所有消息加入当前的VerifyMessage中
	 */
	public void add(VerifyMessage vm) {
		this.msgs.addAll(vm.msgs);
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setPrefix(String prefix) {
		Tools.check(prefix, "前缀不能为null");
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		Tools.check(suffix, "后缀不能为null");
		this.suffix = suffix;
	}
	
}
