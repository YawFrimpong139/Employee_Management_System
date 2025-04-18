package com.example.employeemgtsystem.controllers;

import com.example.employeemgtsystem.models.*;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML private TableView<Employee<Integer>> employeeTable;
    @FXML private TableColumn<Employee<Integer>, Integer> idCol;
    @FXML private TableColumn<Employee<Integer>, String> nameCol;
    @FXML private TableColumn<Employee<Integer>, String> deptCol;
    @FXML private TableColumn<Employee<Integer>, Number> salaryCol;
    @FXML private TableColumn<Employee<Integer>, Number> ratingCol;
    @FXML private TableColumn<Employee<Integer>, Integer> expCol;
    @FXML private TableColumn<Employee<Integer>, String> statusCol;

    @FXML private ComboBox<String> departmentFilter;
    @FXML private TextField searchField;
    @FXML private ToggleGroup sortGroup;
    @FXML private Label statusLabel;

    private final EmployeeDatabase<Integer> employeeDatabase = new EmployeeDatabase<>();

    @FXML
    public void initialize() {
        // Initialize table columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        expCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        statusCol.setCellValueFactory(cellData -> {
            BooleanProperty activeProp = cellData.getValue().isActiveProperty();
            return activeProp.asString().map(active -> active.equals("true") ? "Active" : "Inactive");
        });

        // Custom cell factory for salary column
        salaryCol.setCellFactory(col -> new TableCell<Employee<Integer>, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%,.2f", value.doubleValue()));
                }
            }
        });

        // Initialize department filter
        departmentFilter.setItems(FXCollections.observableArrayList(
                "All Departments", "IT", "HR", "Finance", "Marketing", "Operations"));
        departmentFilter.setValue("All Departments");

        // Initialize sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Using the Builder pattern to create employee objects
        employeeDatabase.addEmployee(new Employee.Builder<>(1)
                .name("John Doe")
                .department("IT")
                .salary(75000)
                .performanceRating(4.2)
                .yearsOfExperience(5)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(2)
                .name("Jane Smith")
                .department("HR")
                .salary(65000)
                .performanceRating(4.8)
                .yearsOfExperience(8)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(3)
                .name("Bob Johnson")
                .department("Finance")
                .salary(85000)
                .performanceRating(3.9)
                .yearsOfExperience(3)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(4)
                .name("Alice Williams")
                .department("IT")
                .salary(90000)
                .performanceRating(4.5)
                .yearsOfExperience(10)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(5)
                .name("Michael Brown")
                .department("IT")
                .salary(80000)
                .performanceRating(4.1)
                .yearsOfExperience(6)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(6)
                .name("Sarah Davis")
                .department("HR")
                .salary(70000)
                .performanceRating(4.9)
                .yearsOfExperience(4)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(7)
                .name("David Wilson")
                .department("Finance")
                .salary(95000)
                .performanceRating(4.7)
                .yearsOfExperience(7)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(8)
                .name("Emily Taylor")
                .department("Marketing")
                .salary(72000)
                .performanceRating(4.3)
                .yearsOfExperience(5)
                .build());

        employeeDatabase.addEmployee(new Employee.Builder<>(9)
                .name("James Anderson")
                .department("Operations")
                .salary(68000)
                .performanceRating(4.0)
                .yearsOfExperience(7)
                .build());

        refreshEmployeeTable();
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        showEmployeeDialog(null);
    }

    @FXML
    private void handleEditEmployee(ActionEvent event) {
        Employee<Integer> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an employee to edit.");
            return;
        }
        showEmployeeDialog(selected);
    }

    @FXML
    private void handleDeleteEmployee(ActionEvent event) {
        Employee<Integer> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an employee to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Employee");
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                employeeDatabase.removeEmployee(selected.getEmployeeId());
                refreshEmployeeTable();
            }
        });
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        refreshEmployeeTable();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleDepartmentFilter(ActionEvent event) {
        String department = departmentFilter.getValue();
        if ("All Departments".equals(department)) {
            refreshEmployeeTable();
        } else {
            employeeTable.setItems(FXCollections.observableArrayList(
                    employeeDatabase.searchEmployeesByDepartment(department)));
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String namePart = searchField.getText();
        if (namePart == null || namePart.trim().isEmpty()) {
            refreshEmployeeTable();
        } else {
            employeeTable.setItems(FXCollections.observableArrayList(
                    employeeDatabase.searchEmployeesByName(namePart)));
        }
    }

    @FXML
    private void handleSortDefault(ActionEvent event) {
        refreshEmployeeTable();
    }

    @FXML
    private void handleSortSalary(ActionEvent event) {
        employeeTable.setItems(FXCollections.observableArrayList(
                employeeDatabase.sortBySalary()));
    }

    @FXML
    private void handleSortPerformance(ActionEvent event) {
        employeeTable.setItems(FXCollections.observableArrayList(
                employeeDatabase.sortByPerformance()));
    }

    @FXML
    private void handleDepartmentSummary(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/employeemgtsystem/views/department_summary.fxml"));
            Parent root = loader.load();

            DepartmentSummaryController controller = loader.getController();
            controller.setEmployeeDatabase(employeeDatabase);

            Stage stage = new Stage();
            stage.setTitle("Department Summary Report");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load department summary view.");
        }
    }

    public void refreshEmployeeTable() {
        employeeTable.setItems(FXCollections.observableArrayList(
                employeeDatabase.sortByYearsOfExperience()));
        statusLabel.setText("Total employees: " + employeeDatabase.getAllEmployees().size());
    }

    private void showEmployeeDialog(Employee<Integer> employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/employeemgtsystem/views/employee_dialog.fxml"));

            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.setDialogPane(dialogPane);
            dialog.setTitle(employee == null ? "Add New Employee" : "Edit Employee");

            EmployeeDialogController controller = loader.getController();
            controller.setEmployeeDatabase(employeeDatabase);
            controller.setMainController(this);
            controller.setupDialogPane(dialogPane);

            if (employee != null) {
                controller.setEmployeeForEdit(employee);
            }

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                // Save is handled in the dialog controller
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load employee dialog.");
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


//package com.example.employeemgtsystem.controllers;
//
//
//
//
//import com.example.employeemgtsystem.models.*;
//import javafx.beans.property.BooleanProperty;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.collections.FXCollections;
//import javafx.event.ActionEvent;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.Optional;
//
//public class MainController {
//    @FXML private TableView<Employee<Integer>> employeeTable;
//    @FXML private TableColumn<Employee<Integer>, Integer> idCol;
//    @FXML private TableColumn<Employee<Integer>, String> nameCol;
//    @FXML private TableColumn<Employee<Integer>, String> deptCol;
//    @FXML private TableColumn<Employee<Integer>, Number> salaryCol;
//    @FXML private TableColumn<Employee<Integer>, Number> ratingCol;
//    @FXML private TableColumn<Employee<Integer>, Integer> expCol;
//    @FXML private TableColumn<Employee<Integer>, String> statusCol;
//
//    @FXML private ComboBox<String> departmentFilter;
//    @FXML private TextField searchField;
//    @FXML private ToggleGroup sortGroup;
//    @FXML private Label statusLabel;
//
//    private final EmployeeDatabase<Integer> employeeDatabase = new EmployeeDatabase<>();
//
//    @FXML
//    public void initialize() {
//        // Initialize table columns
//        idCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
//        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
//        ratingCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
//        expCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
//        statusCol.setCellValueFactory(cellData -> {
//            BooleanProperty activeProp = cellData.getValue().isActiveProperty();
//            return activeProp.asString().map(active -> active.equals("true") ? "Active" : "Inactive");
//        });
//
//        // Custom cell factory for salary column
//        salaryCol.setCellFactory(col -> new TableCell<Employee<Integer>, Number>() {
//            @Override
//            protected void updateItem(Number value, boolean empty) {
//                super.updateItem(value, empty);
//                if (empty || value == null) {
//                    setText(null);
//                } else {
//                    setText(String.format("$%,.2f", value.doubleValue()));
//                }
//            }
//        });
//
//        // Initialize department filter
//        departmentFilter.setItems(FXCollections.observableArrayList(
//                "All Departments", "IT", "HR", "Finance", "Marketing", "Operations"));
//        departmentFilter.setValue("All Departments");
//
//        // Initialize sample data
//        initializeSampleData();
//    }
//
//    private void initializeSampleData() {
//        employeeDatabase.addEmployee(new Employee<>(1, "John Doe", "IT", 75000, 4.2, 5));
//        employeeDatabase.addEmployee(new Employee<>(2, "Jane Smith", "HR", 65000, 4.8, 8));
//        employeeDatabase.addEmployee(new Employee<>(3, "Bob Johnson", "Finance", 85000, 3.9, 3));
//        employeeDatabase.addEmployee(new Employee<>(4, "Alice Williams", "IT", 90000, 4.5, 10));
//        employeeDatabase.addEmployee(new Employee<>(5, "Michael Brown", "IT", 80000, 4.1, 6));
//        employeeDatabase.addEmployee(new Employee<>(6, "Sarah Davis", "HR", 70000, 4.9, 4));
//        employeeDatabase.addEmployee(new Employee<>(7, "David Wilson", "Finance", 95000, 4.7, 7));
//        employeeDatabase.addEmployee(new Employee<>(8, "Emily Taylor", "Marketing", 72000, 4.3, 5));
//        employeeDatabase.addEmployee(new Employee<>(9, "James Anderson", "Operations", 68000, 4.0, 7));
//
//        refreshEmployeeTable();
//    }
//
//    @FXML
//    private void handleAddEmployee(ActionEvent event) {
//        showEmployeeDialog(null);
//    }
//
//    @FXML
//    private void handleEditEmployee(ActionEvent event) {
//        Employee<Integer> selected = employeeTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            showAlert("No Selection", "Please select an employee to edit.");
//            return;
//        }
//        showEmployeeDialog(selected);
//    }
//
//    @FXML
//    private void handleDeleteEmployee(ActionEvent event) {
//        Employee<Integer> selected = employeeTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            showAlert("No Selection", "Please select an employee to delete.");
//            return;
//        }
//
//        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
//        confirm.setTitle("Confirm Deletion");
//        confirm.setHeaderText("Delete Employee");
//        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");
//
//        confirm.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.OK) {
//                employeeDatabase.removeEmployee(selected.getEmployeeId());
//                refreshEmployeeTable();
//            }
//        });
//    }
//
//    @FXML
//    private void handleRefresh(ActionEvent event) {
//        refreshEmployeeTable();
//    }
//
//    @FXML
//    private void handleExit(ActionEvent event) {
//        System.exit(0);
//    }
//
//    @FXML
//    private void handleDepartmentFilter(ActionEvent event) {
//        String department = departmentFilter.getValue();
//        if ("All Departments".equals(department)) {
//            refreshEmployeeTable();
//        } else {
//            employeeTable.setItems(FXCollections.observableArrayList(
//                    employeeDatabase.searchEmployeesByDepartment(department)));
//        }
//    }
//
//    @FXML
//    private void handleSearch(ActionEvent event) {
//        String namePart = searchField.getText();
//        if (namePart == null || namePart.trim().isEmpty()) {
//            refreshEmployeeTable();
//        } else {
//            employeeTable.setItems(FXCollections.observableArrayList(
//                    employeeDatabase.searchEmployeesByName(namePart)));
//        }
//    }
//
//    @FXML
//    private void handleSortDefault(ActionEvent event) {
//        refreshEmployeeTable();
//    }
//
//    @FXML
//    private void handleSortSalary(ActionEvent event) {
//        employeeTable.setItems(FXCollections.observableArrayList(
//                employeeDatabase.sortBySalary()));
//    }
//
//    @FXML
//    private void handleSortPerformance(ActionEvent event) {
//        employeeTable.setItems(FXCollections.observableArrayList(
//                employeeDatabase.sortByPerformance()));
//    }
//
//    @FXML
//    private void handleDepartmentSummary(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/employeemgtsystem/views/department_summary.fxml"));
//            Parent root = loader.load();
//
//            DepartmentSummaryController controller = loader.getController();
//            controller.setEmployeeDatabase(employeeDatabase);
//
//            Stage stage = new Stage();
//            stage.setTitle("Department Summary Report");
//            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Could not load department summary view.");
//        }
//    }
//
//    public void refreshEmployeeTable() {
//        employeeTable.setItems(FXCollections.observableArrayList(
//                employeeDatabase.sortByYearsOfExperience()));
//        statusLabel.setText("Total employees: " + employeeDatabase.getAllEmployees().size());
//    }
//
//    private void showEmployeeDialog(Employee<Integer> employee) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(
//                    "/com/example/employeemgtsystem/views/employee_dialog.fxml"));
//
//            DialogPane dialogPane = loader.load();
//
//            Dialog<ButtonType> dialog = new Dialog<>();
//
//            dialog.setDialogPane(dialogPane);
//            dialog.setTitle(employee == null ? "Add New Employee" : "Edit Employee");
//
//            EmployeeDialogController controller = loader.getController();
//            controller.setEmployeeDatabase(employeeDatabase);
//            controller.setMainController(this);
//            controller.setupDialogPane(dialogPane);
//
//            if (employee != null) {
//                controller.setEmployeeForEdit(employee);
//            }
//
//            Optional<ButtonType> result = dialog.showAndWait();
//            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
//                // Save is handled in the dialog controller
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Could not load employee dialog.");
//        }
//        }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
