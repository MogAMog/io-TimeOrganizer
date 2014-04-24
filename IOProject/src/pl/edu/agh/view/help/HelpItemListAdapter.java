package pl.edu.agh.view.help;

import java.util.List;

import com.example.ioproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpItemListAdapter extends ArrayAdapter<HelpItem>{
	
	private final HelpActivity activity;
	private List<HelpItem> helpItems;
	private LayoutInflater layoutInflater;

	public HelpItemListAdapter(Context context, int resource, List<HelpItem> helpItems) {
		super(context, resource);
		layoutInflater = LayoutInflater.from(context);
		this.helpItems = helpItems;
		this.activity = (HelpActivity) context;
	}
	
	@Override
	public int getCount() {
		return helpItems.size();
	}

	@Override
	public HelpItem getItem(int position) {
		return helpItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView == null) {
			convertView = layoutInflater.inflate(R.layout.help_list_item, parent, false);
			viewHolder = new ViewHolder();
			initHolder(position, viewHolder, convertView);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		fillRow(viewHolder, position);
		
		return convertView;
	}
	
	private void initHolder(int position, ViewHolder holder, View convertView) {
		holder.itemTitle = (TextView) convertView.findViewById(R.id.HelpItem_Title);
		holder.itemPicture = (ImageView) convertView.findViewById(R.id.HelpItem_Image);
	}
	
	private void fillRow(ViewHolder holder, int position) {
		HelpItem helpItem = helpItems.get(position);
		holder.itemTitle.setText(activity.getString(helpItem.getTitleID()));
		holder.itemPicture.setImageResource(helpItem.getPictureID());
	}
	
	
	static class ViewHolder {
		TextView itemTitle;
		ImageView itemPicture;
	}
}