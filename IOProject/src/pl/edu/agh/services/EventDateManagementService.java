package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.DatabaseProperties;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.EventDateTable;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.interfaces.IEntityValidation;
import pl.edu.agh.tools.BooleanTools;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.eventdescription.EventDescriptionActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ioproject.R;

public class EventDateManagementService implements IDatabaseDmlProvider<EventDate>, IEntityValidation<EventDate> {

	private SQLiteOpenHelper dbHelper;
	private LocationManagementService locationManagementService;

	public EventDateManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.locationManagementService = new LocationManagementService(dbHelper);
	}

	@Override
	public List<FormValidationError> validate(EventDate entity) {
		List<FormValidationError> errors = new ArrayList<FormValidationError>();
		if (entity.getEvent() == null) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_Event_NotNull));
		}
		if(entity.getLocation() != null) {
			errors.addAll(locationManagementService.validate(entity.getLocation()));
		}
		if (entity.getDate() == null) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_Date_NotNull));
		}
		if (entity.getStartTime() == null) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_StartTime_NotNull));
		}
		if (entity.getEndTime() == null) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_EndTime_NotNull));
		}
		if (entity.getDuration() == null) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_Duration_NotNull));
		}
		if(entity.getStartTime() != null && entity.getEndTime() != null && entity.getStartTime().compareTo(entity.getEndTime()) > 0) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_EndTimeBeforeStartTime));
		}
		Calendar yesterday = new GregorianCalendar();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		if(entity.getDate() != null && entity.getDate().compareTo(yesterday.getTime()) <= 0) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_PastDate));
		}
		if (entity.isFinished() == null) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_IsFinished_NotNull));
		}
		
		return errors;
	}

	public List<FormValidationError> validateCollisions(EventDate entity, List<Event> events) {
		List<FormValidationError> errors = new ArrayList<FormValidationError>();
		if (entity.getEvent().isDraft() || !entity.getEvent().isRequired() || !entity.getEvent().isConstant())
			return errors;
		
		List<Event> deleteList = new ArrayList<Event>();
		
		for(Event event : events) {
			if(event.isDraft() || !event.isRequired() || !event.isConstant()) {
				deleteList.add(event);
			}
		}
		events.removeAll(deleteList);
		
		for(Event event : events) {
			for(EventDate eventDate : event.getEventDates()) {
				if (areIntersected(entity, eventDate)) {
					errors.add(new FormValidationError(R.string.Validation_EventDate_Collision_Found));
					return errors;
				}
			}
		}
		
		return errors;
	}

	public long insert(EventDate insertObject) {
		ContentValues values = new ContentValues();
		values.put(EventDateTable.COLUMN_NAME_EVENT_ID, insertObject.getEvent().getId());
		if(insertObject.getLocation() != null) {
			if(insertObject.getLocation().getId() == DatabaseProperties.UNSAVED_ENTITY_ID) {
				locationManagementService.insert(insertObject.getLocation());
			}
			values.put(EventDateTable.COLUMN_NAME_LOCATION_ID, insertObject.getLocation().getId());
		}
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
			String[] selectionArguments = new String[] { String.valueOf(event
					.getId()) };
			cursor = dbHelper.getWritableDatabase().query(
					EventDateTable.TABLE_NAME, null, selection,
					selectionArguments, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				EventDate eventDate = new EventDate();
				eventDate.setId(cursor.getLong(cursor
						.getColumnIndex(EventDateTable._ID)));
				eventDate
						.setDate(DateTimeTools.convertStringToDate(cursor.getString(cursor
								.getColumnIndex(EventDateTable.COLUMN_NAME_DATE))));
				eventDate
						.setStartTime(DateTimeTools.convertStringToTime(cursor.getString(cursor
								.getColumnIndex(EventDateTable.COLUMN_NAME_START_TIME))));
				eventDate
						.setEndTime(DateTimeTools.convertStringToTime(cursor.getString(cursor
								.getColumnIndex(EventDateTable.COLUMN_NAME_END_TIME))));
				eventDate.setDuration(cursor.getInt(cursor
						.getColumnIndex(EventDateTable.COLUMN_NAME_DURATION)));
				eventDate
						.setFinished(BooleanTools.convertIntToBoolean(cursor.getInt(cursor
								.getColumnIndex(EventDateTable.COLUMN_NAME_FINISHED))));
				eventDate
						.setLocation(locationManagementService.getByIdAllData(cursor.getLong(cursor
								.getColumnIndex(EventDateTable.COLUMN_NAME_LOCATION_ID))));
				eventDate.setEvent(event);
				resultSet.add(eventDate);
				cursor.moveToNext();
			}
			return resultSet;
		} finally {
			cursor.close();
		}
	}
	
	public Set<EventDate> getAllEventDatesForChosenDate() {
		return null;
		
	}

	@Override
	public EventDate getByIdAllData(long id) {
		return null;
	}
	
	public void changeFinishedEventDateState(EventDate eventDate, boolean isFinished) {
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(EventDateTable.COLUMN_NAME_FINISHED, isFinished);
		
		String selection = EventDateTable._ID + " = ? ";
		String[] selectionArgs = { Long.toString(eventDate.getId()) };
		
		dbHelper.getWritableDatabase().update(EventDateTable.TABLE_NAME, updatedValues, selection, selectionArgs);
		
		eventDate.setFinished(isFinished);
	}

	@Override
	public List<EventDate> getAll() {
		return null;
	}
	
	public void deleteEventDateById(long id) {
		String selection = EventDateTable._ID + " = ?";
		String[] selectionArgs = { Long.toString(id) };
		dbHelper.getWritableDatabase().delete(EventDateTable.TABLE_NAME, selection, selectionArgs);
	}
	
	public void deleteAllEventDatesForEvent(Event event) {
		String selection = EventDateTable.COLUMN_NAME_EVENT_ID + " = ?";
		String[] selectionArgs = { Long.toString(event.getId()) };
		dbHelper.getWritableDatabase().delete(EventDateTable.TABLE_NAME, selection, selectionArgs);
	}

	public boolean updateEventDateStartEndTime(EventDate eventDate, Date startTime, Date endTime) {
		if(eventDate == null || startTime == null || endTime == null) {
			return false;
		}
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(EventDateTable.COLUMN_NAME_START_TIME, DateTimeTools.convertTimeToString(startTime));
		updatedValues.put(EventDateTable.COLUMN_NAME_END_TIME, DateTimeTools.convertTimeToString(endTime));
		String selection = EventDateTable._ID + " = ?";
		String[] selectionArguments = new String[] { Long.valueOf(eventDate.getId()).toString() };
		dbHelper.getWritableDatabase().update(EventDateTable.TABLE_NAME, updatedValues, selection, selectionArguments);
		eventDate.setStartTime(startTime);
		eventDate.setEndTime(endTime);
		return true;
	}
	
	public boolean updateEventDateStartEndTime(EventDate eventDate) {
		if(eventDate == null || eventDate.getStartTime() == null || eventDate.getEndTime() == null) {
			return false;
		}
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(EventDateTable.COLUMN_NAME_START_TIME, DateTimeTools.convertTimeToString(eventDate.getStartTime()));
		updatedValues.put(EventDateTable.COLUMN_NAME_END_TIME, DateTimeTools.convertTimeToString(eventDate.getEndTime()));
		String selection = EventDateTable._ID + " = ?";
		String[] selectionArguments = new String[] { Long.valueOf(eventDate.getId()).toString() };
		dbHelper.getWritableDatabase().update(EventDateTable.TABLE_NAME, updatedValues, selection, selectionArguments);
		return true;
	}
	
	public boolean areIntersected(EventDate eventDate1, EventDate eventDate2) {
		Date startTime1 = DateTimeTools.getCalendarFromDates(eventDate1.getDate(), eventDate1.getStartTime()).getTime();
		Date endTime1 = DateTimeTools.getCalendarFromDates(eventDate1.getDate(), eventDate1.getEndTime()).getTime();
		Date startTime2 = DateTimeTools.getCalendarFromDates(eventDate2.getDate(), eventDate2.getStartTime()).getTime();
		Date endTime2 = DateTimeTools.getCalendarFromDates(eventDate2.getDate(), eventDate2.getEndTime()).getTime();
		
		if (DateTimeTools.isBetween(startTime1, startTime2, endTime2)
			|| DateTimeTools.isBetween(endTime1, startTime2, endTime2)
			|| DateTimeTools.isBetween(startTime2, startTime1, endTime1)
			|| DateTimeTools.isBetween(endTime2, startTime1, endTime1))
			return true;
		
		return false;
	}
}
