package pl.edu.agh.view.fragments.pickers;

import java.util.Calendar;
import java.util.GregorianCalendar;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.widget.TimePicker;

public class EndTimePickerFragment extends DialogFragment {

	private SetTimePeriodInterface rootActivity;
	private Calendar calendar = new GregorianCalendar();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.rootActivity = (SetTimePeriodInterface)activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				rootActivity.setEndTime(hourOfDay, minute);
			}
		};
		return new TimePickerDialog(getActivity(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
	}
}
