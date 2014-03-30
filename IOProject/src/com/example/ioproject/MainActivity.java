package com.example.ioproject;

import pl.edu.agh.domain.Account;
import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.domain.databasemanagement.SqlDatabaseTableScriptBuilder;
import pl.edu.agh.domain.tables.AccountTable;
import pl.edu.agh.domain.tables.EventDateTable;
import pl.edu.agh.domain.tables.EventTable;
import pl.edu.agh.domain.tables.LocationTable;
import pl.edu.agh.services.EventManagementService;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

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
        
        Account account = new Account("Janek", "Kowalski", "Zdzisia");
        Event event1 = new Event(account, null, null, "title", "description", true, false);
        EventManagementService ems = new EventManagementService(new MainDatabaseHelper(this));
        ems.insert(event1);
        
        ((TextView)findViewById(R.id.text)).setText(ems.getAll().toString());
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
