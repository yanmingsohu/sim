// CatfoOD 2012-3-1 上午09:57:51 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;
import java.util.Map;

import jym.sim.parser.IItem;

/**
 * 创建表达式对象
 */
public class Expression {

	private String exp;
	/** 0:first, 1:number, 2:string */
	private int mode 	= 0;
	private Opt last	= null;
	private Opt new_opt	= null;
	private Opt root	= null;
	private Map<String, IItem> vmap;
	
	
	public Expression(String exp, Map<String, IItem> vmap) throws ExprException {
		this.exp = exp;
		this.vmap = vmap;
		
		StringBuilder buff = new StringBuilder();
		int i		= -1;
		char ch		= 0;
		
		last = root = new MathematicsOps.ADD();
		last.left(new ConstVal());
		last.right(new ConstVal());
		
		while (++i < exp.length()) {
			ch  = exp.charAt(i);
			
			if (ch == ' ' || ch == '\t') 
				continue;
			
			first_check(ch);
			
			if (ch == '+') {
				new_opt = new MathematicsOps.ADD();
			}
			else if (ch == '-') {
				new_opt = new MathematicsOps.SUB();
			}
			else if (ch == '*') {
				new_opt = new MathematicsOps.MUL();
			}
			else if (ch == '/') {
				new_opt = new MathematicsOps.DIV();
			}
			else if (ch == '=') {
				if (exp.charAt(i+1) == '=') {
					new_opt = new ComparisonOps.EQ(); i++;
				}
			}
			else if (ch == '!') {
				if (exp.charAt(i+1) == '=') {
					new_opt = new ComparisonOps.UEQ(); i++;
				}
			}
			else if (ch == '>') {
				if (exp.charAt(i+1) == '=') {
					new_opt = new ComparisonOps.GEQ(); i++;
				} else {
					new_opt = new ComparisonOps.GE();
				}
			}
			else if (ch == '<') {
				if (exp.charAt(i+1) == '=') {
					new_opt = new ComparisonOps.LEQ(); i++;
				} else {
					new_opt = new ComparisonOps.LE();
				}
			}
			else if (ch == '|') {
				if (exp.charAt(i+1) == '|') {
					new_opt = new LogicalOps.OR(); i++;
				}
			}
			else if (ch == '&') {
				if (exp.charAt(i+1) == '&') {
					new_opt = new LogicalOps.AND(); i++;
				}
			}
			
			if (new_opt != null) {
				createOp(buff);
				continue;
			}
			
			check_ch(ch);
			buff.append(ch);
		}
		
		if (buff.length() > 0) {
			IVal nvel = createVal(buff);
			last.right(nvel);
		}
	}
	
	private IVal createVal(StringBuilder buff) throws ExprException {
		IVal nval = null;
		
		if (mode == 1) {
			nval = new ConstVal(buff.toString());
		} else if (mode == 2) {
			nval = new Variable(buff.toString(), vmap);
		} else { 
			throw new ExprException("操作符缺少左值: " + exp);
		}
		buff.setLength(0);
		
		return nval; 
	}
	
	private void createOp(StringBuilder buff) throws ExprException {
		IVal nvel = createVal(buff);
		
		/* 高优先级的运算符作为右值,低优先级作为根元素追加,
		 * 优先级相同则插入元素 */
		if (new_opt.level() > last.level()) {
			last.right(new_opt);
			new_opt.left(nvel);
		} 
		else if (new_opt.level() < last.level()) {
			last.right(nvel);
			new_opt.left(root);
			root = new_opt;
		}
		else {
			last.right(nvel);
			new_opt.left(last);
			if (last == root) {
				root = new_opt;
			} else {
				root.right(new_opt);
			}
		}
		
		last	= new_opt;
		new_opt	= null;
		mode	= 0;
	}
	
	private void check_ch(char ch) throws ExprException {
		if (ch != '.') { /* .是有效符号 */;
			if (mode == 1) {
				if (!Character.isDigit(ch))
					throw new ExprException("无效的数字常量: " + exp);
			}
			else if (mode == 2) {
				if (ch == '(' || ch == ')') ;
				else if (!Character.isJavaIdentifierPart(ch)) 
					throw new ExprException("无效的变量名: " + exp);
			}
		}
	}
	
	/**
	 * 起始必须是一个常量或变量
	 */
	private void first_check(char ch) throws ExprException {
		if (mode == 0) {
			if (Character.isDigit(ch)) 
				mode = 1;
			else if (Character.isJavaIdentifierStart(ch)) 
				mode = 2;
			else
				throw new ExprException("无效的字符: '"+ ch +"' 在表达式 " + exp);
		}
	}

	public BigDecimal compute() {
		return root.get();
	}
}
