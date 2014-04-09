package pl.edu.agh.view.addconstantevent;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.Account;
import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.addevent.EndTimePickerFragment;
import pl.edu.agh.view.addevent.SetTimePeriodInterface;
import pl.edu.agh.view.addevent.StartTimePickerFragment;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ioproject.R;


public class ConstantEventAddActivity extends Activity implements SetDatePeriodInterface, SetTimePeriodInterface {
	
	private Event event;
	private Set<EventDate> eventDates;
	private Calendar startDate;
	private Calendar endDate;
	private EventManagementService eventManagementService;
	private boolean[] areDaysOfWeekSelected;
	private Date stime;
	private Date etime;
	private int duration = 0;
	
	private Spinner spinner;
	private String selectedItem = "everyday";
	
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constant_event_add);
		// Show the Up button in the action bar.
		setupActionBar();
		
		event = new Event();
		eventDates = new HashSet<EventDate>();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		areDaysOfWeekSelected = new boolean[7];
		
		
		final EditText eventTitle = (EditText) findViewById(R.id.ConstantEventAdd_eventTitle);
		eventTitle.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setTitle(eventTitle.getText().toString());
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
		});
		
		final EditText eventDescripton = (EditText) findViewById(R.id.ConstantEventAdd_eventDescription);
		eventDescripton.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setDescription(eventDescripton.getText().toString());			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
		});
		
		CheckBox isEventRequired = (CheckBox) findViewById(R.id.ConstantEventAdd_checkBoxRequired);
		isEventRequired.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
			}
		});
		
		final SeekBar eventDurationSeekBar = (SeekBar) findViewById(R.id.ConstantEventAdd_seekBarDuration);
		final TextView textSeekBarProgress = (TextView) findViewById(R.id.ConstantEventAdd_textSeekBarProgress);
		textSeekBarProgress.setText("How many minutes long: " + eventDurationSeekBar.getProgress());
		eventDurationSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				eventDurationSeekBar.setSecondaryProgress(eventDurationSeekBar.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				textSeekBarProgress.setText("How many minutes long: " + progress);
				duration = progress;
			}
		});
		
		
		spinner = (Spinner) findViewById(R.id.ConstantEventAdd_spinner);
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.AddNewConstantEventView_noSelectionItem));
		list.add(getString(R.string.AddNewConstantEventView_EveryWeekItem));
		list.add(getString(R.string.AddNewConstantEventView_EveryTwoWeeksItem));
		list.add(getString(R.string.AddNewConstantEventView_EveryFourWeeksItem));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, list);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener( new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				spinner.setSelection(position);
				selectedItem = (String) spinner.getSelectedItem();
				if(selectedItem == "everyday") {
					markAllDayCheckBoxesTrue();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { 
				//spinner.setSelection(0);
				//selectedItem = (String) spinner.getItemAtPosition(0);
			}
		});
		
		setDayCheckboxProperties(R.id.checkBoxSUNDAY, 1);
		setDayCheckboxProperties(R.id.checkBoxMONDAY, 2);
		setDayCheckboxProperties(R.id.checkBoxTUESDAY, 3);
		setDayCheckboxProperties(R.id.checkBoxWEDNESDAY, 4);
		setDayCheckboxProperties(R.id.checkBoxTHURSDAY, 5);
		setDayCheckboxProperties(R.id.checkBoxFRIDAY, 6);
		setDayCheckboxProperties(R.id.checkBoxSATURDAY, 7);

	}
	
	private void setDayCheckboxProperties(int id, final int dayOfWeek) {
		((CheckBox) findViewById(id)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				areDaysOfWeekSelected[dayOfWeek - 1] = isChecked;
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
		getMenuInflater().inflate(R.menu.constant_event_add, menu);
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

	@Override
	public void setStartDate(int year, int month, int day) {
		startDate = getCalendarInstanceWithDate(year, month, day);	
		//TextView startDateTextView = (TextView) findViewById(R.id.ConstantEventAdd_textStartDate);
		//startDateTextView.setText(getDateDescription("Start date", startDate));
		((TextView) findViewById(R.id.ConstantEventAdd_textStartDate)).setText(getDateDescription("Start date", startDate));
	}

	private String getDateDescription(String label, Calendar calendar) {
		StringBuilder dateToWrite = new StringBuilder();
		dateToWrite.append(label)
				.append(": ")
				//.append(DateTimeTools.parseDateFromCalendar(calendar));
				.append(DateTimeTools.convertDateToString(calendar));
		return dateToWrite.toString(); 
	}

	private Calendar getCalendarInstanceWithDate(int year, int month, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar;
	}

	@Override
	public void setEndDate(int year, int month, int day) {
		Calendar calendar = getCalendarInstanceWithDate(year, month, day);
		endDate = calendar;
		TextView endDate = (TextView) findViewById(R.id.ConstantEventAdd_textEndDate);
		endDate.setText(getDateDescription("End date", calendar));
	}

	@Override
	public void setStartTime(int hour, int minute) {
		Calendar calendar = getCalendarInstanceWithTime(hour, minute);
		stime = calendar.getTime();
		TextView startTime = (TextView) findViewById(R.id.ConstantEventAdd_textStartTime);
		startTime.setText(getTimeDescription("Start time", calendar));		
	}

	@Override
	public void setEndTime(int hour, int minute) {
		Calendar calendar = getCalendarInstanceWithTime(hour, minute);
		etime = calendar.getTime();
		TextView endTime = (TextView) findViewById(R.id.ConstantEventAdd_textEndTime);
		endTime.setText(getTimeDescription("End time", calendar));
	}
	
	private String getTimeDescription(String label, Calendar calendar) {
		StringBuilder startTimeToWrite = new StringBuilder();
		startTimeToWrite.append(label)
				.append(": ")
				.append((calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + calendar.get(Calendar.HOUR_OF_DAY))
				.append(":")
				.append((calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE));
		return startTimeToWrite.toString();
	}

	private Calendar getCalendarInstanceWithTime(int hour, int minute) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
	
	public void showStartDatePickerDialog(View v) {
		DialogFragment newFragment = new StartDatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showEndDatePickerDialog(View v) {
		DialogFragment newFragment = new EndDatePickerFragment();
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
	
	private void markAllDayCheckBoxesTrue() {
		for(int i=0; i<7; i++) {
			areDaysOfWeekSelected[i] = true;
		}
	}
	
	public void calculateEventDates(Location location, Calendar edate, Date startTime, Date endTime,
			int duration, boolean isFinished) {
		Calendar currentDay = startDate;

		if (!selectedItem.equals(getString(R.string.AddNewConstantEventView_noSelectionItem))) {
			while(!currentDay.getTime().after(edate.getTime())) {
				int dayNumber = currentDay.get(Calendar.DAY_OF_WEEK);
		
				if(areDaysOfWeekSelected[dayNumber - 1] == true) {
					event.addEventDate(new EventDate(location, currentDay.getTime(),
							startTime, endTime, duration, isFinished));
				}
				
				String what = getString(R.string.AddNewConstantEventView_EveryTwoWeeksItem);
				String sel = selectedItem;
				
				if(selectedItem.equals(getString(R.string.AddNewConstantEventView_EveryTwoWeeksItem)) && 
						currentDay.get(Calendar.DAY_OF_WEEK) == 1) {
					currentDay.add(Calendar.DATE, 8);
				}
				else if(selectedItem.equals(getString(R.string.AddNewConstantEventView_EveryFourWeeksItem)) && 
						currentDay.get(Calendar.DAY_OF_WEEK) == 1) {
					currentDay.add(Calendar.DATE, 22);
				}
				else {
					currentDay.add(Calendar.DATE, 1);
				}
			}
		}
		

	}
	
	public void addNewConstantEventAction(View view) {
		Location location = new Location("Basen", "D17 AGH", "Krakow", 50.068408, 19.901062, true);
		Date startTime = stime;
		Date endTime = etime;
		
		calculateEventDates(location, endDate, startTime, endTime, duration, false);
		
		Account account = new Account("Janek", "Kowalski", "Zdzisia");
		event.setAccount(account);
		event.setPredecessorEvent(null);
		event.setDefaultLocation(new Location("Basen", "D17 AGH", "Krakow", 50.068408, 19.901062, true));
		eventManagementService.insert(event);
		finish();
	}

}

