// CatfoOD 2009-12-20 下午10:35:28

package jym.sim.tags.template;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jym.sim.css.InnerCss;
import jym.sim.tags.HtmlTagBase;
import jym.sim.tags.ITag;
import jym.sim.tags.TableTag;

/**
 * 日历显示组件
 */
public class CalendarTemplate extends HtmlTagBase {
	
	private static int[] Month = 
		{-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	private static String[] wk = 
		{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	
	private static String CALE_TITAL_CLASS = "caletitlediv";
	private static String QUICK_DIV_ID = "quickdiv";
	private static String TABLE_ID = "calendartable";
	
	private ICalendarData data;

	/**
	 * 从回调接口中取得数据，生成日历
	 * 
	 * @param cdata - 回调接口
	 */
	public CalendarTemplate(ICalendarData cdata) {
		super("div");
		data = cdata;
	}
	
	@Override
	public void printout(PrintWriter out) {
		init();
		super.printout(out);
	}

	private void init() {
		InnerCss css = new InnerCss("/jym/sim/tags/template/calendar.css");
		append(css);
		
		Calendar cale = (Calendar) data.getCalendar().clone();
		cale.set(Calendar.DAY_OF_MONTH, 1);
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH)+1;
		int dow = cale.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
		int days = getDayofMonth(year, month);
		
		append(createTitleDiv(year, month));
		append(createQuickDiv(year));

		ITag table = creatTable();
		for (int s=0; s<dow; ++s) {
			table.append("");
		}
		
		for (int i=1; i<=days; ++i) {
			cale.set(Calendar.DAY_OF_MONTH, i);
			table.append(getDayBulletin(cale));
		}
		table.addAttribute("id", TABLE_ID);
		
		append(table);
	}
	
	private ITag createQuickDiv(int year) {
		ITag div = new HtmlTagBase("div");
		div.addAttribute("id", QUICK_DIV_ID);	
		
		Calendar cale = (Calendar) data.getCalendar().clone();
		int month = cale.get(Calendar.MONTH);
		
		ITag tdiv = new HtmlTagBase("div");
		tdiv.addAttribute("style", "background-color:#93B6FF;");
		for (int m=0; m<12; ++m) {
			ITag mlink = tdiv.create("a");
			mlink.addAttribute("href", "?month="+m+"&year="+year);
			mlink.append((m+1)+"月");
			tdiv.append(ITag.SP);
		}
		
		final int ylen = 8;
		ITag ydiv = new HtmlTagBase("div");
		ydiv.addAttribute("style", "background-color:#FF8291;");
		for (int y=year-ylen; y<year+ylen; ++y) {
			ITag ylink = ydiv.create("a");
			ylink.addAttribute("href", "?year="+y+"&month="+month);
			ylink.append(y+"年");
			ydiv.append(ITag.SP);
		}
		
		div.append(tdiv);
		div.append(ydiv);
		
		return div;
	}
	
	private ITag createTitleDiv(int year, int month) {
		ITag tdiv = new HtmlTagBase("div");
		tdiv.addAttribute("class", CALE_TITAL_CLASS);
		tdiv.append(year+"年"+month+"月");
		return tdiv;
	}

	private ITag getDayBulletin(Calendar c) {		
		ITag d = new HtmlTagBase("div");
		d.append( String.valueOf(c.get(Calendar.DAY_OF_MONTH)) );
		
		data.event(c, d);
		
		return d;
	}
	
	private ITag creatTable() {		
		TableTag table = new TableTag(wk.length);
		for (int i=0; i<wk.length; ++i) {
			ITag d = new HtmlTagBase("div");
			d.addAttribute("class", "day");
			d.append(wk[i]);
			table.appendHead(d);
		}
		return table;
	}
	
	private int getDayofMonth(int year, int month) {
		int days;
		if (month!=2) {
			days = Month[month];
		} else {
			days = (new GregorianCalendar().isLeapYear(year))? 29:28;
		}
		return days;
	}
}
