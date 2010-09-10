// CatfoOD 2010-9-9 обнГ03:40:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

class Template {
	
	public Object v1;
	public Object v2;
	
	
	public String toString() {
		return "select * from " + v1 + " where " + v2;
	}
	
}
