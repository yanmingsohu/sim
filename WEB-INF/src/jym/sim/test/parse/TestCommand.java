// CatfoOD 2012-3-2 下午01:02:17 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jym.sim.sql.reader.SqlLink;


public class TestCommand {

	public static void main(String[] s) throws IOException, NoSuchFieldException {
		
		
		List<Object> list = create(
				create(1,2,3),
				create(4,5,6),
				create(7,8,9)
				);
		
		SqlLink sl = new SqlLink(TestCommand.class, "commands.sql");
		sl.lockFile(1667704139L);
		sl.set("id", 1);
		sl.set("list1", list);
		sl.set("list2", list.iterator());
		sl.set("list3", new String[]{"a", "b", "c"});
		
		sl.showSql();
	}
	
	public static List<Object> create(Object...o) {
		List<Object> list = new ArrayList<Object>();
		if (o != null) {
			for (int i=0; i<o.length; ++i) {
				list.add(o[i]);
			}
		}
		return list;
	}
}
