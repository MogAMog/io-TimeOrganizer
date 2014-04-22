package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.List;

import com.example.ioproject.R;

import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.LocationTable;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.interfaces.IEntityValidation;
import pl.edu.agh.tools.BooleanTools;
import pl.edu.agh.tools.StringTools;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


public class LocationManagementService implements IDatabaseDmlProvider<Location>, IEntityValidation<Location> {

	private static final String ONE_TIME_SPECIFIC_LOCALIZATION = "One Time Event Specific Localization\nAdrress and City not provided.";
	
	private SQLiteOpenHelper dbHelper;

	public LocationManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	
	@Override
	public List<FormValidationError> validate(Location entity) {
		List<FormValidationError> errors = new ArrayList<FormValidationError>();
		if(StringTools.isNullOrEmpty(entity.getName())) {
			errors.add(new FormValidationError(R.string.Validation_Location_Name_NotNull));
		}
		if(entity.getLatitude() == null || entity.getLongitude() == null) {
			errors.add(new FormValidationError(R.string.Validation_Location_Coordinates_NotNull));
		}
		if(entity.isDefaultLocation() == null) {
			errors.add(new FormValidationError(R.string.Validation_Location_IsDefault_NotNull));
		}
		return errors;
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
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + LocationTable.TABLE_NAME, null);
			cursor.moveToFirst();
			List<Location> locations = new ArrayList<Location>();
			while(!cursor.isAfterLast()) {
				locations.add(getLocationFromCursor(cursor));
				cursor.moveToNext();
			}
			return locations;
		} finally {
			cursor.close();
		}
	}

	@Override
	public Location getByIdAllData(long id) {
		Cursor cursor = null;
		try {
			String selection = LocationTable._ID + " = ?";
			String[] selectionArgument = new String[] { String.valueOf(id) };
			cursor = dbHelper.getReadableDatabase().query(LocationTable.TABLE_NAME, null,selection, selectionArgument, null, null, null);
			cursor.moveToFirst();
			Location location = getLocationFromCursor(cursor);	
			return location;
		} finally {
			cursor.close();
		}
	}
	
	//@Override
	public List<Location> getDefaultLocalizationsAllData() {
		List<Location> locations = new ArrayList<Location>();
		Cursor cursor = null;
		try {
			String selection = LocationTable.COLUMN_NAME_DEFAULT + " = ?";
			String[] selectionArguments = new String[] { Integer.toString(BooleanTools.convertBooleanToInt(true)) };
			cursor = dbHelper.getReadableDatabase().query(LocationTable.TABLE_NAME, null, selection, selectionArguments, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				locations.add(getLocationFromCursor(cursor));
				cursor.moveToNext();
			}
			return locations;
		} finally {
			cursor.close();
		}
	}
	
	public Location getLocationByName(String name) {
		Cursor cursor = null;
		try {
			String selection = LocationTable.COLUMN_NAME_NAME + " = ?";
			String[] selectionArgument = new String[] { name };
			cursor = dbHelper.getReadableDatabase().query(LocationTable.TABLE_NAME, null,selection, selectionArgument, null, null, null);
			cursor.moveToFirst();
			Location location = getLocationFromCursor(cursor);	
			return location;
		} finally {
			cursor.close();
		}
	}
	
	public void setValuesForNotDefaultLocation(Location location) {
		location.setName(ONE_TIME_SPECIFIC_LOCALIZATION);
		location.setAddress("");
		location.setCity("");
		location.setDefaultLocation(false);
	}
	
	private Location getLocationFromCursor(Cursor cursor) {
		Location location = new Location();
		location.setId(cursor.getLong(cursor.getColumnIndex(LocationTable._ID)));
		location.setName(cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_NAME)));
		location.setAddress(cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_ADDRESS)));
		location.setCity(cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_CITY)));
		location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationTable.COLUMN_NAME_LONGITUDE)));
		location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationTable.COLUMN_NAME_LATITUDE)));
		location.setDefaultLocation(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(LocationTable.COLUMN_NAME_DEFAULT))));
		return location;
	}
	
}
