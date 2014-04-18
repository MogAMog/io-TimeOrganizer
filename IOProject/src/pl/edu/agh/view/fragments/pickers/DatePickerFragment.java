package pl.edu.agh.view.fragments.pickers;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment {
	
	public static interface SetDateInterface {
		public abstract void setDate(int year, int month, int day);
	}
	
	private SetDateInterface rootActivity;
	private Calendar calendar = new GregorianCalendar();
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		OnDateSetListener listener = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				rootActivity.setDate(year, monthOfYear, dayOfMonth); 				
			}
		};
		return new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.rootActivity = (SetDateInterface)activity;
	}
	
}
