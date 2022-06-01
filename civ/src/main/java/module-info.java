module civ {
    requires com.google.gson;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens ir.ap.model to com.google.gson;
    opens ir.ap.client to javafx.fxml;
    exports ir.ap.client;
}