// CatfoOD 2009-11-16 上午12:53:28

package test;

import java.net.URL;

import jym.sim.util.ResourceLoader;

public class TestClassLoader {
	
	public static void main(String s[]) {
		TestClassLoader t = new TestClassLoader();
		
		String str = "/test/datasource.conf";
		
		URL url = t.getUrlRes(str);
		pl( url );
		
		URL url2 = t.getUrlFromRL(str);
		pl( url2 );
		
		// 两个方法是用了相同的原理
	}
	
	public URL getUrlRes(String urlstr) {
		URL url = null;
		Class<?> c = this.getClass();
		
		while (url==null) {
			url = c.getResource(urlstr);
			ClassLoader loader = c.getClassLoader();
			
			if (loader!=null) {
				c = loader.getClass();
			} else {
				break;
			}
		}
		return url;
	}
	
	public URL getUrlFromRL(String urlStr) {
		return ResourceLoader.getUrl(urlStr);
	}
	
	public static void pl(Object o) {
		System.out.println(o);
	}
	
}
