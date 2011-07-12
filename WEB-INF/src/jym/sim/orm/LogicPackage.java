// CatfoOD 2010-11-10 上午10:48:52 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import jym.sim.sql.IWhere;
import jym.sim.sql.Logic;


public class LogicPackage {
	
	public static final LogicPackage DEFAULT = new LogicPackage();
	
	private IWhere whereLogic;
	private IUpdateLogic updateLogic;
	private ISelectJoin joinLogic;
	
	
	protected LogicPackage(ISqlLogic[] log) {
		if (log!=null) {
			for (int i=0; i<log.length; ++i) {
				if (log[i] instanceof IWhere) {
					whereLogic = (IWhere) log[i];
				} else
				if (log[i] instanceof IUpdateLogic) {
					updateLogic = (IUpdateLogic) log[i];
				} else
				if (log[i] instanceof ISelectJoin) {
					joinLogic = (ISelectJoin) log[i];
				}
			}
		}
		
		if (whereLogic==null) {
			whereLogic = Logic.EQ;
		}
	}
	
	private LogicPackage() {}
	
	/**
	 * 默认如果没有指定where策略,则返回相等比较策略
	 */
	protected IWhere getWhereLogic() {
		return whereLogic;
	}
	
	/**
	 * 如果没有指定策略,则返回null
	 */
	protected IUpdateLogic getUpdateLogic() {
		return updateLogic;
	}
	
	/**
	 * 如果没有指定策略,则返回null
	 */
	protected ISelectJoin getJoinLogic() {
		return joinLogic;
	}
}