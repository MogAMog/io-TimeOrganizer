package pl.edu.agh.view.addevent;

import java.util.Calendar;
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
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.ioproject.R;


public class EventAddActivity extends Activity implements SetDateInterface, SetTimePeriodInterface {

	private EventDate eventDate;
	private Event event;
	private EventManagementService eventManagementService;
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
		startTimePickerFragment = new StartTimePickerFragment();
		endTimePickerFragment = new EndTimePickerFragment();
		datePickerFragment = new DatePickerFragment();
		startTimeButton = ((Button)findViewById(R.id.buttonStartTime));
		endTimeButton = ((Button)findViewById(R.id.buttonEndTime));
		startTimeTextView = ((TextView) findViewById(R.id.textViewStartTime));
		endTimeTextView = ((TextView) findViewById(R.id.textViewEndTime));
		eventDurationSeekBar = (SeekBar) findViewById(R.id.seekBarEventDuration);
		textSeekBarProgress = (TextView) findViewById(R.id.textSeekBarProgress);
		
		
		((EditText)findViewById(R.id.editTextEventTitle)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setTitle(((EditText)findViewById(R.id.editTextEventTitle)).getText().toString());	
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		

		((EditText)findViewById(R.id.editTextDescription)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setDescription(((EditText)findViewById(R.id.editTextDescription)).getText().toString());
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		
		((CheckBox) findViewById(R.id.checkBoxFixedTime)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
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
		
		((CheckBox) findViewById(R.id.checkBoxRequired)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
			}
		});
		
		textSeekBarProgress.setText(getString(R.string.AddNewEventView_SeekBar_TextView_Minutes) + eventDurationSeekBar.getProgress());
		eventDurationSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				eventDurationSeekBar.setSecondaryProgress(eventDurationSeekBar.getProgress());
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				textSeekBarProgress.setText(getString(R.string.AddNewEventView_SeekBar_TextView_Minutes) + progress);
				eventDate.setDuration(progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
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
		((TextView) findViewById(R.id.textViewCurrentDate)).setText(new StringBuilder().append(getString(R.string.EventDate_date)).append(": ").append(DateTimeTools.convertDateToString(calendar)));;
	}
	
	@Override
	public void setStartTime(int hour, int minute) {
		Calendar calendar = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		eventDate.setStartTime(calendar.getTime());
		startTimeTextView.setText(getTimeDescription(getString(R.string.EventDate_start_time), calendar));
	}
	
	@Override
	public void setEndTime(int hour, int minute) {
		Calendar calendar = DateTimeTools.getCalendarInstanceWithTime(hour, minute);
		eventDate.setEndTime(calendar.getTime());	
		endTimeTextView.setText(getTimeDescription(getString(R.string.EventDate_end_time), calendar));
	}

	private String getTimeDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertTimeToString(calendar)).toString();
	}
	
	private void disableStartEndTimeButtons() {
		startTimeButton.setEnabled(false);
		endTimeButton.setEnabled(false);
		startTimeTextView.setText(getString(R.string.AddNewEventView_NotConstantMode_Disabled));
		endTimeTextView.setText(getString(R.string.AddNewEventView_NotConstantMode_Disabled));
	}
	
	private void enableStartEndTimeButtons() {
		startTimeButton.setEnabled(true);
		endTimeButton.setEnabled(true);
		startTimeTextView.setText(getString(R.string.AddNewEventView_text_view_start_time));
		endTimeTextView.setText(getString(R.string.AddNewEventView_text_view_end_time));
	}
	
	private void enabledSeekBar() {
		eventDurationSeekBar.setEnabled(true);
		textSeekBarProgress.setText(getString(R.string.AddNewEventView_SeekBar_TextView_Minutes) + eventDurationSeekBar.getProgress());
	}
	
	private void disableSeekBar() {
		eventDurationSeekBar.setEnabled(false);
		textSeekBarProgress.setText("");
	}
	
	public void addNewEventAction(View view) {
		eventDate.setFinished(false);
		eventDate.setLocation(null);
		event.addEventDate(eventDate);
		event.setAccount(AccountManagementService.DEFAULT_ACCOUNT);
		event.setPredecessorEvent(null);
		event.setDefaultLocation(new Location("Basen", "D17 AGH", "Krakow", 50.068408, 19.901062, true));
		List<FormValidationError> errors = eventManagementService.validate(event);
		if(!errors.isEmpty()) {
			ErrorDialog.createDialog(this, errors).show();
		} else {
			eventManagementService.insert(event);
			finish();
		}
	}

	
}
