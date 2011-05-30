package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import jym.sim.jstags.TableMouseOverColor;
import jym.sim.jstags.TableRowClick;
import jym.sim.tags.HtmlTagBase;
import jym.sim.tags.IPrinter;
import jym.sim.tags.TableTag;

public class TestHtmlTagBase {

	public static void main(String s[]) throws Exception {
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
		click.appendCall("/test/tableevent.js", "editTableData");
		click.setTarget(table);
		click.setTarget(table1);
		
		openTag(tag);
	}
	
	public static void pl(Object o) {
		System.out.println(o);
	}
	
	public static void openTag(IPrinter tag) {
		try {
			File file = new File("c:/_temp_index.html");
			
			FileOutputStream fout = new FileOutputStream(file);
			PrintWriter pout = new PrintWriter(fout);
			tag.printout(pout);
			pout.flush();
			fout.close();
			
			openUrl(file.getPath());
	
			file.delete();
			pl("over.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openUrl(String url) {
		try {
			Process p = Runtime.getRuntime().exec("cmd /c \"" + url + "\"");
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
