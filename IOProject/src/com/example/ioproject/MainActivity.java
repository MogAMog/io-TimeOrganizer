package com.example.ioproject;

import java.util.Calendar;
import java.util.Date;

import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.ConnectionsFinderService;
import pl.edu.agh.view.adddefaultlocalization.AddDefaultLocalizationActivity;
import pl.edu.agh.view.addevent.ConstantEventAddActivity;
import pl.edu.agh.view.addevent.EventAddActivity;
import pl.edu.agh.view.defaultlocalizationlist.DefaultLocalizationListActivity;
import pl.edu.agh.view.help.HelpActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.deleteDatabase(MainDatabaseHelper.DATABASE_NAME);
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
    		case R.id.MainActivity_ActionBar_Localization_AddDefaultLocalization:
    			startActivity(new Intent(this, AddDefaultLocalizationActivity.class));
    			return true;
    		case R.id.MainActivity_ActionBar_Localiation_ShowDefaultLocalizationList:
    			startActivity(new Intent(this, DefaultLocalizationListActivity.class));
    			return true;
    		case R.id.MainActivity_ActionBar_Help:
    			startActivity(new Intent(this, HelpActivity.class));
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
}
