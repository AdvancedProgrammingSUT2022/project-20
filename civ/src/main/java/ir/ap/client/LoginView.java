package ir.ap.client;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginView extends View {

    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    TextField nickname;
    @FXML
    Label error;
    public void login() {
        error.setText("");
        String Username = username.getText();
        String Password = password.getText();
        if(Username.equals("")) {error.setText("username field is empty"); return;}
        if(Password.equals("")) {error.setText("password field is empty"); return;}
        JsonObject respone = USER_CONTROLLER.login(Username,Password);

        String[] args = respone.toString().split("\"");
        String message = args[3];

        if(message != null) error.setText(message);
    }

    public void signup() {
        error.setText("");
        String Username = username.getText();
        String Password = password.getText();
        String Nickname = nickname.getText();
        if(Username.equals("")) error.setText("username field is empty");
        if(Password.equals("")) error.setText("password field is empty");
        JsonObject respone = USER_CONTROLLER.register(Username,Nickname,Password);

        String[] args = respone.toString().split("\"");
        String message = args[3];

        if(message != null) error.setText(message);
    }
}
