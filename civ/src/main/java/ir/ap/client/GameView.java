package ir.ap.client;

import com.google.gson.JsonObject;
import ir.ap.client.components.Tile;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class GameView extends View {

    @FXML
    private AnchorPane root;

    public void initialize() {
        showCurrentMap(GAME_CONTROLLER.mapShow(currentUsername, 0));
    }

    private void showCurrentMap(JsonObject response) {

    }
}
