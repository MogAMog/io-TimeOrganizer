package pl.edu.agh.view.eventdescription;

import pl.edu.agh.tools.DataTimeTools;
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
		initializeActivityTextViews(eventListItem);
		setRequirementPicture(R.id.EventDescription_required_image, eventListItem.isRequired());
		setRequirementPicture(R.id.EventDescription_constant_image, eventListItem.isConstant());
		setRequirementPicture(R.id.EventDescription_finished_image, eventListItem.isFinished());

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
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
			((ImageView) findViewById(imageViewResId))
					.setImageResource(R.drawable.accept);
		} else {
			((ImageView) findViewById(imageViewResId))
					.setImageResource(R.drawable.decline);
		}
	}

	private void initializeActivityTextViews(EventListItem eventListItem) {
		((TextView) findViewById(R.id.EventDescription_title))
				.setText(new StringBuilder()
						.append(getString(R.string.Event_title)).append(": ")
						.append(eventListItem.getTitle()).toString());
		((TextView) findViewById(R.id.EventDescription_description))
				.setText(new StringBuilder()
						.append(getString(R.string.Event_title)).append(": ")
						.append(eventListItem.getDescription()));
		((TextView) findViewById(R.id.EventDescription_date))
				.setText(new StringBuilder()
						.append(getString(R.string.EventDate_date))
						.append(": ")
						.append(DataTimeTools.parseDateFromDate(eventListItem
								.getDate())));
		((TextView) findViewById(R.id.EventDescription_start_time))
				.setText(new StringBuilder()
						.append(getString(R.string.EventDate_start_time))
						.append(": ")
						.append(DataTimeTools.parseTimeFromDate(eventListItem
								.getStartTime())));
		((TextView) findViewById(R.id.EventDescription_end_time))
				.setText(new StringBuilder()
						.append(getString(R.string.EventDate_end_time))
						.append(": ")
						.append(DataTimeTools.parseTimeFromDate(eventListItem
								.getEndTime())));
		((TextView) findViewById(R.id.EventDescription_location))
				.setText(eventListItem.getLocation().toString());
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
