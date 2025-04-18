package com.example.employeemgtsystem.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class LoginController {

    @FXML
    private ImageView imageView;

    @FXML
    private Button getStartedBtn;

    @FXML
    private void initialize() {
        // Initialize any required components
        InputStream inputStream = getClass().getResourceAsStream("/com/example/employeemgtsystem/img/team-building_8163723.png");
        if (inputStream != null) {
            Image image = new Image(inputStream);
            imageView.setImage(image);
            try {
                inputStream.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error loading image in LoginController!");
        }
    }

    @FXML
    private void handleGetStarted() {
        try {
            // Load the main management system FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/employeemgtsystem/views/main.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) getStartedBtn.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Employee Management System");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately
            System.err.println("Error loading the main view: " + e.getMessage());
        }
    }
}
