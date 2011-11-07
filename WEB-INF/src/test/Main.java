// CatfoOD 2010-4-16 下午01:47:40 yanming-sohu@sohu.com/@qq.com

package test;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.io.InputStreamReader;

import jym.sim.util.ResourceLoader;
import jym.sim.util.Tools;
import jym.sim.util.Version;

public class Main {

	public static void main(String[] args) {
		Tools.pl("欢迎使用sim工具集合 " + Version.v);
		
		TextArea t = new TextArea();
		t.setText(getText());
		t.setEditable(false);
		
		Frame d = new Frame();
		d.setSize(500, 300);
		d.setTitle("sim for JEE tools." + Version.v);
		d.add(t);
		d.setVisible(true);
		d.addWindowListener(WL);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension sd = tk.getScreenSize();
		
		d.setLocation(
				(sd.width - d.getWidth()) / 2, 
				(sd.height - d.getHeight()) /2
				);
	}

	public static String getText() {
		return "为开发 java-web 程序设计的开源工具集合\n\n" +
				"由CatfoOD负责支持 Em: yanming-sohu@sohu.com\n\n" +
				"http://github.com/yanmingsohu\n\n" +
				"-------------------------------------------------------------------------------------------\n\n\n" +
				getReadme();
	}
	
	public static String getReadme() {
		InputStream in = ResourceLoader.getInputStream("/jym/readme.txt");
		
		if (in!=null) {
			try {
				InputStreamReader read = new InputStreamReader(in, "UTF-8");
				StringBuilder buff = new StringBuilder();
				char[] cs = new char[256];
				int len = read.read(cs);
				
				while (len>0) {
					buff.append(cs, 0, len);
					len = read.read(cs);
				}
				
				return buff.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "";
	}
	
	private static WindowListener WL = new WindowListener() {
			public void windowActivated(WindowEvent e) 		{}
			public void windowClosed(WindowEvent e) 		{ System.exit(0);	}
			public void windowClosing(WindowEvent e) 		{ windowClosed(e);	}
			public void windowDeactivated(WindowEvent e) 	{}
			public void windowDeiconified(WindowEvent e) 	{}
			public void windowIconified(WindowEvent e) 		{}
			public void windowOpened(WindowEvent e) 		{}
	};
	
}
