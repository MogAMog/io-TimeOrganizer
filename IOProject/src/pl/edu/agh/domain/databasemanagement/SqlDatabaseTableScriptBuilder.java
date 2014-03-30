package pl.edu.agh.domain.databasemanagement;

public abstract class SqlDatabaseTableScriptBuilder {
	
	protected static String COMMA_SEPARATOR = ", ";
	private String statement = new String();
	
	public static String buildCreateTableStatement(IDatabaseTableSchema table) {
		SqlDatabaseTableScriptBuilder builder = null;
		if(table.getPrimaryKeyColumns().size() == 1) {
			builder = new SinglePrimaryKeyTableScriptBuilder();
		} else {
			builder = new MultiplePrimaryKeyTableScriptBuilder();
		}
		builder.appendTableName(table);
		builder.appendPrimaryKeyAndColumns(table);
		if(table.getForeignKeyColumns() != null && !table.getForeignKeyColumns().isEmpty()) {
			builder.setStatement(builder.getStatement() + ",\n");
			builder.appendForeignKeys(table);
		}
		builder.setStatement(builder.getStatement() + "\n);\n");
		appendAdditionalStatements(builder, table);
		return builder.getStatement();
	}
	
	public static String buildDropTableStatement(IDatabaseTableSchema table) {
		return "DROP TABLE IF EXISTS " + table.getTableName();
	}
	
	protected abstract void appendPrimaryKeyAndColumns(IDatabaseTableSchema table);
	
	private static void appendAdditionalStatements(SqlDatabaseTableScriptBuilder builder, IDatabaseTableSchema table) {
		if(table.getUniqueColumns() != null && !table.getUniqueColumns().isEmpty()) {
			builder.appendUniqueConstraints(table);
		}
		if(table.getCheckConstraints() != null && !table.getCheckConstraints().isEmpty()) {
			builder.appendCheckConstraints(table);
		}
		if(table.getIndexesColumns() != null && !table.getIndexesColumns().isEmpty()) {
			builder.appendIndexes(table);
		}
	}
	
	private void appendTableName(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		statementBuilder.append("CREATE TABLE ").append(table.getTableName()).append("\n(\n");
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
	private void appendForeignKeys(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		for(IDatabaseTableSchema.ForeignKeyMapping foreignKeyMapping : table.getForeignKeyColumns()) {
			statementBuilder.append("FOREIGN KEY (").append(foreignKeyMapping.getForeignKeyColumnName()).append(")")
							.append(" REFERENCES ").append(foreignKeyMapping.getReferenceTableName())
							.append("(").append(foreignKeyMapping.getReferenceTablePrimaryKeyColumn()).append("),\n");
		}
		statementBuilder.replace(statementBuilder.length() - 2, statementBuilder.length(), "");
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
	private void appendCheckConstraints(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		for(String check : table.getCheckConstraints()) {
			statementBuilder.append("ALTER TABLE ").append(table.getTableName()).append(" ")
							.append(" ADD CHECK (").append(check).append(")\n");
		}
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
	private void appendIndexes(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		int indexNumber = 0;
		for(String indexColumn : table.getIndexesColumns()) {
			statementBuilder.append("CREATE INDEX ").append(table.getTableName()).append("_index_").append(indexNumber++)
							.append(" ON ").append(table.getTableName())
							.append(" (").append(indexColumn).append(")\n");
		}
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
//	private void appendUniqueConstraints(IDatabaseTableSchema table) {
//		StringBuilder statementBuilder = new StringBuilder();
//		statementBuilder.append("ALTER TABLE ").append(table.getTableName()).append(" ")
//						.append(" ADD CONSTRAINT ").append("unique").append(table.getTableName())
//						.append(" UNIQUE (");
//		for(String uniqueColumn : table.getUniqueColumns()) {
//			statementBuilder.append(uniqueColumn).append(COMMA_SEPARATOR);
//		}
//		statementBuilder.replace(statementBuilder.length() - 2, statementBuilder.length(), ")\n");
//		setStatement(getStatement().concat(statementBuilder.toString()));
//	}
	
	//CREATE UNIQUE INDEX UniqueIndexName ON Table(Columns)
	private void appendUniqueConstraints(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		statementBuilder.append("CREATE UNIQUE INDEX ").append("unique").append(table.getTableName())
						.append(" ON ").append(table.getTableName()).append("(");
		for (String uniqueColumn : table.getUniqueColumns()) {
			statementBuilder.append(uniqueColumn).append(COMMA_SEPARATOR);
		}
		statementBuilder.replace(statementBuilder.length() - 2, statementBuilder.length(), ");\n");
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
	protected boolean checkNotNull(IDatabaseTableSchema table, String columnName) {
		return (table.getNotNullColumns() != null && table.getNotNullColumns().contains(columnName)) || table.getPrimaryKeyColumns().contains(columnName);
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
}
