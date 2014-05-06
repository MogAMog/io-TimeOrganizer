package pl.edu.agh.services.interfaces;

import pl.edu.agh.domain.Location;

public interface IDistanceStrategy {

	int getTimeDistanceBetween(Location location, Location location2);

}
