package services;

import model.Column;
import model.DatabaseResponse;
import model.Table;
import model.WhereCondition;

import java.util.List;

public class DatabaseServicesImpl implements DatabaseServices{
    @Override
    public DatabaseResponse createDatabase(String dbName) {
        return null;
    }

    @Override
    public DatabaseResponse useDatabase(String dbName) {
        return null;
    }

    @Override
    public DatabaseResponse createTable(String tableName, List<Column> columns) {
        return null;
    }

    @Override
    public DatabaseResponse insertTable(String tableName, Table tableDate) {
        return null;
    }

    @Override
    public DatabaseResponse selectTable(String tableName, WhereCondition whereCondition) {
        return null;
    }

    @Override
    public DatabaseResponse updateTable(String tableName, String column, String value, WhereCondition whereCondition) {
        return null;
    }

    @Override
    public DatabaseResponse deleteTable(String tableName, WhereCondition whereCondition) {
        return null;
    }

    @Override
    public DatabaseResponse dropTable(String tableName) {
        return null;
    }
}
