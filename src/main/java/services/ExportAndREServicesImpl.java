package services;

import exceptions.DatabaseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportAndREServicesImpl implements ExportAndREServices{
    @Override
    public void exportStructure(String fileName) throws DatabaseException, IOException {
        File outputFile = new File(fileName);
        if(outputFile.isFile()){
            throw new DatabaseException("File Already Exist");
        }
        outputFile.createNewFile();
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile))){

        }catch (IOException io){
            io.printStackTrace();
        }

    }
}
