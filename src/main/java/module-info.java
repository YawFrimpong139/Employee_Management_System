module com.example.employeemgtsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.employeemgtsystem to javafx.fxml;
    opens com.example.employeemgtsystem.models to javafx.base;
    exports com.example.employeemgtsystem;
    exports com.example.employeemgtsystem.controllers;
    opens com.example.employeemgtsystem.controllers to javafx.fxml;
}