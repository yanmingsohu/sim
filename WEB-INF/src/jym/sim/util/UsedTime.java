// CatfoOD 2010-7-29 上午08:47:54 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;

/**
 * 测试一段过程消耗的时间, 线程安全的类, 无需实例
 */
public final class UsedTime {
	
	private static ThreadLocal<Time> times = new ThreadLocal<Time>();
	private static final String NULL_STR = "";
	
	
	/**
	 * 开始计时
	 */
	public static void start() {
		start(NULL_STR);
	}
	
	/**
	 * 开始计时,并赋予一个描述
	 * @param desc - 对此次计时的描述
	 */
	public static void start(String desc) {
		Time t = times.get();
		
		if (t == null) {
			t = new Time();
			times.set(t);
		} else {
			t.reset();
		}
		t.setName(desc);
	}
	
	/**
	 * 结束计时,并返回start与end调用之间,经过的时间<br>
	 * 调用此方法会影响结果,所以如果需要多次取得结果,应使用getUsedTime
	 */
	public static long end() {
		Time t = times.get();
		Tools.check(t, "错误: 尚未调用start()");
		t.end();
		
		return t.usedTime();
	}
	
	/**
	 * 返回上次start()与end()调用之间,经过的时间
	 */
	public static long getUsedTime() {
		Time t = times.get();
		Tools.check(t, "错误: 尚未调用start()");
		return t.usedTime();
	}
	
	/**
	 * 停止计时,并且在控制台输出使用的时间
	 */
	public static void endAndPrint() {
		Time t = times.get();
		Tools.check(t, "错误: 尚未调用start()");
		t.end();
		
		Tools.pl(t.getName() + "使用了: " + t.usedTime() + " ms");
	}
	
	
	private static class Time {
		private long startT;
		private long endT;
		private String name;
		
		private Time() {
			reset();
		}
		public void reset() {
			startT = System.currentTimeMillis();
			endT = startT;
			name = NULL_STR;
		}
		public void end() {
			endT = System.currentTimeMillis();
		}
		public long usedTime() {
			return endT - startT;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
}
