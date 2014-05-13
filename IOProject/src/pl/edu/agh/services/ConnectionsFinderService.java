package pl.edu.agh.services;

import pl.edu.agh.domain.EventDate;
import pl.edu.agh.domain.Location;
import pl.edu.agh.exceptions.IntentBuilderNotInitializedCorrectly;
import pl.edu.agh.intents.IntentsBuilderCreatorImpl;
import pl.edu.agh.intents.interfaces.IntentsBuilder;
import pl.edu.agh.intents.interfaces.IntentsBuilderCreator;
import pl.edu.agh.intents.interfaces.IntentsBuilderCreator.BuilderType;
import android.content.Intent;

public class ConnectionsFinderService {

	// 5000 means Kraków
	private final static int CITY_PARAMETER_VALUE = 5000;
	private IntentsBuilderCreator builderCreator;

	public ConnectionsFinderService() {
		builderCreator = new IntentsBuilderCreatorImpl();
	}

	public Intent createIntent(EventDate firstEvent, EventDate secondEvent) {
		Intent intent = null;
		IntentsBuilder builder = createIntentBuilder();

		Location locationFrom = firstEvent.getLocation();
		Location locationTo = secondEvent.getLocation();

		builder.setCityID(CITY_PARAMETER_VALUE).
			fromLocation(locationFrom.getLatitude(),locationFrom.getLongitude()).
			toLocation(locationTo.getLatitude(), locationTo.getLongitude()).
			setDate(secondEvent.getDate()).
			setTime(secondEvent.getStartTime()).
			IsArrival(true).
			IsAutoSearch(true);

		try {
			intent = builder.createIntent();
		} catch (IntentBuilderNotInitializedCorrectly e) {
			e.printStackTrace();
		}

		return intent;
	}

	private IntentsBuilder createIntentBuilder() {
		return builderCreator.createIntentBuilder(BuilderType.WEB_BROWSER);
	}
}
