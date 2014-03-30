package pl.edu.agh.domain.databasemanagement;

import java.util.List;

public interface IDatabaseDmlProvider<T> {

	public long insert(T insertObject);
	
	public T getByIdAllData(long id);
	
	//public T getSelectedColumnsById(long id, String[] projections);
	
	public List<T> getAll();
	
}

