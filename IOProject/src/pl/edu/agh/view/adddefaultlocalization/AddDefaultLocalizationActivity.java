package pl.edu.agh.view.adddefaultlocalization;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.errors.FormValidationError;
import pl.edu.agh.services.LocationManagementService;
import pl.edu.agh.services.LocationSearchingService;
import pl.edu.agh.view.fragments.dialogs.ErrorDialog;
import android.app.Activity;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ioproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddDefaultLocalizationActivity extends Activity {

	private LatLng chosenLatLng;
	private TextView coordinatesState;
	private pl.edu.agh.domain.Location location = new pl.edu.agh.domain.Location();
	private LocationManagementService locationManagementService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_default_localization);
		
		locationManagementService = new LocationManagementService(new MainDatabaseHelper(this));
		coordinatesState = (TextView)findViewById(R.id.AddDefaultLocalizationView_localization_cooridnates_state);
		
		final GoogleMap map = ((MapFragment)getFragmentManager().findFragmentById(R.id.AddDefaultLocalizationView_map)).getMap();
		map.setMyLocationEnabled(true);
		
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Location currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
		LatLng currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		
		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				map.clear();
				map.addMarker(new MarkerOptions()
					.title(getString(R.string.AddDefaultLocalizationActivity_current_chosen_localization))
					.position(point));
				chosenLatLng = point;
				coordinatesState.setText(getString(R.string.AddDefaultLocalizationActivity_corrdinates_chosen_ok));
				coordinatesState.setTextColor(Color.GREEN);
				location.setLatitude(chosenLatLng.latitude);
				location.setLongitude(chosenLatLng.longitude);
			}
		});
		
		((TextView)findViewById(R.id.AddDefaultLocalizationView_add_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				location.setName(((TextView)findViewById(R.id.AddDefaultLocalizationView_localization_name)).getText().toString());
				location.setAddress(((TextView)findViewById(R.id.AddDefaultLocalizationView_localization_address)).getText().toString());
				location.setCity(((TextView)findViewById(R.id.AddDefaultLocalizationView_localization_city)).getText().toString());
				location.setDefaultLocation(true);
				List<FormValidationError> errors = locationManagementService.validate(location);
				if(!errors.isEmpty()) {
					ErrorDialog.createDialog(v.getContext(), errors).show();
				} else {
					locationManagementService.insert(location);
					finish();
				}
			}
		});
		
		((ImageButton)findViewById(R.id.AddDefaultLocalizationView_searchImageButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String address = ((TextView)findViewById(R.id.AddDefaultLocalizationView_localization_address)).getText().toString();
					String city = ((TextView)findViewById(R.id.AddDefaultLocalizationView_localization_city)).getText().toString();
					LatLng latLng;
					latLng = new LocationSearchingService(address + "," + city).execute("").get();
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
					map.clear();
					map.addMarker(new MarkerOptions()
						.title(getString(R.string.AddDefaultLocalizationActivity_current_chosen_localization))
						.position(latLng));
					chosenLatLng = latLng;
					coordinatesState.setText(getString(R.string.AddDefaultLocalizationActivity_corrdinates_chosen_ok));
					coordinatesState.setTextColor(Color.GREEN);
					location.setLatitude(chosenLatLng.latitude);
					location.setLongitude(chosenLatLng.longitude);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_default_localization, menu);
		return true;
	}

}
