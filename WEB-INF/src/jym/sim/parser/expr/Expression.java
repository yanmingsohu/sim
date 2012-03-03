// CatfoOD 2012-3-1 上午09:57:51 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;
import java.util.Map;

import jym.sim.parser.IItem;

/**
 * 创建表达式对象
 */
public class Expression {

	private Map<String, IItem> vmap;
	private String exp;
	private Opt root;
	private int brackets;
	
	
	public Expression(String exp, Map<String, IItem> vmap) throws ExprException {
		this.exp = exp;
		this.vmap = vmap;
		brackets = 0;
		
		Data d = beginParse(0);
		root = d.root;
		
		if (brackets != 0) {
			throw new ExprException("括号不匹配: " + exp);
		}
	}
	
	private Data beginParse(final int beginIndex) throws ExprException {
		Data d	= new Data();
		int i	= beginIndex - 1;
		char ch	= 0;
		
		d.last = d.root = new MathematicsOps.ADD();
		d.last.left(new ConstVal());
		
		while (++i < exp.length()) {
			ch  = exp.charAt(i);
			
			if (ch == ' ' || ch == '\t') 
				continue;
			
			if (ch == '(') {
				brackets++;
				Data ret = beginParse(i + 1);
				i = ret.lastIdx;
				d.mode3 = ret.root;
				d.mode = 3;
				continue;
			} 
			if (ch == ')') {
				brackets--;
				break;
			}
			
			first_check(d, ch);
			
			switch (ch) {
				case '+':
					d.new_opt = new MathematicsOps.ADD();
					break;
				case '-':
					d.new_opt = new MathematicsOps.SUB();
					break;
				case '*':
					d.new_opt = new MathematicsOps.MUL();
					break;
				case '/':
					d.new_opt = new MathematicsOps.DIV();
					break;
				case '=':
					if (exp.charAt(i+1) == '=') { i++;
						d.new_opt = new ComparisonOps.EQ(); 
					}
					break;
				case '!':
					if (exp.charAt(i+1) == '=') { i++;
						d.new_opt = new ComparisonOps.UEQ();
					}
					break;
				case '>':
					if (exp.charAt(i+1) == '=') { i++;
						d.new_opt = new ComparisonOps.GEQ();
					} else {
						d.new_opt = new ComparisonOps.GE();
					}
					break;
				case '<':
					if (exp.charAt(i+1) == '=') { i++;
						d.new_opt = new ComparisonOps.LEQ();
					} else {
						d.new_opt = new ComparisonOps.LE();
					}
					break;
				case '|':
					if (exp.charAt(i+1) == '|') { i++;
						d.new_opt = new LogicalOps.OR();
					}
					break;
				case '&':
					if (exp.charAt(i+1) == '&') { i++;
						d.new_opt = new LogicalOps.AND();
					}
					break;
				
				default:
					addToBuffer(d, ch);
					continue;
			}
			
			if (d.new_opt != null) {
				createOp(d);
			}
		}
		
		if (d.buff.length() > 0) {
			IVal nvel = createVal(d);
			d.last.right(nvel);
		}
		else if (d.last.right() == null) {
			if (d.mode == 3) {
				d.last.right(d.mode3);
			} else {
				throw new ExprException("表达式错误: " + exp);
			}
		}
		
		d.lastIdx = i;
		
		return d;
	}
	
	private IVal createVal(Data d) throws ExprException {
		IVal nval = null;
		
		switch (d.mode) {
			case 1:
				nval = new ConstVal(d.buff.toString());
				break;
			case 2:
				nval = new Variable(d.buff.toString(), vmap);
				break;
			case 3:
				nval = d.mode3;
				break;
			default:
				throw new ExprException("操作符缺少左值: " + exp);
		}
		
		d.mode = 0;
		d.buff.setLength(0);
		return nval; 
	}
	
	private void createOp(Data d) throws ExprException {
		IVal nvel = createVal(d);
		
		/* 高优先级的运算符作为右值,低优先级作为根元素追加,
		 * 优先级相同则插入元素 */
		if (d.new_opt.level() > d.last.level()) {
			d.last.right(d.new_opt);
			d.new_opt.left(nvel);
		} 
		else if (d.new_opt.level() < d.last.level()) {
			d.last.right(nvel);
			d.new_opt.left(d.root);
			d.root = d.new_opt;
		}
		else {
			d.last.right(nvel);
			d.new_opt.left(d.last);
			
			if (d.last == d.root) {
				d.root = d.new_opt;
			} else {
				d.root.right(d.new_opt);
			}
		}
		
		d.last = d.new_opt;
		d.new_opt= null;
	}
	
	private void addToBuffer(Data d, char ch) throws ExprException {
		if (ch != '.') { /* .是有效符号 */;
			if (d.mode == 1) {
				if ( !Character.isDigit(ch) )
				throw new ExprException("无效的数字常量: " + exp);
			}
			else if (d.mode == 2) {
				if ( !Character.isJavaIdentifierPart(ch) ) 
				throw new ExprException("无效的变量名: " + exp);
			}
		}
		d.buff.append(ch);
	}
	
	/**
	 * 起始必须是一个常量或变量
	 */
	private void first_check(Data d, char ch) throws ExprException {
		if (d.mode == 0) {
			if (Character.isDigit(ch)) 
				d.mode = 1;
			else if (Character.isJavaIdentifierStart(ch)) 
				d.mode = 2;
			else
				throw new ExprException("无效的字符: '"+ ch +"' 在表达式 " + exp);
		}
	}

	public BigDecimal compute() {
		return root.get();
	}
	
	
	/** 在函数间传递参数 */
	private class Data {
		StringBuilder buff	= new StringBuilder(); 
		/** 0:first, 1:number, 2:string, 3:来自另一个表达式 */
		int mode 	= 0;
		Opt last	= null;
		Opt new_opt	= null;
		Opt root	= null;
		/** mode == 3 的时候, 该参数有效 */
		IVal mode3	= null;
		/** lastIdx + 1 指向下一次要读取的字符 */
		int lastIdx	= -1;
	}
}
