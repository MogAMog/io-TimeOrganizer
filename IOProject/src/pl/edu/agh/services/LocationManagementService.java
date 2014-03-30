package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.LocationTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


public class LocationManagementService implements IDatabaseDmlProvider<Location> {

	private SQLiteOpenHelper dbHelper;

	public LocationManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	
	@Override
	public long insert(Location insertObject) {
		ContentValues values = new ContentValues();
		values.put(LocationTable.COLUMN_NAME_NAME, insertObject.getName());
		values.put(LocationTable.COLUMN_NAME_ADDRESS, insertObject.getAddress());
		values.put(LocationTable.COLUMN_NAME_CITY, insertObject.getCity());
		values.put(LocationTable.COLUMN_NAME_LATITUDE, insertObject.getLatitude());
		values.put(LocationTable.COLUMN_NAME_LONGITUDE, insertObject.getLongitude());
		values.put(LocationTable.COLUMN_NAME_DEFAULT, insertObject.isDefaultLocation());
		long id = dbHelper.getWritableDatabase().insert(LocationTable.TABLE_NAME, null, values);
		insertObject.setId(id);
		return id;
	}

	@Override
	public List<Location> getAll() {
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + LocationTable.TABLE_NAME, null);
		cursor.moveToFirst();
		List<Location> locations = new ArrayList<Location>();
		while(!cursor.isAfterLast()) {
			Location location = new Location();
			location.setId(cursor.getLong(cursor.getColumnIndex(LocationTable._ID)));
			location.setName(cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_NAME)));
			location.setAddress(cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_ADDRESS)));
			location.setCity(cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_CITY)));
			location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationTable.COLUMN_NAME_LONGITUDE)));
			location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationTable.COLUMN_NAME_LATITUDE)));
			location.setDefaultLocation(cursor.getInt(cursor.getColumnIndex(LocationTable.COLUMN_NAME_DEFAULT)) == 1 ? true : false);
			locations.add(location);
			cursor.moveToNext();
		}
		return locations;
	}

	@Override
	public Location getByIdAllData(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
