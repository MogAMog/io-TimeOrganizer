package pl.edu.agh.view.addconstantevent;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class EndDatePickerFragment extends DialogFragment {
	
	private SetDatePeriodInterface rootActivity;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		OnDateSetListener listener = new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				rootActivity.setEndDate(year, monthOfYear, dayOfMonth);				
			}
		};
		
		final Calendar c = new GregorianCalendar();
		return new DatePickerDialog(getActivity(), listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.rootActivity = (SetDatePeriodInterface) activity;
	}

}