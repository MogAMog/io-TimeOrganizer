package pl.edu.agh.view.addconstantevent;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.edu.agh.domain.Account;
import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.tools.DateTimeTools;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ioproject.R;
import com.google.android.gms.wallet.EnableWalletOptimizationReceiver;


public class ConstantEventAddActivity extends Activity implements SetDatePeriodInterface, SetTimePeriodInterface {
	
	private Event event;
	private EventManagementService eventManagementService;
	
	private DialogFragment startTimePickerFragment;
	private DialogFragment endTimePickerFragment;
	private DialogFragment startDatePickerFragment;
	private DialogFragment endDatePickerFragment;
	
	private Calendar startDate;
	private Calendar endDate;
	private Calendar startTime;
	private Calendar endTime;
	
	private CheckBox[] weekdayCheckboxes;
	private boolean[] areDaysOfWeekSelected;
	
	private Spinner spinner;
	private String selectedItem;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constant_event_add);
		setupActionBar();
		
		event = new Event();
		eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		
		startTimePickerFragment = new StartTimePickerFragment();
		endTimePickerFragment = new EndTimePickerFragment();
		startDatePickerFragment = new StartDatePickerFragment();
		endDatePickerFragment = new EndDatePickerFragment();
		
		weekdayCheckboxes = new CheckBox[7];
		areDaysOfWeekSelected = new boolean[7];
		selectedItem = getString(R.string.AddNewConstantEventView_noSelectionItem);
		
		((EditText) findViewById(R.id.ConstantEventAdd_eventTitle)).addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				event.setTitle(((EditText) findViewById(R.id.ConstantEventAdd_eventTitle)).getText().toString());
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
		});
		
		((EditText) findViewById(R.id.ConstantEventAdd_eventDescription)).addTextChangedListener(new TextWatcher() {	
			@Override
			public void afterTextChanged(Editable s) {
				event.setDescription(((EditText) findViewById(R.id.ConstantEventAdd_eventDescription)).getText().toString());			
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
		});
		
		((CheckBox) findViewById(R.id.ConstantEventAdd_checkBoxRequired)).setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				event.setRequired(isChecked);				
			}
		});
		
		spinner = (Spinner) findViewById(R.id.ConstantEventAdd_spinner);
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.AddNewConstantEventView_noSelectionItem));
		list.add(getString(R.string.AddNewConstantEventView_Everyday));
		list.add(getString(R.string.AddNewConstantEventView_EveryWeekItem));
		list.add(getString(R.string.AddNewConstantEventView_EveryTwoWeeksItem));
		list.add(getString(R.string.AddNewConstantEventView_EveryFourWeeksItem));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener( new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinner.setSelection(position);
				selectedItem = (String) spinner.getSelectedItem();
				if(selectedItem.equals(getString(R.string.AddNewConstantEventView_Everyday))) {
					markAllDayCheckBoxesTrue();
				} else {
					enableAllCheckBoxes();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { 
			}
		});
		
		setDayCheckboxProperties(R.id.checkBoxSUNDAY, Calendar.SUNDAY);
		setDayCheckboxProperties(R.id.checkBoxMONDAY, Calendar.MONDAY);
		setDayCheckboxProperties(R.id.checkBoxTUESDAY, Calendar.TUESDAY);
		setDayCheckboxProperties(R.id.checkBoxWEDNESDAY, Calendar.WEDNESDAY);
		setDayCheckboxProperties(R.id.checkBoxTHURSDAY, Calendar.THURSDAY);
		setDayCheckboxProperties(R.id.checkBoxFRIDAY, Calendar.FRIDAY);
		setDayCheckboxProperties(R.id.checkBoxSATURDAY, Calendar.SATURDAY);

	}
	
	private void setDayCheckboxProperties(int id, final int dayOfWeek) {
		weekdayCheckboxes[dayOfWeek - 1] = ((CheckBox) findViewById(id));
		weekdayCheckboxes[dayOfWeek - 1].setOnCheckedChangeListener( new OnCheckedChangeListener() {
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
		StringBuilder dateToWrite = new StringBuilder();
		dateToWrite.append(label)
				.append(": ")
				.append(DateTimeTools.convertDateToString(calendar));
		return dateToWrite.toString(); 
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
	
	private void markAllDayCheckBoxesTrue() {
		for(int i = 0; i < 7; i++) {
			weekdayCheckboxes[i].setChecked(true);
			weekdayCheckboxes[i].setEnabled(false);
			areDaysOfWeekSelected[i] = true;
		}
	}
	
	private void enableAllCheckBoxes() {
		for(CheckBox checkBox : weekdayCheckboxes) {
			checkBox.setEnabled(true);
		}
	}
	
	public void calculateEventDates(Location location, Calendar edate, Date startTime, Date endTime, boolean isFinished) {
		Calendar currentDay = startDate;
		int duration = 0;
		
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
		//Date startTime = startTime;
		//Date endTime = etime;
		
		calculateEventDates(location, endDate, startTime.getTime(), endTime.getTime(), false);
		
		Account account = new Account("Janek", "Kowalski", "Zdzisia");
		event.setAccount(account);
		event.setPredecessorEvent(null);
		event.setDefaultLocation(new Location("Basen", "D17 AGH", "Krakow", 50.068408, 19.901062, true));
		eventManagementService.insert(event);
		finish();
	}

}

