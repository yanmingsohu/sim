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
 * 封装查询结果集为List对象
 * 
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
	 * Connection对象由jdbc管理
	 * 
	 * @param sql 必须是查询类的sql语句,且只能是单个查询语句
	 * @throws SQLException
	 */
	public ResultSetList(String sql, JdbcTemplate jdbc, IGetBean<BEAN> gb) 
	throws SQLException {
		this(sql, jdbc.getConnection(), gb);
		// 如果connection==null,则不会关闭它
		connection = null;
	}
	
	/**
	 * 封装查询方法,
	 *
	 * @param sql - 必须是查询类的sql语句,且只能是单个查询语句
	 * @param conn - 该连接不能在外部关闭，否则会引起异常
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
	 * 才可以实现很好的兼容性,否则可能抛出异常(驱动不支持的情况)<br>
	 * <b>负值</b>的索引在不同实现中会有不同，但不会从结果集中取
	 */
	@Override
	public BEAN get(int index) {
		if (index>=size()) {
			throw new IndexOutOfBoundsException("max: "+size()+" not: "+index);
		}
		
		try {
			if (index>=0) {
				index++;
				if (index-1 == currentRow) {
					resultSet.next();
				} else {
					resultSet.absolute(index);
				}
				currentRow = index;
			}
			
			return getter.fromRowData(columnNames, resultSet, index);
		
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
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		clear();
	}
	
	/**
	 * 数据行转换为实体类过程的实现
	 * @param <BEAN> 存储数据的对象类型
	 */
	public interface IGetBean<BEAN> {
		/**
		 * 用rs当前行中的数据,生成BEAN对象,
		 * <b>必须定义</b>负值rowNum的行为,一般是抛出异常
		 * 
		 * @param columnNames - 列名表
		 * @param rs - 结果集
		 * @param rowNum - 当前数据行，索引负值范围在不同的实现中有不同的定义,
		 *  		通常会抛异常;rowNum的最大值是查询结果集的最大行数
		 * @return 结果集当前行对应的实体对象
		 */
		public BEAN fromRowData(String[] columnNames, 
				ResultSet rs, int rowNum) throws Exception;
	}
	
}
