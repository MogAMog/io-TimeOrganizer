package pl.edu.agh.view.defaultlocalizationlist;

import java.util.List;

import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.LocationManagementService;

import com.example.ioproject.R;

import android.os.Bundle;
import android.app.ListActivity;

public class DefaultLocalizationListActivity extends ListActivity {

	private DefaultLocalizationListAdapter localizationListAdapter;
	private LocationManagementService locationManagementService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default_localization_list);
		
		locationManagementService = new LocationManagementService(new MainDatabaseHelper(this));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		reloadLocationsList();
	}

	public void reloadLocationsList() {
		List<Location> defaultLocations = locationManagementService.getDefaultLocalizationsAllData();
		localizationListAdapter = new DefaultLocalizationListAdapter(this, R.layout.default_localization_list_item, defaultLocations);
		setListAdapter(localizationListAdapter);
	}
	
}
