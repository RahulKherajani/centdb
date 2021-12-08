package services;

import model.Column;
import model.DatabaseResponse;
import model.Table;
import model.WhereCondition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface DatabaseServices {
    DatabaseResponse createDatabase(String dbName);

    DatabaseResponse useDatabase(String dbName);

    DatabaseResponse createTable(String tableName, List<Column> columns) throws IOException;

    DatabaseResponse insertTable(String tableName, Table tableDate) throws IOException;

    DatabaseResponse selectTable(String tableName, String columns, WhereCondition whereCondition) throws IOException;

    DatabaseResponse updateTable(String tableName, String column, String value, WhereCondition whereCondition) throws IOException;

    DatabaseResponse deleteTable(String tableName, WhereCondition whereCondition) throws IOException;

    DatabaseResponse dropTable(String tableName);

    DatabaseResponse beginTransaction() throws IOException;

    DatabaseResponse endTransaction(String transactionEndKeyword) throws IOException;

}
