package pl.edu.agh.domain.databasemanagement;

import java.util.List;
import java.util.Map;

public interface IDatabaseTableSchema {

	public static class ForeignKeyMapping {
		
		private final String foreignKeyColumnName;
		private final String referenceTableName;
		private final String referenceTablePrimaryKeyColumn;
		
		public ForeignKeyMapping(String foreignKeyColumnName, String referenceTableName, String referenceTablePrimaryKeyColumn) {
			this.foreignKeyColumnName = foreignKeyColumnName;
			this.referenceTableName = referenceTableName;
			this.referenceTablePrimaryKeyColumn = referenceTablePrimaryKeyColumn;
		}
		
		public String getForeignKeyColumnName() {
			return foreignKeyColumnName;
		}
		public String getReferenceTableName() {
			return referenceTableName;
		}
		public String getReferenceTablePrimaryKeyColumn() {
			return referenceTablePrimaryKeyColumn;
		}
		
	}
	
	String getTableName();
	
	Map<String, String> getColumnNamesWithSqliteTypes();
	
	List<String> getUniqueColumns();
	
	List<String> getNotNullColumns();
	
	List<String> getPrimaryKeyColumns();
	
	List<ForeignKeyMapping> getForeignKeyColumns();
	
	List<String> getIndexesColumns();
	
	List<String> getCheckConstraints();

}
