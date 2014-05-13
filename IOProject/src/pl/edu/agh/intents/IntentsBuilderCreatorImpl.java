package pl.edu.agh.intents;

import pl.edu.agh.intents.interfaces.IntentsBuilder;
import pl.edu.agh.intents.interfaces.IntentsBuilderCreator;

public class IntentsBuilderCreatorImpl implements IntentsBuilderCreator {

	@Override
	public IntentsBuilder createIntentBuilder(BuilderType builderType) {
		IntentsBuilder builder = null;
		switch (builderType) {
		case WEB_BROWSER:
			builder = new WebBrowserIntentsBuilder();
			break;
		case APPLICATION:
			builder = new ApplicationIntentsBuilder();
			break;
		case AUTO:
			builder = getDefaultBuilder();
			break;
		}
		return builder;
	}

	private IntentsBuilder getDefaultBuilder() {
		// TODO: do some magic to know whether jakDojade application is
		// installed

		return new WebBrowserIntentsBuilder();
	}
}
