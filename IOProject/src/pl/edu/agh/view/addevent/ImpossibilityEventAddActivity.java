package pl.edu.agh.view.addevent;

import java.util.Calendar;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.tools.DateTimeTools;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ioproject.R;

public class ImpossibilityEventAddActivity extends Activity 
		implements SetDateInterface, SetTimePeriodInterface, SetDatePeriodInterface {
	
	private Event event;
	private EventManagementService eventManagementService;
	private EventDate oneTimeEventDate;
	
	private EventTimeAndDescriptionFold eventTimeAndDescriptionFold;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impossibility_event_add);
		setupActionBar();
		
		event = new Event();
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
		
		eventTimeAndDescriptionFold.initializeListeners();
		
		constantEventRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					enableStartEndDateButtons();
					disableDateButton();
					oneTimeEventRadioButton.setChecked(false);
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
				}
			}
		});
		
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void enableStartEndDateButtons() {
		startDateButton.setEnabled(true);
		endDateButton.setEnabled(true);
		startDateTextView.setText(getString(R.string.EventDateFold_StartDate_Label_NoSet));
		startDateTextView.setText(getString(R.string.EventDateFold_EndDate_Label_NoSet));
	}
	
	private void disableStartEndDateButton() {
		startDateButton.setEnabled(false);
		endDateButton.setEnabled(false);
		startDateTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
	}
	
	
	private void disableDateButton() {
		oneDateButton.setEnabled(false);
		oneDateTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
	}
	
	private void enableDateButton() {
		oneDateButton.setEnabled(true);
		oneDateTextView.setText(getString(R.string.EventDateFold_Date_Label_NoSet));
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
		startTime = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		((TextView) findViewById(R.id.ImpossibilityEventAdd_textStartTime))
				.setText(getTimeDescription(getString(R.string.EventTimeFold_StartTime_Label), startTime));
	}

	@Override
	public void setEndTime(int hour, int minute) {
		// TODO Auto-generated method stub
		
	}

	
	private String getDateDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertDateToString(calendar)).toString();
	}
	
	private String getTimeDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertTimeToString(calendar)).toString();
	}

}
