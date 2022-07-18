package ir.ap.client;

import ir.ap.client.components.map.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class GameView extends View {

    @FXML
    private BorderPane root;

    @FXML
    private AnchorPane mapPart;

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
        mapPart.getChildren().add(scrollMap);
    }

    private void initializeMap() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameView.class.getResource("fxml/components/map/map-view.fxml"));
        map = fxmlLoader.load();
        mapView = fxmlLoader.getController();
    }
}
