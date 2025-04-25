package com.example.employeemgtsystem.controllers;

import com.example.employeemgtsystem.models.Employee;
import com.example.employeemgtsystem.models.EmployeeDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EmployeeDialogController {
    @FXML private Label dialogTitle;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> deptField;
    @FXML private TextField salaryField;
    @FXML private TextField ratingField;
    @FXML private TextField expField;
    @FXML private CheckBox activeField;
    @FXML private ButtonType saveButtonType;
    @FXML private ButtonType cancelButtonType;

    private EmployeeDatabase<Integer> employeeDatabase;
    private MainController mainController;
    private Employee<Integer> employeeToEdit;

    public void setEmployeeDatabase(EmployeeDatabase<Integer> employeeDatabase) {
        this.employeeDatabase = employeeDatabase;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEmployeeForEdit(Employee<Integer> employee) {
        this.employeeToEdit = employee;
        dialogTitle.setText("Edit Employee");

        idField.setText(employee.getEmployeeId().toString());
        idField.setEditable(false);
        nameField.setText(employee.getName());
        deptField.setValue(employee.getDepartment());
        salaryField.setText(String.valueOf(employee.getSalary()));
        ratingField.setText(String.valueOf(employee.getPerformanceRating()));
        expField.setText(String.valueOf(employee.getYearsOfExperience()));
        activeField.setSelected(employee.isActive());
    }

    @FXML
    public void initialize() {
        deptField.getItems().addAll("IT", "HR", "Finance", "Marketing", "Operations", "Engineering");


    }

    public void setupDialogPane(DialogPane dialogPane) {

        // Set up save button
        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateInput()) {
                event.consume(); // Prevent dialog from closing
            } else {
                saveEmployee(); // Perform save operation
            }
        });

        // Set up cancel button
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
            // Default cancel behavior is fine
        });
    }

    private void handleSaveAction(ActionEvent event) {
        if (!validateInput()) {
            event.consume(); // Prevent dialog from closing
        } else {
            saveEmployee(); // Perform the save operation
        }
    }

//    private void handleCancelAction(ActionEvent event) {
//        // Just let the dialog close (default behavior)
//    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || deptField.getValue() == null) {
            showAlert("Validation Error", "Name and Department are required fields.");
            return false;
        }

        try {
            double salary = Double.parseDouble(salaryField.getText());
            double rating = Double.parseDouble(ratingField.getText());
            int experience = Integer.parseInt(expField.getText());

            if (salary < 0) {
                showAlert("Validation Error", "Salary cannot be negative.");
                return false;
            }
            if (rating < 0 || rating > 5) {
                showAlert("Validation Error", "Performance rating must be between 0 and 5.");
                return false;
            }
            if (experience < 0) {
                showAlert("Validation Error", "Years of experience cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for salary, rating, and experience.");
            return false;
        }

        return true;
    }

    private void saveEmployee() {
        try {
            int employeeId = Integer.parseInt(idField.getText());

            Employee<Integer> employee = new Employee.Builder<>(employeeId)
                    .name(nameField.getText())
                    .department(deptField.getValue())
                    .salary(Double.parseDouble(salaryField.getText()))
                    .performanceRating(Double.parseDouble(ratingField.getText()))
                    .yearsOfExperience(Integer.parseInt(expField.getText()))
                    .isActive(activeField.isSelected())
                    .build();

            if (employeeToEdit != null) {
                employeeDatabase.removeEmployee(employeeToEdit.getEmployeeId());
            }
            employeeDatabase.addEmployee(employee);
            mainController.refreshEmployeeTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
