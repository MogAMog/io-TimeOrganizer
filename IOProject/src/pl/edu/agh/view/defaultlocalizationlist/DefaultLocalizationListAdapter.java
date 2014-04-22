package pl.edu.agh.view.defaultlocalizationlist;

import java.io.Serializable;
import java.util.List;

import com.example.ioproject.R;

import pl.edu.agh.domain.Location;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.LocationManagementService;
import pl.edu.agh.tools.StringTools;
import pl.edu.agh.view.showlocationsonmap.ShowLocationsOnMapActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	private LocationManagementService locationManagementService;
	private DefaultLocalizationListActivity activity;
	
	public DefaultLocalizationListAdapter(Context context, int resource, List<Location> items) {
		super(context, resource);
		this.items = items;
		this.activity = (DefaultLocalizationListActivity)context;
		this.locationManagementService = new LocationManagementService(new MainDatabaseHelper(context));
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
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
				alertDialog.setTitle(getContext().getString(R.string.DefaultLocalizationList_DeleteLocalization_AlertDialog_Title));
				alertDialog.setMessage(getContext().getString(R.string.DefaultLocalizationList_DeleteLocalization_AlertDialog_Message) + " " + items.get(position).getName());
				alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						locationManagementService.setLocationNotDefault(items.get(position).getId());
						activity.reloadLocationsList();
						Toast.makeText(activity, items.get(position).getName() + " " + getContext().getString(R.string.DefaultLocalizationList_DeleteLocalization_Toast_Text), Toast.LENGTH_SHORT).show();
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
		
		viewHolder.showOnMapLocationImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showOnLocationMapIntent = new Intent(getContext(), ShowLocationsOnMapActivity.class);
				showOnLocationMapIntent.putExtra(ShowLocationsOnMapActivity.LOCATIONS_TO_SHOW_KEY, (Serializable)items);
				showOnLocationMapIntent.putExtra(ShowLocationsOnMapActivity.MAIN_LOCATION_TO_SHOW_KEY, items.get(position));
				getContext().startActivity(showOnLocationMapIntent);
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
