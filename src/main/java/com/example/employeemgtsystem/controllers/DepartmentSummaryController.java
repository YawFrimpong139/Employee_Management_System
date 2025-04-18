package com.example.employeemgtsystem.controllers;



import com.example.employeemgtsystem.models.Employee;
import com.example.employeemgtsystem.models.EmployeeDatabase;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentSummaryController {
    @FXML private TableView<Map.Entry<String, DepartmentStats>> summaryTable;
    @FXML private TableColumn<Map.Entry<String, DepartmentStats>, String> deptCol;
    @FXML private TableColumn<Map.Entry<String, DepartmentStats>, Integer> countCol;
    @FXML private TableColumn<Map.Entry<String, DepartmentStats>, Double> avgSalaryCol;
    @FXML private TableColumn<Map.Entry<String, DepartmentStats>, Double> avgRatingCol;

    private EmployeeDatabase<Integer> employeeDatabase;

    public void setEmployeeDatabase(EmployeeDatabase<Integer> employeeDatabase) {
        this.employeeDatabase = employeeDatabase;
        initializeTable();
        loadData();
    }

    private void initializeTable(){

            deptCol.setCellValueFactory(cellData -> {
                if (cellData != null && cellData.getValue() != null) {
                    return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKey());
                } else {
                    return new javafx.beans.property.SimpleStringProperty(""); // Or null
                }
            });
            countCol.setCellValueFactory(cellData -> cellData.getValue().getValue().countProperty().asObject());
        avgSalaryCol.setCellValueFactory(cellData -> {
            DepartmentStats stats = cellData.getValue().getValue();
            return Bindings.createObjectBinding(() -> stats.getAvgSalary());
        });

        avgRatingCol.setCellValueFactory(cellData -> {
            DepartmentStats stats = cellData.getValue().getValue();
            return Bindings.createObjectBinding(() -> stats.getAvgRating());
        });
        avgSalaryCol.setCellFactory(col -> new TableCell<Map.Entry<String, DepartmentStats>, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", item));
                }
            }
        });

        avgRatingCol.setCellFactory(col -> new TableCell<Map.Entry<String, DepartmentStats>, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item)); // Format to 2 decimal places
                }
            }
        });
    }

    private void loadData() {
        Map<String, DepartmentStats> stats = employeeDatabase.getAllEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                DepartmentStats::new)));

        //System.out.println("Department Keys in Stats Map: " + stats.keySet());

        summaryTable.setItems(FXCollections.observableArrayList(stats.entrySet()));
    }

    @FXML
    private void handleClose() {
        ((Stage) summaryTable.getScene().getWindow()).close();
    }

    public static class DepartmentStats {
        private final int count;
        private final double avgSalary;
        private final double avgRating;

        public DepartmentStats(java.util.List<Employee<?>> employees) {
            this.count = employees.size();
            this.avgSalary = employees.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0);
            this.avgRating = employees.stream()
                    .mapToDouble(Employee::getPerformanceRating)
                    .average()
                    .orElse(0);
        }

        public int getCount() { return count; }
        public double getAvgSalary() { return avgSalary; }
        public double getAvgRating() { return avgRating; }

        // Property methods for table binding
        public IntegerProperty countProperty() { return new SimpleIntegerProperty(count); }
        public DoubleProperty avgSalaryProperty() { return new SimpleDoubleProperty(avgSalary); }
        public DoubleProperty avgRatingProperty() { return new SimpleDoubleProperty(avgRating); }
    }
}
