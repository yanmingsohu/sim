package jym.sim.java;

/**
 * Java的访问权限修饰符
 */
public final class Access extends Key {

	public final Access PUBLIC		= new Access("public");
	public final Access PRIVATE		= new Access("private");
	public final Access PROTECTED	= new Access("protected");
	
	
	protected Access(String key) {
		super(key);
	}
}
