package pl.edu.agh.view.addevent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.EventTemplate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.AccountManagementService;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.services.EventTemplateManagementService;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.tools.StringTools;
import pl.edu.agh.view.addevent.EventFrequencyFold.Frequency;
import pl.edu.agh.view.addevent.TemplateChooseFold.SetEventAsTemplate;
import pl.edu.agh.view.fragments.dialogs.ErrorDialog;
import pl.edu.agh.view.fragments.pickers.EndDatePickerFragment;
import pl.edu.agh.view.fragments.pickers.EndTimePickerFragment;
import pl.edu.agh.view.fragments.pickers.SetDatePeriodInterface;
import pl.edu.agh.view.fragments.pickers.SetTimePeriodInterface;
import pl.edu.agh.view.fragments.pickers.StartDatePickerFragment;
import pl.edu.agh.view.fragments.pickers.StartTimePickerFragment;
import pl.edu.agh.view.onetimelocalization.OneTimeLocalizationActivity;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.ioproject.R;


public class ConstantEventAddActivity extends Activity implements SetDatePeriodInterface, SetTimePeriodInterface, SetEventAsTemplate {
	
	private static final int ONE_TIME_LOCATION_ACTIVITY_ID = 1;
	
	private Event event;
	private EventManagementService eventManagementService;
	private EventTemplateManagementService eventTemplateManagementService;
	
	private EventTimeAndDescriptionFold eventTimeAndDescriptionFold;
	private EventLocalizationFold eventLocalizationFold;
	private EventFrequencyFold eventFrequencyFold;
	private TemplateChooseFold templateChooseFold;
	
	private DialogFragment startTimePickerFragment;
	private DialogFragment endTimePickerFragment;
	private DialogFragment startDatePickerFragment;
	private DialogFragment endDatePickerFragment;
	
