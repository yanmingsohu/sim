// CatfoOD Nov 23, 2009 8:31:01 PM

package jym.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {
	
	/**
	 * 取得资源的URL
	 */
	public static URL getUrl(String fromfile) {
		URL url = null;
		Class<?> c = ResourceLoader.class;
		
		while (url==null) {
			url = c.getResource(fromfile);
			ClassLoader loader = c.getClassLoader();
			
			if (loader!=null) {
				c = loader.getClass();
			} else {
				break;
			}
		}
		
		return url;
	}
	
	/**
	 * 取得资源的输入流
	 */
	public static InputStream getInputStream(String fromfile) {
		InputStream ins = null;
		
		URL url = getUrl(fromfile);
		if (url!=null) {
			try {
				ins = url.openStream();
			} catch (IOException e) {
			}
		}
		
		return ins;
	}
}
