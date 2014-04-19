package pl.edu.agh.errors;

public class FormValidationError {

	private int messageKey;
	
	public FormValidationError(int messageKey) {
		this.messageKey = messageKey;
	}

	public int getMessageKey() {
		return messageKey;
	}
	
}
