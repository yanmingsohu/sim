// CatfoOD 2010-10-16 下午08:42:30

package jym.sim.test.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jym.sim.json.Json;
import jym.sim.util.Tools;

public class TestJson {
	
	public static void main(String[] args) {
		bean();
		list();
		map();
	}
	
	public static void list() {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<10; ++i) {
			list.add("["+i+"]");
		}
		
		Json json = new Json();
		json.set("list", list);
		Tools.pl(json);
	}
	
	public static void map() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i=0; i<10; ++i) {
			map.put("["+i+"]", i);
		}
		
		Json json = new Json();
		json.set("map", map);
		Tools.pl(json);
	}
	
	public static void bean() {
		TestBean b = new TestBean();
		b.setA(1);
		b.setB(1.5f);
//		b.setC("a");
		b.setD(new BigDecimal(80));
		
		Json json = new Json();
		json.setBean("bean", b);
		
		Tools.pl(json);
	}
}

