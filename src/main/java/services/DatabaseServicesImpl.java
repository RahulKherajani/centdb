package services;

import Constants.QueryConstants;
import Utility.Utility;
import model.Column;
import model.DatabaseResponse;
import model.Table;
import model.WhereCondition;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServicesImpl implements DatabaseServices{
    @Override
    public DatabaseResponse createDatabase(String dbName) {
        String dbPath = QueryConstants.DB_PATH +dbName;
        File file = new File(dbPath);
        if(file.exists()) {
            Utility.displayMessage("Database already exist");
            return new DatabaseResponse(false,"Database already exist");
        }
        boolean  isCreated = file.mkdir();
        Utility.displayMessage("Database created successfully");
        return new DatabaseResponse(isCreated,"Database created successfully");
    }

    @Override
    public DatabaseResponse useDatabase(String dbName) {
        boolean fileExist = new File(QueryConstants.DB_PATH+dbName).exists();
        if(fileExist) {
            QueryConstants.CURRENT_DB = dbName;
            Utility.displayMessage("Selected the database");
            return new DatabaseResponse(true,"Selected the database"+dbName);
        } else {
            Utility.displayMessage("Database does not exist");
            return new DatabaseResponse(false,"Database does not exist");
        }
    }

    @Override
    public DatabaseResponse createTable(String tableName, List<Column> columns) throws IOException {

        if(QueryConstants.CURRENT_DB=="") {
            Utility.displayMessage("Please select a database");
            return new DatabaseResponse(false,"Please select a database");
        }
        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName+".tsv";
        File file = new File(tablePath);
        if(file.exists()) {
            Utility.displayMessage("Table exists");
            return new DatabaseResponse(false,"Table already exists");
        }
        boolean isCreated = false;
        try {
            isCreated = file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter columnNameWriter = new FileWriter(tablePath, true);
        StringBuilder sb = new StringBuilder();

        for(Column columnName: columns) {
            sb.append(columnName.getColumnName()+"~");
        }
        columnNameWriter.write(sb.toString() + "\n");
        columnNameWriter.close();
        Utility.displayMessage("Created");
        return new DatabaseResponse(true,"Created "+tableName);
    }

    @Override
    public DatabaseResponse insertTable(String tableName, Table tableDate) throws IOException {

        if(QueryConstants.CURRENT_DB=="") {
            Utility.displayMessage("Please select a database");
            return new DatabaseResponse(false,"Please select a database");
        }

        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName+".tsv";

        File file = new File(tablePath);
        if(!file.exists()) {
            Utility.displayMessage("Table doesn't exist");
            return new DatabaseResponse(false,"Table doesn't exist");
        }
        FileWriter columnNameWriter = new FileWriter(tablePath, true);
        StringBuilder insertValuesStringBuilder = new StringBuilder();

        ArrayList<String[]> rowVals = tableDate.getRows();
        for (String[] row: rowVals) {
            for (String rowV : row) {
                insertValuesStringBuilder.append(rowV + "~");
            }
            insertValuesStringBuilder.append("\n");
        }

        columnNameWriter.write(insertValuesStringBuilder.toString());
        columnNameWriter.close();
        Utility.displayMessage("Inserted successfully");
        return new DatabaseResponse(true,"Inserted successfully"+tableName);
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
    public DatabaseResponse deleteTable(String tableName, WhereCondition whereCondition) throws IOException {
        if(QueryConstants.CURRENT_DB=="") {
            Utility.displayMessage("Please select a database");
            return new DatabaseResponse(false,"Please select a database");
        }

        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName+".tsv";
        String tempTablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+"temp"+tableName+".tsv";

        File file = new File(tablePath);
        if(!file.exists()) {
            Utility.displayMessage("Table doesn't exist");
            return new DatabaseResponse(false,"Table doesn't exist");
        }

        File createTempDeleteFile = new File(tempTablePath);
        createTempDeleteFile.createNewFile(); // create file

        String value = whereCondition.getValue();
        String column = whereCondition.getColumn();

        BufferedReader deleteRowReader = new BufferedReader(new FileReader(tablePath));
        BufferedWriter deleteWriter = new BufferedWriter(new FileWriter(tempTablePath));
        String deleteRowLine = null;

        while((deleteRowLine=deleteRowReader.readLine())!=null) {
            if(!deleteRowLine.contains(value)) {
                deleteWriter.write(deleteRowLine + "\n");
            }
        }

        deleteRowReader.close();
        deleteWriter.close();

        file.delete();
        createTempDeleteFile.renameTo(file);

        Utility.displayMessage("Deleted row");
        return new DatabaseResponse(true,"Inserted successfully"+tableName);
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
