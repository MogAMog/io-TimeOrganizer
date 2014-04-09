package pl.edu.agh.view.eventdescription;

import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.view.eventlist.EventListAdapter;
import pl.edu.agh.view.eventlist.EventListItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ioproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventDescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_description);

		EventListItem eventListItem = (EventListItem) getIntent().getSerializableExtra(EventListAdapter.CURRENT_EVENT_LIST_ITEM_KEY);
		setTextViewText(R.id.EventDescription_title, getString(R.string.Event_title), eventListItem.getTitle());
		setTextViewText(R.id.EventDescription_description, getString(R.string.Event_title), eventListItem.getDescription());
		setTextViewText(R.id.EventDescription_date, getString(R.string.EventDate_date), DateTimeTools.convertDateToString(eventListItem.getDate()));
		setTextViewText(R.id.EventDescription_start_time, getString(R.string.EventDate_start_time), DateTimeTools.convertTimeToString(eventListItem.getStartTime()));
		setTextViewText(R.id.EventDescription_end_time, getString(R.string.EventDate_end_time), DateTimeTools.convertTimeToString(eventListItem.getEndTime()));
		((TextView) findViewById(R.id.EventDescription_location)).setText(eventListItem.getLocation().toString());
		setRequirementPicture(R.id.EventDescription_required_image, eventListItem.isRequired());
		setRequirementPicture(R.id.EventDescription_constant_image, eventListItem.isConstant());
		setRequirementPicture(R.id.EventDescription_finished_image, eventListItem.isFinished());

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.EventDescription_map)).getMap();
		map.setMyLocationEnabled(true);

		LatLng sydney = new LatLng(eventListItem.getLocation().getLatitude(),
				eventListItem.getLocation().getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.addMarker(new MarkerOptions()
				.title(eventListItem.getLocation().getName())
				.snippet(
						new StringBuilder(eventListItem.getLocation()
								.getAddress()).append("\n")
								.append(eventListItem.getLocation().getCity())
								.toString()).position(sydney));
	}

	private void setRequirementPicture(int imageViewResId, boolean isTrue) {
		if (isTrue) {
			((ImageView) findViewById(imageViewResId)).setImageResource(R.drawable.accept);
		} else {
			((ImageView) findViewById(imageViewResId)).setImageResource(R.drawable.decline);
		}
	}
	
	private void setTextViewText(int textViewId, String label, String value) { 
		((TextView)findViewById(textViewId)).setText(new StringBuilder().append(label).append(": ").append(value).toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event_description, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.EventDescription_ActionBar_Back:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
