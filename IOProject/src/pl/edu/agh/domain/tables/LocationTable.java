package pl.edu.agh.domain.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.edu.agh.domain.databasemanagement.IDatabaseTableSchema;
import pl.edu.agh.domain.databasemanagement.SqliteDatatypesHelper;
import android.provider.BaseColumns;

public class LocationTable implements BaseColumns, IDatabaseTableSchema {

	public static final String TABLE_NAME = "Location";
	public static final String COLUMN_NAME_NAME = "Name";
	public static final String COLUMN_NAME_ADDRESS = "Address";
	public static final String COLUMN_NAME_CITY = "City";
	public static final String COLUMN_NAME_LATITUDE = "Latitude";
	public static final String COLUMN_NAME_LONGITUDE = "Longitude";
	public static final String COLUMN_NAME_DEFAULT = "IsDeafult";

	public static final LocationTable INSTANCE = new LocationTable();
	
	private LocationTable() {
	}
	
	public static LocationTable getInstance() {
		return INSTANCE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public Map<String, String> getColumnNamesWithSqliteTypes() {
		Map<String, String> columnsWithNames = new TreeMap<String, String>();
		columnsWithNames.put(LocationTable._ID, SqliteDatatypesHelper.getSqliteDatabaseType(Long.class));
		columnsWithNames.put(COLUMN_NAME_NAME, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_ADDRESS, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_CITY, SqliteDatatypesHelper.getSqliteDatabaseType(String.class));
		columnsWithNames.put(COLUMN_NAME_LATITUDE, SqliteDatatypesHelper.getSqliteDatabaseType(Double.class));
		columnsWithNames.put(COLUMN_NAME_LONGITUDE, SqliteDatatypesHelper.getSqliteDatabaseType(Double.class));
		columnsWithNames.put(COLUMN_NAME_DEFAULT, SqliteDatatypesHelper.getSqliteDatabaseType(Boolean.class));
		return columnsWithNames;
	}

	@Override
	public List<String> getUniqueColumns() {
		return null;
	}

	@Override
	public List<String> getNotNullColumns() {
		return new ArrayList<String>(Arrays.asList(COLUMN_NAME_NAME, COLUMN_NAME_LATITUDE, COLUMN_NAME_LONGITUDE, COLUMN_NAME_DEFAULT));
	}

	@Override
	public List<String> getPrimaryKeyColumns() {
		return new ArrayList<String>(Arrays.asList(LocationTable._ID));
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
