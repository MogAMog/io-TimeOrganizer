package pl.edu.agh.errors;

public class FormValidationError {

	private String messageKey;
	
	public FormValidationError(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
	
}
