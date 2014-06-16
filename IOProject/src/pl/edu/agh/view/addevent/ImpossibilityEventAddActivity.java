package pl.edu.agh.view.addevent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.edu.agh.domain.Account;
import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.AccountManagementService;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.services.LocationManagementService;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.addevent.EventFrequencyFold.Frequency;
import pl.edu.agh.view.fragments.dialogs.ErrorDialog;
import pl.edu.agh.view.fragments.pickers.DatePickerFragment;
import pl.edu.agh.view.fragments.pickers.DatePickerFragment.SetDateInterface;
import pl.edu.agh.view.fragments.pickers.EndDatePickerFragment;
import pl.edu.agh.view.fragments.pickers.EndTimePickerFragment;
import pl.edu.agh.view.fragments.pickers.SetDatePeriodInterface;
import pl.edu.agh.view.fragments.pickers.SetTimePeriodInterface;
import pl.edu.agh.view.fragments.pickers.StartDatePickerFragment;
import pl.edu.agh.view.fragments.pickers.StartTimePickerFragment;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ioproject.R;

public class ImpossibilityEventAddActivity extends Activity 
		implements SetDateInterface, SetTimePeriodInterface, SetDatePeriodInterface {
	
	private Event event;
	private EventManagementService eventManagementService;
	private EventDate oneTimeEventDate;
	
	//private EventTimeAndDescriptionFold eventTimeAndDescriptionFold;
	
	private DialogFragment startDatePickerFragment;
	private DialogFragment endDatePickerFragment;
	private DialogFragment datePickerFragment;
	private DialogFragment startTimePickerFragment;
	private DialogFragment endTimePickerFragment;
	
	private Button startDateButton;
	private Button endDateButton;
	private Button oneDateButton;
	private Button startTimeButton;
	private Button endTimeButton;
	
	private TextView startDateTextView;
	private TextView endDateTextView;
	private TextView oneDateTextView;
	
	private RadioButton constantEventRadioButton;
	private RadioButton oneTimeEventRadioButton;
	
	private Calendar startDate;
	private Calendar endDate;
	private Calendar oneDate;
	private Calendar startTime;
	private Calendar endTime;
	
	private EventFrequencyFold eventFrequencyFold;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impossibility_event_add);
		setupActionBar();
		
		oneTimeEventDate =  new EventDate();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		startDatePickerFragment = new StartDatePickerFragment();
		endDatePickerFragment = new EndDatePickerFragment();
		datePickerFragment = new DatePickerFragment();
		startTimePickerFragment = new StartTimePickerFragment();
		endTimePickerFragment = new EndTimePickerFragment();
		
		startDateButton = (Button) findViewById(R.id.ImpossibilityEventAdd_buttonStartDate);
		endDateButton = (Button) findViewById(R.id.ImpossibilityEventAdd_buttonEndDate);
		oneDateButton = (Button) findViewById(R.id.ImpossibilityEventAdd_buttonDate);
		startTimeButton = (Button) findViewById(R.id.ImpossibilityEventAdd_buttonStartTime);
		
		startDateTextView = (TextView) findViewById(R.id.ImpossibilityEventAdd_textStartDate);
		endDateTextView = (TextView) findViewById(R.id.ImpossibilityEventAdd_textEndDate);
		oneDateTextView = (TextView) findViewById(R.id.ImpossibilityEventAdd_textDate);
		
		constantEventRadioButton = (RadioButton) findViewById(R.id.ImpossibilityEventTypeFold_Constant_Id);
		oneTimeEventRadioButton = (RadioButton) findViewById(R.id.ImpossibilityEventTypeFold_OneTime_Id);
		
		
		eventFrequencyFold = new EventFrequencyFold(this, R.id.ImpossibilityEventAdd_spinner, new int[] {
				R.id.ImpossibilityEvent_checkBoxSUNDAY, R.id.ImpossibilityEvent_checkBoxMONDAY, 
				R.id.ImpossibilityEvent_checkBoxTUESDAY, R.id.ImpossibilityEvent_checkBoxWENDESDAY, 
				R.id.ImpossibilityEvent_checkBoxTHURSDAY, R.id.ImpossibilityEvent_checkBoxFRIDAY, 
				R.id.ImpossibilityEvent_checkBoxSATURDAY
		});
		
		
		constantEventRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					enableStartEndDateButtons();
					disableDateButton();
					oneTimeEventRadioButton.setChecked(false);
					eventFrequencyFold.enableAllCheckBoxes();
					enableFrequencySpinner();
				}
			}
		});
		
		oneTimeEventRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					disableStartEndDateButton();
					enableDateButton();
					constantEventRadioButton.setChecked(false);
					eventFrequencyFold.disableAllCheckBoxes();
					disableFrequencySpinner();
				}
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.impossibility_event_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addNewImpossibilityEventAction(View view) {
		List<FormValidationError> errors = new ArrayList<FormValidationError>();
		Account account = AccountManagementService.DEFAULT_ACCOUNT;
		
		
		if(((RadioButton) findViewById(R.id.ImpossibilityEventTypeFold_OneTime_Id)).isChecked()) {
			
			event = new Event();
			event.setAccount(account);
			event.setPredecessorEvent(null);
			event.setTitle("Impossibility");
			
			
			if(oneDate != null)
				oneTimeEventDate.setDate(oneDate.getTime());
			
			if(startTime != null)
				oneTimeEventDate.setStartTime(startTime.getTime());
			
			if(endTime != null)
				oneTimeEventDate.setEndTime(endTime.getTime());
			
			oneTimeEventDate.setFinished(false);
			oneTimeEventDate.setLocation(LocationManagementService.DEAFULT_LOCATION);
			
			if(startTime != null && endTime != null) {
				oneTimeEventDate.setDuration(DateTimeTools.getMinuteDifferenceBetweenTwoDates(startTime.getTime(), endTime.getTime()));
			}
			else {
				oneTimeEventDate.setDuration(0);
			}

			event.addEventDate(oneTimeEventDate);	
			
			errors.addAll(eventManagementService.validate(event));
			
			if(!errors.isEmpty()) {
				ErrorDialog.createDialog(this, errors).show();
			} else {
				eventManagementService.insert(event);
				finish();
			}
			
		}
		
		if(((RadioButton) findViewById(R.id.ImpossibilityEventTypeFold_Constant_Id)).isChecked()) {
			
			event = new Event();
			event.setAccount(account);
			event.setPredecessorEvent(null);
			event.setTitle("Impossibility");
			
			if(!eventFrequencyFold.isFrequencyChosen()) {
				errors.add(new FormValidationError(R.string.Validation_Event_FrequencyNotChosen));
			}
			if(!eventFrequencyFold.isAtLeastOneWeekDayIsSelected()) {
				errors.add(new FormValidationError(R.string.Validation_Event_OneDayMustBeChosen));	
			}
			if(startDate == null)
				errors.add(new FormValidationError(R.string.Validation_StartDate_NotNull));
			if(endDate == null)
				errors.add(new FormValidationError(R.string.Validation_EndDate_NotNull));
			if(startDate != null && endDate != null && startDate.compareTo(endDate) > 0) 
				errors.add(new FormValidationError(R.string.Validation_EndDateBeforeStartDate));
			
			if(startTime != null && endTime != null && startTime.compareTo(endTime) > 0)
				errors.add(new FormValidationError(R.string.Validation_EventDate_EndTimeBeforeStartTime));
		
			
			errors.addAll(eventManagementService.validate(event));
			
			if(!errors.isEmpty()) {
				ErrorDialog.createDialog(this, errors).show();
			} else {
				calculateEventDates();
				eventManagementService.insert(event);
				finish();
			}
		}

	}
	
	public void calculateEventDates() {
		Calendar currentDay = startDate;
		Frequency frequency = eventFrequencyFold.getChosenFrequency();
		while(!currentDay.getTime().after(endDate.getTime())) {
			if(eventFrequencyFold.isWeekdayChecked(currentDay.get(Calendar.DAY_OF_WEEK))) {
				event.addEventDate(new EventDate(LocationManagementService.DEAFULT_LOCATION, currentDay.getTime(), startTime.getTime(), endTime.getTime(), DateTimeTools.getMinuteDifferenceBetweenTwoDates(startTime.getTime(), endTime.getTime()), false));
			}
			if(currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				currentDay.add(Calendar.DATE, frequency.getDaysToAdd());
			} else {
				currentDay.add(Calendar.DATE, 1);
			}
		}
	}
	
	private void enableStartEndDateButtons() {
		startDateButton.setEnabled(true);
		endDateButton.setEnabled(true);
		startDateTextView.setText(getString(R.string.EventDateFold_StartDate_Label_NoSet));
		endDateTextView.setText(getString(R.string.EventDateFold_EndDate_Label_NoSet));
	}
	
	private void disableStartEndDateButton() {
		startDateButton.setEnabled(false);
		endDateButton.setEnabled(false);
		startDateTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
		endDateTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
	}
	
	
	private void disableDateButton() {
		oneDateButton.setEnabled(false);
		oneDateTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
	}
	
	private void enableDateButton() {
		oneDateButton.setEnabled(true);
		oneDateTextView.setText(getString(R.string.EventDateFold_Date_Label_NoSet));
	}
	
	private void enableFrequencySpinner() {
		((Spinner) findViewById(R.id.ImpossibilityEventAdd_spinner)).setClickable(true);
		((TextView) findViewById(R.id.ImpossibilityEventAdd_frequencyFoldListLabel)).setText(getString(R.string.EventFrequencyFold_List_Label));
	}
	
	private void disableFrequencySpinner() {
		((Spinner) findViewById(R.id.ImpossibilityEventAdd_spinner)).setClickable(false);
		((TextView) findViewById(R.id.ImpossibilityEventAdd_frequencyFoldListLabel)).setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
	}

	@Override
	public void setStartDate(int year, int month, int day) {
		startDate = DateTimeTools.getCalendarInstanceWithDate(year, month, day);
		startDateTextView.setText(getDateDescription("Start time", startDate));
	}

	@Override
	public void setEndDate(int year, int month, int day) {
		endDate = DateTimeTools.getCalendarInstanceWithDate(year, month, day);
		endDateTextView.setText(getDateDescription("End time", endDate));
	}
	
	@Override
	public void setDate(int year, int month, int day) {
		oneDate = DateTimeTools.getCalendarInstanceWithDate(year, month, day);
		//oneTimeEventDate.setDate(oneDate.getTime());
		oneDateTextView.setText(getDateDescription(getString(R.string.EventDate_Date), oneDate));
	}

	@Override
	public void setStartTime(int hour, int minute) {
		startTime = DateTimeTools.getCalendarInstanceWithTime(hour, minute + 1);
		((TextView) findViewById(R.id.ImpossibilityEventAdd_textStartTime))
				.setText(getTimeDescription(getString(R.string.EventTimeFold_StartTime_Label), startTime));
	}

	@Override
	public void setEndTime(int hour, int minute) {
		endTime = DateTimeTools.getCalendarInstanceWithTime(hour, minute + 1);
		((TextView) findViewById(R.id.ImpossibilityEventAdd_textEndTime))
				.setText(getTimeDescription(getString(R.string.EventTimeFold_StartTime_Label), endTime));
	}

	
	private String getDateDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertDateToString(calendar)).toString();
	}
	
	private String getTimeDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertTimeToString(calendar)).toString();
	}
	
	public void showOneDatePickerDialog(View view) {
		datePickerFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showStartDatePickerDialog(View view) {
		startDatePickerFragment.show(getFragmentManager(), "startDatePicker");
	}
	
	public void showEndDatePickerDialog(View view) {
		endDatePickerFragment.show(getFragmentManager(), "endDatePicker");
	}
	
	public void showStartTimePickerDialog(View view) {
		startTimePickerFragment.show(getFragmentManager(), "startTimePicker");
	}
	
	public void showEndTimePickerDialog(View view) {
		endTimePickerFragment.show(getFragmentManager(), "endTimePicker");
	}

}

