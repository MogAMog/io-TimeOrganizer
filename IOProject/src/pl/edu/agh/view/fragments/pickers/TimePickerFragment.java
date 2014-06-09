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

public class TimePickerFragment extends DialogFragment {
	
	public static interface SetTimeInterface {
		public abstract void setTime(int hours, int minutes);
	}
	
	private SetTimeInterface rootActivity;
	private Calendar calendar = new GregorianCalendar();
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		OnTimeSetListener listener = new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				rootActivity.setTime(hourOfDay, minute);
			}
		};
		
		return new TimePickerDialog(getActivity(), listener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.rootActivity = (SetTimeInterface)activity;
	}
	
}