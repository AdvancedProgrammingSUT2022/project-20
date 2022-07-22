package ir.ap.client;

import ir.ap.client.components.UserSerializer;
import ir.ap.model.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static final int SERVER_PORT = 8000;

    public static final double SCREEN_WIDTH = 1024;
    public static final double SCREEN_HEIGHT = 576;

    private static Scene scene;

    private static MediaPlayer menuMusic;
    private static MediaPlayer launchGameMusic;

    public static Scene getScene() {
        return scene;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void setRoot(String fxml) throws IOException {
        if( fxml.equals("fxml/launch-game-view.fxml") ){
            menuMusic.stop();
            launchGameMusic = new MediaPlayer(new Media(App.class.getResource("png/civAsset/Sounds/LaunchGameMusic.mp3").toExternalForm()));
            launchGameMusic.setAutoPlay(true);
            launchGameMusic.play();
        }
        else if( fxml.equals("fxml/game-view.fxml") ){
            launchGameMusic.stop();
        }
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
        scene = new Scene(loadFXML("fxml/login-view.fxml"), SCREEN_WIDTH, SCREEN_HEIGHT);
<<<<<<< HEAD
        scene.getStylesheets().add(GameView.class.getResource("css/styles.css").toExternalForm());
        menuMusic = new MediaPlayer(new Media(App.class.getResource("png/civAsset/Sounds/MenuMusic.mp3").toExternalForm()));
        menuMusic.setAutoPlay(true);
        menuMusic.play();
=======
>>>>>>> 4009d0c80c1d102fbac16689f3b4166283c06cb8
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Civ");
        stage.show();
    }
}
