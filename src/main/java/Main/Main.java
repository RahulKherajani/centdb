package Main;

import Constants.Operation;
import User.UserMenuDriver;
import logmanagement.GeneralLogWriter;

import model.WhereCondition;
import org.json.simple.parser.ParseException;
import services.DatabaseServices;
import services.DatabaseServicesImpl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws IOException, ParseException, NoSuchAlgorithmException {
//        UserMenuDriver driver = new UserMenuDriver();
//
//        GeneralLogWriter gen = new GeneralLogWriter();
//        Runnable helloRunnable = new Runnable() {
//            public void run() {
//                gen.addMetadata();
//            }
//        };
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(helloRunnable, 0, 60, TimeUnit.SECONDS);
//
//        driver.showForm();
//        executor.shutdown();
//
        transcation();
    }

    public static void transcation () throws IOException {
        DatabaseServicesImpl obj = new DatabaseServicesImpl();
        obj.useDatabase("test2");


        obj.beginTransaction();
//
//        WhereCondition whereCondition = new WhereCondition();
//
//        whereCondition.setColumn("id");
//        whereCondition.setValue("99");
//        whereCondition.setOperation(Operation.GREATER_THAN);
//        DatabaseServices services = new DatabaseServicesImpl();
//        services.updateTable("test1table", "name", "greatest", whereCondition);

        obj.endTransaction("COMMIT");

    }

}
