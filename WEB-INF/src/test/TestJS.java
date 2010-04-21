// CatfoOD 2009-11-10 ионГ09:47:12

package test;

import java.io.PrintWriter;

import jym.sim.jstags.InnerScript;

public class TestJS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		js();
	}
	
	public static void js() {
		String f = "/jym/javascript/test.js";
		InnerScript js = new InnerScript(f);
		js.append("ffff");
		js.append("sss");
		PrintWriter out = js.creatText();
		out.print(":aaa\n");
		
		System.out.println(js);
	}
}
