package jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JResultSetMetaData implements ResultSetMetaData {
	private int columnCount;
	private ArrayList<LinkedHashMap<String, String>> MetaData;
	private ArrayList<String> colMapping;
	private JResultSet ResultSet;

	public JResultSetMetaData(int columnCount, ArrayList<LinkedHashMap<String, String>> MetaData,
			ArrayList<String> colMapping) {
		this.columnCount = columnCount;
		this.MetaData = MetaData;
		this.colMapping = colMapping;

	}

	protected void setResultSet(JResultSet resultSet) {
		ResultSet = resultSet;
	}

	@Override
	public int getColumnCount() throws SQLException {
		if (ResultSet.isClosed())
			throw new SQLException("ResultSet Has Been Closed!");
		return columnCount;
	}

	@Override
	public String getTableName(int column) throws SQLException {
		if (ResultSet.isClosed())
			throw new SQLException("ResultSet Has Been Closed!");
		return MetaData.get(0).get("tableName");
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		if (ResultSet.isClosed())
			throw new SQLException("ResultSet Has Been Closed!");
		if (column < 1 || column > colMapping.size())
			throw new SQLException("Invalid Column Index !");
		String key = colMapping.get(column);
		if (MetaData.get(1).get(key).equals("int")) {
			return Types.INTEGER;
		}
		if (MetaData.get(1).get(key).equals("varchar")) {
			return Types.VARCHAR;
		}
		if (MetaData.get(1).get(key).equals("float")) {
			return Types.FLOAT;
		}
		if (MetaData.get(1).get(key).equals("date")) {
			return Types.DATE;
		}
		return 0;
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		if (ResultSet.isClosed())
			throw new SQLException("ResultSet Has Been Closed!");
		if (column < 1 || column > colMapping.size())
			throw new SQLException("Invalid Column Index !");

		return colMapping.get(column - 1);
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		return getColumnLabel(column);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int isNullable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
