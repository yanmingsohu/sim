// CatfoOD 2012-2-28 上午08:55:16 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ItemBase;
import jym.sim.parser.Type;


public class ComplieFactory extends IItemFactory {
	
	public static final String VAR_PREFIX = "__";

	
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
			return "\"\\n\"";
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
		
		public String filter() {
			return "\"" + str + "\"";
		}
		public String getText() {
			return str;
		}
		public Type getType() {
			return Type.STR;
		}
		public void init(Object... datas) {
			str = (String)datas[0];
		}
	}

	
	private class TVariable extends ItemBase {
		private String var;

		public void init(Object... datas) {
			var = ((String)datas[0]).trim();
		}
		public String filter() {
			return VAR_PREFIX + var;
		}
		public String getText() {
			return var;
		}
		public Type getType() {
			return Type.VAR;
		}
	}

}
