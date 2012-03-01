// CatfoOD 2012-2-29 下午02:35:16 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jym.sim.parser.IComponent;
import jym.sim.parser.IItem;

/**
 * 命令由命令名, 命令参数, 命令内容体(被控体)组成:<br><br>
 * <code>
 * #命令 (参数)<br>
 * 内容体<br>
 * #end<br>
 * </code><br>
 * 迭代器部分用来控制如何输出内容体, 实现类必须有默认构造函数, 
 * 线程不安全的, 可变类, 不能重复使用
 */
public interface ICommand extends IComponent {
	
	/**
	 * 返回保存内容体的容器, 解析器负责插入元素, 元素可以是另一个命令
	 * 该方法返回的集合不受命令控制
	 */
	List<IComponent> getBag();
	
	/**
	 * 经过计算, 返回符合条件的集合, 
	 * 该方法返回的集合受命令控制
	 * <b>如果返回null, 则会立即终止文件的输出</b>
	 */
	Iterator<IComponent> getItem();
	
	/**
	 * 设置解析器中保存的变量, 用于双向通信
	 */
	void setVariableBag(Map<String, IItem> vars);
	
	/**
	 * 向命令传递参数, 之前的参数被丢弃
	 */
	void setParameter(String param_str);
	
}
