package services;

import Constants.Datatype;
import Constants.QueryConstants;
import exceptions.DatabaseException;
import model.Column;
import model.DatabaseResponse;
import model.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Constants.QueryConstants.*;

public class MetadataServicesImpl implements MetadataServices {

    private final String META_DIR = "meta-info";
    private final String COLUMN_DETAILS_TABLE = "column_details.tsv";
    private final String TABLE_DETAILS_TABLE = "table_details.tsv";

    @Override
    public DatabaseResponse createMeta() throws IOException {
        //Create Meta folder if not exist
        File metaDir = new File(DB_PATH + META_DIR);
        if (!metaDir.isDirectory()) {
            boolean test = metaDir.mkdir();
        }

        //Create Column details if not exist
        File columnDetailsTable = new File(metaDir.getAbsolutePath() + SLASH + COLUMN_DETAILS_TABLE);
        if (!columnDetailsTable.isFile()) {
            columnDetailsTable.createNewFile();
            FileWriter fileWriter = new FileWriter(columnDetailsTable);
            fileWriter.write("TableName" + DELIMITER + "ColumnName" + DELIMITER + "Datatype" + DELIMITER + "Constraints" + EOL);
            fileWriter.close();
        }

        //Create Table details if not exist
        File tableDetailsTable = new File(metaDir.getAbsolutePath() + SLASH + TABLE_DETAILS_TABLE);
        if (!tableDetailsTable.isFile()) {
            tableDetailsTable.createNewFile();
            FileWriter fileWriter = new FileWriter(tableDetailsTable);
            fileWriter.write("Database" + DELIMITER + "TableName" + EOL);
            fileWriter.close();
        }

        return new DatabaseResponse(true, "Meta Created");
    }

    @Override
    public DatabaseResponse insertTableDetailsTable(Table table) {
        File tableDetailsTable = new File(DB_PATH + META_DIR + SLASH + TABLE_DETAILS_TABLE);
        if (!tableDetailsTable.isFile()) {
            return new DatabaseResponse(false, "Meta file not found. Create meta first");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tableDetailsTable, true))) {
            for (String[] row : table.getRows()) {
                bufferedWriter.append(String.join(DELIMITER, row)).append(EOL);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return new DatabaseResponse(false, "Table entry failed");
        }
        return new DatabaseResponse(true, "Success");
    }

    @Override
    public DatabaseResponse insertColumnDetailsTable(Table table) {
        File columnDetailsTable = new File(DB_PATH + META_DIR + SLASH + COLUMN_DETAILS_TABLE);
        if (!columnDetailsTable.isFile()) {
            return new DatabaseResponse(false, "Meta file not found. Create meta first");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(columnDetailsTable, true))) {
            for (String[] row : table.getRows()) {
                bufferedWriter.append(String.join(DELIMITER, row)).append(EOL);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return new DatabaseResponse(false, "Table entry failed");
        }
        return new DatabaseResponse(true, "Success");
    }

    @Override
    public List<Column> getColumnDetailsForTable(String tableName) throws DatabaseException {
        File columnDetailsTable = new File(DB_PATH + META_DIR + SLASH + COLUMN_DETAILS_TABLE);
        if (!columnDetailsTable.isFile()) {
            throw new DatabaseException("Metafile not found");
        }
        List<Column> columns = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(columnDetailsTable))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                String[] tempArr = temp.split(DELIMITER);
                if (tempArr[0].equalsIgnoreCase(tableName)) {
                    Column column = new Column();
                    column.setColumnName(tempArr[1]);
                    column.setDatatype(Datatype.valueOf(tempArr[2]));
                    column.setConstraints(tempArr.length == 4 ? tempArr[3].split(",") : null);
                    columns.add(column);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columns;
    }

    @Override
    public DatabaseResponse dropTable(String tableName) {
        File columnDetailsTable = new File(DB_PATH + META_DIR + SLASH + COLUMN_DETAILS_TABLE);
        if (columnDetailsTable.isFile()) {
            return new DatabaseResponse(false, "Meta file not found. Create meta first");
        }
        StringBuilder finalValues = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(columnDetailsTable))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                String[] tempArr = temp.split(DELIMITER);
                if (!tempArr[0].equalsIgnoreCase(tableName)) {
                    finalValues.append(temp).append(EOL);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new DatabaseResponse(false, "Table entry failed");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(columnDetailsTable))) {
            bufferedWriter.write(finalValues.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DatabaseResponse(true, "Success");
    }
}
