// CatfoOD 2010-8-19 обнГ12:40:34 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter.sql;

import jym.sim.filter.IFilter;
import jym.sim.filter.SimFilterException;

public class TrimStrFilter implements IFilter<String> {

	public String see(String src) throws SimFilterException {
		return src.trim();
	}

}
