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
	private class Position {

		public void generateEventList(List<EventDate> tempEventList2,
				EventDate[] placedEventArray, EventDate[] requiredEventArray) {
			// TODO Auto-generated method stub
			
		}

	}

	private class PositionGenerator implements Iterator<Position> {

		public PositionGenerator(int size, int length) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Position next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}

	}

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
//		if(!canEventFitBetween(,,,)){
//			throw new OptimalPathFindingException("Event lasts too long for this gap.");
//		}
//		eventDate.setStartTime(new Date(Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime())));
//		eventDate.setEndTime(new Date(Math.max(prevEventDate.getEndTime().getTime(),startDate.getTime())+eventDate.getDuration().getTime()));
//		tempEventList.add(eventDate);
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
	
//	private boolean canEventFitBetween(EventDate event, EventDate beforeEvent, EventDate afterEvent){
//		return beforeEvent.getEndTime().getTime() + getTimeDistance(beforeEvent,event) + event.getDuration().getTime() + getTimeDistance(event,afterEvent) < afterEvent.getStartTime().getTime();
//		
//	}
//	
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
		//TODO
		
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
		//TODO
	}
	private void placeRequiredEvents(List<EventDate> tempEventList) throws OptimalPathFindingException {
		EventDate[] placedEventArray = (EventDate[]) tempEventList.toArray();
		EventDate[] requiredEventArray = (EventDate[]) notConstantRequiredEventSet.toArray();
		Iterator<Position> positions = new PositionGenerator(tempEventList.size(), requiredEventArray.length);
		List<EventDate> bestEventList = null;
		long bestTime = -1;
		while(positions.hasNext()){
			Position position = positions.next();
			List<EventDate> tempEventList2 = new ArrayList<EventDate>(tempEventList);
			position.generateEventList(tempEventList2,placedEventArray,requiredEventArray);
			if(isPlanSuitable(tempEventList2)){
				long currTime = getPlanCost(tempEventList2);
				if(currTime < bestTime || bestTime == -1){
					bestTime = currTime;
					bestEventList = tempEventList2;
				}
			}
		}
		if(bestTime == -1){
			throw new OptimalPathFindingException("Unable to find optimal schedule.");
		}
		tempEventList.clear();
		tempEventList.addAll(bestEventList);
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
	
	private long getPlanCost(List<EventDate> tempEventList){
		long cost = 0;
		int eventIterator;
		for(eventIterator = 1; eventIterator < tempEventList.size(); eventIterator++){
			cost += this.getTimeDistance(tempEventList.get(eventIterator-1), tempEventList.get(eventIterator));
		}
		return cost;
	}
}
