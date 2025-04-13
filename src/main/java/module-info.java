module ru.ssau.colormodel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires opencv;
    requires java.desktop;

    opens ru.ssau.colormodel to javafx.fxml;
    exports ru.ssau.colormodel;
    exports controller;
    opens controller to javafx.fxml;
}