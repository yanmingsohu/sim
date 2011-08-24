// CatfoOD 2011-8-22 上午10:51:58 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jym.sim.util.Tools;


/**
 * 把普通语句转换为绑定变量,并用PreparedStatement调用<br>
 * 不能检查语法错误, 当该方法被JdbcTemplate.quary调用后会设置绑定变量的值
 * 并且会执行execute方法,通常需要包装之后调用quary<br>
 * <br>
 * 当前支持的变量转换类型,简单的>=,<=,<>,>,<,like,values中的变量集
 */
public class TransformPrep implements IPrepSql {
	
	private final static char[][] W_KEY =	{
		 {'>', '='							} 
		,{'<', '='							}
		,{'<', '>'							}
		,{'='								}
		,{'>'								}
		,{'<'								}
		,{'l', 'i', 'k', 'e'				}
		,{'v', 'a', 'l', 'u', 'e', 's'		} // 7
//		,{'b', 'e', 't', 'w', 'e', 'e', 'n'	} // 涉及第二个参数?
	};
	
	private List<Param> params;
	private StringBuilder sql;
	private boolean succDo;
	
	
	public TransformPrep() {
		params = new ArrayList<Param>();
		sql = new StringBuilder();
		succDo = false;
	}
	
	/**
	 * 清除缓冲区,以便执行下一个语句
	 */
	public void clear() {
		params.clear();
		sql.setLength(0);
		succDo = false;
	}

	public void exe(PreparedStatement pstm) throws Throwable {
		if (!succDo) throw new IllegalStateException("sql语句没有正确转换");
		
		for (int i=0; i<params.size(); ++i) {
			Param p = params.get(i);
			p.setPram(pstm, i+1);
		}
		
		pstm.execute();
	}

	public String getSql() {
		return sql.toString();
	}
	
	/**
	 * 转换一个sql语句到内部缓冲区,转换成功返回true
	 * @param _sql - 原始sql语句
	 */
	public boolean setSql(String _sql) {

		char[] ch = _sql.toCharArray();
		int chi = 0;
		int key_len = 0;
		boolean find = false;
		boolean inSubstr = false;
		boolean isValues = false;
		
		while (chi<ch.length) {
			if (find) {
				/* 等待指针指向符号的下一个位置 */
				if (--key_len==0) {
					int _chi;
					
					if (isValues) {
						_chi = parseValues(ch, chi);
					} else {
						_chi = parseValue(ch, chi);
					}
	
					/* 结束 */
					find = false;
					if (_chi>0) {
						chi = _chi;
						continue;
					}
				}
			} else {
				/* 查找某个sql比较序列字符,忽略在字符串中的符号 */
				if (!inSubstr) {
					for (int s = 0; s<W_KEY.length; ++s) {
						int wki = 0;
						key_len = W_KEY[s].length;
						
						while ( Character.toLowerCase(ch[chi+wki]) == W_KEY[s][wki] ) {
							if (++wki>=key_len) {
								find = true;
								isValues = ( s==7 ); /* 第7个是values关键字 */
								break;
							}
						}
						
						if ( find )
							break;
					}
				}
			}
			
			if (ch[chi]=='\'') inSubstr = !inSubstr;
			sql.append( ch[chi++] );
		}
		
		return succDo = true;
	}
	
	private int parseValues(char[] ch, int _chi) {
		/* 前导空字符 */
		_chi = space(ch, _chi);
		char c = ch[_chi];
		
		if (c=='(') {
			sql.append(c);
		} else {
			return -1;
		}
		
		do {
			_chi++;
			_chi = parseValue(ch, _chi);
			_chi = space(ch, _chi);
			c = ch[_chi];
			sql.append(c);
		} while(c==',');
		
		return _chi+1;
	}

	/** 解析失败返回-1否则返回下一个指针的位置 */
	private int parseValue(char[] ch, int _chi) {
		/** 没有处理多个字串连接的情况 */
		Param leftvalue;
		boolean right;
		char c;
		
		/* 前导空字符 */
		_chi = space(ch, _chi);
		
		/* 如果以'开始,则认为是字符串类型 */
		if (ch[_chi  ]=='n' && 
			ch[_chi+1]=='u' && 
			ch[_chi+2]=='l' && 
			ch[_chi+3]=='l' ) {
			
			leftvalue = new NullParam();
			_chi+=4;
			right = true;
		}
		else if (ch[_chi]=='\'') {
			leftvalue = new StrParam();
			right = false;

			while (++_chi<ch.length) {
				c = ch[_chi];
				if (c=='\'') {
					_chi++;
					right = true;
					break;
				}
				leftvalue.append(c);
			}
		} 
		else {
			leftvalue = new NumParam();
			right = true;
			
			while (_chi<ch.length) {
				c = ch[_chi];
				if (c==' ' || c=='\n' || c=='\t' || c=='\r' || 
					c==',' || c==')') {
					break;
				}
				if (c!='.' && c!='-' && (c<'0' || c>'9')) {
					right = false;
					break;
				}
				leftvalue.append(c);
				_chi++;
			}
		}

		/* 结束 */
		if (right) {
			params.add(leftvalue);
			sql.append(" ? ");
		} else {
			_chi = -1;
		}
		
		return _chi;
	}
	
	/** 返回下一个不是空字符的索引,用来跳过空字符 */
	private int space(char[] ch, int _chi) {
		char c = ch[_chi];
		while (c==' ' || c=='\n' || c=='\t' || c=='\r') { 
			c = ch[++_chi];
		}
		return _chi;
	}
	
	/**
	 * 在控制台显示分析后的参数情况
	 */
	public void debug() {
		Tools.plsql(getSql());
		for (int i=0; i<params.size(); ++i) {
			Param p = params.get(i);
			Tools.pl("index:", i+1, ",\ttype:[", p.getClass().getSimpleName(), 
					"], value::", p.value);
		}
	}
	
	
	private abstract class Param {
		StringBuilder value;
		
		public Param() {
			value = new StringBuilder();
		}
		public void append(char c) {
			value.append(c);
		}
		/** 调用该方法把参数值压入PreparedStatement */
		abstract void setPram(PreparedStatement ps, int index) throws SQLException;
	}
	
	private class StrParam extends Param {
		void setPram(PreparedStatement ps, int index) throws SQLException {
			ps.setString(index, value.toString());
		}
	}
	
	private class NumParam extends Param {
		void setPram(PreparedStatement ps, int index) throws SQLException {
			BigDecimal num = new BigDecimal(value.toString());
			ps.setBigDecimal(index, num);
		}
	}
	
	private class NullParam extends Param {
		void setPram(PreparedStatement ps, int index) throws SQLException {
			ps.setNull(index, java.sql.Types.NULL);
		}
	}
}
