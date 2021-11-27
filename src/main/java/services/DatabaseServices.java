package services;

import model.Column;
import model.DatabaseResponse;
import model.Table;
import model.WhereCondition;

import java.util.List;

public interface DatabaseServices {
    DatabaseResponse createDatabase(String dbName);

    DatabaseResponse useDatabase(String dbName);

    DatabaseResponse createTable(String tableName, List<Column> columns);

    DatabaseResponse insertTable(String tableName, Table tableDate);

    DatabaseResponse selectTable(String tableName, WhereCondition whereCondition);

    DatabaseResponse updateTable(String tableName, String column, String value, WhereCondition whereCondition);

    DatabaseResponse deleteTable(String tableName, WhereCondition whereCondition);

    DatabaseResponse dropTable(String tableName);
}
