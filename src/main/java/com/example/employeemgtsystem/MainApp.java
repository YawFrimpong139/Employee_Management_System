package com.example.employeemgtsystem;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/employeemgtsystem/views/getStarted.fxml"));
        Parent root = fxmlLoader.load();

//        Scene scene = new Scene(root, 1000, 700);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/example/employeemgtsystem/design.css")).toExternalForm());

        stage.setTitle("Employee Management System");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/com/example/employeemgtsystem/img/team-building_8163723.png")).toString()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
