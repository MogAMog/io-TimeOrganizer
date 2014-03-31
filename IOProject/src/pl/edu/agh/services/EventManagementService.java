package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.EventDateTable;
import pl.edu.agh.domain.tables.EventTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class EventManagementService implements IDatabaseDmlProvider<Event> {

	private SQLiteOpenHelper dbHelper;
	private AccountManagementService accountManagementService;
	private LocationManagementService locationManagementService;
	
	public EventManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.accountManagementService = new AccountManagementService(dbHelper);
		this.locationManagementService = new LocationManagementService(dbHelper);
	}
	
	@Override
	public long insert(Event insertObject) {
		ContentValues values = new ContentValues();
		values.put(EventTable.COLUMN_NAME_TITLE, insertObject.getTitle());
		values.put(EventTable.COLUMN_NAME_DESCRIPTION, insertObject.getDescription());
		values.put(EventTable.COLUMN_NAME_IS_CONSTANT, insertObject.isConstant() ? 1 : 0);
		values.put(EventTable.COLUMN_NAME_IS_REQUIRED, insertObject.isRequired() ? 1 : 0);
		if(insertObject.getPredecessorEvent() != null) {
			if(insertObject.getPredecessorEvent().getId() <= 0) {
				insert(insertObject.getPredecessorEvent());
			}
			values.put(EventTable.COLUMN_NAME_PREDECESSOR_EVENT_ID, insertObject.getPredecessorEvent().getId());
		}
		if(insertObject.getAccount() != null) {
			if(insertObject.getAccount().getId() <= 0) {
				accountManagementService.insert(insertObject.getAccount());
			}
			values.put(EventTable.COLUMN_NAME_ACCOUNT_ID, insertObject.getAccount().getId());
		}
		long id = dbHelper.getWritableDatabase().insert(EventTable.TABLE_NAME, null, values);
		insertObject.setId(id);
		return id;
	}

	@Override
	public List<Event> getAll() {
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + EventTable.TABLE_NAME, null);
		cursor.moveToFirst();
		List<Event> events = new ArrayList<Event>();
		while(!cursor.isAfterLast()) {
			Event event = new Event();
			event.setId(cursor.getLong(cursor.getColumnIndex(EventTable._ID)));
			event.setTitle(cursor.getString(cursor.getColumnIndex(EventTable.COLUMN_NAME_TITLE)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(EventTable.COLUMN_NAME_DESCRIPTION)));
			event.setConstant(cursor.getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_IS_CONSTANT)) == 1 ? true : false);
			event.setRequired(cursor.getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_IS_REQUIRED)) == 1 ? true : false);
			event.setAccount(accountManagementService.getByIdAllData(cursor.getLong(cursor.getColumnIndex(EventTable.COLUMN_NAME_ACCOUNT_ID))));
			//event.setEventDates();
			//event.setPredecessorEvent(getById)
			events.add(event);
			cursor.moveToNext();
		}
		return events;
	}

	@Override
	public Event getByIdAllData(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Set<EventDate> getAllEventDatesForEvent(Event event) {
		Set<EventDate> resultSet = new HashSet<EventDate>();
		String selection = EventDateTable.COLUMN_NAME_EVENT_ID + " = ?";
		String[] selectionArguments = new String[] { String.valueOf(event.getId()) };
		Cursor cursor = dbHelper.getWritableDatabase().query(EventDateTable.TABLE_NAME, null, selection, selectionArguments, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			EventDate eventDate = new EventDate();
			eventDate.setId(cursor.getLong(cursor.getColumnIndex(EventDateTable._ID)));
			//eventDate.setDate(cursor.get)
			eventDate.setFinished(cursor.getInt(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_FINISHED)) == 1 ? true : false);
			eventDate.setLocation(locationManagementService.getByIdAllData(cursor.getLong(cursor.getColumnIndex(EventDateTable.COLUMN_NAME_LOCATION_ID))));
			resultSet.add(eventDate);
		}
		return resultSet;
	}
	

}
