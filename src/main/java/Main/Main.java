package Main;

import User.UserMenuDriver;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException, NoSuchAlgorithmException {
        UserMenuDriver driver = new UserMenuDriver();
        driver.showForm();
    }
}
