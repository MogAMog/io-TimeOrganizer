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

public class EventTemplateTable implements BaseColumns, IDatabaseTableSchema {

	public static final String TABLE_NAME = "EventTemplates";
	public static final String COLUMN_NAME_TEMPLATENAME = "TemplateName";
	public static final String COLUMN_NAME_TITLE = "Title";
	public static final String COLUMN_NAME_DESCRIPTION = "Description";
	public static final String COLUMN_NAME_REQUIRED = "Required";
	public static final String COLUMN_NAME_CONSTANT = "Constant";
	public static final String COLUMN_NAME_DRAFT = "Draft";
	public static final String COLUMN_NAME_FIXED_TIME = "FixedTime";
	public static final String COLUMN_NAME_DURATION = "Duration";
	public static final String COLUMN_NAME_START_DATE = "StartDate";
	public static final String COLUMN_NAME_END_DATE = "EndDate";
	public static final String COLUMN_NAME_START_TIME = "StartTime";
	public static final String COLUMN_NAME_END_TIME = "EndTime";
	public static final String COLUMN_NAME_FREQUENCY = "Frequency";
	public static final String COLUMN_NAME_MONDAY_SELECTED = "MondaySelected";
	public static final String COLUMN_NAME_TUESDAY_SELECTED = "TuesdaySelected";
	public static final String COLUMN_NAME_WEDNESDAY_SELECTED = "WednesdaySelected";
	public static final String COLUMN_NAME_THURSDAY_SELECTED = "ThursdaySelected";
	public static final String COLUMN_NAME_FRIDAY_SELECTED = "FridaySelected";
	public static final String COLUMN_NAME_SATURDAY_SELECTED = "SaturdaySelected";
	public static final String COLUMN_NAME_SUNDAY_SELECTED = "SundaySelected";
	
	public static final EventTemplateTable INSTANCE = new EventTemplateTable();
	
	private EventTemplateTable() {
	}
	
	public static EventTemplateTable getInstance() {
		return INSTANCE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public Map<String, String> getColumnNamesWithSqliteTypes() {
		Map<String, String> columnsWithNames = new TreeMap<String, String>();
		columnsWithNames.put(EventTemplateTable._ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_TEMPLATENAME, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_TITLE, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_DESCRIPTION, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_REQUIRED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_CONSTANT, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_DRAFT, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_FIXED_TIME, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_DURATION, SqliteDatatypesHelper.getSqliteDatabaseType(Integer.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_START_DATE, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_END_DATE, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_START_TIME, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_END_TIME, SqliteDatatypesHelper.getSqliteDatabaseType(Date.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_FREQUENCY, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_MONDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_TUESDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_WEDNESDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_THURSDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_FRIDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_SATURDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		columnsWithNames.put(EventTemplateTable.COLUMN_NAME_SUNDAY_SELECTED, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		return columnsWithNames;
	}

	@Override
	public List<String> getUniqueColumns() {
		return null;
	}

	@Override
	public List<String> getNotNullColumns() {
		return new ArrayList<String>(Arrays.asList(EventTemplateTable.COLUMN_NAME_TEMPLATENAME));
	}

	@Override
	public List<String> getPrimaryKeyColumns() {
		return new ArrayList<String>(Arrays.asList(EventTemplateTable._ID));
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
