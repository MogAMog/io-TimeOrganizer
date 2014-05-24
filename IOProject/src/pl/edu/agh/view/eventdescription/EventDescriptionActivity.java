package pl.edu.agh.view.eventdescription;

import pl.edu.agh.tools.DateTimeTools;
import pl.edu.agh.tools.StringTools;
import pl.edu.agh.view.eventlist.EventListAdapter;
import pl.edu.agh.view.eventlist.EventListItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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
		setTextViewText(R.id.EventDescription_Title_Id, getString(R.string.Event_Title), eventListItem.getTitle());
		setTextViewText(R.id.EventDescription_Description_Id, getString(R.string.Event_Description), eventListItem.getDescription());
		setTextViewText(R.id.EventDescription_Date_Id, getString(R.string.EventDate_Date), DateTimeTools.convertDateToString(eventListItem.getDate()));
		setTextViewText(R.id.EventDescription_StartTime_Id, getString(R.string.EventDate_StartTime), DateTimeTools.convertTimeToString(eventListItem.getStartTime()));
		setTextViewText(R.id.EventDescription_EndTime_Id, getString(R.string.EventDate_EndTime), DateTimeTools.convertTimeToString(eventListItem.getEndTime()));
		((TextView) findViewById(R.id.EventDescription_Location_Id)).setText(eventListItem.getLocation().toString());
		setRequirementPicture(R.id.EventDescription_Required_ImageView_Id, eventListItem.isRequired());
		setRequirementPicture(R.id.EventDescription_Constant_ImageView_Id, eventListItem.isConstant());
		setRequirementPicture(R.id.EventDescription_Finished_ImageView_Id, eventListItem.isFinished());
		setRequirementPicture(R.id.EventDescription_Draft_ImageView_Id, eventListItem.getEvent().isDraft());

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.EventDescription_map)).getMap();
		map.setMyLocationEnabled(true);

		LatLng eventLocalization = new LatLng(eventListItem.getLocation().getLatitude(),
				eventListItem.getLocation().getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocalization, 16));
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.addMarker(new MarkerOptions()
				.title(eventListItem.getLocation().getName())
				.snippet(
						new StringBuilder(eventListItem.getLocation()
								.getAddress()).append("\n")
								.append(eventListItem.getLocation().getCity())
								.toString()).position(eventLocalization));
	}

	private void setRequirementPicture(int imageViewResId, boolean isTrue) {
		if (isTrue) {
			((ImageView) findViewById(imageViewResId)).setImageResource(R.drawable.icon_accept);
		} else {
			((ImageView) findViewById(imageViewResId)).setImageResource(R.drawable.decline);
		}
	}
	
	private void setTextViewText(int textViewId, String label, String value) {
		if(!StringTools.isNotNullOrEmpty(value)) {
			((TextView)findViewById(textViewId)).setText(new StringBuilder().append(label).append(": ").toString());
		} else {
			((TextView)findViewById(textViewId)).setText(new StringBuilder().append(label).append(": ").append(value).toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event_description, menu);
		return true;
	}

}
