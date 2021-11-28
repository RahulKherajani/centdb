package services;

import model.Column;
import model.DatabaseResponse;
import model.Table;

import java.io.IOException;
import java.util.List;

public interface MetadataServices {
    DatabaseResponse createMeta() throws IOException;
    DatabaseResponse insertColumnDetailsTable(Table table);
    DatabaseResponse insertTableDetailsTable(Table table);
    List<Column> getColumnDetailsForTable(String tableName);
    DatabaseResponse dropTable(String tableName);
}
