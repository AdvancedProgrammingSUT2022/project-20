package ir.ap.client;

import com.google.gson.JsonObject;
import ir.ap.client.components.map.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class GameView extends View {

    @FXML
    private AnchorPane root;

    private ScrollPane scrollMap;

    private AnchorPane map;
    private MapView mapView;

    public void initialize() throws IOException {
        initializeMap();
        scrollMap = new ScrollPane(map);
        scrollMap.setMaxWidth(App.SCREEN_WIDTH);
        scrollMap.setMaxHeight(App.SCREEN_HEIGHT);
        // scrollMap.setLayoutX(value);
        // scrollMap.setLayoutY(value);
        scrollMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Platform.runLater(() -> {
            scrollMap.requestFocus();
        });
        root.getChildren().add(scrollMap);
    }

    private void initializeMap() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameView.class.getResource("fxml/components/map/map-view.fxml"));
        map = fxmlLoader.load();
        mapView = fxmlLoader.getController();
    }
}
