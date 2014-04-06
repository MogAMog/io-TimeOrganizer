package com.example.ioproject;

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
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    public void addNewEventAction(View view) {
		startActivity(new Intent(this, EventAddActivity.class));
	}
    
}
