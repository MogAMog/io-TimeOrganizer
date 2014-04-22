package pl.edu.agh.view.addevent;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import pl.edu.agh.domain.Event;

public class EventTimeAndDescriptionFold {

	private Event event;
	private EditText titleTextView;
	private EditText descriptionTextView;
	
	public EventTimeAndDescriptionFold(Activity activity, Event event, int titleTextViewId, int descriptionTextViewId) {
		this.event = event;
		this.titleTextView = (EditText)activity.findViewById(titleTextViewId);
		this.descriptionTextView = (EditText)activity.findViewById(descriptionTextViewId);
	}
	
	public void initializeListeners() {
		titleTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setTitle(titleTextView.getText().toString());	
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		
		descriptionTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				event.setDescription(descriptionTextView.getText().toString());
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
	}
	
}
