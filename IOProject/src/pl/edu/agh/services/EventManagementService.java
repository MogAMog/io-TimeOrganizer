package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.List;

import com.example.ioproject.R;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.DatabaseProperties;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.EventTable;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.interfaces.IEntityValidation;
import pl.edu.agh.tools.BooleanTools;
import pl.edu.agh.tools.StringTools;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class EventManagementService implements IDatabaseDmlProvider<Event>, IEntityValidation<Event> {

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
	public List<FormValidationError> validate(Event entity) {
		List<FormValidationError> errors = new ArrayList<FormValidationError>();
		if(entity.getAccount() == null) {
			errors.add(new FormValidationError(R.string.Validation_Event_Account_NotNull));
		}
		if(StringTools.isNullOrEmpty(entity.getTitle())) {
			errors.add(new FormValidationError(R.string.Validation_Event_Title_NotNull));
		}
		if(entity.getDefaultLocation() != null) {
			errors.addAll(locationManagementService.validate(entity.getDefaultLocation()));
		}
//		if(entity.isRequired() == null) {
//			errors.addAll(new FormValidationError("R.string.Validation_Event_IsRequired_NotNull"));
//		}
//		if(entity.isConstant() == null) {
//			errors.addAll(new FormValidationError("R.string.Validation_Event_IsConstant_NotNull"));
//		}
		for(EventDate eventDate : entity.getEventDates()) {
			errors.addAll(eventDateManagementService.validate(eventDate));
		}
		return errors;
	}



	@Override
	public long insert(Event insertObject) {
		ContentValues values = new ContentValues();
		values.put(EventTable.COLUMN_NAME_TITLE, insertObject.getTitle());
		values.put(EventTable.COLUMN_NAME_DESCRIPTION, insertObject.getDescription());
		values.put(EventTable.COLUMN_NAME_IS_CONSTANT, BooleanTools.convertBooleanToInt(insertObject.isConstant()));
		values.put(EventTable.COLUMN_NAME_IS_REQUIRED, BooleanTools.convertBooleanToInt(insertObject.isRequired()));
		if(insertObject.getPredecessorEvent() != null) {
			if(insertObject.getPredecessorEvent().getId() == DatabaseProperties.UNSAVED_ENTITY_ID) {
				insert(insertObject.getPredecessorEvent());
			}
			values.put(EventTable.COLUMN_NAME_PREDECESSOR_EVENT_ID, insertObject.getPredecessorEvent().getId());
		}
		if(insertObject.getAccount() != null) {
			if(insertObject.getAccount().getId() == DatabaseProperties.UNSAVED_ENTITY_ID) {
				accountManagementService.insert(insertObject.getAccount());
			}
			values.put(EventTable.COLUMN_NAME_ACCOUNT_ID, insertObject.getAccount().getId());
		}
		if(insertObject.getDefaultLocation() != null) {
			if(insertObject.getDefaultLocation().getId() == DatabaseProperties.UNSAVED_ENTITY_ID) {
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
		event.setConstant(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_IS_CONSTANT))));
		event.setRequired(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_IS_REQUIRED))));
		event.setAccount(accountManagementService.getByIdAllData(cursor.getLong(cursor.getColumnIndex(EventTable.COLUMN_NAME_ACCOUNT_ID))));
		event.setEventDates(eventDateManagementService.getAllEventDatesForEventId(event));
		event.setPredecessorEvent(null);
		return event;
	}

}
