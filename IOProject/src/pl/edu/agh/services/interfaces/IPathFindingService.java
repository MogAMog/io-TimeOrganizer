package pl.edu.agh.services.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.EventDate;
import pl.edu.agh.exceptions.OptimalPathFindingException;

public interface IPathFindingService {

	// Sets strategy for calculating time between two locations
	public abstract void setDistanceStrategy(IDistanceStrategy distanceStrategy);

	public abstract void addEventDates(Set<EventDate> eventDateSet);

	//clears and adds events
	public abstract void setEventDates(Set<EventDate> eventDateSet);

	//clears list of events and event sets
	public abstract void clear();

	//puts event after certain date
	public abstract void inputEventDate(EventDate eventDate, Date startDate)
			throws OptimalPathFindingException;

	//returns list of events
	public abstract List<EventDate> getEventDateOrder();

	//calculates event order, must be used before get EventDateOrder
	public abstract void calculateOptimalEventOrder()
			throws OptimalPathFindingException;

}