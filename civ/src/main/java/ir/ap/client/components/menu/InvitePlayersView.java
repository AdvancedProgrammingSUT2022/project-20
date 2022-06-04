package ir.ap.client.components.menu;

import com.google.gson.JsonObject;
import ir.ap.client.View;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;

public class InvitePlayersView extends View {
    @FXML
    private TextField invitePlayer;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<List<String>> invitedPlayersTable;

    private ArrayList<String> invitedUsers;

    {
        invitedUsers = new ArrayList<>();
    }

    public void initialize() {
        initializeTable();
    }

    private void initializeTable() {
        invitedPlayersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void invite() {
        String username = invitePlayer.getText();
        JsonObject response = USER_CONTROLLER.inviteUser(currentUsername, username);
        if (responseOk(response)) {
            success(messageLabel, getField(response, "msg", String.class));
            addInvitedUser(username);
        } else {
            error(messageLabel, getField(response, "msg", String.class));
        }
    }

    public void addInvitedUser(String username) {
        invitedUsers.add(username);
        invitedPlayersTable.getItems().add(Arrays.asList(username, "Yes"));
    }

    private void error(Label messageLabel, String msg) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(Color.RED);
    }

    private void success(Label messageLabel, String msg) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(Color.GREEN);
    }
}