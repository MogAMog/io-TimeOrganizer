package pl.edu.agh.view.eventlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
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
		reloadEventList(null);
	}
	
	public void reloadEventList(List<EventDate> eventDates) {
		ArrayList<EventListItem> todoItems = new ArrayList<EventListItem>();
		
		if (eventDates != null)
			for (EventDate eventDate : eventDates) {
				todoItems.add(new EventListItem(eventDate.getEvent(), eventDate));
			}
		else //wywo³uje siê ten fragment kodu
			for (Event event : mainActivity.getEventList()) {
				for (EventDate eventDate : event.getEventDates()) {
					todoItems.add(new EventListItem(event, eventDate));
				}
			}
		
//		Collections.sort(todoItems, new Comparator<EventListItem>() {
//			@SuppressWarnings("deprecation")
//			@Override
//			public int compare(EventListItem lhs, EventListItem rhs) {
//				if(lhs.getEventDate().getStartTime().getHours() != rhs.getEventDate().getStartTime().getHours()) {
//					return lhs.getEventDate().getStartTime().getHours() > rhs.getEventDate().getStartTime().getHours() ? 1 : -1;
//				}
//				return lhs.getEventDate().getStartTime().getMinutes() >= rhs.getEventDate().getStartTime().getMinutes() ? 1 : -1;
//			}
//		});

		todoListAdapter = new EventListAdapter(getActivity(), R.layout.todo_list_item, todoItems);
		setListAdapter(todoListAdapter);
		
	}
	
	public List<EventDate> getCurrentEventDates() {
		List<EventDate> eventDates = new ArrayList<EventDate>();
		for(EventListItem eli : todoListAdapter.getItems()) {
			eventDates.add(eli.getEventDate());
		}
		return eventDates;
	}
	
}