	private Calendar startDate;
	private Calendar endDate;
	private Calendar startTime;
	private Calendar endTime;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constant_event_add);
		setupActionBar();
		
		event = new Event();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		eventTemplateManagementService = new EventTemplateManagementService(new MainDatabaseHelper(this));
		
		eventTimeAndDescriptionFold = new EventTimeAndDescriptionFold(this, event, R.id.ConstantEventAdd_eventTitle, R.id.ConstantEventAdd_eventDescription);
		eventLocalizationFold = new EventLocalizationFold(this, R.id.LocationChoiceFold_DefaultLocationList_Id, R.id.LocationChoiceFold_OneTimeLocation_Button_Id, R.id.LocationChoiceFold_OneTimeLocation_ImageView_Id);
		eventFrequencyFold = new EventFrequencyFold(this, R.id.ConstantEventAdd_spinner, new int[] {R.id.checkBoxSUNDAY, R.id.checkBoxMONDAY, 
				R.id.checkBoxTUESDAY, R.id.checkBoxWEDNESDAY, R.id.checkBoxTHURSDAY, R.id.checkBoxFRIDAY, R.id.checkBoxSATURDAY});
		templateChooseFold = new TemplateChooseFold(this, R.id.TemplateChooseFold_SpinnerList_Id, true);
		
		eventTimeAndDescriptionFold.initializeListeners();
		
		startTimePickerFragment = new StartTimePickerFragment();
		endTimePickerFragment = new EndTimePickerFragment();
		startDatePickerFragment = new StartDatePickerFragment();
		endDatePickerFragment = new EndDatePickerFragment();
		
		((CheckBox) findViewById(R.id.ConstantEventAdd_checkBoxRequired)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		eventLocalizationFold.reinitializeSpinnerList(R.id.LocationChoiceFold_DefaultLocationList_Id);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.constant_event_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.AddConstantEvent_ActionBar_AddAsTemplate:
			return saveEventAsTemplate();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setStartDate(int year, int month, int day) {
		startDate = DateTimeTools.getCalendarInstanceWithDate(year, month, day);	
		((TextView) findViewById(R.id.ConstantEventAdd_textStartDate)).setText(getDateDescription("Start date", startDate));
	}

	@Override
	public void setEndDate(int year, int month, int day) {
		endDate = DateTimeTools.getCalendarInstanceWithDate(year, month, day);
		((TextView) findViewById(R.id.ConstantEventAdd_textEndDate)).setText(getDateDescription("End date", endDate));
	}
	
	@Override
	public void setStartTime(int hour, int minute) {
		startTime = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		((TextView) findViewById(R.id.ConstantEventAdd_textStartTime)).setText(getTimeDescription("Start time", startTime));		
	}

	@Override
	public void setEndTime(int hour, int minute) {
		endTime = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		((TextView) findViewById(R.id.ConstantEventAdd_textEndTime)).setText(getTimeDescription("End time", endTime));
	}

	private String getDateDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertDateToString(calendar)).toString();
	}
	
	
	private String getTimeDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertTimeToString(calendar)).toString();
	}
	
	public void showStartDatePickerDialog(View v) {
		startDatePickerFragment.show(getFragmentManager(), "startDatePicker");
	}
	
	public void showEndDatePickerDialog(View v) {
		endDatePickerFragment.show(getFragmentManager(), "endDatePicker");
	}
	
	public void showStartTimePickerDialog(View v) {
		startTimePickerFragment.show(getFragmentManager(), "startTimePicker");
	}
	
	public void showEndTimePickerDialog(View v) {
		endTimePickerFragment.show(getFragmentManager(), "endTimePicker");
	}
	
	public void calculateEventDates() {
		Location location = eventLocalizationFold.getLocationForEvent();
		Calendar currentDay = (Calendar) startDate.clone();
		Frequency frequency = eventFrequencyFold.getChosenFrequency();
		while(!currentDay.getTime().after(endDate.getTime())) {
			if(eventFrequencyFold.isWeekdayChecked(currentDay.get(Calendar.DAY_OF_WEEK))) {
				event.addEventDate(new EventDate(location, currentDay.getTime(), startTime.getTime(), endTime.getTime(), DateTimeTools.getMinuteDifferenceBetweenTwoDates(startTime.getTime(), endTime.getTime()), false));
			}
			if(currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				currentDay.add(Calendar.DATE, frequency.getDaysToAdd());
			} else {
				currentDay.add(Calendar.DATE, 1);
			}
		}
	}
	
	public void addNewConstantEventAction(View view) {
		event.setAccount(AccountManagementService.DEFAULT_ACCOUNT);
		event.setPredecessorEvent(null);
		event.setConstant(true);
		
		List<FormValidationError> errors = new ArrayList<FormValidationError>();
		if(!eventFrequencyFold.isFrequencyChosen()) {
			errors.add(new FormValidationError(R.string.Validation_Event_FrequencyNotChosen));
		}
		if(!eventFrequencyFold.isAtLeastOneWeekDayIsSelected()) {
			errors.add(new FormValidationError(R.string.Validation_Event_OneDayMustBeChosen));	
		}
		if(!eventLocalizationFold.isLocationChosen()) {
			errors.add(new FormValidationError(R.string.Validation_EventDate_Location_NotNull));
		}
		
		if(startDate == null)
			errors.add(new FormValidationError(R.string.Validation_StartDate_NotNull));
		if(endDate == null)
			errors.add(new FormValidationError(R.string.Validation_EndDate_NotNull));
		if(startDate != null && endDate != null && startDate.compareTo(endDate) > 0) 
			errors.add(new FormValidationError(R.string.Validation_EndDateBeforeStartDate));
		
		if(startTime == null)
			errors.add(new FormValidationError(R.string.Validation_EventDate_StartTime_NotNull));
		if(endTime == null)
			errors.add(new FormValidationError(R.string.Validation_EventDate_EndTime_NotNull));
		if(startTime != null && endTime != null && startTime.compareTo(endTime) > 0)
			errors.add(new FormValidationError(R.string.Validation_EventDate_EndTimeBeforeStartTime));
		
		errors.clear();
		errors.addAll(eventManagementService.validate(event));
		if(errors.isEmpty()) {
			calculateEventDates();
			errors.addAll(eventManagementService.validate(event));
		}
		
		if(!errors.isEmpty()) {
			ErrorDialog.createDialog(this, errors).show();
			event.getEventDates().clear();
		} else {
			//calculateEventDates();
			eventManagementService.insert(event);
			event.getEventDates().clear();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case(ONE_TIME_LOCATION_ACTIVITY_ID):
				if(resultCode == RESULT_OK) {
					eventLocalizationFold.setOneTimeLocation((Location)data.getSerializableExtra(OneTimeLocalizationActivity.LOCATION_RESULT_KEY));
					eventLocalizationFold.setLocalizationSelectionWasMade();
					break;
				}
			default: super.onActivityResult(requestCode, resultCode, data);		
		}
	}
	
	private boolean saveEventAsTemplate() {
		String templateName = StringTools.isNullOrEmpty(event.getTitle()) ? (Integer.toString(Math.abs(new Random().nextInt()))) : event.getTitle();
		if(templateChooseFold.containsKey(templateName)) {
			ErrorDialog.createDialog(this, new ArrayList<FormValidationError>() {{ add(new FormValidationError(R.string.TemplateChooseFold_Error_NameDuplication)); }} ).show();
			return true;
		} else {
			EventTemplate eventTemplate = new EventTemplate();
			eventTemplate.setTemplateName(templateName);
			eventTemplate.setConstant(true);
			eventTemplate.setTitle(event.getTitle());
			eventTemplate.setDescription(event.getDescription());
			eventTemplate.setRequired(event.isRequired());
			if(startDate != null) {
				eventTemplate.setStartDate(startDate.getTime());
			}
			if(endDate != null) {
				eventTemplate.setEndDate(endDate.getTime());
			}
			if(startTime != null) {
				eventTemplate.setStartTime(startTime.getTime());
			}
			if(endTime != null) {
				eventTemplate.setEndTime(endTime.getTime());
			}
			eventTemplate.setFrequency(eventFrequencyFold.getChosenFrequency());
			eventTemplate.setMondaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.MONDAY));
			eventTemplate.setTuesdaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.TUESDAY));
			eventTemplate.setWednesdaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.WEDNESDAY));
			eventTemplate.setThursdaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.THURSDAY));
			eventTemplate.setFridaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.FRIDAY));
			eventTemplate.setSaturdaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.SATURDAY));
			eventTemplate.setSundaySelected(eventFrequencyFold.isWeekdayChecked(Calendar.SUNDAY));
			eventTemplateManagementService.insert(eventTemplate);
			finish();
			return true;
		}
	}

	@Override
	public void setFieldsFromTemplate(EventTemplate eventTemplate) {
		((CheckBox) findViewById(R.id.ConstantEventAdd_checkBoxRequired)).setChecked(eventTemplate.isRequired());
		((EditText) findViewById(R.id.ConstantEventAdd_eventTitle)).setText(eventTemplate.getTitle());
		((EditText) findViewById(R.id.ConstantEventAdd_eventDescription)).setText(eventTemplate.getDescription());
		if(eventTemplate.getStartDate() != null) {
			Calendar calendar = Calendar.getInstance(new Locale("PL"));
			calendar.setTime(eventTemplate.getStartDate());
			setStartDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		}
		if(eventTemplate.getEndDate() != null) {
			Calendar calendar = Calendar.getInstance(new Locale("PL"));
			calendar.setTime(eventTemplate.getEndDate());
			setEndDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		}
		if(eventTemplate.getStartTime() != null) {
			setStartTime(eventTemplate.getStartTime().getHours(), eventTemplate.getStartTime().getMinutes());
		}
		if(eventTemplate.getEndTime() != null) {
			setEndTime(eventTemplate.getEndTime().getHours(), eventTemplate.getEndTime().getMinutes());
		}
		eventFrequencyFold.setFrequency(eventTemplate.getFrequency());
		eventFrequencyFold.setWeekdayChecked(Calendar.SUNDAY, eventTemplate.isSundaySelected());
		eventFrequencyFold.setWeekdayChecked(Calendar.MONDAY, eventTemplate.isMondaySelected());
		eventFrequencyFold.setWeekdayChecked(Calendar.TUESDAY, eventTemplate.isTuesdaySelected());
		eventFrequencyFold.setWeekdayChecked(Calendar.WEDNESDAY, eventTemplate.isWednesdaySelected());
		eventFrequencyFold.setWeekdayChecked(Calendar.THURSDAY, eventTemplate.isThursdaySelected());
		eventFrequencyFold.setWeekdayChecked(Calendar.FRIDAY, eventTemplate.isFridaySelected());
		eventFrequencyFold.setWeekdayChecked(Calendar.SATURDAY, eventTemplate.isSaturdaySelected());
	}

	@Override
	public void clearAllFields() {
		((CheckBox) findViewById(R.id.ConstantEventAdd_checkBoxRequired)).setChecked(false);
		((EditText) findViewById(R.id.ConstantEventAdd_eventTitle)).setText(null);
		((EditText) findViewById(R.id.ConstantEventAdd_eventDescription)).setText(null);
		startDate = null;
		endDate = null;
		startTime = null;
		endTime = null;
		((TextView) findViewById(R.id.ConstantEventAdd_textStartDate)).setText(R.string.EventDateFold_StartDate_Label_NoSet);
		((TextView) findViewById(R.id.ConstantEventAdd_textEndDate)).setText(R.string.EventDateFold_StartDate_Label_NoSet);
		((TextView) findViewById(R.id.ConstantEventAdd_textStartTime)).setText(R.string.EventTimeFold_StartTime_Label_NoSet);
		((TextView) findViewById(R.id.ConstantEventAdd_textEndTime)).setText(R.string.EventTimeFold_EndTime_Label_NoSet);
		eventFrequencyFold.setFrequency(null);
		eventFrequencyFold.setWeekdayChecked(Calendar.SUNDAY, false);
		eventFrequencyFold.setWeekdayChecked(Calendar.MONDAY, false);
		eventFrequencyFold.setWeekdayChecked(Calendar.TUESDAY, false);
		eventFrequencyFold.setWeekdayChecked(Calendar.WEDNESDAY, false);
		eventFrequencyFold.setWeekdayChecked(Calendar.THURSDAY, false);
		eventFrequencyFold.setWeekdayChecked(Calendar.FRIDAY, false);
		eventFrequencyFold.setWeekdayChecked(Calendar.SATURDAY, false);
	}
}