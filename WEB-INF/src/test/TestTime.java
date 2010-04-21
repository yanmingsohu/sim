// CatfoOD 2010-4-21 下午01:19:53 yanming-sohu@sohu.com/@qq.com

package test;

import jym.sim.util.Tools;

public class TestTime {
	private long start;
	
	public void start() {
		start = System.currentTimeMillis();
	}
	
	public long end() {
		return System.currentTimeMillis() - start;
	}
	
	public void showtime() {
		double use = end() / 1000f;
		Tools.pl("使用了:" + use + " 秒");
	}
}
