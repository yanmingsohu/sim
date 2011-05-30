// CatfoOD 2009-12-14 下午08:14:05

package jym.sim.tags.template;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jym.sim.tags.HtmlTagBase;
import jym.sim.tags.ITag;
import jym.sim.tags.PaginationTag;
import jym.sim.tags.TableTag;

/**
 * 封装分页的结果集表格
 * 
 * 内容格式:<br/>
 * <code>
 * &lt;div&gt; <br/>
 * &nbsp;&nbsp; &lt;table&gt; <br/>
 * 	&nbsp;&nbsp;&nbsp;&nbsp;... // 数据的表格 <br/>
 * &nbsp;&nbsp; &lt;/table&gt; <br/>
 * &nbsp;&nbsp; &lt;div&gt; <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;... // 分页按钮 <br/>
 * &nbsp;&nbsp; &lt;/div&gt; <br/>
 * &lt;/div&gt; <br/>
 * <code>
 */
public class ResultTableTemplate<E> extends HtmlTagBase {
	private List<E> list;
	private String[] heads;
	private IResultCallback<E> template;
	private String tableid;

	/**
	 * 封装分页的结果集表格
	 * 
	 * @param t - 回调函数
	 * @param li - 数据列表
	 * @param ip - 分页数据
	 */
	public ResultTableTemplate(IResultCallback<E> t, List<E> li) {
		super("div");
		template = t;
		list = li;
	}
	
	public void setTableId(String id) {
		tableid = id;
	}
	
	@Override
	public void printout(PrintWriter out) {
		createTableHeads();
		append( createResultTable() );
		append( createPaginationDiv() );
		super.printout(out);
	}
	
	private void createTableHeads() {
		heads = new String[template.getColumn()];
		for (int i=0; i<heads.length; ++i) {
			heads[i] = template.getColumnName(i);
		}
	}

	private ITag createPaginationDiv() {
		PaginationTag pagetag = new PaginationTag();
		pagetag.setCurrentPage( template.getCurrentPage() );
		pagetag.setDisplaySize(10);
		pagetag.setTotalPage( template.getTotalPage() );
		pagetag.setUrlPattern( template.getUrlPattern() );
		
		return pagetag;
	}
	
	private ITag createResultTable() {		
		TableTag table = new TableTag(heads);
		table.addAttribute("id", tableid);
		table.addAttribute("width", "100%");
		
		List<String> row = new ArrayList<String>();
		Iterator<E> iter = list.iterator();
		while (iter.hasNext()) {
			E src = iter.next();
			row.clear();
			
			if (src!=null) {
				template.mappingRowValue(row, src);
				
				for (int i=0; i<heads.length; ++i) {
					table.append( row.get(i) );
				}
			}
		}
		return table;
	}
	
}
