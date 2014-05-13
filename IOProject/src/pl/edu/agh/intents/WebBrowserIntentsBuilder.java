package pl.edu.agh.intents;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import pl.edu.agh.exceptions.IntentBuilderNotInitializedCorrectly;
import pl.edu.agh.intents.interfaces.IntentsBuilder;
import android.content.Intent;
import android.net.Uri;

public class WebBrowserIntentsBuilder implements IntentsBuilder {

	// API: http://jakdojade.pl/pages/api/http_get.html

	private final static String BASE_URL = "http://m.jakdojade.pl/";

	private final static String PARAMETERS_PREFIX = "?";
	private final static String PARAMETER_DELIMITER = "&";
	private final static String PARAMETER_INFIX = "=";

	// required parameters are: cityID, from coordinate, to coordinate
	private static List<String> requiredParameters = Arrays.asList("cid", "fc",
			"tc");

	private HashMap<String, String> parameters;

	WebBrowserIntentsBuilder() {
		parameters = new HashMap<String, String>();
	}

	@Override
	public Intent createIntent() throws IntentBuilderNotInitializedCorrectly {
		if (!validate())
			throw new IntentBuilderNotInitializedCorrectly(
					"Required parameters are: "
							+ Arrays.toString(requiredParameters.toArray()));

		String url = buildURL();
		System.out.println(url);
		Intent webPage = new Intent(Intent.ACTION_VIEW);
		webPage.setData(Uri.parse(url));

		return webPage;
	}

	@Override
	public boolean validate() {
		for (String parameter : requiredParameters) {
			if (!parameters.containsKey(parameter))
				return false;
		}
		return true;
	}

	@Override
	public IntentsBuilder fromLocation(double latitiude, double longitiude) {
		String value = latitiude + ":" + longitiude;
		parameters.put("fc", value);
		return this;
	}

	@Override
	public IntentsBuilder toLocation(double latitiude, double longitiude) {
		String value = latitiude + ":" + longitiude;
		parameters.put("tc", value);
		return this;
	}

	@Override
	public IntentsBuilder setCityID(int cityID) {
		String value = String.valueOf(cityID);
		parameters.put("cid", value);
		return this;
	}

	@Override
	public IntentsBuilder setDate(Date date) {
		String value = new SimpleDateFormat("dd.MM.yy", Locale.getDefault())
				.format(date);
		parameters.put("d", value);
		return this;
	}

	@Override
	public IntentsBuilder setTime(Date date) {
		String value = new SimpleDateFormat("HH:mm", Locale.getDefault())
				.format(date);
		parameters.put("h", value);
		return this;
	}

	@Override
	public IntentsBuilder IsArrival(boolean isArrival) {
		String value = String.valueOf(isArrival);
		parameters.put("ia", value);
		return this;
	}

	@Override
	public IntentsBuilder IsAutoSearch(boolean isAutoSearch) {
		String value = String.valueOf(isAutoSearch);
		parameters.put("as", value);
		return this;
	}

	private String buildURL() {
		StringBuilder URLBuilder = new StringBuilder();
		URLBuilder.append(BASE_URL);
		URLBuilder.append(PARAMETERS_PREFIX);

		// adding parameters
		String name;
		String value;
		for (Entry<String, String> entry : parameters.entrySet()) {
			name = entry.getKey();
			value = entry.getValue();

			URLBuilder.append(name);
			URLBuilder.append(PARAMETER_INFIX);
			URLBuilder.append(value);
			URLBuilder.append(PARAMETER_DELIMITER);
		}
		// removing last PARAMETR_DELIMITER
		URLBuilder.delete(URLBuilder.length() - PARAMETER_DELIMITER.length(),
				URLBuilder.length());

		return URLBuilder.toString();
	}

}
