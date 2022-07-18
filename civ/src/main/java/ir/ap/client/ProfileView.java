package ir.ap.client;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class ProfileView extends View {

    @FXML
    private Label messagePassword;
    @FXML
    private Label messageNickname;
    @FXML
    private TextField oldPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField nickname;
    @FXML
    GridPane imagesGrid;
    @FXML
    ImageView selectedImage;

    Image[][] images = new Image[2][2];

    public void initialize() {
        images[0][0] = new Image(ProfileView.class.getResource("png/Backgrounds/14.png").toExternalForm());
        images[0][1] = new Image(ProfileView.class.getResource("png/Backgrounds/16.png").toExternalForm());
        images[1][0] = new Image(ProfileView.class.getResource("png/Backgrounds/18.png").toExternalForm());
        images[1][1] = new Image(ProfileView.class.getResource("png/Backgrounds/DioBackground.png").toExternalForm());
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 2; j++)
            {
                ImageView imageView = new ImageView(images[i][j]);
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setOnMouseClicked(this::onImageAction);
                imagesGrid.add(imageView, i,j);
            }
    }

    private void onImageAction(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView)mouseEvent.getSource();
        String imageUrl = imageView.getImage().getUrl();
        //TODO: server
    }

    public void chooseImage()
    {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getStage());
        Image image = new Image(file.getAbsolutePath());
        selectedImage.setImage(image);
        //TODO: server
    }

    public void onSavePassword() {
        String OldPassword = oldPassword.getText();
        String NewPassword = newPassword.getText();

        JsonObject response = send("changePassword", currentUsername,OldPassword,NewPassword);
        String Message = getField(response, "msg", String.class);
        if(Message != null) {
            if(responseOk(response))
                success(messagePassword, Message);
            else
                error(messagePassword, Message);
        }
    }
    public void onSaveNickname()
    {
        String Nickname = nickname.getText();
        JsonObject response = send("changeNickname", currentUsername,Nickname);
        String Message = getField(response, "msg", String.class);
        if(Message != null) {
            if(responseOk(response))
                success(messageNickname, Message);
            else
                error(messageNickname, Message);
        }
    }
    public void error(Label label, String msg) {
        label.setText(msg);
        label.setTextFill(Color.RED);
    }

    public void success(Label label, String msg) {
        label.setText(msg);
        label.setTextFill(Color.GREEN);
    }
}
