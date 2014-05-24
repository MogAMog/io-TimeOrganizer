package pl.edu.agh.view.eventlist;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ioproject.R;

public class EventListFragment extends ListFragment {

	public interface ProvideEventList {
		public List<Event> getEventList();
		public void reloadCurrentFragmentList();
	}
	
	private EventListAdapter todoListAdapter;
	private ProvideEventList mainActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.todo_list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = (ProvideEventList)activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadEventList();
	}
	
	public void reloadEventList() {
		ArrayList<EventListItem> todoItems = new ArrayList<EventListItem>();

		for (Event event : mainActivity.getEventList()) {
			for (EventDate eventDate : event.getEventDates()) {
				todoItems.add(new EventListItem(event, eventDate));
			}
		}

		todoListAdapter = new EventListAdapter(getActivity(), R.layout.todo_list_item, todoItems);
		setListAdapter(todoListAdapter);
	}
	
}
