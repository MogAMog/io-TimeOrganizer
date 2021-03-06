package pl.edu.agh.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.domain.databasemanagement.DatabaseProperties;

@SuppressWarnings("serial")
public class Event implements Serializable {

	private long id = DatabaseProperties.UNSAVED_ENTITY_ID;
	private Account account;
	private Event predecessorEvent;
	private Set<EventDate> eventDates = new HashSet<EventDate>();
	private String title;
	private String description;
	private boolean required;
	private boolean constant;
	private boolean draft;
	
	public Event() {
		super();
	}

	public Event(Account account, Event predecessorEvent, String title, String description, boolean required, boolean constant, boolean draft) {
		this.account = account;
		this.predecessorEvent = predecessorEvent;
		this.title = title;
		this.description = description;
		this.required = required;
		this.constant = constant;
		this.draft = draft;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Event getPredecessorEvent() {
		return predecessorEvent;
	}

	public void setPredecessorEvent(Event predecessorEvent) {
		this.predecessorEvent = predecessorEvent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isConstant() {
		return constant;
	}

	public void setConstant(boolean constant) {
		this.constant = constant;
	}

	public Set<EventDate> getEventDates() {
		return eventDates;
	}

	public void setEventDates(Set<EventDate> eventDates) {
		this.eventDates = eventDates;
	}

	public void addEventDate(EventDate eventDate) {
		eventDates.add(eventDate);
		eventDate.setEvent(this);
	}
	
	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", account=" + account
				+ ", predecessorEvent=" + predecessorEvent
				+ ", eventDates=" + eventDates + ", title=" + title + ", description="
				+ description + ", required=" + required + ", constant="
				+ constant + "]";
	}
	
	

}

