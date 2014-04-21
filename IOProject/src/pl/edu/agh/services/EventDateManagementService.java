package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ioproject.R;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.DatabaseProperties;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.EventDateTable;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.interfaces.IEntityValidation;
import pl.edu.agh.tools.BooleanTools;
import pl.edu.agh.tools.DateTimeTools;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

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

	@Override
	public EventDate getByIdAllData(long id) {
		return null;
	}

	@Override
	public List<EventDate> getAll() {
		return null;
	}

}
