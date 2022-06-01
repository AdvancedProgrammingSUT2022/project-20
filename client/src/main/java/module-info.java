module ir.ap.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens ir.ap.client to javafx.fxml;
    exports ir.ap.client;
}