// CatfoOD 2009-12-20 下午10:41:46

package jym.sim.tags.template;

import java.util.Calendar;

import jym.sim.tags.ITag;

public interface ICalendarData {
	/**
	 * 返回要处理的日期(年/月)
	 */
	Calendar getCalendar();
	
	/**
	 * 取得data的日期的事件，用来显示在格式中
	 * 
	 * @param data - 日历中的日期
	 * @param disp - 如果data的日期中含有事件，<br>
	 * 				则用disp.append()把要显示的数据添加到日历中
	 */
	void event(Calendar data, ITag disp);
}
