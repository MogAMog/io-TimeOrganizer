package pl.edu.agh.view.help;


import java.util.Arrays;
import java.util.List;

import com.example.ioproject.R;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class HelpListFragment extends ListFragment {
	
	private OnHelpItemSelectedListener callback;
	private List<HelpItem> items;
	private int selectedPosition = 0;
	
	public interface OnHelpItemSelectedListener {
		public void onHelpItemSelected();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.items = Arrays.asList(HelpItem.values());
		setListAdapter(new HelpItemListAdapter(getActivity(), R.layout.help_list_item, items));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (OnHelpItemSelectedListener) activity;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		selectedPosition = position;
		callback.onHelpItemSelected();
		getListView().setItemChecked(position, true);
	}
	
	public int getCurrentlySelectedId() {
		return items.get(selectedPosition).ordinal();
	}
	
}
