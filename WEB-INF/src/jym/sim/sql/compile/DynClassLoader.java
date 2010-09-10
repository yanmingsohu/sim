// CatfoOD 2010-9-10 下午01:43:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jym.sim.util.Tools;


/**
 *  由不同类加载器实例重复强制加载(含有定义类型defineClass动作)<br>
 *  同一类型不会引起java.lang.LinkageError错误，<br>
 *  但是加载结果对应的Class类型实例是不同的，即实际上是不同的类型(虽然包名+类名相同).<br> 
 *  如果强制转化使用，会引起ClassCastException.<br>
 *  <br>
 *  <b>不同</b>类加载器加载<b>同名</b>类型实际得到的结果其实是不同类型，<br> 
 *  在JVM中一个类用其全名和一个加载类ClassLoader的实例作为唯一标识，<br>
 *  不同类加载器加载的类将被置于不同的命名空间.
 */
public class DynClassLoader extends ClassLoader {
	
	private static final String CLASS = ".class";
	private static final Map<String, CacheClass> 	cache = new HashMap<String, CacheClass>();

	
	public Class<?> reLoadClass(String classname) throws ClassNotFoundException {
		
		char[] chs = classname.toCharArray();
		Tools.replaceAll(chs, '.', '/');
		
		String name = "/" + new String(chs) + CLASS;
		URL u = super.getClass().getResource(name);
		
		if (u==null) {
			throw new ClassNotFoundException(name);
		}
		
		File file = new File(u.getFile());
		CacheClass cc = cache.get(classname);
		if (cc!=null) {
			if (cc.modify==file.lastModified()) {
				return cc.clazz;
			}
		}
		
		byte[] bin = new byte[(int)file.length()];
		try {
			u.openStream().read(bin);
		} catch (IOException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
		
		cc = new CacheClass();
		cc.clazz = super.defineClass(classname, bin, 0, bin.length);
		cc.modify = file.lastModified();
		cache.put(classname, cc);
		
		return cc.clazz;
	}

	
	private static class CacheClass {
		private Class<?> clazz;
		private long modify;
	}
}
