package ir.ap.client;

import ir.ap.client.components.menu.InvitePlayersView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

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
        initializeInvitationMenu("fxml/components/menu/invite-players.fxml");
        currentMenu = inviteMenuFXML;
        invitePlayersView.addInvitedUser(currentUsername, false);
        menuPane.setContent(currentMenu);
        selectMapButtons.managedProperty().bind(selectMapButtons.visibleProperty());
        inviteButtons.managedProperty().bind(inviteButtons.visibleProperty());
        selectMapButtons.setVisible(false);
    }

    private void initializeInvitationMenu(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchGameView.class.getResource(fxml));
        inviteMenuFXML = fxmlLoader.load();
        invitePlayersView = fxmlLoader.getController();
    }

}
