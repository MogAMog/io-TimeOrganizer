package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#setDistanceStrategy(pl.edu.agh.services.interfaces.IDistanceStrategy)
	 */
	@Override
	public void setDistanceStrategy(IDistanceStrategy distanceStrategy) {
		this.distanceStrategy = distanceStrategy;
		
	}
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#addEventDates(java.util.Set)
	 */
	@Override
	public void addEventDates(Set<EventDate> eventDateSet){
		for(EventDate eventDate: eventDateSet){
			if(eventDate.getEvent().isRequired() && eventDate.getEvent().isConstant()){
				constantRequiredEventList.add(eventDate);
			} else if(eventDate.getEvent().isRequired()){
				notConstantRequiredEventList.add(eventDate);
			} else if(eventDate.getEvent().isConstant()){
				constantNotRequiredEventList.add(eventDate);
			} else {
				notConstantNotRequiredEventList.add(eventDate);
			}
		}
	}
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#setEventDates(java.util.Set)
	 */
	@Override
	public void setEventDates(Set<EventDate> eventDateSet){
		clear();
		addEventDates(eventDateSet);
	}
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#clear()
	 */
	@Override
	public void clear(){
		constantRequiredEventList = new ArrayList<EventDate>();
		notConstantRequiredEventList = new ArrayList<EventDate>();
		constantNotRequiredEventList = new ArrayList<EventDate>();
		notConstantNotRequiredEventList = new ArrayList<EventDate>();
		eventList = new ArrayList<EventDate>();
	}
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#inputEventDate(pl.edu.agh.domain.EventDate, java.util.Date)
	 */
	@Override
	public void inputEventDate(EventDate eventDate, Date startDate) throws OptimalPathFindingException {
		List<EventDate> tempEventList = new ArrayList<EventDate>();
		EventDate tempEventDate = null;
		EventDate prevEventDate = new EventDate(null, null, null, startDate, 0, false);
		boolean placeForEventFound = false;
		Iterator<EventDate> eventListIterator = eventList.iterator();
		while(eventListIterator.hasNext() && (tempEventDate = (EventDate) eventListIterator.next()).getStartTime().before(startDate)){
			tempEventList.add(tempEventDate);
			prevEventDate = tempEventDate;
		}
		if(tempEventDate!=null){
			tempEventList.add(tempEventDate);
		}
		EventDate mockEventDate;
		if(DateTimeTools.happenedBefore(prevEventDate.getEndTime(), startDate)){
			mockEventDate = new EventDate(prevEventDate.getLocation(), prevEventDate.getDate(), startDate, startDate, 0, false);
			
		}else{
			mockEventDate = prevEventDate;
		}
		if(canEventFitBetween(eventDate, mockEventDate, tempEventDate)){
			placeEventAfter(mockEventDate, eventDate);
			tempEventList.add(eventDate);
			placeForEventFound = true;
		}
		while(eventListIterator.hasNext() && !placeForEventFound){
			prevEventDate = tempEventDate;
			tempEventDate = eventListIterator.next();
			tempEventList.add(tempEventDate);
			if(canEventFitBetween(eventDate, prevEventDate, tempEventDate)){
				placeEventAfter(prevEventDate, eventDate);
				tempEventList.add(eventDate);
				placeForEventFound = true;
			}
		}
		while(eventListIterator.hasNext()){
			tempEventDate = eventListIterator.next();
			tempEventList.add(tempEventDate);
		}
		if(placeForEventFound){
			eventList = tempEventList;
			Set<EventDate> eventSet = new HashSet<EventDate>();
			eventSet.add(eventDate);
			addEventDates(eventSet);
		}else{
			throw new OptimalPathFindingException("Unable to find place for the event");
		}
	}
	
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#getEventDateOrder()
	 */
	@Override
	public List<EventDate> getEventDateOrder(){
		return eventList;
		
	}
	
	private boolean canEventFitBetween(EventDate event, EventDate beforeEvent, EventDate afterEvent){
		return getTimeOccupiedByEvent(event, beforeEvent, afterEvent) < DateTimeTools.getMinuteDifferenceBetweenTwoDates(beforeEvent.getEndTime(), afterEvent.getStartTime());
	}
	
	private long getTimeOccupiedByEvent(EventDate event, EventDate beforeEvent, EventDate afterEvent){
		return getTimeDistance(beforeEvent,event) + event.getDuration() + getTimeDistance(event,afterEvent);
	}
	
	private boolean areOverlaping(EventDate event1, EventDate event2){
		return DateTimeTools.getMinuteDifferenceBetweenTwoDates(event1.getEndTime(), event2.getStartTime()) >= getTimeDistance(event1,event2);
	}
	
	private int getTimeDistance(EventDate event1, EventDate event2) {
		return distanceStrategy.getTimeDistanceBetween(event1.getLocation(),event2.getLocation());
	}
	private void fitEventAfter(EventDate event, EventDate beforeEvent, List<EventDate> tempEventList){
		int eventIterator = 0;
		while(tempEventList.get(eventIterator).getId() != beforeEvent.getId() && eventIterator < tempEventList.size()){
			eventIterator++;
		}
		if(tempEventList.get(eventIterator).getId() != beforeEvent.getId()){
			tempEventList.add(eventIterator, event);
		}
		if(!event.getEvent().isConstant()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beforeEvent.getEndTime());
			calendar.add(Calendar.MINUTE, (int) getTimeDistance(beforeEvent, event));
			event.setStartTime(calendar.getTime());
			calendar.add(Calendar.MINUTE, event.getDuration());
			event.setEndTime(calendar.getTime());
		}
	}
	
	/* (non-Javadoc)
	 * @see pl.edu.agh.services.IPathFindingService#calculateOptimalEventOrder()
	 */
	@Override
	public void calculateOptimalEventOrder() throws OptimalPathFindingException{
		List<EventDate> tempEventList = new ArrayList<EventDate>();
		placeConstantRequiredEvents(tempEventList);
		placeRequiredEvents(tempEventList);
		placeConstantEvents(tempEventList);
		placeNonConstantEvents(tempEventList);
		eventList = tempEventList;
	}
	
	private void placeEventAfter(EventDate beforeEvent, EventDate event){
		Date startTime = DateTimeTools.addMinutesToDate(beforeEvent.getEndTime(), getTimeDistance(beforeEvent, event));
		event.setStartTime(startTime);
		Date endTime = DateTimeTools.addMinutesToDate(startTime, event.getDuration());
		event.setEndTime(endTime);
	}
	
	private void placeNonConstantEvents(List<EventDate> tempEventList) {
		Set<EventDate> tempNotConstantEventSet = new HashSet<EventDate>(notConstantNotRequiredEventList);
		while(!tempNotConstantEventSet.isEmpty()){
			EventDate bestBeforeEvent = null;
			EventDate bestEvent = null;
			long bestTime = Long.MAX_VALUE;
			for(EventDate beforeEvent: tempEventList.subList(0, tempEventList.size()-1)){
				for(EventDate event: tempNotConstantEventSet){
					EventDate afterEvent = tempEventList.get(tempEventList.indexOf(beforeEvent)+1);
					if(canEventFitBetween(event, beforeEvent, afterEvent)){
						long currTime = getTimeOccupiedByEvent(event, beforeEvent, afterEvent);
						if(currTime<bestTime){
							bestTime = currTime;
							bestEvent = event;
							bestBeforeEvent = beforeEvent;
						}
					}
				}
			}
			if(bestEvent == null){
				break;
			}
			fitEventAfter(bestEvent, bestBeforeEvent, tempEventList);
			tempNotConstantEventSet.remove(bestEvent);
		}
		
	}
	private void placeConstantEvents(List<EventDate> tempEventList) {
		for(EventDate event : constantNotRequiredEventList){
			Calendar eventStartTime = DateTimeTools.getCalendarFromDate(event.getStartTime());
			int newEventLocation;
			for(newEventLocation = 0;newEventLocation < tempEventList.size() && DateTimeTools.getCalendarFromDate(tempEventList.get(newEventLocation).getStartTime()).before(eventStartTime);newEventLocation++){
			}
			tempEventList.add(newEventLocation, event);
			if(!isPlanSuitable(tempEventList)){
				tempEventList.remove(event);
			}
		}
	}
	private void placeRequiredEvents(List<EventDate> tempEventList) throws OptimalPathFindingException {
		Set<EventDate> tempNotConstantEventSet = new HashSet<EventDate>(notConstantRequiredEventList);
		while(!tempNotConstantEventSet.isEmpty()){
			EventDate bestBeforeEvent = null;
			EventDate bestEvent = null;
			long bestTime = Long.MAX_VALUE;
			for(EventDate beforeEvent: tempEventList.subList(0, tempEventList.size()-1)){
				for(EventDate event: tempNotConstantEventSet){
					EventDate afterEvent = tempEventList.get(tempEventList.indexOf(beforeEvent)+1);
					if(canEventFitBetween(event, beforeEvent, afterEvent)){
						long currTime = getTimeOccupiedByEvent(event, beforeEvent, afterEvent);
						if(currTime<bestTime){
							bestTime = currTime;
							bestEvent = event;
							bestBeforeEvent = beforeEvent;
						}
					}
				}
			}
			if(bestEvent == null){
				throw new OptimalPathFindingException("Unable to find optimal schedule.");
			}
			fitEventAfter(bestEvent, bestBeforeEvent, tempEventList);
			tempNotConstantEventSet.remove(bestEvent);
		}
		
	}
	
	private void placeConstantRequiredEvents(List<EventDate> tempEventList) throws OptimalPathFindingException {
		for(EventDate event : constantRequiredEventList){
			Calendar eventStartTime = DateTimeTools.getCalendarFromDate(event.getStartTime());
			int newEventLocation;
			for(newEventLocation = 0;newEventLocation < tempEventList.size() && DateTimeTools.getCalendarFromDate(tempEventList.get(newEventLocation).getStartTime()).before(eventStartTime);newEventLocation++){
			}
			tempEventList.add(newEventLocation, event);
		}
		if(!isPlanSuitable(tempEventList))throw new OptimalPathFindingException("Unable to find optimal schedule.");
	}
	
	private boolean isPlanSuitable(List<EventDate> tempEventList){
		int eventIterator;
		for(eventIterator = 1; eventIterator < tempEventList.size(); eventIterator++){
			if(areOverlaping(tempEventList.get(eventIterator-1), tempEventList.get(eventIterator)))return false;
		}
		return true;
	}
}
