// CatfoOD 2009-11-1 обнГ11:11:52

package test;

import jym.base.HtmlPack;
import jym.base.tags.ExternalCSSTag;
import jym.base.tags.ITag;
import jym.base.tags.TableTag;

public class test {

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

