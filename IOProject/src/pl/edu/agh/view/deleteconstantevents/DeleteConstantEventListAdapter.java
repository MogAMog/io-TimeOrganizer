package pl.edu.agh.view.deleteconstantevents;

import java.util.List;

import com.example.ioproject.R;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
import pl.edu.agh.tools.StringTools;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class DeleteConstantEventListAdapter extends ArrayAdapter<Event> {

	private static class ViewHolder {
		private final TextView titleTextView;
		private final TextView eventDateCountTextView;
		private final ImageButton deleteEventImageButton;
		
		ViewHolder(View view) {
			titleTextView = (TextView) view.findViewById(R.id.DeleteEventListItem_Title);
			eventDateCountTextView = (TextView) view.findViewById(R.id.DeleteEventListItem_EventDateCount);
			deleteEventImageButton = (ImageButton) view.findViewById(R.id.DeleteEventListItem_DeleteEventButton);
		}

		public void fillRow(Event event) {
			setValueIfNotNullOrEmpty(titleTextView, event.getTitle());
			setValueIfNotNullOrEmpty(eventDateCountTextView, "Events Amount: " + event.getEventDates().size());
		}
		
		private void setValueIfNotNullOrEmpty(TextView textView, String value) {
			if(StringTools.isNotNullOrEmpty(value)) {
				textView.setText(value);
			}
		}
	}
	
	private List<Event> items;
	private EventManagementService eventManagementService;
	private DeleteConstantEventActivity activity;
	
	public DeleteConstantEventListAdapter(Context context, int resource, List<Event> items) {
		super(context, resource, items);
		this.items = items;
		this.activity = (DeleteConstantEventActivity)context;
		this.eventManagementService = new EventManagementService(new MainDatabaseHelper(context));
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.delete_constant_event_item, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		viewHolder.fillRow(items.get(position));
		
		viewHolder.deleteEventImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
				alertDialog.setTitle(getContext().getString(R.string.DeleteConstantEvent_Dialog_Title));
				alertDialog.setMessage(getContext().getString(R.string.DeleteConstantEvent_Dialog_Message) + " " + items.get(position).getTitle());
				alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						eventManagementService.deleteEvent(items.get(position));
						activity.reloadEventList();
					}
				});
				alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		});
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Event getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
