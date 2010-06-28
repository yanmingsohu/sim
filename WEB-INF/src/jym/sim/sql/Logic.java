// CatfoOD 2010-4-16 上午11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * 数据库查询where字句中逻辑判断策略
 */
public class Logic implements IWhere {
	
	/** 等于 */
		public static final IWhere EQ = new Logic("%1$s='%2$s'");
		
	/** 不等于 */
		public static final IWhere NE = new Logic("%1$s!='%2$s'");
		
	/** 小于 */
		public static final IWhere LT = new Logic("%1$s<'%2$s'");
		
	/** 小于等于 */
		public static final IWhere LE = new Logic("%1$s<='%2$s'");
		
	/** 大于 */
		public static final IWhere GT = new Logic("%1$s>'%2$s'");
		
	/** 大于等于 */
		public static final IWhere GE = new Logic("%1$s>='%2$s'");
		
	/** like查询 */
		public static final IWhere LIKE = new Logic("%1$s like '%2$s'");
		
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
	 * * * */
		public static final IWhere DATE_RANGE(String beginFieldName, String endFieldName) {
			return new DateRange(beginFieldName, endFieldName);
		};
	
	////////////////////// ----------------------------------
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};

	public String w(String columnName, Object value, Object model) {
		return String.format(format, columnName, value);
	}
	
}
