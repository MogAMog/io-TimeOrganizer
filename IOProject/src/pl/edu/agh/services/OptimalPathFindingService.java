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
import pl.edu.agh.tools.DateTimeTools;

public class OptimalPathFindingService {

	private Set<EventDate> constantRequiredEventSet;
	private Set<EventDate> notConstantRequiredEventSet;
	private Set<EventDate> constantNotRequiredEventSet;
	private Set<EventDate> notConstantNotRequiredEventSet;
	private List<EventDate> eventList;
	public OptimalPathFindingService() {
		constantRequiredEventSet = new HashSet<EventDate>();
		notConstantRequiredEventSet = new HashSet<EventDate>();
		constantNotRequiredEventSet = new HashSet<EventDate>();
		notConstantNotRequiredEventSet = new HashSet<EventDate>();
		eventList = new ArrayList<EventDate>();
	}
	public OptimalPathFindingService(Set<EventDate> eventDateSet) {
		this();
		addEventDates(eventDateSet);
	}
	public void addEventDates(Set<EventDate> eventDateSet){
		for(EventDate eventDate: eventDateSet){
			if(eventDate.getEvent().isRequired() && eventDate.getEvent().isConstant()){
				constantRequiredEventSet.add(eventDate);
			} else if(eventDate.getEvent().isRequired()){
				notConstantRequiredEventSet.add(eventDate);
			} else if(eventDate.getEvent().isConstant()){
				constantNotRequiredEventSet.add(eventDate);
			} else {
				notConstantNotRequiredEventSet.add(eventDate);
			}
		}
	}
	public void setEventDates(Set<EventDate> eventDateSet){
		clear();
		addEventDates(eventDateSet);
	}
	public void clear(){
		constantRequiredEventSet = new HashSet<EventDate>();
		notConstantRequiredEventSet = new HashSet<EventDate>();
		constantNotRequiredEventSet = new HashSet<EventDate>();
		notConstantNotRequiredEventSet = new HashSet<EventDate>();
		eventList = new ArrayList<EventDate>();
	}
	public void inputEventDate(EventDate eventDate, Date startDate) throws OptimalPathFindingException {
		List<EventDate> tempEventList = new ArrayList<EventDate>();
		EventDate tempEventDate = null;
		EventDate prevEventDate = new EventDate(null, null, null, startDate, 0, false);
		boolean fit = false;
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
			mockEventDate = new EventDate(prevEventDate.getLocation(), prevEventDate.getDate(), startDate, startDate, 0, false)
			
		}else{
			mockEventDate = prevEventDate;
		}
		if(canEventFitBetween(eventDate, mockEventDate, tempEventDate)){
			placeEventAfter(mockEventDate, eventDate);
			tempEventList.add(eventDate);
			fit = true;
		}
		while(eventListIterator.hasNext() && !fit){
			prevEventDate = tempEventDate;
			tempEventDate = eventListIterator.next();
			tempEventList.add(tempEventDate);
			if(canEventFitBetween(eventDate, prevEventDate, tempEventDate)){
				placeEventAfter(prevEventDate, eventDate);
				tempEventList.add(eventDate);
				fit = true;
			}
		}
		while(eventListIterator.hasNext()){
			tempEventDate = eventListIterator.next();
			tempEventList.add(tempEventDate);
		}
		if(fit){
			eventList = tempEventList;
			Set<EventDate> eventSet = new HashSet<EventDate>();
			eventSet.add(eventDate);
			addEventDates(eventSet);
		}else{
			throw new OptimalPathFindingException("Unable to find place for the event");
		}
	}
	
	public List<EventDate> getEventDateOrder(){
		calculateOptimalEventOrder();
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
		// TODO Auto-generated method stub
		return 0;
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
	
	private void calculateOptimalEventOrder(){
		List<EventDate> tempEventList = new ArrayList<EventDate>();
		try {
			placeConstantRequiredEvents(tempEventList);
			placeRequiredEvents(tempEventList);
			placeConstantEvents(tempEventList);
			placeNonConstantEvents(tempEventList);
			eventList = tempEventList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void placeEventAfter(EventDate beforeEvent, EventDate event){
		Date startTime = DateTimeTools.addMinutesToDate(beforeEvent.getEndTime(), getTimeDistance(prevEventDate, eventDate));
		eventDate.setStartTime(startTime);
		Date endTime = DateTimeTools.addMinutesToDate(startTime, event.getDuration());
		eventDate.setEndTime(endTime);
	}
	
	private void placeNonConstantEvents(List<EventDate> tempEventList) {
		Set<EventDate> tempNotConstantEventSet = new HashSet<EventDate>(notConstantNotRequiredEventSet);
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
		for(EventDate event : constantNotRequiredEventSet){
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
		Set<EventDate> tempNotConstantEventSet = new HashSet<EventDate>(notConstantRequiredEventSet);
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
		for(EventDate event : constantRequiredEventSet){
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
