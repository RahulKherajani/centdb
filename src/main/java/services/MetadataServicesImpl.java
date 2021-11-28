package services;

import Constants.QueryConstants;
import model.Column;
import model.DatabaseResponse;
import model.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static Constants.QueryConstants.*;

public class MetadataServicesImpl implements MetadataServices{

    private final String META_DIR = "META-INFO";
    private final String COLUMN_DETAILS_TABLE = "";
    private final String TABLE_DETAILS_TABLE = "";

    @Override
    public DatabaseResponse createMeta() throws IOException {
        //Create Meta folder if not exist
        File metaDir = new File(DB_PATH + SLASH + META_DIR);
        if (!metaDir.isDirectory()) {
            metaDir.mkdir();
        }
        //Create Column details if not exist
        File columnDetailsTable = new File(metaDir.getAbsolutePath() + SLASH + COLUMN_DETAILS_TABLE);
        if (!columnDetailsTable.isFile()) {
            columnDetailsTable.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(columnDetailsTable);
        fileWriter.write("TableName"+DELIMITER+"ColumnName"+DELIMITER+"Datatype"+DELIMITER+"Constraints"+EOL);
        fileWriter.close();

        //Create Table details if not exist
        File tableDetailsTable = new File(metaDir.getAbsolutePath() + SLASH + TABLE_DETAILS_TABLE);
        if (!tableDetailsTable.isFile()) {
            tableDetailsTable.createNewFile();
        }
        fileWriter = new FileWriter(columnDetailsTable);
        fileWriter.write("Database"+DELIMITER+"TableName"+EOL);
        fileWriter.close();

        return new DatabaseResponse(true, "Meta Created");
    }

    @Override
    public DatabaseResponse insertColumnDetailsTable(Table table) {
        return null;
    }

    @Override
    public DatabaseResponse insertTableDetailsTable(Table table) {
        return null;
    }

    @Override
    public List<Column> getColumnDetailsForTable(String tableName) {
        return null;
    }

    @Override
    public DatabaseResponse dropTable(String tableName) {
        return null;
    }
}
