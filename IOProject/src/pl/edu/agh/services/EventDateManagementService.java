package pl.edu.agh.services;

import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.SqliteDatatypesHelper;
import pl.edu.agh.domain.tables.EventDateTable;
import pl.edu.agh.tools.BooleanTools;
import pl.edu.agh.tools.DateTimeTools;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDateManagementService {

	private SQLiteOpenHelper dbHelper;
	private LocationManagementService locationManagementService;
	
	public EventDateManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.locationManagementService = new LocationManagementService(dbHelper);
	}
	
	public long insert(EventDate insertObject, Event event) {
		ContentValues values = new ContentValues();
		values.put(EventDateTable.COLUMN_NAME_EVENT_ID, event.getId());
		values.put(EventDateTable.COLUMN_NAME_LOCATION_ID, event.getDefaultLocation().getId());
		values.put(EventDateTable.COLUMN_NAME_DATE, DateTimeTools.convertDateToString(insertObject.getDate()));
		values.put(EventDateTable.COLUMN_NAME_START_TIME, DateTimeTools.convertTimeToString(insertObject.getStartTime()));
		values.put(EventDateTable.COLUMN_NAME_END_TIME, DateTimeTools.convertTimeToString(insertObject.getEndTime()));
		values.put(EventDateTable.COLUMN_NAME_DURATION, insertObject.getDuration());
		values.put(EventDateTable.COLUMN_NAME_FINISHED, BooleanTools.convertBooleanToInt(insertObject.isFinished()));
		long id = dbHelper.getWritableDatabase().insert(EventDateTable.TABLE_NAME, null, values);
		insertObject.setId(id);
		return id;	
	}
	
	public long insert(EventDate insertObject, Event event, Location location) {
		ContentValues values = new ContentValues();
		values.put(EventDateTable.COLUMN_NAME_EVENT_ID, event.getId());
		values.put(EventDateTable.COLUMN_NAME_LOCATION_ID, location.getId());
		values.put(EventDateTable.COLUMN_NAME_DATE, DateTimeTools.convertDateToString(insertObject.getDate()));
		values.put(EventDateTable.COLUMN_NAME_START_TIME, DateTimeTools.convertTimeToString(insertObject.getStartTime()));
		values.put(EventDateTable.COLUMN_NAME_END_TIME, DateTimeTools.convertTimeToString(insertObject.getEndTime()));
		values.put(EventDateTable.COLUMN_NAME_DURATION, insertObject.getDuration());
		values.put(EventDateTable.COLUMN_NAME_FINISHED, BooleanTools.convertBooleanToInt(insertObject.isFinished()));
		long id = dbHelper.getWritableDatabase().insert(EventDateTable.TABLE_NAME, null, values);
		insertObject.setId(id);
		return id;	
	}

	public Set<EventDate> getAllEventDatesForEventId(Event event) {
		Set<EventDate> resultSet = new HashSet<EventDate>();
		Cursor cursor = null;
		try {
			String selection = EventDateTable.COLUMN_NAME_EVENT_ID + " = ?";
			String[] selectionArguments = new String[] { String.valueOf(event.getId()) };
			cursor = dbHelper.getWritableDatabase().query(EventDateTable.TABLE_NAME, null, selection, selectionArguments, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				EventDate eventDate = new EventDate();
				eventDate.setId(cursor.getLong(cursor.getColumnIndex(EventDateTable._ID)));
				eventDate.setDate(DateTimeTools.convertStringToDate(cursor.getString(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_DATE))));
				eventDate.setStartTime(DateTimeTools.convertStringToTime(cursor.getString(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_START_TIME))));
				eventDate.setEndTime(DateTimeTools.convertStringToTime(cursor.getString(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_END_TIME))));
				eventDate.setDuration(cursor.getInt(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_DURATION)));
				eventDate.setFinished(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_FINISHED))));
				eventDate.setLocation(locationManagementService.getByIdAllData(cursor.getLong(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_LOCATION_ID))));
				eventDate.setEvent(event);
				resultSet.add(eventDate);
				cursor.moveToNext();
			}
			return resultSet;
		} finally {
			cursor.close();
		}
	}
	
}
