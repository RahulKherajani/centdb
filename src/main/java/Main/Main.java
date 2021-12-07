package Main;

import User.UserMenuDriver;
import logmanagement.GeneralLogWriter;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws IOException, ParseException, NoSuchAlgorithmException {
        UserMenuDriver driver = new UserMenuDriver();
        
        GeneralLogWriter gen = new GeneralLogWriter();
        Runnable helloRunnable = new Runnable() {
            public void run() {
                gen.addMetadata();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 60, TimeUnit.SECONDS);
        
        driver.showForm();
        executor.shutdown();
    }
}
