package pl.edu.agh.view.showlocationsonmap;

import java.util.List;

import com.example.ioproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.edu.agh.domain.Location;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShowLocationsOnMapActivity extends Activity {

	public static final String LOCATIONS_TO_SHOW_KEY = "LocationsToShowKey";
	public static final String MAIN_LOCATION_TO_SHOW_KEY = "MainLocationToShowKey";
	
	private List<Location> locationsToShow;
	private Location mainLocation;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_locations_on_map);
		
		locationsToShow = (List<Location>)getIntent().getSerializableExtra(LOCATIONS_TO_SHOW_KEY);
		mainLocation = (Location)getIntent().getSerializableExtra(MAIN_LOCATION_TO_SHOW_KEY);
		
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.ShowLocationsOnMapView_Map)).getMap();
		map.setMyLocationEnabled(true);

		for(Location location : locationsToShow) {
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			map.addMarker(new MarkerOptions()
					.title(location.getName())
					.snippet(new StringBuilder().append(location.getAddress()).append("\n")
							 					.append(location.getCity()).toString())
					.position(latLng));
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mainLocation.getLatitude(), mainLocation.getLongitude()), 16));
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_locations_on_map, menu);
		return true;
	}

}
