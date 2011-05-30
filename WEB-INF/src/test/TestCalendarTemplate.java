// CatfoOD 2010-8-27 下午03:03:30 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.Calendar;

import jym.sim.tags.ITag;
import jym.sim.tags.template.CalendarTemplate;
import jym.sim.tags.template.ICalendarData;

public class TestCalendarTemplate {

	public static void main(String[] args) {
		CalendarTemplate ct = new CalendarTemplate(new ICalendarData () {

			public void event(Calendar data, ITag disp) {
			}

			public Calendar getCalendar() {
				return Calendar.getInstance();
			}
			
		});
		
		TestHtmlTagBase.openTag(ct);
	}

}
