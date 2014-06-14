package pl.edu.agh.view.addevent;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.EventTemplate;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventTemplateManagementService;

import com.example.ioproject.R;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class TemplateChooseFold {

	public interface SetEventAsTemplate {
		void setFieldsFromTemplate(EventTemplate eventTemplate);
		void clearAllFields();
	}
	
	private Activity activity;
	private SimpleItemSpinner<String> spinnerList;
	private EventTemplateManagementService eventTemplateManagementService;
	
	public TemplateChooseFold(Activity activity, int spinnerListId, boolean isConstantEvent) {
		super();
		this.eventTemplateManagementService = new EventTemplateManagementService(new MainDatabaseHelper(activity));
		this.activity = activity;
		this.spinnerList = initializeSpinnerList(spinnerListId, isConstantEvent);
		initializeListeners();
	}
	
	private SimpleItemSpinner<String> initializeSpinnerList(int spinnerListId, boolean isConstantEvent) {
		List<String> items = new ArrayList<String>();
		String noSelectionItem = activity.getString(R.string.EventFrequencyFold_List_NoSelection);
		items.add(noSelectionItem);
		if(!isConstantEvent) {
			items.addAll(eventTemplateManagementService.getSimpleEventNames());
		} else {
			items.addAll(eventTemplateManagementService.getContantEventNames());
		}
		return new SimpleItemSpinner<String>(activity, spinnerListId, noSelectionItem, items);
	}
	
	private void initializeListeners() {
		
		spinnerList.getListSpinner().setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				spinnerList.setSelectedPosition(arg2);
				if(!spinnerList.getSelectedItem().equals(activity.getString(R.string.EventFrequencyFold_List_NoSelection))) {
					((SetEventAsTemplate)activity).setFieldsFromTemplate(eventTemplateManagementService.getEventTemplateByName(spinnerList.getSelectedItem()));
				} else {
					((SetEventAsTemplate)activity).clearAllFields();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
		});
	}
	
	
	
	
	
}
