package pl.edu.agh.domain.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.edu.agh.domain.databasemanagement.IDatabaseTableSchema;
import pl.edu.agh.domain.databasemanagement.SqliteDatatypesHelper;
import android.provider.BaseColumns;

public class AccountTable implements BaseColumns, IDatabaseTableSchema {

	public static final String TABLE_NAME = "Account";
	public static final String COLUMN_NAME_LOGIN = "Login";
	public static final String COLUMN_NAME_PASSWORD = "Password";
	public static final String COLUMN_NAME_EMAIL = "Email";
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@Override
	public Map<String, String> getColumnNamesWithSqliteTypes() {
		Map<String, String> columnsWithNames = new TreeMap<String, String>();
		columnsWithNames.put(AccountTable._ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_LOGIN, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_PASSWORD, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_EMAIL, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		return columnsWithNames;
	}
	
	@Override
	public List<String> getUniqueColumns() {
		return new ArrayList<String>(Arrays.asList(COLUMN_NAME_LOGIN));
	}
	@Override
	public List<String> getNotNullColumns() {
		return new ArrayList<String>(Arrays.asList(COLUMN_NAME_LOGIN, COLUMN_NAME_PASSWORD));
	}
	
	@Override
	public List<String> getPrimaryKeyColumns() {
		return new ArrayList<String>(Arrays.asList(AccountTable._ID));
	}
	
	@Override
	public List<ForeignKeyMapping> getForeignKeyColumns() {
		return null;
	}
	
	@Override
	public List<String> getIndexesColumns() {
		return null;
	}
	
	@Override
	public List<String> getCheckConstraints() {
		return null;
	}
	
}
