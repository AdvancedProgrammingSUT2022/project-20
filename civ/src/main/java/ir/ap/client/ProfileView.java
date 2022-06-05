package ir.ap.client;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class ProfileView extends View {

    @FXML
    private Label messagePassword;
    @FXML
    private Label messageNickname;
    @FXML
    private TextField oldPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField nickname;


    public void initialize() {

    }

    public void onSavePassword() {
        String OldPassword = oldPassword.getText();
        String NewPassword = newPassword.getText();

        JsonObject response = USER_CONTROLLER.changePassword(currentUsername,OldPassword,NewPassword);
        String Message = getField(response, "msg", String.class);
        if(Message != null) {
            if(responseOk(response))
                success(messagePassword, Message);
            else
                error(messagePassword, Message);
        }
    }
    public void onSaveNickname()
    {
        String Nickname = nickname.getText();
        JsonObject response = USER_CONTROLLER.changeNickname(currentUsername,Nickname);
        String Message = getField(response, "msg", String.class);
        if(Message != null) {
            if(responseOk(response))
                success(messageNickname, Message);
            else
                error(messageNickname, Message);
        }
    }
    public void error(Label label, String msg) {
        label.setText(msg);
        label.setTextFill(Color.RED);
    }

    public void success(Label label, String msg) {
        label.setText(msg);
        label.setTextFill(Color.GREEN);
    }
}
