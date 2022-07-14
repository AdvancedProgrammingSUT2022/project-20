package ir.ap.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static final int SERVER_PORT = 8000;

    public static final double SCREEN_WIDTH = 1024;
    public static final double SCREEN_HEIGHT = 576;

    private static Scene scene;

    public static Scene getScene() {
        return scene;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void exit() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("fxml/game-view.fxml"), SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Civ");
        stage.show();
    }
}
