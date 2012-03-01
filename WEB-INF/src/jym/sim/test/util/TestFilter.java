// CatfoOD 2010-8-10 下午01:50:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.util;

import java.util.Date;

import jym.sim.filter.FilterPocket;
import jym.sim.filter.SimFilterException;
import jym.sim.filter.sql.HtmlDisplayFilter;
import jym.sim.filter.sql.SafeSqlStringFilter;
import jym.sim.filter.sql.SqlDateFilter;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;


public class TestFilter {

	public static void main(String[] args) throws SimFilterException {
		String a = "_fdsafsaf\"'fdsaffn_";
		
		Date d = new Date();
		
		SafeSqlStringFilter sf = new SafeSqlStringFilter();
		SqlDateFilter df = new SqlDateFilter();
		HtmlDisplayFilter hf = new HtmlDisplayFilter();
		
		FilterPocket fb = new FilterPocket();
		fb.reg(String.class, sf);
		fb.reg(Date.class, df);
		fb.add(String.class, hf);
		
		int count = 100;
		
		UsedTime.start(count+"次测试");
		for (int i=0; i<count; ++i) {
			fb.filter(a);
			fb.filter(d);
		}
		Tools.pl(fb.filter(a));
		UsedTime.endAndPrint();
	}

}
