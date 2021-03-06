// CatfoOD 2010-7-6 上午10:53:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.sql.IWhere;

public class OperatorOR implements IWhere, ISkipValueCheck {
	
	private IWhere[] w;
	
	public OperatorOR(IWhere[] w) {
		this.w = w;
	}

	public String w(String columnName, Object value, Object model) {
		String result = null;
		
		for (int i=0; i<w.length; ++i) {
			IWhere wh = w[i];
		
	if ( wh!=null && (value!=null || (wh instanceof ISkipValueCheck)) ) {
			
			result = wh.w(columnName, value, model);
				if (result!=null) {
				break;
				}
			}
			
		}
		
		return result;
	}

}
