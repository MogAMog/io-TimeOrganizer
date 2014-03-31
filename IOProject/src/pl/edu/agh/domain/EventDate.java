package pl.edu.agh.domain;

import java.util.Date;

public class EventDate {

	private long id = -1;
	private Event event;
	private Location location;
	private Date date;
	private Date startTime;
	private Date endTime;
	private Date duration;
	private boolean finished;
	
	public EventDate() {
		super();
	}

	public EventDate(Location location, Date date, Date startTime, Date endTime, Date duration, boolean finished) {
		super();
		//this.event = event;
		this.location = location;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.finished = finished;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return startTime + " - " + endTime + ": " + event.getDescription();
	}
	
	
	
}
