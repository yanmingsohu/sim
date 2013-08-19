// CatfoOD 2013-8-9 下午01:38:45 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;

import java.io.File;
import java.net.URL;

public class DllLoader {
	
	/**
	 * 读取 DllLoader 目录中的动态库
	 * @param dll_name
	 */
	public static void load(String dll_name) {
		load(DllLoader.class, dll_name);
	}
	
	/**
	 * 读取 clazz 目录中与类同名的动态库
	 * @param clazz
	 */
	public static void load(Class<?> clazz) {
		load(clazz, clazz.getSimpleName());
	}
	
	/**
	 * 读取 与 clazz 同目录的动态库 dll_name
	 * @param clazz
	 * @param dll_name -- 不要带有与操作系统有关的后缀名如 .dll
	 */
	public static void load(Class<?> clazz, String dll_name) {
		String filename = System.mapLibraryName(dll_name);
		URL url = clazz.getResource(filename);
		
		if (url == null) {
			url = clazz.getResource(".");
			File file = new File(url.getFile());
			System.load(file.getPath() + File.separatorChar + filename);
		} else {
			File file = new File(url.getFile());
			System.load(file.getPath());
		}
	}

}
