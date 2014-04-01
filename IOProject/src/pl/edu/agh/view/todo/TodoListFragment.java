package pl.edu.agh.view.todo;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.databasemanagement.MainDatabaseHelper;
import pl.edu.agh.services.EventManagementService;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ioproject.R;

public class TodoListFragment extends ListFragment {

	private TodoListAdapter todoListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.todo_list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<TodoListItem> todoItems = new ArrayList<TodoListItem>();

		EventManagementService ems = new EventManagementService(
				new MainDatabaseHelper(getActivity()));

		List<Event> events = ems.getAll();
		for (Event event : events) {
			for (EventDate eventDate : event.getEventDates()) {
				todoItems.add(new TodoListItem(event, eventDate));
			}
		}

		todoListAdapter = new TodoListAdapter(getActivity(),
				R.layout.todo_list_item, todoItems);
		setListAdapter(todoListAdapter);
	}
}
