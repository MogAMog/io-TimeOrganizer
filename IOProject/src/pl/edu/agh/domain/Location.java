package pl.edu.agh.domain;

import java.io.Serializable;

import pl.edu.agh.domain.databasemanagement.DatabaseProperties;

public class Location implements Serializable {

	private long id = DatabaseProperties.UNSAVED_ENTITY_ID;
	private String name;
	private String address;
	private String city;
	private double latitude;
	private double longitude;
	private boolean defaultLocation;
	
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isDefaultLocation() {
		return defaultLocation;
	}

	public void setDefaultLocation(boolean defaultLocation) {
		this.defaultLocation = defaultLocation;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(name).append("\n")
								  .append(address).append("\n")
								  .append(city).toString();
	}
	
}
