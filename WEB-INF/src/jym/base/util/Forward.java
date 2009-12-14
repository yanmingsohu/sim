// CatfoOD Nov 23, 2009 8:23:28 PM

package jym.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Forward {
	private Map<Object, Object> paths;
	
	public Forward() {
		paths = new HashMap<Object, Object>();
	}
	
	public Forward(String sourceFile) {
		InputStream ins = ResourceLoader.getInputStream(sourceFile);
		Properties pro = new Properties();
		try {
			pro.load(ins);
			paths = pro;
		} catch (IOException e) {
			Tools.pl( e.getMessage() );
			paths = new HashMap<Object, Object>();
		}
	}
	
	public void put(String name, String path) {
		paths.put(name, path);
	}
	
	public String getPath(String name) {
		return (String) paths.get(name);
	}
}
