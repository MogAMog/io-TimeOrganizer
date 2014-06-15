package pl.edu.agh.view.addevent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.ioproject.R;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.AdapterView.OnItemSelectedListener;

public class EventFrequencyFold {

	public static enum Frequency {
		
		EVERYDAY(1, 1, R.string.EventFrequencyFold_List_Item_Everyday),
		EVERY_WEEK(2, 1, R.string.EventFrequencyFold_List_Item_EveryWeekItem),
		EVERY_TWO_WEEKS(3, 8, R.string.EventFrequencyFold_List_Item_EveryTwoWeeksItem),
		EVERY_FOUR_WEEKS(4, 22, R.string.EventFrequencyFold_List_Item_EveryFourWeeksItem);
		
		private final int position;
		private final int daysToAdd;
		private final int listItemId;
		
		private Frequency(int position, int daysToAdd, int listItemId) {
			this.position = position;
			this.daysToAdd = daysToAdd;
			this.listItemId = listItemId;
		}

		public int getDaysToAdd() {
			return daysToAdd;
		}

		public int getListItemId() {
			return listItemId;
		}

		public int getPosition() {
			return position;
		}
	}
	
	private Activity activity;
	private SimpleItemSpinner<String> spinnerList;
	private CheckBox[] weekdayCheckboxes;
	
	public EventFrequencyFold(Activity activity, int spinnerListId, int[] checkboxesIds) {
		this.activity = activity;
		initializeSpinnerList(spinnerListId);
		initializeWeekdayCheckboxes(checkboxesIds);
		initializeListeners();
	}
	
	private void initializeSpinnerList(int spinnerListId) {
		List<String> items = new ArrayList<String>();
		String noSelectionItem = activity.getString(R.string.EventFrequencyFold_List_NoSelection);
		items.add(noSelectionItem);
		items.add(activity.getString(Frequency.EVERYDAY.getListItemId()));
		items.add(activity.getString(Frequency.EVERY_WEEK.getListItemId()));
		items.add(activity.getString(Frequency.EVERY_TWO_WEEKS.getListItemId()));
		items.add(activity.getString(Frequency.EVERY_FOUR_WEEKS.getListItemId()));
		spinnerList = new SimpleItemSpinner<String>(activity, spinnerListId, noSelectionItem, items);
	}

	private void initializeWeekdayCheckboxes(int[] checkboxesIds) {
		weekdayCheckboxes = new CheckBox[7];
		initializeCheckbox(checkboxesIds[0], Calendar.SUNDAY);
		initializeCheckbox(checkboxesIds[1], Calendar.MONDAY);
		initializeCheckbox(checkboxesIds[2], Calendar.TUESDAY);
		initializeCheckbox(checkboxesIds[3], Calendar.WEDNESDAY);
		initializeCheckbox(checkboxesIds[4], Calendar.THURSDAY);
		initializeCheckbox(checkboxesIds[5], Calendar.FRIDAY);
		initializeCheckbox(checkboxesIds[6], Calendar.SATURDAY);
	}
	
	private void initializeListeners() {
		spinnerList.getListSpinner().setOnItemSelectedListener( new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinnerList.setSelectedPosition(position);
				if(spinnerList.getSelectedItem().equals(activity.getString(Frequency.EVERYDAY.getListItemId()))) {
					markAllCheckBoxedTrue();
					disableAllCheckBoxes();
				} else {
					enableAllCheckBoxes();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	private void initializeCheckbox(int id, final int dayOfWeek) {
		weekdayCheckboxes[dayOfWeek - 1] = (CheckBox) activity.findViewById(id);
	}
	
	public void reinitializeSpinnerList(int spinnerId) {
		initializeSpinnerList(spinnerId);
	}
	
	public boolean isWeekdayChecked(int weekday) {
		return weekdayCheckboxes[weekday - 1].isChecked();
	}

	public void setWeekdayChecked(int weekday, boolean isChecked) {
		weekdayCheckboxes[weekday - 1].setChecked(isChecked);
	}
	
	public void markAllCheckBoxedTrue() {
		for(CheckBox checkBox : weekdayCheckboxes) {
			checkBox.setChecked(true);
		}
	}
	
	public void disableAllCheckBoxes() {
		for(CheckBox checkBox : weekdayCheckboxes) {
			checkBox.setEnabled(false);
		}
	}
	
	public void enableAllCheckBoxes() {
		for(CheckBox checkBox : weekdayCheckboxes) {
			checkBox.setEnabled(true);
		}
	}
	
	public boolean isAtLeastOneWeekDayIsSelected() {
		for(CheckBox checkBox : weekdayCheckboxes) {
			if(checkBox.isChecked()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFrequencyChosen() {
		return spinnerList.checkIfSelectionWasMade();
	}
	
	public Frequency getChosenFrequency() {
		for(Frequency frequency : Frequency.values()) {
			if(activity.getString(frequency.getListItemId()).equals(spinnerList.getSelectedItem())) {
				return frequency;
			}
		}
		return null;
	}
	
	public void setFrequency(Frequency frequency) {
		if(frequency != null) {
			spinnerList.setSelectedPosition(frequency.getPosition());
		} else {
			spinnerList.setSelectedPosition(0);
		}
	}
}

