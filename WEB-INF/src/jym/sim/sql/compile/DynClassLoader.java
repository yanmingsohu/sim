// CatfoOD 2010-9-10 下午01:43:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jym.sim.util.Tools;


/**
 *  <b>若想重复加载同一个类，必须创建多个该类的实例</b><br>
 *  <br>
 *  由不同类加载器实例重复强制加载(含有定义类型defineClass动作)<br>
 *  同一类型不会引起java.lang.LinkageError错误，<br>
 *  但是加载结果对应的Class类型实例是不同的，即实际上是不同的类型(虽然包名+类名相同).<br> 
 *  如果强制转化使用，会引起ClassCastException.<br>
 *  <br>
 *  <b>不同</b>类加载器加载<b>同名</b>类型，实际得到的结果其实是不同类型，<br> 
 *  在JVM中一个类用其全名和一个加载类ClassLoader的实例作为唯一标识，<br>
 *  <b>不同类加载器加载的类将被置于不同的命名空间.</b><br>
 *  <br>
 */
public class DynClassLoader extends ClassLoader {
	
	private static final String CLASS = ".class";
	private static final Map<String, Cache> cache = new HashMap<String, Cache>();

	
	public Class<?> reLoadClass(String classname) throws ClassNotFoundException {
		
		URL url = openUrl(classname);
		
		File file = new File(url.getFile());
		long lastModify = file.lastModified();
		
		Cache cc = getCache(classname, lastModify);
		
		if (cc==null) {
			byte[] bin = readFromFile(url, file.length());
			cc = createCache(classname, bin, lastModify);
		}
		
		return cc.clazz;
	}
	
	private Cache createCache(String classname, byte[] classbyte, long lastModify) {
		Cache cc = new Cache();
		cc.clazz = super.defineClass(classname, classbyte, 0, classbyte.length);
		cc.modify = lastModify;
		cache.put(classname, cc);
		
		return cc;
	}
	
	/**
	 * 如果缓存中class的修改时间与字节码文件的修改时间不同，则认为
	 * 该class的定义已经被修改，此时返回null
	 */
	private Cache getCache(String classname, long lastModify) {
		Cache cc = cache.get(classname);
		if (cc!=null) {
			if (cc.modify!=lastModify) {
				cache.remove(classname);
				cc = null;
			}
		}
		return cc;
	}
	
	private byte[] readFromFile(URL url, long fileLen) throws ClassNotFoundException {
		byte[] bin = new byte[(int)fileLen];
		try {
			url.openStream().read(bin);
		} catch (IOException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
		return bin;
	}
	
	private URL openUrl(String classname) throws ClassNotFoundException {
		char[] chs = classname.toCharArray();
		Tools.replaceAll(chs, '.', '/');
		
		String name = "/" + new String(chs) + CLASS;
		URL u = super.getClass().getResource(name);
		
		if (u==null) {
			throw new ClassNotFoundException(name);
		}
		
		return u;
	}

	
	private static class Cache {
		private Class<?> clazz;
		private long modify;
	}
}
