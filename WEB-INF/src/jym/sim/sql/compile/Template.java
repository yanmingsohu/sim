// CatfoOD 2010-9-9 下午03:40:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;


/**
 * sql编译为Java文件后的样式模板，无实际作用
 */
class Template {
	
	public long _lastModify = 0l;
	public Object v1;
	public Object v2;
	
	
	public String toString() {
		return "select * from " + v1 + " where " + v2;
	}
	
}
