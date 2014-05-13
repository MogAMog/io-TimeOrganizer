package pl.edu.agh.intents.interfaces;

public interface IntentsBuilderCreator {
	public enum BuilderType {
		WEB_BROWSER, APPLICATION, AUTO;
	}

	public IntentsBuilder createIntentBuilder(BuilderType builderType);
}
