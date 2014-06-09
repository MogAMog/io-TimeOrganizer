package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.EventDate;
import pl.edu.agh.exceptions.OptimalPathFindingException;
import pl.edu.agh.services.interfaces.IDistanceStrategy;
import pl.edu.agh.services.interfaces.IPathFindingService;
import pl.edu.agh.tools.DateTimeTools;

public class OptimalPathFindingService implements IPathFindingService {

	private List<EventDate> constantRequiredEventList;
	private List<EventDate> notConstantRequiredEventList;
	private List<EventDate> constantNotRequiredEventList;
	private List<EventDate> notConstantNotRequiredEventList;
	private List<EventDate> eventList;
	private IDistanceStrategy distanceStrategy;

	private Date morningBoundary;
	private Date eveningBoundary;

	public OptimalPathFindingService() {
		constantRequiredEventList = new ArrayList<EventDate>();
		notConstantRequiredEventList = new ArrayList<EventDate>();
		constantNotRequiredEventList = new ArrayList<EventDate>();
		notConstantNotRequiredEventList = new ArrayList<EventDate>();
		eventList = new ArrayList<EventDate>();
	}

	public OptimalPathFindingService(Set<EventDate> eventDateSet) {
		this();
		addEventDates(eventDateSet);
	}

	public OptimalPathFindingService(IDistanceStrategy distanceStrategy) {
		this();
		setDistanceStrategy(distanceStrategy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.agh.services.IPathFindingService#setDistanceStrategy(pl.edu.agh
	 * .services.interfaces.IDistanceStrategy)
	 */
	@Override
	public void setDistanceStrategy(IDistanceStrategy distanceStrategy) {
		this.distanceStrategy = distanceStrategy;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.agh.services.IPathFindingService#addEventDates(java.util.Set)
	 */
	@Override
	public void addEventDates(Set<EventDate> eventDateSet) {
		for (EventDate eventDate : eventDateSet) {
			if (eventDate.getEvent().isRequired()
					&& eventDate.getEvent().isConstant()) {
				constantRequiredEventList.add(eventDate);
			} else if (eventDate.getEvent().isRequired()) {
				notConstantRequiredEventList.add(eventDate);
			} else if (eventDate.getEvent().isConstant()) {
				constantNotRequiredEventList.add(eventDate);
			} else {
				notConstantNotRequiredEventList.add(eventDate);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.agh.services.IPathFindingService#setEventDates(java.util.Set)
	 */
	@Override
	public void setEventDates(Set<EventDate> eventDateSet) {
		clear();
		addEventDates(eventDateSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.agh.services.IPathFindingService#clear()
	 */
	@Override
	public void clear() {
		constantRequiredEventList = new ArrayList<EventDate>();
		notConstantRequiredEventList = new ArrayList<EventDate>();
		constantNotRequiredEventList = new ArrayList<EventDate>();
		notConstantNotRequiredEventList = new ArrayList<EventDate>();
		eventList = new ArrayList<EventDate>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.agh.services.IPathFindingService#inputEventDate(pl.edu.agh.domain
	 * .EventDate, java.util.Date)
	 */
	@Override
	public void inputEventDate(EventDate eventDate, Date startDate)
			throws OptimalPathFindingException {
		boolean placeForEventFound = false;
		if (eventDate.getEvent().isConstant()) {
			Calendar eventStartTime = DateTimeTools.getCalendarFromDate(eventDate
					.getStartTime());
			int newEventLocation;
			for (newEventLocation = 0; newEventLocation < eventList.size()
					&& DateTimeTools.getCalendarFromDate(
							eventList.get(newEventLocation).getStartTime())
							.before(eventStartTime); newEventLocation++) {
			}
			eventList.add(newEventLocation, eventDate);
			if(isPlanSuitable(eventList))
				placeForEventFound = true;
			else {
				eventList.remove(eventDate);
			}
		} else {
			for (int i = 1; i < eventList.size() && !placeForEventFound; i++) {
				EventDate beforeEvent;
				if (!DateTimeTools.happenedBefore(eventList.get(i - 1)
						.getEndTime(), startDate)) {
					beforeEvent = eventList.get(i - 1);
				} else {
					beforeEvent = new EventDate(eventList.get(i - 1)
							.getLocation(), eventList.get(i - 1).getDate(),
							startDate, startDate, 0, false);
				}
				if (eventList.get(i).getStartTime().after(startDate)
						&& canEventFitBetween(eventDate, beforeEvent,
								eventList.get(i))) {

					Date startTime = DateTimeTools.addMinutesToDate(
							beforeEvent.getEndTime(),
							getTimeDistance(beforeEvent, eventDate));
					eventDate.setStartTime(startTime);
					Date endTime = DateTimeTools.addMinutesToDate(startTime,
							eventDate.getDuration());
					eventDate.setEndTime(endTime);
					eventList.add(i, eventDate);
					placeForEventFound = true;
				}
			}
		}
		if (!placeForEventFound) {
			throw new OptimalPathFindingException(
					"Unable to find place for the event");
		} else {
			Set<EventDate> tempSet = new HashSet<EventDate>();
			tempSet.add(eventDate);
			this.addEventDates(tempSet);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.agh.services.IPathFindingService#getEventDateOrder()
	 */
	@Override
	public List<EventDate> getEventDateOrder() {
		return eventList;

	}

	private boolean canEventFitBetween(EventDate event, EventDate beforeEvent,
			EventDate afterEvent) {
		return getTimeOccupiedByEvent(event, beforeEvent, afterEvent) < DateTimeTools
				.getMinuteDifferenceBetweenTwoDates(beforeEvent.getEndTime(),
						afterEvent.getStartTime());
	}

	private long getTimeOccupiedByEvent(EventDate event, EventDate beforeEvent,
			EventDate afterEvent) {
		return getTimeDistance(beforeEvent, event) + event.getDuration()
				+ getTimeDistance(event, afterEvent);
	}

	private boolean areOverlaping(EventDate event1, EventDate event2) {
		return DateTimeTools.getMinuteDifferenceBetweenTwoDates(
				event1.getEndTime(), event2.getStartTime()) < getTimeDistance(
				event1, event2);
	}

	private int getTimeDistance(EventDate event1, EventDate event2) {
		if (event1.getLocation() == null || event2.getLocation() == null) {
			return 0;
		}
		return distanceStrategy.getTimeDistanceBetween(event1.getLocation(),
				event2.getLocation());
	}

	private void fitEventAfter(EventDate event, EventDate beforeEvent,
			List<EventDate> tempEventList) {
		int eventIterator = 0;
		while (tempEventList.get(eventIterator).getId() != beforeEvent.getId()) {
			eventIterator++;
		}

		tempEventList.add(eventIterator + 1, event);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) beforeEvent.getEndTime().clone());
		calendar.add(Calendar.MINUTE, (int) getTimeDistance(beforeEvent, event));
		event.setStartTime(calendar.getTime());

		calendar = Calendar.getInstance();
		calendar.setTime((Date) beforeEvent.getEndTime().clone());
		calendar.add(Calendar.MINUTE, (int) getTimeDistance(beforeEvent, event));
		calendar.add(Calendar.MINUTE, event.getDuration());
		event.setEndTime(calendar.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.agh.services.IPathFindingService#calculateOptimalEventOrder()
	 */
	@Override
	public void calculateOptimalEventOrder() throws OptimalPathFindingException {
		List<EventDate> tempEventList = new ArrayList<EventDate>();
		
		if(!constantRequiredEventList.isEmpty())
			placeConstantRequiredEvents(tempEventList);
		
		if(!notConstantRequiredEventList.isEmpty())
			placeRequiredEvents(tempEventList);
		
		if(!constantNotRequiredEventList.isEmpty())
			placeConstantEvents(tempEventList);
		
		if(!notConstantNotRequiredEventList.isEmpty())
			placeNonConstantEvents(tempEventList);
		eventList = tempEventList;
	}

	private void placeNonConstantEvents(List<EventDate> tempEventList) {
		Set<EventDate> tempNotConstantEventSet = new HashSet<EventDate>(
				notConstantNotRequiredEventList);
		EventDate morningBoundaryEvent = createBoundaryEvent(morningBoundary);
		EventDate eveningBoundaryEvent = createBoundaryEvent(eveningBoundary);
		addBoundaryEvents(tempEventList, morningBoundaryEvent,
				eveningBoundaryEvent);
		while (!tempNotConstantEventSet.isEmpty()) {
			EventDate bestBeforeEvent = null;
			EventDate bestEvent = null;
			long bestTime = Long.MAX_VALUE;
			for (EventDate beforeEvent : tempEventList.subList(0,
					tempEventList.size() - 1)) {
				for (EventDate event : tempNotConstantEventSet) {
					EventDate afterEvent = tempEventList.get(tempEventList
							.indexOf(beforeEvent) + 1);
					if (canEventFitBetween(event, beforeEvent, afterEvent)) {
						long currTime = getTimeOccupiedByEvent(event,
								beforeEvent, afterEvent);
						if (currTime < bestTime) {
							bestTime = currTime;
							bestEvent = event;
							bestBeforeEvent = beforeEvent;
						}
					}
				}
			}
			if (bestEvent == null) {
				break;
			}

			tempNotConstantEventSet.remove(bestEvent);
			fitEventAfter(bestEvent, bestBeforeEvent, tempEventList);
		}
		removeBoundaryEvents(tempEventList, morningBoundaryEvent,
				eveningBoundaryEvent);

	}

	private void placeConstantEvents(List<EventDate> tempEventList) {
		for (EventDate event : constantNotRequiredEventList) {
			Calendar eventStartTime = DateTimeTools.getCalendarFromDate(event
					.getStartTime());
			int newEventLocation;
			for (newEventLocation = 0; newEventLocation < tempEventList.size()
					&& DateTimeTools.getCalendarFromDate(
							tempEventList.get(newEventLocation).getStartTime())
							.before(eventStartTime); newEventLocation++) {
			}
			tempEventList.add(newEventLocation, event);
			if (!isPlanSuitable(tempEventList)) {
				tempEventList.remove(event);
			}
		}
	}

	private void placeRequiredEvents(List<EventDate> tempEventList)
			throws OptimalPathFindingException {
		Set<EventDate> tempNotConstantEventSet = new HashSet<EventDate>(
				notConstantRequiredEventList);
		EventDate morningBoundaryEvent = createBoundaryEvent(morningBoundary);
		EventDate eveningBoundaryEvent = createBoundaryEvent(eveningBoundary);
		addBoundaryEvents(tempEventList, morningBoundaryEvent,
				eveningBoundaryEvent);
		while (!tempNotConstantEventSet.isEmpty()) {
			EventDate bestBeforeEvent = null;
			EventDate bestEvent = null;
			long bestTime = Long.MAX_VALUE;
			for (EventDate beforeEvent : tempEventList.subList(0,
					tempEventList.size() - 1)) {
				for (EventDate event : tempNotConstantEventSet) {
					EventDate afterEvent = tempEventList.get(tempEventList
							.indexOf(beforeEvent) + 1);
					if (canEventFitBetween(event, beforeEvent, afterEvent)) {
						long currTime = getTimeOccupiedByEvent(event,
								beforeEvent, afterEvent);
						if (currTime < bestTime) {
							bestTime = currTime;
							bestEvent = event;
							bestBeforeEvent = beforeEvent;
						}
					}
				}
			}
			if (bestEvent == null) {
				throw new OptimalPathFindingException(
						"Unable to find optimal schedule.");
			}
			tempNotConstantEventSet.remove(bestEvent);
			fitEventAfter(bestEvent, bestBeforeEvent, tempEventList);
		}
		removeBoundaryEvents(tempEventList, morningBoundaryEvent,
				eveningBoundaryEvent);
	}

	private void placeConstantRequiredEvents(List<EventDate> tempEventList)
			throws OptimalPathFindingException {
		for (EventDate event : constantRequiredEventList) {
			Calendar eventStartTime = DateTimeTools.getCalendarFromDate(event
					.getStartTime());
			int newEventLocation;
			for (newEventLocation = 0; newEventLocation < tempEventList.size()
					&& DateTimeTools.getCalendarFromDate(
							tempEventList.get(newEventLocation).getStartTime())
							.before(eventStartTime); newEventLocation++) {
			}
			tempEventList.add(newEventLocation, event);
		}

		if (!isPlanSuitable(tempEventList))
			throw new OptimalPathFindingException(
					"Unable to find optimal schedule.");
	}

	public boolean isPlanSuitable(List<EventDate> tempEventList) {
		int eventIterator;
		for (eventIterator = 1; eventIterator < tempEventList.size(); eventIterator++) {
			if (areOverlaping(tempEventList.get(eventIterator - 1),
					tempEventList.get(eventIterator)))
				return false;
		}
		return true;
	}

	private void addBoundaryEvents(List<EventDate> tempEventList,
			EventDate morningBoundaryEvent, EventDate eveningBoundaryEvent) {
		if (!tempEventList.isEmpty()) {
			if (tempEventList.get(0).getStartTime()
					.after(morningBoundaryEvent.getStartTime())) {
				tempEventList.add(0, morningBoundaryEvent);
			}
			if (tempEventList.get(tempEventList.size() - 1).getEndTime()
					.before(morningBoundaryEvent.getEndTime())) {
				tempEventList.add(tempEventList.size(), eveningBoundaryEvent);
			}
		} else {
			tempEventList.add(morningBoundaryEvent);
			tempEventList.add(eveningBoundaryEvent);
		}
	}

	private void removeBoundaryEvents(List<EventDate> tempEventList,
			EventDate morningBoundaryEvent, EventDate eveningBoundaryEvent) {
		tempEventList.remove(morningBoundaryEvent);
		tempEventList.remove(eveningBoundaryEvent);
	}

	private EventDate createBoundaryEvent(Date date) {
		return new EventDate(null, null, date, date, 0, false);
	}

	public void setMorningBoundary(Date morningBoundary) {
		this.morningBoundary = morningBoundary;
	}

	public void setEveningBoundary(Date eveningBoundary) {
		this.eveningBoundary = eveningBoundary;
	}
}
