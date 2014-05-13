package pl.edu.agh.intents.interfaces;

import java.util.Date;

import pl.edu.agh.exceptions.IntentBuilderNotInitializedCorrectly;
import android.content.Intent;

public interface IntentsBuilder {
	public Intent createIntent() throws IntentBuilderNotInitializedCorrectly;

	public boolean validate();

	public IntentsBuilder fromLocation(double latitiude, double longitiude);

	public IntentsBuilder toLocation(double latitiude, double longitiude);

	public IntentsBuilder setCityID(int cityID);

	public IntentsBuilder setDate(Date date);

	public IntentsBuilder setTime(Date date);

	public IntentsBuilder IsArrival(boolean isArrival);

	public IntentsBuilder IsAutoSearch(boolean isAutoSearch);
}
