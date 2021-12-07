package services;

import exceptions.DatabaseException;

import java.io.IOException;

public interface ExportAndREServices {
    void exportStructure(String fileName) throws DatabaseException, IOException;
}
