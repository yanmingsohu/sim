// CatfoOD 2010-4-16 обнГ04:49:18 yanming-sohu@sohu.com/@qq.com

package jym.base.orm;

import javax.sql.DataSource;

public class UpdateTemplate<T> extends OrmTemplate<T> implements IUpdate<T> {

	public UpdateTemplate(DataSource ds, Class<T> modelclass, String simSql) {
		super(ds, modelclass, simSql);
	}

	public UpdateTemplate(DataSource ds, IOrm<T> orm) {
		super(ds, orm);
	}

	public int add(T model) {
		return 0;
	}

	public int delete(T model) {
		return 0;
	}

	public int update(T model) {
		return 0;
	}

}
