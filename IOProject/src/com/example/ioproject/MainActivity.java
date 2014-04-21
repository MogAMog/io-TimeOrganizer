package com.example.ioproject;

import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.view.adddefaultlocalization.AddDefaultLocalizationActivity;
import pl.edu.agh.view.addevent.ConstantEventAddActivity;
import pl.edu.agh.view.addevent.EventAddActivity;
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
    			//startActivity(new Intent(this, AddDefaultLocalizationActivity.class));
    			return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

	public void addNewEventAction(View view) {
		startActivity(new Intent(this, EventAddActivity.class));
	}

	public void addNewConstantEventAction(View view) {
		startActivity(new Intent(this, ConstantEventAddActivity.class));
	}
}
