// CatfoOD 2012-2-28 上午09:02:38 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.reader;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ItemBase;
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
	}

	
	private class TVariable extends ItemBase {
		private String var;
		private Object value;

		/**
		 * 参数1 : 变量名(可以null)
		 * 参数2 : 变量值
		 */
		public void init(Object... datas) {
			if (datas[0] != null) {
				var = ((String)datas[0]).trim();
			}
			if (datas.length == 2) {
				value = datas[1];
			}
		}
		public String filter() {
			return String.valueOf(value);
		}
		public String getText() {
			return var;
		}
		public Type getType() {
			return Type.VAR;
		}
	}

}
