package pl.edu.agh.domain;

import java.io.Serializable;

import pl.edu.agh.domain.databasemanagement.DatabaseProperties;

@SuppressWarnings("serial")
public class Location implements Serializable {

	private long id = DatabaseProperties.UNSAVED_ENTITY_ID;
	private String name;
	private String address;
	private String city;
	private Double latitude;
	private Double longitude;
	private Boolean defaultLocation;
	
	public Location() {
		super();
	}

	public Location(String name, String address, String city, double latitude, double longitude, boolean defaultLocation) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.defaultLocation = defaultLocation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Boolean isDefaultLocation() {
		return defaultLocation;
	}

	public void setDefaultLocation(Boolean defaultLocation) {
		this.defaultLocation = defaultLocation;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(name).append("\n")
								  .append(address).append("\n")
								  .append(city).toString();
	}
	
}
