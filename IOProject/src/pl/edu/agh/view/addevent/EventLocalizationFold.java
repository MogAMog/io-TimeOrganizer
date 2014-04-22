package pl.edu.agh.view.addevent;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.LocationManagementService;
import pl.edu.agh.view.onetimelocalization.OneTimeLocalizationActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;

import com.example.ioproject.R;

public class EventLocalizationFold {
	
	public static final int ONE_TIME_LOCATION_ACTIVITY_ID = 1;
	
	private Activity activity;
	private Button oneTimeLocationButton;
	private SimpleItemSpinner<String> spinnerList;
	private LocationManagementService locationManagementService;
	private ImageView oneTimeLocationImageView;
	private Location oneTimeLocation;
	
	public EventLocalizationFold(Activity activity, int spinnerId, int oneTimeLocationButtonId, int oneTimeLocationImageViewId) {
		this.activity = activity;
		this.oneTimeLocation = new Location();
		this.oneTimeLocationButton = (Button)activity.findViewById(oneTimeLocationButtonId);
		this.oneTimeLocationImageView = (ImageView) activity.findViewById(oneTimeLocationImageViewId);
		this.locationManagementService = new LocationManagementService(new MainDatabaseHelper(activity));
		initializeSpinnerList(spinnerId);
		initializeListeners();
	}
	
	private void initializeSpinnerList(int spinnerId) {
		List<String> itemList = new ArrayList<String>();
		String noSelectionItem = activity.getString(R.string.LocationChoiceFold_DefaultLocationList_NoSelection);
		itemList.add(noSelectionItem);
		for(Location location : locationManagementService.getDefaultLocalizationsAllData()) {
			itemList.add(location.getName());
		}
		spinnerList = new SimpleItemSpinner<String>(activity, spinnerId, noSelectionItem, itemList);
	}
	
	private void initializeListeners() {
		spinnerList.getListSpinner().setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinnerList.setSelectedPosition(position);
				if(spinnerList.checkIfSelectionWasMade()) {
					oneTimeLocationButton.setEnabled(false);
					setLocalizationSelectionWasMade();
				} else {
					oneTimeLocationButton.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		oneTimeLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent oneTimeLocationIntent = new Intent(activity, OneTimeLocalizationActivity.class);
				activity.startActivityForResult(oneTimeLocationIntent, ONE_TIME_LOCATION_ACTIVITY_ID);
			}
		});
	}
	
	public void reinitializeSpinnerList(int spinnerId) {
		initializeSpinnerList(spinnerId);
	}
	
	public Location getLocationForEvent() {
		if(spinnerList.checkIfSelectionWasMade()) {
			return locationManagementService.getLocationByName(spinnerList.getSelectedItem());
		} else {
			locationManagementService.setValuesForNotDefaultLocation(oneTimeLocation);
			return oneTimeLocation;
		}
	}

	public Location getOneTimeLocation() {
		return oneTimeLocation;
	}

	public void setOneTimeLocation(Location oneTimeLocation) {
		this.oneTimeLocation = oneTimeLocation;
	}
	
	public void setLocalizationSelectionWasMade() {
		oneTimeLocationImageView.setImageResource(R.drawable.icon_accept);
	}
}
