package pl.edu.agh.domain.databasemanagement;

import pl.edu.agh.domain.tables.AccountTable;
import pl.edu.agh.domain.tables.EventDateTable;
import pl.edu.agh.domain.tables.EventTable;
import pl.edu.agh.domain.tables.LocationTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainDatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "IOProject.db";
	
	
	public MainDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new AccountTable()));
		db.execSQL(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new LocationTable()));
		db.execSQL(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new EventTable()));
		db.execSQL(SqlDatabaseTableScriptBuilder.buildCreateTableStatement(new EventDateTable()));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//new PersonTable(this).dropTable();
		//onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//onUpgrade(db, oldVersion, newVersion);
	}
	
}
