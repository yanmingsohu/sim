// CatfoOD 2009-10-27 ����01:40:23

package jym.sim.tags;

import java.io.PrintWriter;


/**
 * append��creat����Ϊ����ͨ�ı�ǲ�ͬ<br>
 * ÿ��append,creat���صı���Ѿ�����װ�ڱ�����<br>
 * �������Զ�������
 */
public class TableTag extends HtmlTagBase {
	//private ITag table;
	private ITag currentRow;
	private ITag head;
	private ITag body;
	private final int col;
	private int c = 0;
	
	/**
	 * table��ǩ�ķ�װ
	 * @param colume - �������
	 */
	public TableTag(int colume) {
		super("table");
		col = colume;
		head = super.creat("thead");
		body = super.creat("tbody");
		currentRow = body.creat("tr");
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
			currentRow = body.creat("tr");
		}
		c++;
		return currentRow.creat("td");
	}

	/**
	 * ������������ı�����<br>
	 * ÿ�ε��ö����ڱ����д���һ���µ��У�
	 * ֱ��ÿ�е��е���ָ��ֵ����ʱ�½�һ����
	 */
	public boolean append(String text) {
		ITag td = getColumeOfRow();
		return td.append(text);
	}
	
	/**
	 * �����ͷ����������
	 * @param text
	 */
	public void appendHead(String text) {
		ITag td = head.creat("td");
		td.append(text);
	}
	
	/**
	 * �����ͷ����������
	 * @param text
	 */
	public void appendHead(ITag tag) {
		ITag td = head.creat("td");
		td.append(tag);
	}

	/**
	 * �ڱ����м���һ���ӱ��<br>
	 * ÿ�ε��ö����ڱ����д���һ���µ��У�
	 * ֱ��ÿ�е��е���ָ��ֵ����ʱ�½�һ����
	 */
	public boolean append(ITag tag) {
		ITag td = getColumeOfRow();
		return td.append(tag);
	}

	/**
	 * �ڱ����н���һ���µı�ǣ���������<br>
	 * ÿ�ε��ö����ڱ����д���һ���µ��У�
	 * ֱ��ÿ�е��е���ָ��ֵ����ʱ�½�һ����
	 */
	public ITag creat(String newtagname) {
		ITag td = getColumeOfRow();
		return td.creat(newtagname);
	}

	public PrintWriter creatText() {
		ITag td = getColumeOfRow();
		return td.creatText();
	}

}