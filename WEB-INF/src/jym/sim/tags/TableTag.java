// CatfoOD 2009-10-27 上午01:40:23

package jym.sim.tags;

import java.io.PrintWriter;


/**
 * append与creat的行为与普通的标记不同<br>
 * 每次append,creat返回的标记已经被封装在表格中<br>
 * 表格是自动增长的
 */
public class TableTag extends HtmlTagBase {
	//private ITag table;
	private ITag currentRow;
	private ITag head;
	private ITag body;
	private final int col;
	private int c = 0;
	
	/**
	 * table标签的封装
	 * @param colume - 表格的列
	 */
	public TableTag(int colume) {
		super("table");
		col = colume;
		head = super.create("thead");
		body = super.create("tbody");
		currentRow = body.create("tr");
	}
	
	public TableTag(String[] heads) {
		this(heads.length);
		
		for (int i=0; i<heads.length; ++i) {
			appendHead(heads[i]);
		}
	}

	public void addAttribute(String name, String value) {
		super.addAttribute(name, value);
	}
	
	private ITag getColumeOfRow() {
		if (c>=col) {
			c=0;
			currentRow = body.create("tr");
		}
		c++;
		return currentRow.create("td");
	}

	/**
	 * 向表格中添加文本数据<br>
	 * 每次调用都会在表格中创建一个新的列，
	 * 直到每行的列到达指定值，此时新建一个行
	 */
	public boolean append(String text) {
		ITag td = getColumeOfRow();
		return td.append(text);
	}
	
	/**
	 * 向表格头中添加数据
	 * @param text
	 */
	public void appendHead(String text) {
		ITag td = head.create("td");
		td.append(text);
	}
	
	/**
	 * 向表格头中添加数据
	 * @param text
	 */
	public void appendHead(ITag tag) {
		ITag td = head.create("td");
		td.append(tag);
	}

	/**
	 * 在表格中加入一个子标记<br>
	 * 每次调用都会在表格中创建一个新的列，
	 * 直到每行的列到达指定值，此时新建一个行
	 */
	public boolean append(ITag tag) {
		ITag td = getColumeOfRow();
		return td.append(tag);
	}

	/**
	 * 在表格中建立一个新的标记，并返回他<br>
	 * 每次调用都会在表格中创建一个新的列，
	 * 直到每行的列到达指定值，此时新建一个行
	 */
	public ITag create(String newtagname) {
		ITag td = getColumeOfRow();
		return td.create(newtagname);
	}

	public PrintWriter createText() {
		ITag td = getColumeOfRow();
		return td.createText();
	}

}
