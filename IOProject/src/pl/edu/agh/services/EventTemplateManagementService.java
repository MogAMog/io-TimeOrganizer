package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.EventTemplate;
import pl.edu.agh.domain.tables.EventTable;
import pl.edu.agh.domain.tables.EventTemplateTable;
import pl.edu.agh.tools.BooleanTools;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.addevent.EventFrequencyFold.Frequency;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class EventTemplateManagementService {

	private SQLiteOpenHelper dbHelper;
	
	public EventTemplateManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public long insert(EventTemplate eventTemplate) { 
		ContentValues values = new ContentValues();
		values.put(EventTemplateTable.COLUMN_NAME_TEMPLATENAME, eventTemplate.getTemplateName());
		values.put(EventTemplateTable.COLUMN_NAME_TITLE, eventTemplate.getTitle());
		values.put(EventTemplateTable.COLUMN_NAME_DESCRIPTION, eventTemplate.getDescription());
		values.put(EventTemplateTable.COLUMN_NAME_REQUIRED, BooleanTools.convertBooleanToInt(eventTemplate.isRequired()));
		values.put(EventTemplateTable.COLUMN_NAME_CONSTANT, BooleanTools.convertBooleanToInt(eventTemplate.isConstant()));
		values.put(EventTemplateTable.COLUMN_NAME_DRAFT, BooleanTools.convertBooleanToInt(eventTemplate.isDraft()));
		values.put(EventTemplateTable.COLUMN_NAME_FIXED_TIME, BooleanTools.convertBooleanToInt(eventTemplate.isFixedTime()));
		values.put(EventTemplateTable.COLUMN_NAME_DURATION, eventTemplate.getDuration());
		values.put(EventTemplateTable.COLUMN_NAME_START_DATE, eventTemplate.getStartDate() != null ? DateTimeTools.convertDateToString(eventTemplate.getStartDate()) : null);
		values.put(EventTemplateTable.COLUMN_NAME_END_DATE, eventTemplate.getEndDate() != null ? DateTimeTools.convertDateToString(eventTemplate.getEndDate()) : null);
		values.put(EventTemplateTable.COLUMN_NAME_START_TIME, eventTemplate.getStartTime() != null ? DateTimeTools.convertTimeToString(eventTemplate.getStartTime()) : null);
		values.put(EventTemplateTable.COLUMN_NAME_END_TIME, eventTemplate.getEndTime() != null ? DateTimeTools.convertTimeToString(eventTemplate.getEndTime()) : null);
		values.put(EventTemplateTable.COLUMN_NAME_FREQUENCY, eventTemplate.getFrequency() != null ? eventTemplate.getFrequency().name() : null);
		values.put(EventTemplateTable.COLUMN_NAME_MONDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isMondaySelected()));
		values.put(EventTemplateTable.COLUMN_NAME_TUESDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isTuesdaySelected()));
		values.put(EventTemplateTable.COLUMN_NAME_WEDNESDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isWednesdaySelected()));
		values.put(EventTemplateTable.COLUMN_NAME_THURSDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isThursdaySelected()));
		values.put(EventTemplateTable.COLUMN_NAME_FRIDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isFridaySelected()));
		values.put(EventTemplateTable.COLUMN_NAME_SATURDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isSaturdaySelected()));
		values.put(EventTemplateTable.COLUMN_NAME_SUNDAY_SELECTED, BooleanTools.convertBooleanToInt(eventTemplate.isSundaySelected()));
		long id = dbHelper.getWritableDatabase().insert(EventTemplateTable.TABLE_NAME, null, values);
		eventTemplate.setId(id);
		return id;
	}
	
	public List<String> getSimpleEventNames() {
		Cursor cursor = null;
		try { 
			String selection = EventTemplateTable.COLUMN_NAME_CONSTANT + " = ?";
			String[] selectionArgument = new String[] { String.valueOf(BooleanTools.convertBooleanToInt(false)) };
			cursor = dbHelper.getReadableDatabase().query(EventTemplateTable.TABLE_NAME, new String[] { EventTemplateTable.COLUMN_NAME_TEMPLATENAME }, selection, selectionArgument, null, null, null);
			List<String> names = new ArrayList<String>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				names.add(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_TEMPLATENAME)));
				cursor.moveToNext();
			}
			return names;
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
	}
	
	public List<String> getContantEventNames() {
		Cursor cursor = null;
		try { 
			String selection = EventTemplateTable.COLUMN_NAME_CONSTANT + " = ?";
			String[] selectionArgument = new String[] { String.valueOf(BooleanTools.convertBooleanToInt(true)) };
			cursor = dbHelper.getReadableDatabase().query(EventTemplateTable.TABLE_NAME, new String[] { EventTemplateTable.COLUMN_NAME_TEMPLATENAME }, selection, selectionArgument, null, null, null);
			List<String> names = new ArrayList<String>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				names.add(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_TEMPLATENAME)));
				cursor.moveToNext();
			}
			return names;
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
	}
	
	public EventTemplate getEventTemplateByName(String name) {
		Cursor cursor = null;
		try { 
			String selection = EventTemplateTable.COLUMN_NAME_TEMPLATENAME + " = ?";
			String[] selectionArgument = new String[] { name };
			cursor = dbHelper.getReadableDatabase().query(EventTemplateTable.TABLE_NAME, null, selection, selectionArgument, null, null, null);
			cursor.moveToFirst();
			return getEventTemplateFromCursor(cursor);
		} finally {
			cursor.close();
		}
	}
	
	private EventTemplate getEventTemplateFromCursor(Cursor cursor) {
		EventTemplate template = new EventTemplate();
		template.setTemplateName(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_TEMPLATENAME)));
		template.setTitle(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_TITLE)));
		template.setDescription(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_DESCRIPTION)));
		template.setRequired(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_REQUIRED))));
		template.setConstant(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_CONSTANT))));
		template.setDraft(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_DRAFT))));
		template.setFixedTime(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_FIXED_TIME))));
		template.setDuration(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_DURATION)));
		
		template.setStartDate(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_START_DATE)) != null ? DateTimeTools.convertStringToDate(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_START_DATE))) : null);
		template.setEndDate(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_END_DATE)) != null ? DateTimeTools.convertStringToDate(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_END_DATE))) : null);
		template.setStartTime(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_START_TIME)) != null ? DateTimeTools.convertStringToTime(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_START_TIME))) : null);
		template.setEndTime(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_END_TIME)) != null ? DateTimeTools.convertStringToTime(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_END_TIME))) : null);
		template.setFrequency(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_FREQUENCY)) != null ? Frequency.valueOf(cursor.getString(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_FREQUENCY))) : null);
		
		template.setMondaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_MONDAY_SELECTED))));
		template.setTuesdaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_TUESDAY_SELECTED))));
		template.setWednesdaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_WEDNESDAY_SELECTED))));
		template.setThursdaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_THURSDAY_SELECTED))));
		template.setFridaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_FRIDAY_SELECTED))));
		template.setSaturdaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_SATURDAY_SELECTED))));
		template.setSundaySelected(BooleanTools.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(EventTemplateTable.COLUMN_NAME_SUNDAY_SELECTED))));
		return template;
	}
}
