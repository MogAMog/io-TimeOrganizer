package pl.edu.agh.view.eventlist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventDateManagementService;
import pl.edu.agh.view.eventdescription.EventDescriptionActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ioproject.R;

public class EventListAdapter extends ArrayAdapter<EventListItem> {

	public static final String CURRENT_EVENT_LIST_ITEM_KEY = "CurrentEventListItemKey";
	
	private ArrayList<EventListItem> items;
	private LayoutInflater layoutInflater;
	private EventDateManagementService eventDateManagementService;
	
	public EventListAdapter(Context context, int resource, ArrayList<EventListItem> items) {
		super(context, resource);
		layoutInflater = LayoutInflater.from(context);
		this.items = items;
		this.eventDateManagementService = new EventDateManagementService(new MainDatabaseHelper(context));
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public EventListItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.todo_list_item, parent, false);
			holder = new ViewHolder();
			initHolder(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		fillRow(holder, position);
		
		holder.isFinished.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isChecked = ((CheckBox)v).isChecked();
				eventDateManagementService.changeFinishedEventDateState(getItem(position).getEventDate(), isChecked);
			}
		});
		
		holder.descriptionEventButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), EventDescriptionActivity.class);
				intent.putExtra(CURRENT_EVENT_LIST_ITEM_KEY, getItem(position));
				getContext().startActivity(intent);
			}
		});
		
		return convertView;
	}

	private void fillRow(ViewHolder holder, int position) {
		EventListItem item = items.get(position);
		holder.isFinished.setChecked(item.isFinished());
		holder.title.setText(item.getEvent().isDraft() ? "[" + getContext().getString(R.string.Event_Draft) + "] " +  item.getTitle() : item.getTitle());
		holder.timeFrom.setText(getReadableTime(item.getStartTime()));
		holder.timeTo.setText(getReadableTime(item.getEndTime()));
	}

	private void initHolder(ViewHolder holder, View convertView) {
		holder.isFinished = (CheckBox) convertView.findViewById(R.id.EventListItem_finished_checkbox);
		holder.title = (TextView) convertView.findViewById(R.id.EventListItem_event_title);
		holder.timeFrom = (TextView) convertView.findViewById(R.id.EventListItem_event_start_time);
		holder.timeTo = (TextView) convertView.findViewById(R.id.EventListItem_event_end_time);
		holder.descriptionEventButton = (ImageButton) convertView.findViewById(R.id.EventListItem_event_description_button);
	}

	@SuppressLint("SimpleDateFormat") 
	private String getReadableTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	static class ViewHolder {
		CheckBox isFinished;
		TextView title;
		TextView timeFrom;
		TextView timeTo;
		ImageButton descriptionEventButton;
		ImageButton editEventButton;
		ImageButton showEventLocationButton;
	}
	
	public void showSomeActoin(View view) {
		System.out.println("HERE");
	}

}
