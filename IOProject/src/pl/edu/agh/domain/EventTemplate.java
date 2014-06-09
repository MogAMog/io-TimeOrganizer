package pl.edu.agh.domain;

import java.io.Serializable;
import java.util.Date;

import pl.edu.agh.domain.databasemanagement.DatabaseProperties;
import pl.edu.agh.view.addevent.EventFrequencyFold.Frequency;

@SuppressWarnings("serial")
public class EventTemplate implements Serializable {

	private long id = DatabaseProperties.UNSAVED_ENTITY_ID;	
	private String templateName;
	private String title;
	private String description;
	private boolean required;
	private boolean constant;
	private boolean draft;
	private boolean fixedTime;
	private int duration;
	private Date startDate;
	private Date endDate;
	private Date startTime;
	private Date endTime;
	private Frequency frequency;
	private boolean mondaySelected;
	private boolean tuesdaySelected;
	private boolean wednesdaySelected;
	private boolean thursdaySelected;
	private boolean fridaySelected;
	private boolean saturdaySelected;
	private boolean sundaySelected;
	
	public EventTemplate() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public boolean isFixedTime() {
		return fixedTime;
	}

	public void setFixedTime(boolean fixedTime) {
		this.fixedTime = fixedTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public boolean isMondaySelected() {
		return mondaySelected;
	}

	public void setMondaySelected(boolean mondaySelected) {
		this.mondaySelected = mondaySelected;
	}

	public boolean isTuesdaySelected() {
		return tuesdaySelected;
	}

	public void setTuesdaySelected(boolean tuesdaySelected) {
		this.tuesdaySelected = tuesdaySelected;
	}

	public boolean isWednesdaySelected() {
		return wednesdaySelected;
	}

	public void setWednesdaySelected(boolean wednesdaySelected) {
		this.wednesdaySelected = wednesdaySelected;
	}

	public boolean isThursdaySelected() {
		return thursdaySelected;
	}

	public void setThursdaySelected(boolean thursdaySelected) {
		this.thursdaySelected = thursdaySelected;
	}

	public boolean isFridaySelected() {
		return fridaySelected;
	}

	public void setFridaySelected(boolean fridaySelected) {
		this.fridaySelected = fridaySelected;
	}

	public boolean isSaturdaySelected() {
		return saturdaySelected;
	}

	public void setSaturdaySelected(boolean saturdaySelected) {
		this.saturdaySelected = saturdaySelected;
	}

	public boolean isSundaySelected() {
		return sundaySelected;
	}

	public void setSundaySelected(boolean sundaySelected) {
		this.sundaySelected = sundaySelected;
	}

	
	

	
}
