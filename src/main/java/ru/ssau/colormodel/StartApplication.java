package ru.ssau.colormodel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("file.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.setTitle("Учебный программный комплекс визуализации цветовых моделей");
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}