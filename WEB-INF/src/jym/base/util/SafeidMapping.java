// CatfoOD 2009-12-17 下午09:28:22

package jym.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 安全的ID映射，防止数据库ID直接传到客户端
 */
public class SafeidMapping {
	
	private Map<Integer, Integer> idmap;
	private Random ran;
	
	private static final int MAX = 0xF00000;
	private static final String NAME = SafeidMapping.class.getName();
	
	public SafeidMapping() {
		idmap = new HashMap<Integer, Integer>();
		ran = new Random();
	}
	
	/**
	 * 便捷的方法<br>
	 * 从session中取得SafeidMapping对象并得到安全id</br>
	 * 如果SafeidMapping不存在则初始化它
	 * 
	 * @param id - 原始id
	 * @param data - Action数据对象
	 * @return 安全的ID
	 */
	public static int getSafeID(int id, ISessionData data) {
		SafeidMapping smp = (SafeidMapping) data.getSessionAttribute(NAME);
		if (smp==null) {
			resetSafeID(data);
		}
		return smp.getSafe(id);
	}
	
	/**
	 * 便捷的方法<br>
	 * 清除session中SafeidMapping对象的所有id映射<br>
	 * 如果SafeidMapping不存在则创建它<br>
	 * <b>此方法一般在构建数据之前调用</b>
	 * 
	 * @param data - Action数据对象
	 * @return 返回与session绑定的SafeidMapping
	 */
	public static SafeidMapping resetSafeID(ISessionData data) {
		SafeidMapping smp = (SafeidMapping) data.getSessionAttribute(NAME);
		if (smp==null) {
			smp = new SafeidMapping();
			data.setSessionAttribute(NAME, smp);
		}
		smp.reset();
		return smp;
	}
	
	/**
	 * 便捷的方法<br>
	 * 从session中取得SafeidMapping对象并得到原始id
	 * 
	 * @param sid - 安全id
	 * @param data - Action数据对象
	 * @return 原始的ID
	 * @throws SafeException - 如果SafeidMapping不能存在或者没有sid的映射
	 */
	public static int getRealID(int sid, ISessionData data) throws SafeException {
		SafeidMapping smp = (SafeidMapping) data.getSessionAttribute(NAME);
		if (smp==null) {
			throw new SafeException();
		}
		return smp.getReal(sid);
	}
	
	
	/**
	 * 从数据库中取得安全映射<br>
	 * 同时ID值被保存以便反向映射
	 */
	public int getSafe(int id) {
		int sid = safe();
		idmap.put(sid, id);
		return sid;
	}
	
	private int safe() {
		int i = 0;
		do {
			i = ran.nextInt(MAX);
		} while (idmap.containsKey(i));
		
		return i;
	}
	
	/**
	 * 从映射中取得真实ID
	 * @throws SafeException - 如果sid并不存说明sid是不安全的，则抛出这个异常
	 */
	public int getReal(int sid) throws SafeException {
		if (!idmap.containsKey(sid)) {
			throw new SafeException();
		}
		return idmap.get(sid);
	}
	
	/**
	 * 清除所有的映射ID
	 */
	public void reset() {
		idmap.clear();
	}
}
