// CatfoOD 2012-2-28 上午09:02:38 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.reader;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ItemBase;
import jym.sim.parser.ObjectAttribute;
import jym.sim.parser.Type;


public class LinkFactory extends IItemFactory {

	protected IItem _create(Type type) {
		if (type == Type.ENT) {
			return new TEnter();
		}
		if (type == Type.STR) {
			return new TString();
		}
		if (type == Type.VAR) {
			return new TVariable();
		}
		return null;
	}

	
	private class TEnter extends ItemBase {
		public String filter() {
			return "\n";
		}
		public String getText() {
			return "\n";
		}
		public Type getType() {
			return Type.ENT;
		}
		public void init(Object... datas) {
		}
		public IItem newInstance() {
			return new TEnter();
		}
	}
	
	
	private class TString extends ItemBase {
		private String str;

		public void init(Object... datas) {
			str = (String)datas[0];
		}
		public String filter() {
			return str;
		}
		public String getText() {
			return str;
		}
		public Type getType() {
			return Type.STR;
		}
		public IItem newInstance() {
			return new TString();
		}
	}

	
	private class TVariable extends ItemBase {
		private String[] var;
		private REF ref = new REF();

		/**
		 * 此三个参数统一时刻只有一个有效
		 * @see jym.sim.parser.IItem#init 变量的init()方法初始化约定
		 */
		public void init(Object... datas) {
			if (datas.length == 1) {
				var = ((String)datas[0]).split("\\.");
			} else
			if (datas.length == 2) {
				ref.value = datas[1];
			} else
			if (datas.length == 3) {
				if (datas[2] instanceof TVariable) {
					ref = ((TVariable)datas[2]).ref;
				}
			}
		}
		public String filter() {
			return String.valueOf( ObjectAttribute.get(ref.value, var, 1) );
		}
		public String getText() {
			return var[0];
		}
		public Type getType() {
			return Type.VAR;
		}
		public IItem newInstance() {
			return new TVariable();
		}
	}

	
	private class REF {
		Object value;
	}
}
