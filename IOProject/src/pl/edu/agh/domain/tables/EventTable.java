package pl.edu.agh.domain.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.edu.agh.domain.databasemanagement.IDatabaseTableSchema;
import pl.edu.agh.domain.databasemanagement.SqliteDatatypesHelper;
import android.provider.BaseColumns;

public class EventTable implements BaseColumns, IDatabaseTableSchema {

	public static final String TABLE_NAME = "Event";
	public static final String COLUMN_NAME_ACCOUNT_ID = "AccountID";
	public static final String COLUMN_NAME_PREDECESSOR_EVENT_ID = "PredecessorEventID";
	public static final String COLUMN_NAME_DEFAULT_LOCATION_ID = "DefaultLocationID";
	public static final String COLUMN_NAME_TITLE = "Title";
	public static final String COLUMN_NAME_DESCRIPTION = "Description";
	public static final String COLUMN_NAME_IS_REQUIRED = "IsRequired";
	public static final String COLUMN_NAME_IS_CONSTANT = "IsConstant";
	
	public static final EventTable INSTANCE = new EventTable();
	
	private EventTable() {
	}
	
	public static EventTable getInstance() {
		return INSTANCE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public Map<String, String> getColumnNamesWithSqliteTypes() {
		Map<String, String> columnsWithNames = new TreeMap<String, String>();
		columnsWithNames.put(EventTable._ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_ACCOUNT_ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_PREDECESSOR_EVENT_ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_DEFAULT_LOCATION_ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_TITLE, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_DESCRIPTION, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_IS_REQUIRED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(COLUMN_NAME_IS_CONSTANT, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		return columnsWithNames;
	}
	
	@Override
	public List<String> getUniqueColumns() {
		return null;
	}
	
	@Override
	public List<String> getNotNullColumns() {
		return new ArrayList<String>(Arrays.asList(COLUMN_NAME_ACCOUNT_ID, COLUMN_NAME_TITLE, COLUMN_NAME_IS_CONSTANT, COLUMN_NAME_IS_REQUIRED));
	}
	
	@Override
	public List<String> getPrimaryKeyColumns() {
		return new ArrayList<String>(Arrays.asList(EventTable._ID));
	}
	
	@Override
	public List<ForeignKeyMapping> getForeignKeyColumns() {
		List<ForeignKeyMapping> fkMapping = new ArrayList<ForeignKeyMapping>();
		fkMapping.add(new ForeignKeyMapping(COLUMN_NAME_ACCOUNT_ID, AccountTable.TABLE_NAME, AccountTable._ID));
		fkMapping.add(new ForeignKeyMapping(COLUMN_NAME_PREDECESSOR_EVENT_ID, EventTable.TABLE_NAME, EventTable._ID));
		fkMapping.add(new ForeignKeyMapping(COLUMN_NAME_DEFAULT_LOCATION_ID, LocationTable.TABLE_NAME, LocationTable._ID));
		return fkMapping;
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
