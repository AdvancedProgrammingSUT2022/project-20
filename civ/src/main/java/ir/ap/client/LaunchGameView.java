package ir.ap.client;

import com.google.gson.JsonObject;
import ir.ap.client.components.UserSerializer;
import ir.ap.client.components.menu.InvitePlayersView;
import ir.ap.controller.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

public class LaunchGameView extends View {

    @FXML
    private BorderPane root;
    @FXML
    private ScrollPane menuPane;
    @FXML
    private Button nextBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button startBtn;

    @FXML
    private HBox inviteButtons;
    @FXML
    private HBox selectMapButtons;

    private Parent currentMenu;
    private Parent inviteMenuFXML;
    private InvitePlayersView invitePlayersView;

    public void initialize() throws IOException {
        initializeInvitationMenu();
        currentMenu = inviteMenuFXML;
        invitePlayersView.addInvitedUser(currentUsername, false);
        menuPane.setContent(currentMenu);
        selectMapButtons.managedProperty().bind(selectMapButtons.visibleProperty());
        inviteButtons.managedProperty().bind(inviteButtons.visibleProperty());
        selectMapButtons.setVisible(false);
    }

    private void initializeInvitationMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchGameView.class.getResource("fxml/components/menu/invite-players.fxml"));
        inviteMenuFXML = fxmlLoader.load();
        invitePlayersView = fxmlLoader.getController();
    }

    public void nextMenu() {
        if (currentMenu.equals(inviteMenuFXML)) {
            ArrayList<UserSerializer> users = invitePlayersView.getInvitedUsers();
            String[] playersInGame = new String[users.size()];
            for (int i = 0; i < users.size(); i++) {
                playersInGame[i] = users.get(i).getUsername();
            }
            JsonObject res = GAME_CONTROLLER.newGame(playersInGame);
            if (responseOk(res))
                enterGame();
        }
    }
}
