package pl.edu.agh.view.eventlist;

import java.io.Serializable;
import java.util.Date;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;

@SuppressWarnings("serial")
public class EventListItem implements Serializable {
	private Event event;
	private EventDate eventDate;

	public EventListItem(Event event, EventDate eventDate) {
		this.event = event;
		this.eventDate = eventDate;
	}

	public String getTitle() {
		return event.getTitle();
	}
	
	public String getDescription() {
		return event.getDescription();
	}

	public Date getDate() {
		return eventDate.getDate();
	}
	
	public Date getStartTime() {
		return eventDate.getStartTime();
	}

	public Date getEndTime() {
		return eventDate.getEndTime();
	}
	
	public boolean isFinished() {
		return eventDate.isFinished();
	}
	
	public boolean isConstant() {
		return event.isConstant();
	}
	
	public boolean isRequired() {
		return event.isRequired();
	}
	
	public Location getLocation() {
		return event.getDefaultLocation() != null ? event.getDefaultLocation() : eventDate.getLocation();
	}
	
	public void setFinished(boolean isFinished) {
		eventDate.setFinished(isFinished);
	}
	
	
}
