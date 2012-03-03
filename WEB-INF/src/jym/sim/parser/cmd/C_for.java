// CatfoOD 2012-3-2 下午02:57:59 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.Iterator;

import jym.sim.parser.IComponent;
import jym.sim.parser.IItem;

/**
 * 语法: for(var : iterator)<br>
 * 参数: iterator:该对象必须是一个迭代器或集合, var:每次迭代器中的元素使用这个变量名, 可在循环中引用<br>
 * 功能: 使用迭代器循环生成内容体
 */
public class C_for extends AbsCommand {

	
	@SuppressWarnings("unchecked")
	public Iterator<IComponent> getItem() {
		try {
			if (params.size() != 2) {
				error("参数不正确");
			}

			String var_name = params.get(0).trim();
			IItem itr_var = vars.get(var_name);
		
			if (itr_var == null) {
				itr_var = createVar(var_name);
				if (itr_var == null) {
					error("迭代器使用的变量必须在文件或参数中存在: " + var_name);
				}
			}
			
			String itr_name = params.get(1).trim();
			Object obj = getVar(itr_name);
			
			if (obj == null) {
				error("迭代器无效 " + itr_name);
			}
			
			
			Iterator itr = null;
			
			if (obj instanceof Iterator) {
				itr = (Iterator) obj;
			}
			else if (obj instanceof Iterable) {
				itr = ((Iterable)obj).iterator();
			}
			else if (obj instanceof Object[]) {
				itr = new ArrayItr((Object[]) obj);
			}
			
			return new ForEach(itr, itr_var);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return NULL_ITR;
	}

	protected char splitCh(String waitSplit) { 
		return ':';
	}
	
	
	private class ArrayItr implements Iterator<Object> {
		
		Object[] arr;
		int idx;
		
		
		ArrayItr(Object[] arr) {
			this.arr = arr;
			idx = 0;
		}
		public boolean hasNext() {
			return idx < arr.length;
		}
		public Object next() {
			return arr[idx++];
		}
		public void remove() {
		}
	}
	
	
	private class ForEach implements Iterator<IComponent> {
		
		Iterator<IComponent> itr_content;
		Iterator<?> itr_each;
		IItem itr_var;
		IComponent next;
		boolean allOver;
		
		
		ForEach(Iterator<?> itr, IItem var) {
			itr_each = itr;
			itr_var = var;
		}
		
		void _next() {
			if ((next != null) || allOver) return;
			
			while (true) {
				if (itr_content != null && itr_content.hasNext()) {
					next = itr_content.next();
					break;
				}
				else if (itr_each.hasNext()) {
					itr_content = content.iterator();
					itr_var.init(null, itr_each.next());
				}
				else {
					allOver = true;
					break;
				}
			}
		}

		public boolean hasNext() {
			_next();
			return next != null;
		}

		public IComponent next() {
			_next();
			IComponent n = next;
			next = null;
			return n;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
