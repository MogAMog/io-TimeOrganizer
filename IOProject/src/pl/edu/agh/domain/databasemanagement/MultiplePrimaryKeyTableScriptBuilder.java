package pl.edu.agh.domain.databasemanagement;

class MultiplePrimaryKeyTableScriptBuilder extends SqlDatabaseTableScriptBuilder {
	
	@Override
	protected void appendPrimaryKeyAndColumns(IDatabaseTableSchema table) {
		appendColumns(table);
		appendPrimaryKey(table);
	}

	private void appendColumns(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		for(String columnName : table.getColumnNamesWithSqliteTypes().keySet()) {
			statementBuilder.append(columnName).append(" ")
							.append(table.getColumnNamesWithSqliteTypes().get(columnName))
							.append(checkNotNull(table, columnName) ? " NOT NULL" + COMMA_SEPARATOR : COMMA_SEPARATOR)
							.append("\n");
		}
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
	private void appendPrimaryKey(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		statementBuilder.append("PRIMARY KEY (");
		for(String primaryKey : table.getPrimaryKeyColumns()) {
			statementBuilder.append(primaryKey).append(",");
		}
		statementBuilder.replace(statementBuilder.length() - 1, statementBuilder.length(), ")\n");
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
}
