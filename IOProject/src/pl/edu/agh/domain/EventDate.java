package pl.edu.agh.domain;

import java.io.Serializable;
import java.util.Date;

import pl.edu.agh.domain.databasemanagement.DatabaseProperties;

@SuppressWarnings("serial")
public class EventDate implements Serializable {

	private long id = DatabaseProperties.UNSAVED_ENTITY_ID;
	private Event event;
	private Location location;
	private Date date;
	private Date startTime;
	private Date endTime;
	private Integer duration;
	private boolean finished;
	
	public EventDate() {
		super();
	}

	public EventDate(Location location, Date date, Date startTime, Date endTime, int duration, boolean finished) {
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Boolean isFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
/*		result = prime * result + (date.getYear() + date.getMonth() + date.getDay());
		result = prime * result + (startTime.getHours() + startTime.getMinutes());
		result = prime * result + (endTime.getHours() + endTime.getMinutes());*/
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventDate other = (EventDate) obj;
		if (id != other.id)
			return false;
//		if(other.getDate().getYear() != date.getYear() || other.getDate().getMonth() != date.getMonth() || other.getDate().getDay() != date.getDay()) {
//			return false;
//		}
//		if(other.getStartTime().getHours() != startTime.getHours() || other.getStartTime().getMinutes() != startTime.getMinutes()) {
//			return false;
//		}
//		if(other.getEndTime().getHours() != endTime.getHours() || other.getEndTime().getMinutes() != endTime.getMinutes()) {
//			return false;
//		}
		return true;
	}
	
	
	
	
}
