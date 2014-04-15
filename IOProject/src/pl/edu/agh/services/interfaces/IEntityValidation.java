package pl.edu.agh.services.interfaces;

import pl.edu.agh.errors.FormValidationError;
import java.util.List;

public interface IEntityValidation<E> {

	List<FormValidationError> validate(E entity); 
	
}
