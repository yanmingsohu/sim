// CatfoOD 2009-12-14 下午09:52:45

package jym.sim.test.sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jym.sim.orm.IOrm;
import jym.sim.orm.IPlot;
import jym.sim.orm.OrmTemplate;
import jym.sim.orm.page.PageBean;
import jym.sim.orm.page.SQLServerPagination;
import jym.sim.tags.PaginationTag;
import jym.sim.util.Tools;

public class TestPaginationTag {

	static OrmTemplate<BA_BRONGTH> orm;
	
	
	public static void main(String[] args) throws Exception {
		createORM();
		
		PageBean page = new PageBean();
		page.setCurrent(3);
		page.setOnesize(5);
		
		BA_BRONGTH model = new BA_BRONGTH();
		List<BA_BRONGTH> list = orm.select(model, "and", page);
		
		Tools.pl(list, page);
	}
	
	public static void testUpdate() {
		BA_BRONGTH model = new BA_BRONGTH();
		model.setBrongthSn(new BigDecimal(1822));
		model.setBrongthName("jym1");
		orm.update(model);
	}
	
	public static void createORM() throws Exception {
		orm = new OrmTemplate<BA_BRONGTH>(
				TestDBPool.getDataSource(),
				new IOrm<BA_BRONGTH>() {

					public String getKey() {
						return "BRONGTH_SN";
					}

					public Class<BA_BRONGTH> getModelClass() {
						return BA_BRONGTH.class;
					}

					public String getTableName() {
						return "BA_BRONGTH";
					}

					public void mapping(IPlot plot) {
						plot.fieldPlot( "rowid",           "ROWID"           );
						plot.fieldPlot( "brongthSn",       "BRONGTH_SN"      );
						plot.fieldPlot( "brongthId",       "BRONGTH_ID"      );
						plot.fieldPlot( "brongthName",     "BRONGTH_NAME"    );
					}
				});
		orm.setPaginationPlot(new SQLServerPagination());
		orm.showSql(true);
		orm.needFormat(true);
	}

	public static void tag(PaginationTag t) {
		if (t == null) {
			t = new PaginationTag();
			t.setCurrentPage(2);
			t.setTotalPage(20);
			t.setUrlPattern("a/b/page?page=%page");
		}
		
		System.out.println(t.toString().replaceAll(">", ">\n"));
	}
	
	
	public static class BA_BRONGTH {

		private UUID rowid;
		private BigDecimal brongthSn;
		private String brongthId;
		private String brongthName;
		
		public UUID getRowid() {
			return rowid;
		}
		public BigDecimal getBrongthSn() {
			return brongthSn;
		}
		public String getBrongthId() {
			return brongthId;
		}
		public String getBrongthName() {
			return brongthName;
		}
		public void setRowid(UUID rowid) {
			this.rowid = rowid;
		}
		public void setBrongthSn(BigDecimal brongthSn) {
			this.brongthSn = brongthSn;
		}
		public void setBrongthId(String brongthId) {
			this.brongthId = brongthId;
		}
		public void setBrongthName(String brongthName) {
			this.brongthName = brongthName;
		}
		public String toString() {
			return "rowid:" + rowid +
				", brongthSn:" + brongthSn +
				", brongthId:" + brongthId +
				", brongthName:" + brongthName + "\n";
		}
	}
	
}
