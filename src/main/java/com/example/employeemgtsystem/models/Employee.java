package com.example.employeemgtsystem.models;

import javafx.beans.property.*;
import java.util.Objects;

public class Employee<T> implements Comparable<Employee<T>> {
    // These are the instance variables for the Employee class
    private final SimpleObjectProperty<T> employeeId;
    private final StringProperty name;
    private final StringProperty department;
    private final DoubleProperty salary;
    private final DoubleProperty performanceRating;
    private final IntegerProperty yearsOfExperience;
    private final BooleanProperty isActive;       //Employment Status of the employee

    // Private constructor - only accessible through the Builder
    private Employee(Builder<T> builder) {
        this.employeeId = new SimpleObjectProperty<>(builder.employeeId);
        this.name = new SimpleStringProperty(builder.name);
        this.department = new SimpleStringProperty(builder.department);
        this.salary = new SimpleDoubleProperty(builder.salary);
        this.performanceRating = new SimpleDoubleProperty(builder.performanceRating);
        this.yearsOfExperience = new SimpleIntegerProperty(builder.yearsOfExperience);
        this.isActive = new SimpleBooleanProperty(builder.isActive);
    }

    // Builder class for Employee
    public static class Builder<T> {
        // Required parameters
        private final T employeeId;

        // Optional parameters - initialized with default values
        private String name = "";
        private String department = "";
        private double salary = 0.0;
        private double performanceRating = 0.0;
        private int yearsOfExperience = 0;
        private boolean isActive = true;

        public Builder(T employeeId) {
            this.employeeId = Objects.requireNonNull(employeeId, "Employee ID cannot be null");
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> department(String department) {
            this.department = department;
            return this;
        }

        public Builder<T> salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Builder<T> performanceRating(double performanceRating) {
            this.performanceRating = performanceRating;
            return this;
        }

        public Builder<T> yearsOfExperience(int yearsOfExperience) {
            this.yearsOfExperience = yearsOfExperience;
            return this;
        }

        public Builder<T> isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        // Validation method
        private void validate() {
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
            if (performanceRating < 0 || performanceRating > 5) {
                throw new IllegalArgumentException("Performance rating must be between 0 and 5");
            }
            if (yearsOfExperience < 0) {
                throw new IllegalArgumentException("Years of experience cannot be negative");
            }
        }

        // Build method - validates and creates a new Employee
        public Employee<T> build() {
            validate();
            return new Employee<>(this);
        }
    }

    // Getters and property methods
    public SimpleObjectProperty<T> employeeIdProperty() {
        return employeeId;
    }

    public T getEmployeeId() {
        return employeeId.get();
    }

    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public StringProperty departmentProperty() { return department; }
    public String getDepartment() { return department.get(); }
    public void setDepartment(String department) { this.department.set(department); }

    public DoubleProperty salaryProperty() { return salary; }
    public double getSalary() { return salary.get(); }
    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary.set(salary);
    }

    public DoubleProperty performanceRatingProperty() { return performanceRating; }
    public double getPerformanceRating() { return performanceRating.get(); }
    public void setPerformanceRating(double performanceRating) {
        if (performanceRating < 0 || performanceRating > 5) {
            throw new IllegalArgumentException("Performance rating must be between 0 and 5");
        }
        this.performanceRating.set(performanceRating);
    }

    public IntegerProperty yearsOfExperienceProperty() { return yearsOfExperience; }
    public int getYearsOfExperience() { return yearsOfExperience.get(); }
    public void setYearsOfExperience(int yearsOfExperience) {
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
        this.yearsOfExperience.set(yearsOfExperience);
    }

    public BooleanProperty isActiveProperty() { return isActive; }
    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean active) { isActive.set(active); }

    @Override
    public int compareTo(Employee<T> other) {
        // Descending order by years of experience
        return Integer.compare(other.getYearsOfExperience(), this.getYearsOfExperience());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee<?> employee = (Employee<?>) o;
        return employeeId.equals(employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return "Employee [employeeId=" + employeeId.get() +
                ", name=" + name.get() +
                ", department=" + department.get() +
                ", salary=" + salary.get() +
                ", performanceRating=" + performanceRating.get() +
                ", yearsOfExperience=" + yearsOfExperience.get() +
                ", isActive=" + isActive.get() +
                "]";
    }
}



