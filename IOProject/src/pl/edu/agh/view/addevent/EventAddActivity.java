package pl.edu.agh.view.addevent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.AccountManagementService;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.fragments.dialogs.ErrorDialog;
import pl.edu.agh.view.fragments.pickers.DatePickerFragment;
import pl.edu.agh.view.fragments.pickers.EndTimePickerFragment;
import pl.edu.agh.view.fragments.pickers.SetTimePeriodInterface;
import pl.edu.agh.view.fragments.pickers.StartTimePickerFragment;
import pl.edu.agh.view.fragments.pickers.DatePickerFragment.SetDateInterface;
import pl.edu.agh.view.onetimelocalization.OneTimeLocalizationActivity;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.ioproject.R;


public class EventAddActivity extends Activity implements SetDateInterface, SetTimePeriodInterface {
	
	private EventDate eventDate;
	private Event event;
	
	private EventManagementService eventManagementService;
	
	private EventTimeAndDescriptionFold eventTimeAndDescriptionFold;
	private EventLocalizationFold eventLocalizationFold;
	
	private DialogFragment startTimePickerFragment;
	private DialogFragment endTimePickerFragment;
	private DialogFragment datePickerFragment;
	private SeekBar eventDurationSeekBar; 
	private TextView textSeekBarProgress;
	private Button startTimeButton;
	private Button endTimeButton;
	private TextView startTimeTextView;
	private TextView endTimeTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_add);
		setupActionBar();
		
		eventDate = new EventDate();
		event = new Event();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		eventTimeAndDescriptionFold = new EventTimeAndDescriptionFold(this, event, R.id.EventTitleAndDescriptionFold_AddEventTitle_Id, R.id.EventTitleAndDescriptionFold_AddEventDescription_Id);
		eventLocalizationFold = new EventLocalizationFold(this, R.id.LocationChoiceFold_DefaultLocationList_Id, R.id.LocationChoiceFold_OneTimeLocation_Button_Id, R.id.LocationChoiceFold_OneTimeLocation_ImageView_Id);
		startTimePickerFragment = new StartTimePickerFragment();
		endTimePickerFragment = new EndTimePickerFragment();
		datePickerFragment = new DatePickerFragment();
		startTimeButton = ((Button)findViewById(R.id.EventTimeFold_StartTime_Button_Id));
		endTimeButton = ((Button)findViewById(R.id.EventTimeFold_EndTime_Button_Id));
		startTimeTextView = ((TextView) findViewById(R.id.EventTimeFold_StartTime_TextView_Id));
		endTimeTextView = ((TextView) findViewById(R.id.EventTimeFold_EndTime_TextView_Id));
		eventDurationSeekBar = (SeekBar) findViewById(R.id.EventTimeFold_Duration_SeekBar_Id);
		textSeekBarProgress = (TextView) findViewById(R.id.EventTimeFold_Duration_TextView_Id);
		
		eventTimeAndDescriptionFold.initializeListeners();
		
		((CheckBox) findViewById(R.id.EventRequirementFold_FixedTime_Id)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) { 
					enableStartEndTimeButtons();
					disableSeekBar();
					event.setConstant(true);
				} else {
					disableStartEndTimeButtons();
					enabledSeekBar();
					event.setConstant(false);
				}			
			}
		});
		
		((CheckBox) findViewById(R.id.EventRequirementFold_Required_Id)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
			}
		});
		
		((CheckBox)findViewById(R.id.EventRequirementFold_IsDraft_Id)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				event.setDraft(isChecked);
			}
		});
		
		textSeekBarProgress.setText(getString(R.string.EventTimeFold_Duration_Label) + eventDurationSeekBar.getProgress());
		eventDurationSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				eventDurationSeekBar.setSecondaryProgress(eventDurationSeekBar.getProgress());
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				textSeekBarProgress.setText(getString(R.string.EventTimeFold_Duration_Label) + progress);
				eventDate.setDuration(progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
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
		getMenuInflater().inflate(R.menu.event_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.AddEvent_ActionBar_AddAsTemplate:
		case R.id.AddConstantEvent_ActionBar_AddAsTemplate:
			return saveEventAsTemplate();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showDatePickerDialog(View v) {
		datePickerFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showStartTimePickerDialog(View v) {
		startTimePickerFragment.show(getFragmentManager(), "startTimePicker");
	}
	
	public void showEndTimePickerDialog(View v) {
		endTimePickerFragment.show(getFragmentManager(), "endTimePicker");
	}

	@Override
	public void setDate(int year, int month, int day) {
		Calendar calendar = DateTimeTools.getCalendarInstanceWithDate(year, month, day);
		eventDate.setDate(calendar.getTime());
		((TextView) findViewById(R.id.EventDateFold_Date_TextView_Id)).setText(new StringBuilder().append(getString(R.string.EventDate_Date)).append(": ").append(DateTimeTools.convertDateToString(calendar)));;
	}
	
	@Override
	public void setStartTime(int hour, int minute) {
		Calendar calendar = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		eventDate.setStartTime(calendar.getTime());
		startTimeTextView.setText(getTimeDescription(getString(R.string.EventTimeFold_StartTime_Label), calendar));
	}
	
	@Override
	public void setEndTime(int hour, int minute) {
		Calendar calendar = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		eventDate.setEndTime(calendar.getTime());	
		endTimeTextView.setText(getTimeDescription(getString(R.string.EventTimeFold_EndTime_Label), calendar));
	}
	private String getTimeDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertTimeToString(calendar)).toString();
	}
	
	private void disableStartEndTimeButtons() {
		startTimeButton.setEnabled(false);
		endTimeButton.setEnabled(false);
		startTimeTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
		endTimeTextView.setText(getString(R.string.AddNewEventView_NotFixedMode_Disabled));
	}
	
	private void enableStartEndTimeButtons() {
		startTimeButton.setEnabled(true);
		endTimeButton.setEnabled(true);
		startTimeTextView.setText(getString(R.string.EventTimeFold_StartTime_Label_NoSet));
		endTimeTextView.setText(getString(R.string.EventTimeFold_EndTime_Label_NoSet));
	}
	
	private void enabledSeekBar() {
		eventDurationSeekBar.setEnabled(true);
		textSeekBarProgress.setText(getString(R.string.EventTimeFold_Duration_Label) + eventDurationSeekBar.getProgress());
	}
	
	private void disableSeekBar() {
		eventDurationSeekBar.setEnabled(false);
		textSeekBarProgress.setText("");
	}
	
	public void addNewEventAction(View view) {
		eventDate.setFinished(false);
		eventDate.setLocation(eventLocalizationFold.getLocationForEvent());
		event.addEventDate(eventDate);
		event.setAccount(AccountManagementService.DEFAULT_ACCOUNT);
		event.setPredecessorEvent(null);
		if(((CheckBox) findViewById(R.id.EventRequirementFold_FixedTime_Id)).isChecked()) {
			eventDate.setDuration(DateTimeTools.getMinuteDifferenceBetweenTwoDates(eventDate.getStartTime(), eventDate.getEndTime()));
		} else {
			eventDate.setStartTime(new GregorianCalendar().getTime());
			eventDate.setEndTime(new GregorianCalendar().getTime());
		}
		List<FormValidationError> errors = eventManagementService.validate(event);
		if(!errors.isEmpty()) {
			ErrorDialog.createDialog(this, errors).show();
		} else {
			eventManagementService.insert(event);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case(EventLocalizationFold.ONE_TIME_LOCATION_ACTIVITY_ID):
				if(resultCode == RESULT_OK) {
					eventLocalizationFold.setOneTimeLocation((Location)data.getSerializableExtra(OneTimeLocalizationActivity.LOCATION_RESULT_KEY));
					eventLocalizationFold.setLocalizationSelectionWasMade();
					break;
				}
			default: super.onActivityResult(requestCode, resultCode, data);		
		}
	}
	
	private boolean saveEventAsTemplate() {
		Toast.makeText(this, "Add As Template", Toast.LENGTH_LONG).show();
		return true;
	}

	
}
