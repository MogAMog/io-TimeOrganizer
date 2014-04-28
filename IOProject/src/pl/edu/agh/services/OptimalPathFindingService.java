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
	public void inputEventDate(EventDate eventDate, Date startDate, Date endDate) throws Exception{
		// poprawiæ tak aby dobrze wstawia³o daty
		if(eventDate.getDuration() > DateTimeTools.getMinuteDifferenceBetweenTwoDates(startDate, endDate)){
			throw new Exception("Event lasts too long for this gap.");
		}
		List<EventDate> tempEventList = new ArrayList<EventDate>();
		EventDate tempEventDate = null;
		EventDate prevEventDate = null;
		Iterator<EventDate> eventListIterator = eventList.iterator();
		while(eventListIterator.hasNext() && (tempEventDate = (EventDate) eventListIterator.next()).getEndTime().before(startDate)){
			tempEventList.add(tempEventDate);
			prevEventDate = tempEventDate;
		}
		if(!canEventFitBetween(eventDate,prevEventDate,tempEventDate)){
			throw new OptimalPathFindingException("Event lasts too long for this gap.");
		}
		//TODO
		eventDate.setStartTime(new Date(Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime())));
		eventDate.setEndTime(new Date(Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime())+eventDate.getDuration()));
		tempEventList.add(eventDate);
		if(tempEventDate != null){
			tempEventList.add(tempEventDate);
		}
		while(eventListIterator.hasNext()){
			tempEventList.add(eventListIterator.next());
		}
		eventList = tempEventList;
	}
	
//	private boolean canEventFitBetween(){
//		if(prevEventDate != null && tempEventDate != null && (Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime()) + eventDate.getDuration() > Math.min(DateTimeTools.getCalendarFromDate(tempEventDate.getStartTime()),DateTimeTools.getCalendarFromDate(endDate)))){
//			throw new OptimalPathFindingException("Event lasts too long for this gap.");
//		}
//		if(prevEventDate != null && (Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime()) + eventDate.getDuration().getTime() > endDate.getTime())){
//			throw new Exception("Event lasts too long for this gap.");
//		}
//		if(tempEventDate != null && (startDate.getTime() + eventDate.getDuration().getTime() > Math.min(tempEventDate.getStartTime().getTime(),endDate.getTime()))){
//			throw new Exception("Event lasts too long for this gap.");
//		}
//		return false;
//	}
	
	public List<EventDate> getEventDateOrder(){
		calculateOptimalEventOrder();
		return eventList;
		
	}
	
	private boolean canEventFitBetween(EventDate event, EventDate beforeEvent, EventDate afterEvent){
		return getTimeDistance(beforeEvent,event) + event.getDuration() + getTimeDistance(event,afterEvent) < DateTimeTools.getMinuteDifferenceBetweenTwoDates(beforeEvent.getEndTime(), afterEvent.getStartTime());
	}
	
	private long getTimeOccupiedByEvent(EventDate event, EventDate beforeEvent, EventDate afterEvent){
		return getTimeDistance(beforeEvent,event) + event.getDuration() + getTimeDistance(event,afterEvent);
	}
	
	private boolean areOverlaping(EventDate event1, EventDate event2){
		return DateTimeTools.getMinuteDifferenceBetweenTwoDates(event1.getEndTime(), event2.getStartTime()) > getTimeDistance(event1,event2);
	}
	
	private long getTimeDistance(EventDate event1, EventDate event2) {
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
