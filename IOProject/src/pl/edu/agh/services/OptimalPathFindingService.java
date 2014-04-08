package pl.edu.agh.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.edu.agh.domain.EventDate;

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
		if(eventDate.getDuration().getTime() > (endDate.getTime() - startDate.getTime())){
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
		if(prevEventDate != null && tempEventDate != null && (Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime()) + eventDate.getDuration().getTime() > Math.min(tempEventDate.getStartTime().getTime(),endDate.getTime()))){
			throw new Exception("Event lasts too long for this gap.");
		}
		if(prevEventDate != null && (Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime()) + eventDate.getDuration().getTime() > endDate.getTime())){
			throw new Exception("Event lasts too long for this gap.");
		}
		if(tempEventDate != null && (startDate.getTime() + eventDate.getDuration().getTime() > Math.min(tempEventDate.getStartTime().getTime(),endDate.getTime()))){
			throw new Exception("Event lasts too long for this gap.");
		}
		eventDate.setStartTime(new Date(Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime())));
		eventDate.setEndTime(new Date(Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime())+eventDate.getDuration().getTime()));
		tempEventList.add(eventDate);
		if(tempEventDate != null){
			tempEventList.add(tempEventDate);
		}
		while(eventListIterator.hasNext()){
			tempEventList.add(eventListIterator.next());
		}
		eventList = tempEventList;
	}
	
	public List<EventDate> getEventDateOrder(){
		calculateOptimalEventOrder();
		return eventList;
		
	}
	
	private boolean canEventFitBetween(EventDate event, EventDate beforeEvent, EventDate afterEvent){
		return beforeEvent.getEndTime().getTime() + getTimeDistance(beforeEvent,event) + event.getDuration().getTime() + getTimeDistance(event,afterEvent) < afterEvent.getStartTime().getTime();
		
	}
	
	private boolean areOverlaping(EventDate event1, EventDate event2){
		return event1.getEndTime().getTime() + getTimeDistance(event1,event2) > event2.getStartTime().getTime();
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
		// TODO Auto-generated method stub
		
	}
	private void placeConstantEvents(List<EventDate> tempEventList) {
		// TODO
		
	}
	private void placeRequiredEvents(List<EventDate> tempEventList) {
		// TODO Auto-generated method stub
		
	}
	private void placeConstantRequiredEvents(List<EventDate> tempEventList) throws Exception {
		for(EventDate event : constantRequiredEventSet){
			long eventStartTime = event.getStartTime().getTime();
			int newEventLocation;
			for(newEventLocation = 0;newEventLocation < tempEventList.size() && tempEventList.get(newEventLocation).getStartTime().getTime() < eventStartTime;newEventLocation++){
			}
			tempEventList.add(newEventLocation, event);
		}
		int eventIterator;
		for(eventIterator = 1; eventIterator < tempEventList.size(); eventIterator++){
			if(areOverlaping(tempEventList.get(eventIterator-1), tempEventList.get(eventIterator)))throw new Exception("Unable to find optimal schedule.");
		}
	}
}
