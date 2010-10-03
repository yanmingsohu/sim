// CatfoOD 2010-10-1 下午03:32:04

package jym.sim.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractList;

import jym.sim.orm.SelectTemplate;

/**
 * @param <BEAN> - 存放查询出数据的实体类类型
 */
public class ResultSetList<BEAN> extends AbstractList<BEAN> {
	
	private Connection connection;
	private ResultSet resultSet;
	private Statement statement;
	private final String[] columnNames;
	private IGetBean<BEAN> getter;
	private int currentRow = 0;
	private int maxRow = -1;
	

	/**
	 * 封装查询方法
	 *
	 * @throws SQLException 
	 */
	public ResultSetList(String sql, Connection conn, IGetBean<BEAN> gb) 
	throws SQLException {
		
		connection = conn;
		getter = gb;
		
	//XXX 这是导致resultSet查询速度慢的原因
		statement = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, 
				ResultSet.CONCUR_READ_ONLY);
		
		resultSet = statement.executeQuery(sql);
		
		ResultSetMetaData rsmd = resultSet.getMetaData();
		columnNames = SelectTemplate.getColumnNames(rsmd);
	}
	
	/**
	 * 取得指定数据行上数据映射的实体类,应该使用连续的索引(0,1,2,N)
	 * 才可以实现很好的兼容性,否则可能抛出异常(驱动不支持的情况)
	 */
	@Override
	public BEAN get(int index) {
		if (index<0 || index>=size()) {
			throw new IndexOutOfBoundsException("max: "+size()+" not: "+index);
		}
		
		try {
			index++;
			if (index-1 == currentRow) {
				resultSet.next();
			} else {
				resultSet.absolute(index);
			}
			currentRow = index;
			
			return getter.fromRowData(columnNames, resultSet);
		
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int size() {
		if (maxRow<0) {
			try {
				if (resultSet.last()) {
					maxRow = resultSet.getRow();
					currentRow = maxRow;
				} else {
					maxRow = 0;
				}
			} catch(Exception e) {
				maxRow = 0;
			}
		}
		return maxRow;
	}

	/**
	 * 释放与数据库连接的对象,相当于close()
	 */
	@Override
	public void clear() {
		try {
			resultSet.close();
		} catch (SQLException e) {
		}
		try {
			statement.close();
		} catch (SQLException e) {
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clear();
	}
	
	/**
	 * 数据行转换为实体类的实现
	 * @param <BEAN>
	 */
	public interface IGetBean<BEAN> {
		/**
		 * 用rs当前行中的数据,生成BEAN对象
		 * 
		 * @param columnNames - 列名表
		 * @param rs - 结果集
		 * @return 结果集当前行对应的实体对象
		 */
		public BEAN fromRowData(String[] columnNames, ResultSet rs) throws Exception;
	}
	
}
