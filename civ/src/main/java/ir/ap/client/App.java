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

    public static final double SCREEN_WIDTH = 1024;
    public static final double SCREEN_HEIGHT = 576;

    private static Scene scene;
    private static Stage stage;

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
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
        App.stage = stage;
        scene = new Scene(loadFXML("fxml/login-view.fxml"), SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Civ");
        stage.show();
    }
}
