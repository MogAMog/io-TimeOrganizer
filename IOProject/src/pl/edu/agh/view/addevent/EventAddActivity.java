package pl.edu.agh.view.addevent;

import java.util.ArrayList;
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
import pl.edu.agh.services.LocationManagementService;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.ioproject.R;


public class EventAddActivity extends Activity implements SetDateInterface, SetTimePeriodInterface {

	private static final int ONE_TIME_LOCATION_ACTIVITY_ID = 1;
	
	private EventDate eventDate;
	private Event event;
	private EventManagementService eventManagementService;
	private LocationManagementService locationManagementService;
	private DialogFragment startTimePickerFragment;
	private DialogFragment endTimePickerFragment;
	private DialogFragment datePickerFragment;
	private SeekBar eventDurationSeekBar; 
	private TextView textSeekBarProgress;
	private Button startTimeButton;
	private Button endTimeButton;
	private TextView startTimeTextView;
	private TextView endTimeTextView;
	
	private Spinner defaultLocalizationSpinner;
	private String selectedLocalizationName;
	
	private List<Location> defaultLocations;
	private Location oneTimeLocation = new Location();
	
	boolean fixedTimeChosen = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_add);
		setupActionBar();
		
		eventDate = new EventDate();
		event = new Event();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		locationManagementService = new LocationManagementService(new MainDatabaseHelper(this));
		startTimePickerFragment = new StartTimePickerFragment();
		endTimePickerFragment = new EndTimePickerFragment();
		datePickerFragment = new DatePickerFragment();
		startTimeButton = ((Button)findViewById(R.id.EventTimeFold_StartTime_Button_Id));
		endTimeButton = ((Button)findViewById(R.id.EventTimeFold_EndTime_Button_Id));
		startTimeTextView = ((TextView) findViewById(R.id.EventTimeFold_StartTime_TextView_Id));
		endTimeTextView = ((TextView) findViewById(R.id.EventTimeFold_EndTime_TextView_Id));
		eventDurationSeekBar = (SeekBar) findViewById(R.id.EventTimeFold_Duration_SeekBar_Id);
		textSeekBarProgress = (TextView) findViewById(R.id.EventTimeFold_Duration_TextView_Id);
		defaultLocalizationSpinner = (Spinner)findViewById(R.id.LocationChoiceFold_DefaultLocationList_Id);
		
		((EditText)findViewById(R.id.EventTitleAndDescriptionFold_AddEventTitle_Id)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setTitle(((EditText)findViewById(R.id.EventTitleAndDescriptionFold_AddEventTitle_Id)).getText().toString());	
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		

		((EditText)findViewById(R.id.EventTitleAndDescriptionFold_AddEventDescription_Id)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setDescription(((EditText)findViewById(R.id.EventTitleAndDescriptionFold_AddEventDescription_Id)).getText().toString());
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		
		((CheckBox) findViewById(R.id.EventRequirementFold_FixedTime_Id)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) { 
					enableStartEndTimeButtons();
					disableSeekBar();
				} else {
					disableStartEndTimeButtons();
					enabledSeekBar();
				}
				event.setConstant(isChecked);				
			}
		});
		
		((CheckBox) findViewById(R.id.EventRequirementFold_Required_Id)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
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
		selectedLocalizationName = getString(R.string.LocationChoiceFold_DefaultLocationList_NoSelection);
		defaultLocations = locationManagementService.getDefaultLocalizationsAllData();
		List<String> defaultLocationsAdapterList = new ArrayList<String>();
		defaultLocationsAdapterList.add(getString(R.string.LocationChoiceFold_DefaultLocationList_NoSelection));
		for(Location location : defaultLocations) {
			defaultLocationsAdapterList.add(location.getName());
		}
		defaultLocalizationSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, defaultLocationsAdapterList));
		defaultLocalizationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				defaultLocalizationSpinner.setSelection(position);
				selectedLocalizationName = (String) defaultLocalizationSpinner.getSelectedItem();
				if(selectedLocalizationName.equals(getString(R.string.LocationChoiceFold_DefaultLocationList_NoSelection))) {
					((TextView)findViewById(R.id.LocationChoiceFold_OneTimeLocation_Button_Id)).setEnabled(true);
				} else {
					((TextView)findViewById(R.id.LocationChoiceFold_OneTimeLocation_Button_Id)).setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
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
		getMenuInflater().inflate(R.menu.event_add, menu);
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
		eventDate.setLocation(getLocationForEvent());
		event.addEventDate(eventDate);
		event.setAccount(AccountManagementService.DEFAULT_ACCOUNT);
		event.setPredecessorEvent(null);
		if(fixedTimeChosen) {
			eventDate.setDuration(45);
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
			case(ONE_TIME_LOCATION_ACTIVITY_ID):
				if(resultCode == RESULT_OK) {
					oneTimeLocation = (Location)data.getSerializableExtra(OneTimeLocalizationActivity.LOCATION_RESULT_KEY);
					((ImageView)findViewById(R.id.LocationChoiceFold_OneTimeLocation_ImageView_Id)).setImageResource(R.drawable.icon_accept);
					break;
				}
			default: super.onActivityResult(requestCode, resultCode, data);		
		}
	}
	
	private Location getLocationForEvent() {
		if(!selectedLocalizationName.equals(getString(R.string.LocationChoiceFold_DefaultLocationList_NoSelection))) {
			return locationManagementService.getLocationByName(selectedLocalizationName);
		} else {
			locationManagementService.setValuesForNotDefaultLocation(oneTimeLocation);
			return oneTimeLocation;
		}
	}
	
	public void invokeOneTimeLocationActivityForResult(View view) {
		Intent oneTimeLocationIntent = new Intent(this, OneTimeLocalizationActivity.class);
		startActivityForResult(oneTimeLocationIntent, ONE_TIME_LOCATION_ACTIVITY_ID);
	}

	
}
