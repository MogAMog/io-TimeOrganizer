package pl.edu.agh.view.addevent;

import java.util.Calendar;
import java.util.GregorianCalendar;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.widget.TimePicker;

public class StartTimePickerFragment extends DialogFragment {

	private SetTimePeriodInterface rootActivity;

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
				rootActivity.setStartTime(hourOfDay, minute);
			}
		};
		Calendar c = new GregorianCalendar();
		return new TimePickerDialog(getActivity(), onTimeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
	}
	
	
}
