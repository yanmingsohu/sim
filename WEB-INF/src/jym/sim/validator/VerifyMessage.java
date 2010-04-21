// CatfoOD 2010-4-21 上午09:21:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 验证消息
 */
public class VerifyMessage {
	private List<Msg> msgs;
	
	/**
	 * 验证消息
	 */
	protected VerifyMessage() {
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
		public String getFieldName() {
			return field.getName();
		}
		public String getMessage() {
			return msg;
		}
		public Field getField() {
			return field;
		}
	}
	
}
