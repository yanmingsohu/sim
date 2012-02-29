// CatfoOD 2012-2-29 下午04:28:44 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class CommandFactory {
	
	public final static CommandFactory instance;
	private final Map<String, Class<?>> cmds;
	
	static { instance = new CommandFactory(); }
	
	
	public CommandFactory() {
		cmds = new HashMap<String, Class<?>>();
		
		cmds.put("if",		C_if.class		);
		cmds.put("rem", 	C_rem.class		);
	}

	/**
	 * 负责解析一行命令并创建该命令<br>
	 * 命令占一行: <br>
	 * <code>#命令 空格* (用','分隔的参数列表) 换行</code><br> 
	 * 
	 * @param commandLine
	 * @return
	 * @throws IOException 
	 */
	public ICommand create(String commandLine) throws IOException {
		
		StringBuilder buff = new StringBuilder();
		boolean hp	= false; // has parameter
		boolean sp	= false;
		int i		= -1;
		char ch		= 0;
		
		if (commandLine.charAt(0) == ' ')
			throw new IOException("[" + commandLine + "] '#'与命令中间不能有空格");
		
		/* 解析命令名字 */
		while (++i < commandLine.length()) {
			ch = commandLine.charAt(i);
			
			if (ch == ' ') { sp = true; continue; }
			if (ch == '(') { hp = true; break; }
			
			if ( (!Character.isLetter(ch)) || sp ) {
				throw new IOException("[" + commandLine + "] 中有无效字符 '" + ch + "'");
			}
			if (!sp) {
				buff.append(ch);
			}
		}
		
		/* 创建命令 */
		String cmdName = buff.toString();
		Class<?> cl = cmds.get(cmdName);
		if (cl == null)
			throw new IOException("未知的命令 [" + cmdName + "]");
		
		ICommand cmd = null;
		try {
			cmd = (ICommand) cl.newInstance();
		} catch (Exception e) {
			throw new IOException("执行命令出错 [" + commandLine + "], " + e);
		}
		
		/* 解析命令参数 */

		if ( hp ) {
			buff.setLength(0);
			List<String> param = new ArrayList<String>();
			boolean hp_end = false;
			
			while (++i < commandLine.length()) {
				ch = commandLine.charAt(i);
				
				if (ch == ')') hp_end = true;
				if (ch == ',' || hp_end) {
					param.add(buff.toString());
					buff.setLength(0);
					if (hp_end) break;
					continue;
				}
				buff.append(ch);
			}
			
			if (!hp_end) {
				throw new IOException("缺少右括号 [" + commandLine + "]");
			}
			cmd.setParameter(param);
		}
		
		return cmd;
	}
	
}
