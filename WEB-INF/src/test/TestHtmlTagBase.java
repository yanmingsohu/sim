package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import jym.base.jstags.TableMouseOverColor;
import jym.base.jstags.TableRowClick;
import jym.base.tags.HtmlTagBase;
import jym.base.tags.TableTag;

public class TestHtmlTagBase {

	public static void main(String s[]) throws IOException {
		TableTag table = new TableTag(4);
		for (int i=0; i<12; ++i) {
			table.append(i+" s");
		}
		table.addAttribute("width", "100%");
		
		TableTag table1 = new TableTag(5);
		for (int i=0; i<200; ++i) {
			table1.append(i+" t");
		}
		table1.addAttribute("width", "100%");
		
		HtmlTagBase tag = new HtmlTagBase("p");
		tag.append("精确查询结果");
		tag.append(table);
		tag.append(table1);
		
		TableMouseOverColor tcolor = new TableMouseOverColor();
		tcolor.setTarget(table);
		tcolor.setTarget(table1);
		
		TableRowClick click = new TableRowClick();
		URL jsu = click.getClass().getResource("/test/tableevent.js");
		click.appendCall(jsu, "editTableData");
		click.setTarget(table);
		click.setTarget(table1);
		
		
		FileOutputStream fout = new FileOutputStream("f:/index.html");
		fout.write(tag.toString().getBytes());
		fout.close();
	}
	
	public static void pl(Object o) {
		System.out.println(o);
	}
}
