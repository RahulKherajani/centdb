package services;

import Constants.Datatype;
import model.Column;
import model.Table;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseServicesTest {

    @org.junit.jupiter.api.Test
    void createDatabase() {
    }

    @org.junit.jupiter.api.Test
    void useDatabase() {
    }

    @org.junit.jupiter.api.Test
    void createTable() throws IOException {
        DatabaseServices services = new DatabaseServicesImpl();
        services.createDatabase("test");
        services.useDatabase("test");
        List<Column> list = new ArrayList<>();
        Column column = new Column();
        column.setColumnName("name");
        column.setDatatype(Datatype.STRING);
        Column column2 = new Column();
        column2.setColumnName("id");
        column2.setDatatype(Datatype.INT);

        list.add(column);
        list.add(column2);


        services.createTable("users",list);

    }

    @Test
    void insertTable() {
        DatabaseServicesImpl services = new DatabaseServicesImpl();
        ArrayList<String[]> list = new ArrayList<String[]>();
        list.add(new String[]{"shiv","101"});
        list.add(new String[]{"shiv2","102"});
        Table table = new Table();
        table.setRows(list);
        table.setColumnCount(2);
        table.setColumns(new String[]{"name","id"});
        services.insertTable("users", table);
    }

    @org.junit.jupiter.api.Test
    void selectTable() {
    }

    @org.junit.jupiter.api.Test
    void updateTable() {
    }

    @org.junit.jupiter.api.Test
    void deleteTable() {

    }

    @org.junit.jupiter.api.Test
    void dropTable() throws FileNotFoundException {
        DatabaseServices services = new DatabaseServicesImpl();
        services.useDatabase("test");
        services.dropTable("users");
    }
}