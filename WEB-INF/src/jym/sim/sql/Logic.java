// CatfoOD 2010-4-16 上午11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import jym.sim.sql.logic.DateRange;
import jym.sim.sql.logic.DefinitionLogic;
import jym.sim.sql.logic.OperatorIN;
import jym.sim.sql.logic.OperatorOR;

/**
 * 数据库查询where字句中逻辑判断策略,IWhere.w()方法返回null,则忽略这个条件
 */
public class Logic implements IWhere {
	
	/** 等于 */
		public static final IWhere EQ = new Easy("=");
		
	/** 不等于 */
		public static final IWhere NE = new Easy("!=");
		
	/** 小于 */
		public static final IWhere LT = new Easy("<");
		
	/** 小于等于 */
		public static final IWhere LE = new Easy("<=");
		
	/** 大于 */
		public static final IWhere GT = new Easy(">");
		
	/** 大于等于 */
		public static final IWhere GE = new Easy(">=");
		
	/** like查询 */
		public static final IWhere LIKE = new Easy("like");
		
	/** 包含查询; 如果结果中含有子串则为true */
		public static final IWhere INCLUDE = new Logic("%1$s like '%%%2$s%%'");
		
	/** 排除查询; 如果结果中不含有子串则为true */
		public static final IWhere EXCLUDE = new Logic("%1$s not like '%%%2$s%%'");
		
	/** 日期查询,精确到日 */
		public static final IWhere DATE = new Logic("to_char(%1$s, 'yyyy-mm-dd') = '%2$s'");
		
	/**
	 * 日期范围查询,查询的结果介于两个参数之间(包含当天的日期),忽略时间部分<br/>
	 * <code>beginFieldName <= result <= endFieldName</code><br/>
	 * 如果有一个属性域为null则此属性域使用当前日期,如果两个都为null,则忽略此列的比较<br/>
	 * <br/>
	 * <b>两个比较用的数据域,类型可以是String,但如果值为''或者格式无效则比较结果可能不正确</b><br/>
	 * 
	 * @param beginFieldName - 实体类的属性名,查询结果大于等于此属性中的值
	 * @param endFieldName - 实体类的属性名,查询结果小于等于此属性中的值
	 * 
	 * */
		public static final IWhere DATE_RANGE(String beginFieldName, String endFieldName) {
			return new DateRange(beginFieldName, endFieldName);
		};
		
	/**
	 * 列表查询,遍历arrayFieldName的值放入IN的查询条件中
	 * 
	 * @param arrayFieldName - 实体类的属性名,属性的类型是一个集合
	 * */
		public static final IWhere IN(String arrayFieldName) {
			return new OperatorIN(arrayFieldName);
		};
	
	/**
	 * 包装多个IWhere条件,实现可选择的逻辑,如果w[n]的结果为null,则使用w[n+1]的结果
	 * */
		public static final IWhere OR(IWhere ...w) {
			return new OperatorOR(w);
		};
		
	/**
	 * where子句由指定的格式字符串生成
	 * <br>格式字符串由<b>固定</b>的部分,和<b>拼装</b>的部分组成,
	 * <br>固定的部分原样输出,拼装的部分则指定使用bean中那个属性的值
	 * <br><br>比如如下字符串:
	 * <br>
	 * <code>"${field1} < ${field2}"</code>
	 * <br>如果bean中field1=10,field2=40则最终的结果是
	 * <br>
	 * <code>"10 < 40"</code>
	 * <br><br>如果field的类型不是String,则使用该对象的toString()方法,
	 * <br>如果格式字符串中指定的域的值有一个为null,则忽略整条子句的生成
	 * <br><b>${}之间不能有空格</b
	 * 
	 * @param defstr - 生成where子句的模式字符串
	 * */
		public static final IWhere DEF(String defstr) {
			return new DefinitionLogic(defstr);
		};
		
		
	////////////////////// ------------------------------------------------- ////
		
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};

	public String w(String columnName, Object value, Object model) {
		return String.format(format, columnName, value);
	}
	
	/**
	 * 简单逻辑表达式, "v1 op v2"
	 */
	private static class Easy implements IWhere {
		
		private String op;
		
		/**
		 * <code>columnName OP 'value'</code>
		 */
		private Easy(String op) {
			this.op = op;
		}
		
		public String w(String columnName, Object value, Object model) {
			StringBuilder buff = new StringBuilder();
			buff.append(columnName);
			buff.append(' ');
			buff.append(op);
			buff.append(' ');
			buff.append('\'');
			buff.append(value);
			buff.append('\'');
			return buff.toString();
		}
	}
}
