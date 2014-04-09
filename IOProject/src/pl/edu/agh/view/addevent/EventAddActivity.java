package pl.edu.agh.view.addevent;

import java.util.Calendar;
import java.util.GregorianCalendar;

import pl.edu.agh.domain.Account;
import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.addevent.DatePickerFragment.SetDateInterface;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_add);
		setupActionBar();
		
		eventDate = new EventDate();
		event = new Event();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		
		
		final EditText eventTitle = (EditText)findViewById(R.id.editTextEventTitle);
		eventTitle.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setTitle(eventTitle.getText().toString());	
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		

		final EditText eventDescription = (EditText)findViewById(R.id.editTextDescription);
		eventDescription.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setDescription(eventDescription.getText().toString());
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		
		CheckBox isEventConstant = (CheckBox) findViewById(R.id.checkBoxConstant);
		isEventConstant.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setConstant(isChecked);				
			}
		});
		
		CheckBox isEventRequired = (CheckBox) findViewById(R.id.checkBoxRequired);
		isEventRequired.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
			}
		});
		
		final SeekBar eventDurationSeekBar = (SeekBar) findViewById(R.id.seekBarEventDuration);
		final TextView textSeekBarProgress = (TextView) findViewById(R.id.textSeekBarProgress);
		textSeekBarProgress.setText("How many minutes long: " + eventDurationSeekBar.getProgress());
		eventDurationSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				eventDurationSeekBar.setSecondaryProgress(eventDurationSeekBar.getProgress());
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				textSeekBarProgress.setText("How many minutes long: " + progress);
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
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showStartTimePickerDialog(View v) {
		DialogFragment newFragment = new StartTimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showEndTimePickerDialog(View v) {
		DialogFragment newFragment = new EndTimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	@Override
	public void setDate(int year, int month, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		eventDate.setDate(calendar.getTime());
		((TextView) findViewById(R.id.textViewCurrentDate)).setText(new StringBuilder().append("Date: ").append(DateTimeTools.convertDateToString(calendar)));;
	}
	
	@Override
	public void setStartTime(int hour, int minute) {
		Calendar calendar = getCalendarInstanceWithTime(hour, minute);
		eventDate.setStartTime(calendar.getTime());
		((TextView) findViewById(R.id.textViewStartTime)).setText(getTimeDescription("Start time", calendar));
	}
	
	@Override
	public void setEndTime(int hour, int minute) {
		Calendar calendar = getCalendarInstanceWithTime(hour, minute);
		eventDate.setEndTime(calendar.getTime());	
		((TextView) findViewById(R.id.textViewEndTime)).setText(getTimeDescription("End time", calendar));
	}

	private String getTimeDescription(String label, Calendar calendar) {
		return new StringBuilder().append(label).append(": ").append(DateTimeTools.convertTimeToString(calendar)).toString();
	}

	private Calendar getCalendarInstanceWithTime(int hour, int minute) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
	
	public void addNewEventAction(View view) {
		Account account = new Account("Janek", "Kowalski", "Zdzisia");
		eventDate.setDuration(15);
		eventDate.setFinished(false);
		eventDate.setLocation(null);
		event.addEventDate(eventDate);
		event.setAccount(account);
		event.setPredecessorEvent(null);
		event.setDefaultLocation(new Location("Basen", "D17 AGH", "Krakow", 50.068408, 19.901062, true));
		eventManagementService.insert(event);
		finish();
	}

	
}
