package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.EventTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class EventManagementService implements IDatabaseDmlProvider<Event> {

	private SQLiteOpenHelper dbHelper;
	private AccountManagementService accountManagementService;
	private EventDateManagementService eventDateManagementService;
	private LocationManagementService locationManagementService;
	
	public EventManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.accountManagementService = new AccountManagementService(dbHelper);
		this.eventDateManagementService = new EventDateManagementService(dbHelper);
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
		if(insertObject.getDefaultLocation() != null) {
			if(insertObject.getDefaultLocation().getId() <= 0) {
				locationManagementService.insert(insertObject.getDefaultLocation());
			}
			values.put(EventTable.COLUMN_NAME_DEFAULT_LOCATION_ID, insertObject.getDefaultLocation().getId());
		}
		long id = dbHelper.getWritableDatabase().insert(EventTable.TABLE_NAME, null, values);
		insertObject.setId(id);
		
		if(insertObject.getEventDates() != null && !insertObject.getEventDates().isEmpty()) {
			for(EventDate eventDate : insertObject.getEventDates()) {
				eventDateManagementService.insert(eventDate, insertObject);
			}
		}
		return id;
	}

	@Override
	public List<Event> getAll() {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + EventTable.TABLE_NAME, null);
			cursor.moveToFirst();
			List<Event> events = new ArrayList<Event>();
			while(!cursor.isAfterLast()) {
				events.add(getEventFromCursor(cursor));
				cursor.moveToNext();
			}
			return events;
		} finally {
			cursor.close();
		}
	}

	@Override
	public Event getByIdAllData(long id) {
		Cursor cursor = null;
		try { 
			String selection = EventTable._ID + " = ?";
			String[] selectionArgument = new String[] { String.valueOf(id) };
			cursor = dbHelper.getReadableDatabase().query(EventTable.TABLE_NAME, null, selection, selectionArgument, null, null, null);
			cursor.moveToFirst();
			Event event = getEventFromCursor(cursor);
			return event;
		} finally {
			cursor.close();
		}
	}
	
	private Event getEventFromCursor(Cursor cursor) {
		Event event = new Event();
		event.setId(cursor.getLong(cursor.getColumnIndex(EventTable._ID)));
		event.setTitle(cursor.getString(cursor.getColumnIndex(EventTable.COLUMN_NAME_TITLE)));
		event.setDescription(cursor.getString(cursor.getColumnIndex(EventTable.COLUMN_NAME_DESCRIPTION)));
		event.setConstant(cursor.getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_IS_CONSTANT)) == 1 ? true : false);
		event.setRequired(cursor.getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_IS_REQUIRED)) == 1 ? true : false);
		event.setAccount(accountManagementService.getByIdAllData(cursor.getLong(cursor.getColumnIndex(EventTable.COLUMN_NAME_ACCOUNT_ID))));
		event.setEventDates(eventDateManagementService.getAllEventDatesForEventId(event.getId()));
		event.setPredecessorEvent(null);
		return event;
	}

}
