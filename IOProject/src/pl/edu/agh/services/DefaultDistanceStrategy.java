package pl.edu.agh.services;

import pl.edu.agh.domain.Location;
import pl.edu.agh.services.interfaces.IDistanceStrategy;

public class DefaultDistanceStrategy implements IDistanceStrategy {
	
	private static int MINUTES_PER_KILOMETER = 5;

	@Override
	public int getTimeDistanceBetween(Location location1, Location location2) {
		
		double longtitudeDiff = Math.abs(location1.getLongitude() - location2.getLongitude());
		double latitudeDiff = Math.abs(location1.getLatitude() - location2.getLatitude());
		
		double distance = Math.sqrt(longtitudeDiff*longtitudeDiff + latitudeDiff*latitudeDiff);
		
		return (int) distance * MINUTES_PER_KILOMETER;
	}

}