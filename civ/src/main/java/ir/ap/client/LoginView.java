package ir.ap.client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

public class LoginView extends View {

    @FXML
    AnchorPane rootPane;

    public void initialize() {
        //setBackground();
    }

    private void setBackground() {
        Image bgImage = new Image(LoginView.class.getResource("png/ARX/Background_A.png").toExternalForm());
        System.out.println(bgImage);
        Background background = new Background(
                new BackgroundImage(bgImage, null, null, null, null)
        );
        rootPane.setBackground(background);
    }
}
