package pl.edu.agh.domain.databasemanagement;

class SinglePrimaryKeyTableScriptBuilder extends SqlDatabaseTableScriptBuilder {
	
	@Override
	protected void appendPrimaryKeyAndColumns(IDatabaseTableSchema table) {
		appendPrimaryKey(table);
		appendColumns(table);
	}

	private void appendColumns(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		for(String columnName : table.getColumnNamesWithSqliteTypes().keySet()) {
			if(table.getPrimaryKeyColumns().get(0).equals(columnName)){
				continue;
			}
			statementBuilder.append(columnName).append(" ")
							.append(table.getColumnNamesWithSqliteTypes().get(columnName))
							.append(checkNotNull(table, columnName) ? " NOT NULL" + COMMA_SEPARATOR : COMMA_SEPARATOR)
							.append("\n");
		}
		statementBuilder.replace(statementBuilder.length() - 3, statementBuilder.length(), "");
		setStatement(getStatement().concat(statementBuilder.toString()));
	}
	
	private void appendPrimaryKey(IDatabaseTableSchema table) {
		StringBuilder statementBuilder = new StringBuilder();
		statementBuilder.append(table.getPrimaryKeyColumns().get(0))
						.append(" INTEGER PRIMARY KEY AUTOINCREMENT")
						.append(COMMA_SEPARATOR)
						.append("\n");
		setStatement(getStatement().concat(statementBuilder.toString()));
	}	
	
}
