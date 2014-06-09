package pl.edu.agh.services;

import pl.edu.agh.domain.Location;
import pl.edu.agh.services.interfaces.IDistanceStrategy;

public class DefaultDistanceStrategy implements IDistanceStrategy {
	
	private static int MINUTES_PER_KILOMETER = 5;
	private static int KILOMETERS_PER_DEGREE = 111;

	@Override
	public int getTimeDistanceBetween(Location location1, Location location2) {
		
		double longtitudeDifferenceInKilometers = (Math.abs(location1.getLongitude() - location2.getLongitude())) * KILOMETERS_PER_DEGREE;
		double latitudeDifferenceInKilometers = (Math.abs(location1.getLatitude() - location2.getLatitude())) * KILOMETERS_PER_DEGREE;
		
		double distance = Math.sqrt(longtitudeDifferenceInKilometers*longtitudeDifferenceInKilometers +
				latitudeDifferenceInKilometers*latitudeDifferenceInKilometers);
		
		return (int) distance * MINUTES_PER_KILOMETER;
	}

}