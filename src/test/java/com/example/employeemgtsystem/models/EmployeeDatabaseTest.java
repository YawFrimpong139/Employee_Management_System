package com.example.employeemgtsystem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDatabaseTest {

    private EmployeeDatabase<String> database;
    private Employee<String> employee1;
    private Employee<String> employee2;

    @BeforeEach
    void setUp(){
        database = new EmployeeDatabase<>();

        employee1 = new Employee.Builder<>("1")
                .name("Ken")
                .department("IT")
                .salary(45000)
                .yearsOfExperience(12)
                .performanceRating(4.5)
                .build();

        employee2 = new Employee.Builder<>("2")
                .name("Adwoa")
                .department("HR")
                .salary(33000)
                .yearsOfExperience(7)
                .performanceRating(4)
                .build();
    }
    //Add employee test,

    @Test
    void addEmployee_shouldBeAddedSuccessfully() throws EmployeeNotFoundException {
        database.addEmployee(employee1);
        assertEquals(1, database.getAllEmployees().size());
        assertNotNull(database.getEmployee("1"));

    }

    @Test
    void addEmployee_shouldThrowErrorWhenAddingDuplicate(){
        database.addEmployee(employee1);
        assertThrows(IllegalArgumentException.class, () -> database.addEmployee(employee1));
    }

    @Test
    void addEmployee_shouldThrowWhenEmployeeIsNull() {
        assertThrows(NullPointerException.class, () -> database.addEmployee(null));
    }



    //==================SEARCH BY DEPARTMENT=====================
    @Test
    void searchEmployeesByDepartment_shouldReturnCorrectEmployees() throws InvalidDepartmentException {
        database.addEmployee(employee1); // Engineering
        database.addEmployee(employee2); // HR

        assertEquals(1, database.searchEmployeesByDepartment("IT").size());
        assertEquals(1, database.searchEmployeesByDepartment("HR").size());
        assertEquals(0, database.searchEmployeesByDepartment("Finance").size());
    }


    @Test
    void searchEmployeesByDepartment_shouldBeCaseInsensitive() throws InvalidDepartmentException {
        database.addEmployee(employee1);
        assertEquals(1, database.searchEmployeesByDepartment("IT").size());
    }

    @Test
    void searchEmployeesByDepartment_shouldThrowWhenDepartmentIsNull() {
        assertThrows(NullPointerException.class, () -> database.searchEmployeesByDepartment(null));
    }

    @Test
    void searchEmployeesByDepartment_shouldThrowWhenDepartmentIsEmpty() throws InvalidDepartmentException {
        database.searchEmployeesByDepartment("IT");
        assertThrows(InvalidDepartmentException.class,
                () -> database.searchEmployeesByDepartment(""));
    }


    // ============ DELETE EMPLOYEE TESTS ============
    @Test
    void removeEmployee_shouldRemoveEmployeeSuccessfully() throws EmployeeNotFoundException {

        //setup
        database.addEmployee(employee1);
        assertEquals(1, database.getAllEmployees().size());
        assertNotNull(database.getEmployee("1"));

        // Action
        Employee<String> removedEmployee = database.removeEmployee("1");

        // Verification
        assertEquals(0, database.getAllEmployees().size());
        assertEquals(employee1, removedEmployee); // Verify correct employee was returned

        // Verify employee is truly gone
        assertThrows(EmployeeNotFoundException.class, () -> database.getEmployee("1"));

        // Verify duplicate removal throws
        assertThrows(EmployeeNotFoundException.class, () -> database.removeEmployee("1"));
    }

    @Test
    void removeEmployee_shouldThrowWhenEmployeeNotFound() {
        assertThrows(EmployeeNotFoundException.class, () -> database.removeEmployee("7"));
    }

    @Test
    void removeEmployee_shouldThrowWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> database.removeEmployee(null));
    }
}