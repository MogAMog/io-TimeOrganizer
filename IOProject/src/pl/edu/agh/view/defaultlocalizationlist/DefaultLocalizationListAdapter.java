package pl.edu.agh.view.defaultlocalizationlist;

import java.util.List;

import com.example.ioproject.R;

import pl.edu.agh.domain.Location;
import pl.edu.agh.tools.StringTools;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultLocalizationListAdapter extends ArrayAdapter<Location> {

	private static class ViewHolder {
		private final TextView locationNameTextView;
		private final TextView locationAddressTextView;
		private final TextView locationCityTextView;
		private final ImageButton deleteLocationImageButton;
		private final ImageButton showOnMapLocationImageButton;
		
		ViewHolder(View view) {
			locationNameTextView = (TextView) view.findViewById(R.id.DefaultLocalizationList_Item_Location_Name_TextView_Id);
			locationAddressTextView = (TextView) view.findViewById(R.id.DefaultLocalizationList_Item_Location_Address_TextView_Id);
			locationCityTextView = (TextView) view.findViewById(R.id.DefaultLocalizationList_Item_Location_City_TextView_Id);
			deleteLocationImageButton = (ImageButton) view.findViewById(R.id.DefaultLocalizationList_DeleteLocation_ImageButton_TextView_Id);
			showOnMapLocationImageButton = (ImageButton) view.findViewById(R.id.DefaultLocalizationList_ShowOnMap_ImageButton_TextView_Id);
		}

		public void fillRow(Location location) {
			setValueIfNotNullOrEmpty(locationNameTextView, location.getName());
			setValueIfNotNullOrEmpty(locationAddressTextView, location.getAddress());
			setValueIfNotNullOrEmpty(locationCityTextView, location.getCity());
		}
		
		private void setValueIfNotNullOrEmpty(TextView textView, String value) {
			if(StringTools.isNotNullOrEmpty(value)) {
				textView.setText(value);
			}
		}
	}
	
	public static final String CURRENT_LOCATION_KEY = "CurrentLocationSelectedKey";
	private List<Location> items;
	
	public DefaultLocalizationListAdapter(Context context, int resource, List<Location> items) {
		super(context, resource);
		this.items = items;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.default_localization_list_item, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		viewHolder.fillRow(items.get(position));
		
		viewHolder.deleteLocationImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Delete Location " + items.get(position).getName(), Toast.LENGTH_LONG).show();
			}
		});
		
		viewHolder.showOnMapLocationImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Show On Map " + items.get(position).getName(), Toast.LENGTH_LONG).show();
			}
		});
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Location getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
}
