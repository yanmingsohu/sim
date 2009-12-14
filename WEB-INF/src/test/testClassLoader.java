// CatfoOD 2009-11-16 ионГ12:53:28

package test;

import java.net.URL;

public class testClassLoader {
	
	public static void main(String s[]) {
		testClassLoader t = new testClassLoader();
		
		URL url = t.getUrlRes("/test/test.file.txt");
		pl( url );
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
	
	public static void pl(Object o) {
		System.out.println(o);
	}
	
}
