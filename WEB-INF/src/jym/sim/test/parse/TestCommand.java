// CatfoOD 2012-3-2 下午01:02:17 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jym.sim.sql.reader.SqlLink;


public class TestCommand {

	public static void main(String[] s) throws IOException, NoSuchFieldException {
		List<String> list = new ArrayList<String>();
		list.add("10");
		list.add("20");
		list.add("30");
		
		SqlLink sl = new SqlLink(TestCommand.class, "commands.sql");
		sl.set("id", 1);
		sl.set("b", list.iterator());
		
		sl.showSql();
	}
}
