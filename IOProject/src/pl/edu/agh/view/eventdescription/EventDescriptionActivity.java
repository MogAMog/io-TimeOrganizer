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

public class EventDescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_description);
		
		EventListItem eventListItem = (EventListItem)getIntent().getSerializableExtra(EventListAdapter.CURRENT_EVENT_LIST_ITEM_KEY);
		
		((TextView)findViewById(R.id.EventDescription_title)).setText(new StringBuilder().append(getString(R.string.Event_title))
																			.append(": ")
																			.append(eventListItem.getEventTitle()).toString());
		((TextView)findViewById(R.id.EventDescription_description)).setText(new StringBuilder().append(getString(R.string.Event_title))
																			.append(": ")
																			.append(eventListItem.getEventDescription()));
		((TextView)findViewById(R.id.EventDescription_date)).setText(new StringBuilder().append(getString(R.string.EventDate_date))
																			.append(": ")
																			.append(DataTimeTools.parseDateFromDate(eventListItem.getEventDate().getDate())));
		((TextView)findViewById(R.id.EventDescription_start_time)).setText(new StringBuilder().append(getString(R.string.EventDate_start_time))
																			.append(": ")
																			.append(DataTimeTools.parseTimeFromDate(eventListItem.getEventDate().getStartTime())));
		((TextView)findViewById(R.id.EventDescription_end_time)).setText(new StringBuilder().append(getString(R.string.EventDate_end_time))
																			.append(": ")
																			.append(DataTimeTools.parseTimeFromDate(eventListItem.getEventDate().getEndTime())));
		((TextView)findViewById(R.id.EventDescription_location)).setText(eventListItem.getEventDate().getLocation().toString());
		setRequirementPicture(R.id.EventDescription_required_image, eventListItem.getEvent().isRequired());
		setRequirementPicture(R.id.EventDescription_constant_image, eventListItem.getEvent().isConstant());
		setRequirementPicture(R.id.EventDescription_finished_image, eventListItem.getEventDate().isFinished());
	}

	private void setRequirementPicture(int imageViewResId, boolean isTrue) {
		if(isTrue) {
			((ImageView)findViewById(imageViewResId)).setImageResource(R.drawable.accept);
		} else {
			((ImageView)findViewById(imageViewResId)).setImageResource(R.drawable.decline);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event_description, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.EventDescription_ActionBar_Back:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
