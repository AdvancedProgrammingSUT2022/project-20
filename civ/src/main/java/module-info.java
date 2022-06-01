module civ {
    requires com.google.gson;

    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens ir.ap.client to javafx.fxml;
    exports ir.ap.client;
}