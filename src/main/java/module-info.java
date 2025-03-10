module ru.ssau.colormodel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires opencv;
    requires java.desktop;

    opens ru.ssau.colormodel to javafx.fxml;
    exports ru.ssau.colormodel;
    exports controller;
    opens controller to javafx.fxml;
}