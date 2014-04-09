package pl.edu.agh.domain.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.edu.agh.domain.databasemanagement.IDatabaseTableSchema;
import pl.edu.agh.domain.databasemanagement.SqliteDatatypesHelper;
import android.provider.BaseColumns;

public class EventDateTable implements BaseColumns, IDatabaseTableSchema {

	public static final String TABLE_NAME = "EventDate";
	public static final String COLUMN_NAME_EVENT_ID = "EventID";
	public static final String COLUMN_NAME_LOCATION_ID = "LocationID";
	public static final String COLUMN_NAME_DATE = "Date";
	public static final String COLUMN_NAME_START_TIME = "StartTime";
	public static final String COLUMN_NAME_END_TIME = "EndTime";
	public static final String COLUMN_NAME_DURATION = "Duration";
	public static final String COLUMN_NAME_FINISHED = "IsFinished";
	
	public static final EventDateTable INSTANCE = new EventDateTable();
	
	private EventDateTable() {
	}
	
	public static EventDateTable getInstance() {
		return INSTANCE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public Map<String, String> getColumnNamesWithSqliteTypes() {
		Map<String, String> columnsWithNames = new TreeMap<String, String>();
		columnsWithNames.put(EventDateTable._ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_EVENT_ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_LOCATION_ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_DATE, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(COLUMN_NAME_START_TIME, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(COLUMN_NAME_END_TIME, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(COLUMN_NAME_DURATION, SqliteDatatypesHelper.getSqliteDatabaseType(Integer.class));
		columnsWithNames.put(COLUMN_NAME_FINISHED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		return columnsWithNames;
	}

	@Override
	public List<String> getUniqueColumns() {
		return null;
	}

	@Override
	public List<String> getNotNullColumns() {
		return new ArrayList<String>(Arrays.asList(COLUMN_NAME_EVENT_ID, COLUMN_NAME_LOCATION_ID, COLUMN_NAME_DATE, COLUMN_NAME_START_TIME, COLUMN_NAME_END_TIME, COLUMN_NAME_DURATION, COLUMN_NAME_FINISHED));
	}

	@Override
	public List<String> getPrimaryKeyColumns() {
		return new ArrayList<String>(Arrays.asList(EventDateTable._ID));
	}

	@Override
	public List<ForeignKeyMapping> getForeignKeyColumns() {
		List<ForeignKeyMapping> fkMapping = new ArrayList<ForeignKeyMapping>();
		fkMapping.add(new ForeignKeyMapping(COLUMN_NAME_EVENT_ID, EventTable.TABLE_NAME, EventTable._ID));
		fkMapping.add(new ForeignKeyMapping(COLUMN_NAME_LOCATION_ID, LocationTable.TABLE_NAME, LocationTable._ID));
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
