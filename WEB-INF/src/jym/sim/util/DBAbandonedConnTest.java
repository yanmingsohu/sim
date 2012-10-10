// CatfoOD 2012-10-8 下午03:13:39 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 僵尸连接检测
 */
public class DBAbandonedConnTest {
	
	/** 
	 * 连接分配经过该时间后, 认为连接泄漏并打印堆栈, 变量值可以直接修改
	 * 单位:分钟 
	 */
	public static final int timeout = 10; 
	
	
	private static Map<Object, ConnInf> map = new HashMap<Object, ConnInf>();
	private static Thread test;
	
	static {
		test = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(20 * 1000);
						test();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		test.setDaemon(true);
		test.start();
	}

	
	public static void add(Object obj) {
		map.put(obj, new ConnInf(obj));
	}
	
	public static void close(Object obj) {
		map.remove(obj);
	}
	
	private static void test() {
		Iterator<Object> itr = map.keySet().iterator();
		long currentTime = System.currentTimeMillis();
		long useTime = timeout * 60 * 1000;
		
		while (itr.hasNext()) {
			Object key = itr.next();
			ConnInf ci = map.get(key);
			if (ci == null) continue;
			
			if (currentTime - ci.beginTime > useTime) {
				ci.ex.printStackTrace();
				map.remove(key);
				break;
			}
		}
	}
	
	
	public static class ConnInf {
		long beginTime;
		Exception ex;
		
		ConnInf(Object o) {	
			ex = new Exception(new Date() + "数据库连接超过使用时间未释放(只显示堆栈)," + o);
			beginTime = System.currentTimeMillis();
		}
	}
}
