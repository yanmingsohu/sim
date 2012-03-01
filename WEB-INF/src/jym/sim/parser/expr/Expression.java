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
	private int mode 	= 0; // 0:first, 1:number, 2:string
	private boolean op	= false;
	private Opt last	= null;
	private Opt new_opt	= null;
	private Opt root	= null;
	
	
	public Expression(String exp, Map<String, IItem> vmap) throws ExprException {
		this.exp = exp;
		
		StringBuilder buff = new StringBuilder();
		int i		= -1;
		char ch		= 0;
		
		last = root = MathematicsOps.ADD();
		last.left(new ConstVal());
		last.right(new ConstVal());
		
		while (++i < exp.length()) {
			ch  = exp.charAt(i);
			
			if (ch == ' ' || ch == '\t') 
				continue;
			
			first_check(ch);
			
			if (ch == '+') {
				new_opt = MathematicsOps.ADD();
				op = true;
			}
			else if (ch == '-') {
				//...
			}
			else if (ch == '*') {
				new_opt = MathematicsOps.MUL();
				op = true;
			}
			
			if (op) {
				createOp(buff, vmap);
				continue;
			}
			else if (mode == 1) {
				if (!Character.isDigit(ch))
					throw new ExprException("无效的数字常量: " + exp);
			}
			else if (mode == 2) {
				if (!Character.isJavaIdentifierPart(ch)) 
					throw new ExprException("无效的变量名: " + exp);
			}	
			
			buff.append(ch);
		}
		
		if (buff.length() > 0) {
			createOp(buff, vmap);
		}
	}
	
	private void createOp(StringBuilder buff, Map<String, IItem> vmap) throws ExprException {
		IVal nvel;
		
		if (mode == 1) {
			nvel = new ConstVal(buff.toString());
		} else if (mode == 2) {
			nvel = new Variable(buff.toString(), vmap);
		} else { 
			throw new ExprException("操作符缺少左值: " + exp);
		}
		buff.setLength(0);
		
		if (new_opt != null) {
			if (new_opt.level() > last.level()) {
				new_opt.left(nvel);
				last.left(new_opt);
			} else {
				new_opt.right(last);
				last.right(nvel);
				root = new_opt;
			}
			last = new_opt;
			new_opt = null;
		} else {
			last.right(nvel);
		}
		
		op = false;
		mode = 0;
	}
	
	private void first_check(char ch) throws ExprException {
		if (mode == 0) {
			if (Character.isDigit(ch)) 
				mode = 1;
			else if (Character.isJavaIdentifierStart(ch)) 
				mode = 2;
			else
				throw new ExprException("无效的常量或变量: '"+ ch +"' " + exp);
		}
	}

	public BigDecimal compute() {
		return root.get();
	}
}
