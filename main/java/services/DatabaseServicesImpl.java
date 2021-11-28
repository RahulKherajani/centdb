package services;

import Constants.QueryConstants;
import Utility.Utility;
import model.Column;
import model.DatabaseResponse;
import model.Table;
import model.WhereCondition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

public class DatabaseServicesImpl implements DatabaseServices{
    @Override
    public DatabaseResponse createDatabase(String dbName) {
        String dbPath = "DatabaseCollection/" +dbName;
        File file = new File(dbPath);
        if(file.exists()) {
            return new DatabaseResponse(false,"Database already exist");
        }
        boolean  isCreated = file.mkdir();
        return new DatabaseResponse(isCreated,"Database created successfully");
    }

    @Override
    public DatabaseResponse useDatabase(String dbName) {
        boolean fileExist = new File(QueryConstants.DB_PATH+dbName).exists();
        if(fileExist) {
            QueryConstants.CURRENT_DB = dbName;
            return new DatabaseResponse(true,"Selected the database"+dbName);
        } else {
            return new DatabaseResponse(false,"Database does not exist");
        }
    }

    @Override
    public DatabaseResponse createTable(String tableName, List<Column> columns) throws IOException {

        if(QueryConstants.CURRENT_DB=="") {
            return new DatabaseResponse(false,"Please select a database");
        }
        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName+".tsv";
        File file = new File(tablePath);
        if(file.exists()) {
            return new DatabaseResponse(false,"Table already exists");
        }
        boolean isCreated = false;
        try {
            isCreated = file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter columnNameWriter = new FileWriter(tablePath);
        StringBuilder sb = new StringBuilder();

        for(Column columnName: columns) {
            sb.append(columnName.getColumnName()+"~");
        }
        columnNameWriter.write(sb.toString() + "\n");
        columnNameWriter.close();
        return new DatabaseResponse(true,"Selected the database"+tableName);
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
        if(QueryConstants.CURRENT_DB=="") {
            Utility.displayMessage("DB NOT SELECTED");
            return new DatabaseResponse(false,"Database not selected");
        }
        boolean fileExist = new File(QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName+".tsv").exists();
        if(fileExist) {
            File file = new File(QueryConstants.DB_PATH + QueryConstants.CURRENT_DB + "/" + tableName + ".tsv");
            Boolean success = file.delete();

            if (success) {
                Utility.displayMessage("success");
                return new DatabaseResponse(true, tableName + " has been deleted successfully");
            }
        }
        return new DatabaseResponse(false, tableName + " Error deleting");
    }
}
