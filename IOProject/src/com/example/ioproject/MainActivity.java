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
        
//        StringBuilder builder = new StringBuilder();
//        builder.append(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new AccountTable()));
//        builder.append("\n\n");
//        builder.append(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new EventTable()));
//        builder.append("\n\n");
//        builder.append(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new EventDateTable()));
//        builder.append("\n\n");
//        builder.append(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new LocationTable()));
//    
//        ((TextView)findViewById(R.id.text)).setText(builder.toString());
        
        //this.deleteDatabase(MainDatabaseHelper.DATABASE_NAME);
        
//        
//        Account account = new Account("Janek", "Kowalski", "Zdzisia");
//        Event event1 = new Event(account, null, null, "Fryzjer", "pójœcie do fryzjera", true, false);
//        event1.getEventDates().add(new EventDate(null, new GregorianCalendar(2014, Calendar.APRIL, 1, 00, 00).getTime(), 
//        											   new GregorianCalendar(2014, Calendar.APRIL, 1, 13, 30).getTime(),
//        											   new GregorianCalendar(2014, Calendar.APRIL, 1, 14, 00).getTime(),
//        											   new GregorianCalendar(0, 0, 0, 0, 35).getTime(),
//        											   false));
//        event1.setDefaultLocation(new Location("Fryzjer", "karmelicka 12", "Krakow", 23.452398, 43.3423415, true));
//        EventManagementService ems = new EventManagementService(new MainDatabaseHelper(this));
//        ems.insert(event1);
        
//        EventManagementService ems = new EventManagementService(new MainDatabaseHelper(this));
//        //((TextView)findViewById(R.id.text)).setText(ems.getAll().toString()); //getByIdAllData(event1.getId()).toString());
//        
//        List<Event> events = ems.getAll();
//        List<String> listValues = new ArrayList<String>();
//        //ListView listView = (ListView)findViewById(R.id.listview);
//        for(Event event : events) {
//        	for(EventDate eventDate : event.getEventDates()) {
//        		listValues.add(eventDate.toString());
//        	}
//        }
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listValues));
        
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
