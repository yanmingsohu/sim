// CatfoOD 2009-12-14 обнГ09:52:45

package test;

import jym.base.tags.PaginationTag;

public class TestPaginationTag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PaginationTag t = new PaginationTag();
		t.setCurrentPage(2);
		t.setTotalPage(20);
		t.setUrlPattern("a/b/page?=%d");
		
		System.out.println(t);
	}

}
