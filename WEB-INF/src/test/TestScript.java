/* CatfoOD 2011-12-16 下午01:24:58 yanming-sohu@sohu.com Q.412475540 */

package test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jym.sim.util.Tools;


public class TestScript {

	public static void main(String[] ss) throws ScriptException {
		ScriptEngineManager m = new ScriptEngineManager();
		ScriptEngine se = m.getEngineByName("javascript");
		
		SimpleBindings bind = new SimpleBindings();
		bind.put("price", 1);
		bind.put("total", 2);
		bind.put("amount", 3);
		
		String script = "parseInt( price ) * amount + a";
		Object o = se.eval(script, bind);
		
		Tools.pl(o, o.getClass());
	}
}
