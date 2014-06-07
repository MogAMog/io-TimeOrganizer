package com.example.ioproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.exceptions.OptimalPathFindingException;
import pl.edu.agh.services.DefaultDistanceStrategy;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.services.OptimalPathFindingService;
import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.adddefaultlocalization.AddDefaultLocalizationActivity;
import pl.edu.agh.view.addevent.ConstantEventAddActivity;
import pl.edu.agh.view.addevent.EventAddActivity;
import pl.edu.agh.view.addevent.ImpossibilityEventAddActivity;
import pl.edu.agh.view.defaultlocalizationlist.DefaultLocalizationListActivity;
import pl.edu.agh.view.deleteconstantevents.DeleteConstantEventActivity;
import pl.edu.agh.view.eventlist.EventListFragment;
import pl.edu.agh.view.fragments.pickers.DatePickerFragment;
import pl.edu.agh.view.fragments.pickers.DatePickerFragment.SetDateInterface;
import pl.edu.agh.view.help.HelpActivity;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements EventListFragment.ProvideEventList, SetDateInterface{

	private EventManagementService eventManagementService;
	private CheckBox constantCheckBox;
	private CheckBox requiredCheckBox;
	private CheckBox draftCheckBox;
	private DialogFragment datePickerFragment;
	private Date chosenDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.deleteDatabase(MainDatabaseHelper.DATABASE_NAME);
		this.eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
		datePickerFragment = new DatePickerFragment();
		chosenDate = Calendar.getInstance().getTime();
		((TextView) findViewById(R.id.MainActivity_Date_TextView)).setText(new StringBuilder().append(getString(R.string.EventDate_Date)).append(": ").append(DateTimeTools.convertDateToString(Calendar.getInstance())));

		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				reloadCurrentFragmentList();
			}
		};
		
		constantCheckBox = ((CheckBox)findViewById(R.id.MainActivity_ConstantCheckBox));
		constantCheckBox.setOnCheckedChangeListener(listener);
		
		requiredCheckBox = ((CheckBox)findViewById(R.id.MainActivity_RequiredCheckBox));
		requiredCheckBox.setOnCheckedChangeListener(listener);
		
		draftCheckBox = ((CheckBox)findViewById(R.id.MainActivity_DraftCheckBox));
		draftCheckBox.setOnCheckedChangeListener(listener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.MainActivity_ActionBar_AddNewEvent:
    			addNewEventAction(item.getActionView());
    			return true;
    		case R.id.MainActivity_ActionBar_AddNewConstantEvent:
    			addNewConstantEventAction(item.getActionView());
    			return true;
    		case R.id.MainActivity_ActionBar_DeleteConstantEvent:
    			deleteConstantEvent(item.getActionView());
    			return true;
    		case R.id.MainActivity_ActionBar_AddNewImpossibilityEvent:
        		startActivity(new Intent(this, ImpossibilityEventAddActivity.class));
        		return true;
    		case R.id.MainActivity_ActionBar_Localization_AddDefaultLocalization:
    			startActivity(new Intent(this, AddDefaultLocalizationActivity.class));
    			return true;
    		case R.id.MainActivity_ActionBar_Localiation_ShowDefaultLocalizationList:
    			startActivity(new Intent(this, DefaultLocalizationListActivity.class));
    			return true;
    		case R.id.MainActivity_ActionBar_Help:
    			startActivity(new Intent(this, HelpActivity.class));
    			return true;
    		case R.id.MainActivity_ActionBar_Algorithm:
    			OptimalPathFindingService optimalPathFindingService = new OptimalPathFindingService(new DefaultDistanceStrategy());
    			optimalPathFindingService.setEventDates(getEventDatesSet());
			try {
				optimalPathFindingService.calculateOptimalEventOrder();
			} catch (OptimalPathFindingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    			return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

	public void addNewEventAction(View view) {
		startActivity(new Intent(this, EventAddActivity.class));
//		ConnectionsFinderService cfs = new ConnectionsFinderService();
//		Location l1 = new Location("basen", "ujazdowska 22", "krakow", 50.05434, 19.93931, false);
//		Location l2 = new Location("dworzec", "pawia 1", "krakow", 50.06265, 19.94259, false);
//		EventDate firstEvent = new EventDate(l1, null, null, null, 0, false);
//		Calendar cal = Calendar.getInstance();
//		cal.set(2014, 04, 14);
//		EventDate secondEvent = new EventDate(l2, cal.getTime(), new Date(2014, 05, 14, 12, 03, 00), null, 0, false);
//		startActivity(cfs.createIntent(firstEvent, secondEvent));
	}

	public void addNewConstantEventAction(View view) {
		startActivity(new Intent(this, ConstantEventAddActivity.class));
	}

	public void deleteConstantEvent(View view) {
		startActivity(new Intent(this, DeleteConstantEventActivity.class));
	}
	
	@Override
	public List<Event> getEventList() {
		eventManagementService.clearCache();
		List<Event> events = eventManagementService.getAll();
		List<Event> deleteList = new ArrayList<Event>();
		
		for(Event event : events) {
			if(!checkIfEventFulfilsFilterRequirements(event)) {
				deleteList.add(event);
			}
		}
		events.removeAll(deleteList);
		
		List<EventDate> deleteEventDates = new ArrayList<EventDate>();
		for(Event event : events) {
			for(EventDate eventDate : event.getEventDates()) {
				if(!checkIfEventDateFulfilsFilterRequirements(eventDate)) {
					deleteEventDates.add(eventDate);
				}
			}
			event.getEventDates().removeAll(deleteEventDates);
		}
		return events;
	}
	
	public Set<EventDate> getEventDatesSet() {
		eventManagementService.clearCache();
		Set<EventDate> eventDatesSet = new HashSet<EventDate>();
		List<Event> events = eventManagementService.getAll();
		List<Event> deleteList = new ArrayList<Event>();
		
		for(Event event : events) {
			if(event.isDraft()) {
				deleteList.add(event);
			}
		}
		
		List<EventDate> deleteEventDates = new ArrayList<EventDate>();
		for(Event event : events) {
			for(EventDate eventDate : event.getEventDates()) {
				if(!checkIfEventDateFulfilsFilterRequirements(eventDate)) {
					deleteEventDates.add(eventDate);
				}
			}
			event.getEventDates().removeAll(deleteEventDates);
		}
		
		for(Event event : events) {
			for(EventDate eventDate : event.getEventDates()) {
				eventDatesSet.add(eventDate);
			}
		}
		return eventDatesSet;
	}
	
	private boolean checkIfEventFulfilsFilterRequirements(Event event) {
		return (!constantCheckBox.isChecked() || event.isConstant() == true) &&
			   (!draftCheckBox.isChecked() || event.isDraft() == true) &&
			   (!requiredCheckBox.isChecked() || event.isRequired() == true);
	}
	
	@SuppressWarnings("deprecation")
	private boolean checkIfEventDateFulfilsFilterRequirements(EventDate eventDate) {
		return (eventDate.getDate().getDay() == chosenDate.getDay()) &&
			   (eventDate.getDate().getMonth() == chosenDate.getMonth()) &&
			   (eventDate.getDate().getYear() == chosenDate.getYear());
	}
	
	@Override
	public void reloadCurrentFragmentList() {
		((EventListFragment)getFragmentManager().findFragmentById(R.id.MainActivity_EventListFragment)).reloadEventList();
	}
	
	public void showDatePickerDialog(View v) {
		datePickerFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void setDate(int year, int month, int day) {
		Calendar calendar = DateTimeTools.getCalendarInstanceWithDate(year, month, day);
		chosenDate = calendar.getTime();
		reloadCurrentFragmentList();
		((TextView) findViewById(R.id.MainActivity_Date_TextView)).setText(new StringBuilder().append(getString(R.string.EventDate_Date)).append(": ").append(DateTimeTools.convertDateToString(calendar)));
	}
	
	
}
