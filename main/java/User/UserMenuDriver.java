package User;

import Constants.MenuConstants;
import Utility.Utility;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UserMenuDriver {

    UserProfile profile;
    CryptoHash encryption;
    FileOperations fileOp;
    Boolean isSuccessfulValidation = false;
    Boolean isDuplicateUser = false;

    public UserMenuDriver() {
        profile = new UserProfile();
        encryption = new CryptoHash();
        fileOp = new FileOperations();
    }

    public void showForm() throws IOException, ParseException, NoSuchAlgorithmException {

        Utility.displayMessage(MenuConstants.Form.getValue());
        String formInput = Utility.receiveInput();

        if (formInput.contentEquals(MenuConstants.Selected1Registration.getValue())) {
            showRegisterForm();
        } else if (formInput.contentEquals(MenuConstants.Selected2Login.getValue())){
            showLoginForm();
        } else {
            Utility.displayMessage(MenuConstants.IncorrectFormInput.getValue());
        }
    }

    public void showRegisterForm() throws IOException, NoSuchAlgorithmException, ParseException {

        Utility.displayMessage(MenuConstants.EnterName.getValue());
        String userID = Utility.receiveInput();
        checkIfUserExists(userID);

        if (!isDuplicateUser) {
            profile.setUserID(encryption.hashCredentials(userID));
            Utility.displayMessage(MenuConstants.EnterPassword.getValue());
            profile.setPassword(encryption.hashCredentials(Utility.receiveInput()));

            Utility.displayMessage(MenuConstants.EnterSecurityQuestion.getValue());
            profile.setSecurityQuestion( Utility.receiveInput());
            Utility.displayMessage(MenuConstants.EnterSecurityQuestionAnswer.getValue());
            profile.setSecurityAnswer(Utility.receiveInput());
            Utility.displayMessage(profile.toString());

            fileOp.writeToFile(profile);
        } else {
            Utility.displayMessage("DUPLICATE USERID");
        }

    }

    public void showLoginForm() throws IOException, ParseException, NoSuchAlgorithmException {

        Utility.displayMessage(MenuConstants.EnterName.getValue());
        String id = Utility.receiveInput();

        Utility.displayMessage(MenuConstants.EnterPassword.getValue());
        String pwd = Utility.receiveInput();

        validateUser(id, pwd);

        if (isSuccessfulValidation) {
            displayQueriesMenu();
        } else {
            Utility.displayMessage(MenuConstants.InvalidCredentials.getValue());
        }
    }

    public Boolean checkIfUserExists(String uid) throws IOException, ParseException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader((new FileReader(MenuConstants.UserProfileFile.getValue())));
        String line = reader.readLine();
        JSONParser parser = new JSONParser();
        String hashedID = encryption.hashCredentials(uid);
        while(line!=null) {
            JSONObject obj = (JSONObject) parser.parse(line);
            if (obj.get("userID").equals(hashedID)) {
                isDuplicateUser = true;
            }
            line = reader.readLine();
        }
        return isDuplicateUser;
    }



    // place holder. delete later
    public void displayQueriesMenu() {
        Utility.displayMessage("QUERIES MENU");
    }

    public Boolean validateUser(String id, String pwd) throws IOException, ParseException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader((new FileReader(MenuConstants.UserProfileFile.getValue())));
        String line = reader.readLine();

        String hashedID = encryption.hashCredentials(id);
        String hashedPWD = encryption.hashCredentials(pwd);

        JSONParser parser = new JSONParser();
        while(line!=null) {
            JSONObject obj = (JSONObject) parser.parse(line);
            if ((obj.get("userID").equals(hashedID)) && ((obj.get("userPwd").equals((hashedPWD))))) {
                isSuccessfulValidation = true;
            }
            line = reader.readLine();
        }
        return isSuccessfulValidation;
    }

}
