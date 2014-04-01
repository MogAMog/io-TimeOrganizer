package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.Account;
import pl.edu.agh.domain.databasemanagement.IDatabaseDmlProvider;
import pl.edu.agh.domain.tables.AccountTable;
import pl.edu.agh.domain.tables.EventTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountManagementService implements IDatabaseDmlProvider<Account> {

	private SQLiteOpenHelper dbHelper;

	public AccountManagementService(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	@Override
	public long insert(Account insertObject) {
		ContentValues values = new ContentValues();
		values.put(AccountTable.COLUMN_NAME_LOGIN, insertObject.getLogin());
		values.put(AccountTable.COLUMN_NAME_PASSWORD, insertObject.getPassword());
		values.put(AccountTable.COLUMN_NAME_EMAIL, insertObject.getEmail());
		long id = dbHelper.getWritableDatabase().insert(AccountTable.TABLE_NAME, null, values);
		insertObject.setId(id);
		return id;
	}

	@Override
	public List<Account> getAll() {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + EventTable.TABLE_NAME, null);
			cursor.moveToFirst();
			List<Account> accounts = new ArrayList<Account>();
			while (!cursor.isAfterLast()) {
				accounts.add(getAccountFromCursor(cursor));
				cursor.moveToNext();
			}
			return accounts;
		} finally {
			cursor.close();
		}
	}

	@Override
	public Account getByIdAllData(long id) {
		Cursor cursor = null;
		try {
			String selection = AccountTable._ID + " = ?";
			String[] selectionArgument = new String[] { String.valueOf(id) };
			cursor = dbHelper.getReadableDatabase().query(AccountTable.TABLE_NAME, null, selection, selectionArgument, null, null, null);
			cursor.moveToFirst();
			Account account = getAccountFromCursor(cursor);
			return account;
		} finally {
			cursor.close();
		}
	}

	private Account getAccountFromCursor(Cursor cursor) {
		Account account = new Account();
		account.setId(cursor.getLong(cursor.getColumnIndex(AccountTable._ID)));
		account.setLogin(cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_LOGIN)));
		account.setPassword(cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_PASSWORD)));
		account.setEmail(cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_EMAIL)));
		return account;
	}

}
