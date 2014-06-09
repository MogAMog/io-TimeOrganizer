package pl.edu.agh.view.onetimelocalization;

import java.util.concurrent.ExecutionException;

import pl.edu.agh.services.LocationSearchingService;

import com.example.ioproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class OneTimeLocalizationActivity extends Activity {

	public static final String LOCATION_RESULT_KEY = "OneTimeLocalizationActivity.LocationResult";
	
	private LatLng chosenLatLng;
	private TextView coordinatesState;
	private pl.edu.agh.domain.Location location = new pl.edu.agh.domain.Location();
	private boolean isChosen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_time_localization);
	
		coordinatesState = (TextView)findViewById(R.id.OneTimeLocalizationView_localization_cooridnates_state);
		
		final GoogleMap map = ((MapFragment)getFragmentManager().findFragmentById(R.id.OneTimeLocalizationView_map)).getMap();
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
				isChosen = true;
			}
		});
		
		((ImageButton)findViewById(R.id.OneTimeLocalizationView_searchImageButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String address = ((TextView)findViewById(R.id.OneTimeLocalizationView_localization_address)).getText().toString();
					String city = ((TextView)findViewById(R.id.OneTimeLocalizationView_localization_city)).getText().toString();
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
					location.setLatitude(chosenLatLng.latitude);
					location.setLongitude(chosenLatLng.longitude);
					isChosen = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void returnLocationToActivity(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra(OneTimeLocalizationActivity.LOCATION_RESULT_KEY, location);
		if(isChosen == true) {
			setResult(RESULT_OK, returnIntent);
		} else {
			setResult(RESULT_CANCELED, returnIntent);
		}
		finish();
	}

}