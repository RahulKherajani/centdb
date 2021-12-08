package services;

import exceptions.DatabaseException;
import model.Column;
import model.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static Constants.QueryConstants.EOL;

public class ExportAndREServicesImpl implements ExportAndREServices {
    @Override
    public void exportStructure(String fileName) throws DatabaseException {
        System.out.println("Enter Database Name:");
        String dbName = new Scanner(System.in).next();
        MetadataServices metadataServices = new MetadataServicesImpl();
        List<String> tables = metadataServices.getTables(dbName);
        if (tables.size() == 0) {
            throw new DatabaseException("Database has no table!");
        }
        File outputFile = new File(fileName);
        if (outputFile.isFile()) {
            throw new DatabaseException("File Already Exist");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile))) {
            outputFile.createNewFile();
            for (String table : tables) {
                //Drop
                bufferedWriter.append("DROP TABLE IF EXISTS `" + table + "`;\n");

                //Create
                bufferedWriter.append("CREATE TABLE `" + table + "` (");
                List<Column> columns = metadataServices.getColumnDetailsForTable(table);
                for (Column column : columns) {
                    bufferedWriter.append("\t `"
                            + column.getColumnName() + "` "
                            + column.getDatatype().type + " "
                            + String.join(" ", column.getConstraints())
                            + "\n");
                }
                bufferedWriter.append(");\n");

                //Lock Table
                bufferedWriter.append("LOCK TABLES `" + table + "` WRITE;\n");

                //Insert
                bufferedWriter.append("INSERT INTO `" + table + "` VALUES ");
                DatabaseServices databaseServices = new DatabaseServicesImpl();
                Table table1 = databaseServices.selectTable(table, "*", null).getTableData();
                List<String> tempList = new ArrayList<>();
                for (String[] row : table1.getRows()) {
                    String temp = Arrays.stream(row).map(x -> "`" + x + "`").collect(Collectors.joining(","));
                    tempList.add("(" + temp + ")");
                }
                bufferedWriter.append(String.join(",", tempList));
                bufferedWriter.append(";\n");

                //UNLOCK
                bufferedWriter.append("UNLOCK TABLES;\n");
            }
            bufferedWriter.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void reverseEngineering(String fileName) throws DatabaseException {
        System.out.println("Enter Database Name:");
        String dbName = new Scanner(System.in).next();
        MetadataServices metadataServices = new MetadataServicesImpl();
        List<String> tables = metadataServices.getTables(dbName);
        if (tables.size() == 0) {
            throw new DatabaseException("Database has no table!");
        }
        File outputFile = new File(fileName);
        if (outputFile.isFile()) {
            throw new DatabaseException("File Already Exist");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile))) {
            outputFile.createNewFile();

            //Tables
            bufferedWriter.append("___________________________\n");
            bufferedWriter.append(String.format("|%-25s|\n","Tables"));
            bufferedWriter.append("___________________________\n");
            tables.stream()
                    .forEach(table->System.out.println(String.format("|%-25s|\n",table)));
            bufferedWriter.append("___________________________\n");
            bufferedWriter.append(EOL);
            bufferedWriter.append(EOL);
            bufferedWriter.append(EOL);
            for (String table : tables) {
                bufferedWriter.append("Table Name:"+table).append(EOL);
                //Columns
                bufferedWriter.append("____________________________________________________________________________________________________________\n");
                bufferedWriter.append(String.format("|%s|%s|%s|%s|%s|%s|\n","Field","Type","Null","Key","Default","Extra"));
                bufferedWriter.append("____________________________________________________________________________________________________________\n");
               List<Column> columns = metadataServices.getColumnDetailsForTable(table);
                for (Column column : columns) {
                    String constraints = String.join(",",column.getConstraints());
                    bufferedWriter.append(String.format("|%s|%s|%s|%s|%s|%s|\n",
                            column.getColumnName(),
                            column.getDatatype().toString(),
                            constraints.contains("not null") ? "NO":"YES",
                            constraints.contains("primary") ? "PRI":"",
                            "NULL",""));
                }
            }
            bufferedWriter.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
