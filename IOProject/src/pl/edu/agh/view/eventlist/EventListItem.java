package pl.edu.agh.view.eventlist;

import java.io.Serializable;
import java.util.Date;

import pl.edu.agh.domain.Event;
import pl.edu.agh.domain.EventDate;

public class EventListItem implements Serializable {
	private Event event;
	private EventDate eventDate;

	public EventListItem(Event event, EventDate eventDate) {
		this.event = event;
		this.eventDate = eventDate;
	}

	public String getEventTitle() {
		return event.getTitle();
	}

	public String getEventDescription() {
		return event.getDescription();
	}

	public Date getStartTime() {
		return eventDate.getStartTime();
	}

	public Date getEndTime() {
		return eventDate.getEndTime();
	}

	public boolean isRequired() {
		return event.isRequired();
	}

	public boolean isConstant() {
		return event.isConstant();
	}
	
	public boolean isFinished() {
		return eventDate.isFinished();
	}
	
	public void setFinished(boolean isFinished) {
		eventDate.setFinished(isFinished);
	}

	public String getDefaultLocationName() {
		if (event.getDefaultLocation() != null)
			return event.getDefaultLocation().getName();
		else
			return "";
	}

	public String getLocationAddress() {
		if (event.getDefaultLocation() != null)
			return event.getDefaultLocation().getAddress();
		else
			return "";
	}

	public Event getEvent() {
		return event;
	}

	public EventDate getEventDate() {
		return eventDate;
	}
	
	
}
