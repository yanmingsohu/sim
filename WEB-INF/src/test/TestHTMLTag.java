// CatfoOD 2009-11-1 обнГ11:11:52

package test;

import jym.sim.base.HtmlPack;
import jym.sim.tags.ExternalCSSTag;
import jym.sim.tags.ITag;
import jym.sim.tags.TableTag;

public class TestHTMLTag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HtmlPack html = new HtmlPack();
		ITag body = html.getBody();
		
		ExternalCSSTag css = new ExternalCSSTag("");
		html.getHead().append(css);
		
		TableTag table = new TableTag(2);
		table.append("a");
		table.append("b");
		table.append("c");
		table.append("d");
		body.append(table);
		
		System.out.println(html);
	}

}

