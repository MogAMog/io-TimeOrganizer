package pl.edu.agh.services.interfaces;

import java.util.List;

import pl.edu.agh.errors.FormValidationError;

public interface IFormValidation {

	List<FormValidationError> validate();
	
}
