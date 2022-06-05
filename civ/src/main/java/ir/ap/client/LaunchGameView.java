package ir.ap.client;

import ir.ap.client.components.menu.InvitePlayersView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LaunchGameView extends View {

    @FXML
    private BorderPane root;

    private Parent currentMenu;
    private Parent inviteMenuFXML;
    private InvitePlayersView invitePlayersView;

    public void initialize() throws IOException {
        initializeInvitationMenu("fxml/components/menu/invite-players.fxml");
        currentMenu = inviteMenuFXML;
        invitePlayersView.addInvitedUser(currentUsername, false);
        root.setCenter(currentMenu);
    }

    private void initializeInvitationMenu(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchGameView.class.getResource(fxml));
        inviteMenuFXML = fxmlLoader.load();
        invitePlayersView = fxmlLoader.getController();
    }

}
