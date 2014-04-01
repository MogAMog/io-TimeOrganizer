package pl.edu.agh.view.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ioproject.R;

public class TodoListAdapter extends ArrayAdapter<TodoListItem> {

	private ArrayList<TodoListItem> items;
	private LayoutInflater layoutInflater;

	public TodoListAdapter(Context context, int resource,
			ArrayList<TodoListItem> items) {
		super(context, resource);
		layoutInflater = LayoutInflater.from(context);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public TodoListItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.todo_list_item,
					parent, false);
			holder = new ViewHolder();
			initHolder(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		fillRow(holder, position);

		return convertView;
	}

	private void fillRow(ViewHolder holder, int position) {
		TodoListItem item = items.get(position);
		holder.description.setText(item.getEventDescription());
		holder.title.setText(item.getEventTitle());
		holder.locationAddress.setText(item.getLocationAddress());
		holder.locationName.setText(item.getDefaultLocationName());
		holder.timeFrom.setText(getReadableTime(item.getStartTime()));
		holder.timeTo.setText(getReadableTime(item.getEndTime()));
	}

	private void initHolder(ViewHolder holder, View convertView) {
		holder.title = (TextView) convertView.findViewById(R.id.event_title);
		holder.description = (TextView) convertView
				.findViewById(R.id.event_description);
		holder.locationAddress = (TextView) convertView
				.findViewById(R.id.location_address);
		holder.locationName = (TextView) convertView
				.findViewById(R.id.location_name);
		holder.timeFrom = (TextView) convertView.findViewById(R.id.time_from);
		holder.timeTo = (TextView) convertView.findViewById(R.id.time_to);
	}

	private String getReadableTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	static class ViewHolder {
		TextView title;
		TextView description;
		TextView locationAddress;
		TextView locationName;
		TextView timeFrom;
		TextView timeTo;
	}

}
