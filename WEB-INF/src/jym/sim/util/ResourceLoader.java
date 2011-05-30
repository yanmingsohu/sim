// CatfoOD Nov 23, 2009 8:31:01 PM

package jym.sim.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.CharBuffer;

/**
 * 取得资源的URL, 一直遍历ClassLoader的ClassLoader
 * 直到达到java核心ClassLoader
 */
public class ResourceLoader {

	private static int BUFF_SIZE = 2048;
	
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
	
	/**
	 * 从URL中加载文本，并输出到out中
	 * @param url
	 */
	public static void urlWriteOut(URL url, PrintWriter out) {
		try {
			InputStreamReader in = new InputStreamReader( url.openStream() );
			CharBuffer buff = CharBuffer.allocate(BUFF_SIZE);
			int len = in.read(buff);
			
			while (len>0) {
				out.write(buff.array(), 0, len);
				buff.clear();
				len = in.read(buff);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取输入流中的数据到输出流
	 */
	public static void writeOut(InputStream in, OutputStream out) throws IOException {
		byte[] buff = new byte[256];
		int len = in.read(buff);
		
		while (len>0) {
			out.write(buff, 0, len);
			len = in.read(buff);
		}
	}
}
