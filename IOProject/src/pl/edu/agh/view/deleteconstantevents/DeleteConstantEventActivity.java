package pl.edu.agh.view.deleteconstantevents;

import java.util.List;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;

import com.example.ioproject.R;

import android.os.Bundle;
import android.app.ListActivity;

public class DeleteConstantEventActivity extends ListActivity {

	private DeleteConstantEventListAdapter deleteConstantEventListAdapter;
	private EventManagementService eventManagementService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_constant_event);
		this.eventManagementService = new EventManagementService(new MainDatabaseHelper(this));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		reloadEventList();
	}

	public void reloadEventList() {
		eventManagementService.clearCache();
		List<Event> events = eventManagementService.getAllConstantEvents();
		deleteConstantEventListAdapter = new DeleteConstantEventListAdapter(this, R.layout.delete_constant_event_item, events);
		setListAdapter(deleteConstantEventListAdapter);
	}

}
