package com.example.employeemgtsystem.models;

import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class EmployeeDatabase<T> {

    private final Map<T, Employee<T>> employees;
    private final String idNotNull = "Employee ID cannot be Null";

    public EmployeeDatabase() {
        this.employees = new HashMap<>();
    }

    //============== METHODS FOR CRUD OPERATIONS ===================

    // This is the method to add Employee to our hash map
    public void addEmployee(Employee<T> employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");   //"Employee cannot be null"

        if(employees.containsKey(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " already exist");
        }
        employees.put(employee.getEmployeeId(), employee);
    }

    //This is also the method to read our data from the hash map
    public Employee<T> getEmployee(T employeeId){
        Objects.requireNonNull(employeeId, idNotNull);

        Employee<T> employee = employees.get(employeeId);

        if(employee == null) {
            throw new NoSuchElementException("Employee with ID " + employeeId + " cannot be found");
        }
        return employee;

    }

    //This creates a copy of all the Employee<T> objects in the Map, for easier manipulation like sorting,
    //filtering and other operations
    //This makes them modify(eg Sorting, filtering etc) without affecting the data in the map
    public List<Employee<T>> getAllEmployees(){
        return new ArrayList<>(employees.values());
    }


    public void UpdateEmployeeDetails(T employeeId, String field, Object newValue) {
        Objects.requireNonNull(employeeId, idNotNull);   //"Employee ID cannot be null"
        Objects.requireNonNull(field, "Field name cannot be null");

        Employee<T> employee = getEmployee(employeeId);

        try {

            switch (field.toLowerCase()) {
                case "name":
                    employee.setName((String) newValue);
                    break;
                case "department":
                    employee.setDepartment((String) newValue);
                    break;
                case "salary":
                    employee.setSalary((double) newValue);
                    break;
                case "performancerating":
                    employee.setPerformanceRating((double) newValue);
                    break;
                case "yearsofexperience":
                    employee.setYearsOfExperience((int) newValue);
                    break;
                case "isactive":
                    employee.setActive((boolean) newValue);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field name: " + field);

            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Invalid type for field " + field, e);
        }
    }


    //This is the method to remove an employee
    public void removeEmployee(T employeeId) {
        Objects.requireNonNull(employeeId, idNotNull);

        if(!employees.containsKey(employeeId)) {
            throw new NoSuchElementException("Employee with ID " + employeeId);
        }
        employees.remove(employeeId);

    }


    //============ METHODS FOR FILTERING AND SORTING ==========================

    // Search by department

    public List<Employee<T>> searchEmployeesByDepartment(String department){
        return employees.values().stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }


    //Search by Name

    public List<Employee<T>> searchEmployeesByName(String namePart){
        return employees.values().stream()
                .filter(e -> e.getName().toLowerCase().contains(namePart.toLowerCase()))
                .collect(Collectors.toList());
    }

    //Search by Performance Rating

    public List<Employee<T>> getHighPerformers(double minRate){
        return employees.values().stream()
                .filter(e -> e.getPerformanceRating() >= minRate)
                .collect(Collectors.toList());
    }

    // Search Employees by Salary range
    public List<Employee<T>> getEmployeesBySalaryRange(double minSalary, double maxSalary){
        return employees.values().stream()
                .filter(e -> e.getSalary() >= minSalary && e.getSalary() <= maxSalary)
                .collect(Collectors.toList());
    }

    //Custom filtering using Predicate

    public List<Employee<T>> filterEmployees(Predicate<Employee<T>> condition){
        return employees.values().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }


    //Iterator Implementation

    public Iterator<Employee<T>> iterator(){
        return employees.values().iterator();
    }


    //============== METHODS FOR SORTING ====================

    // Sorting using natural ordering(Years of Experience)

    public List<Employee<T>> sortByYearsOfExperience(){
        return employees.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }


    // Sorting by Salary (Highest first)

    public List<Employee<T>> sortBySalary(){
        return employees.values().stream()
                .sorted(new EmployeeSalaryComparator<T>())
                .collect(Collectors.toList());
    }

    // Sorting by Performance Rating

    public List<Employee<T>> sortByPerformance(){
        return employees.values().stream()
                .sorted(new EmployeePerformanceComparator<T>())
                .collect(Collectors.toList());
    }


    // Custom Sorting with Comparator

    public List<Employee<T>> sortEmployees(Comparator<Employee<T>> comparator){
        return employees.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }



    // ========== DISPLAY METHODS ==========

//    public void displayEmployees(List<Employee<T>> employeeList) {
//        if (employeeList.isEmpty()) {
//            System.out.println("No employees found.");
//            return;
//        }
//
//        System.out.println("\nEMPLOYEE LIST:");
//        System.out.println("-".repeat(120));
//        employeeList.forEach(System.out::println);
//        System.out.println("-".repeat(120));
//        System.out.println("Total employees: " + employeeList.size());
//    }


    //======================= SALARY MANAGEMENT================================

    // Method to give a percentage raise when an employee meets the minimum performance rating
    public List<Employee<T>> salaryRaiseToHighPerformers(double minRating, double raisePercentage){

        List<Employee<T>> employeesWhoGotRaise = employees.values().stream()
                .filter(e -> e.getPerformanceRating() >= minRating)
                .peek(e -> e.setSalary(e.getSalary() * (1 + raisePercentage / 100)))
                .collect(Collectors.toList());

        return employeesWhoGotRaise;
    }


    //This method gets the top N highest paid employees

    public List<Employee<T>> getTopPaidEmployees(int count){
        return employees.values().stream()
                .sorted(new EmployeeSalaryComparator<>())
                .limit(count)
                .collect(Collectors.toList());
    }


    // This method calculates the average salary for a department
    public double getAverageSalaryForDepartment(String department) {
        return employees.values().stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);
    }


    //This method calculates the overall salary statistics
    //Which returns a map containing min, max and avg salary

    public Map<String, Double> getSalaryStatistics(){
        DoubleSummaryStatistics stats = employees.values().stream()
                .mapToDouble(Employee::getSalary)
                .summaryStatistics();

        Map<String, Double> result = new HashMap<>();
        result.put("min", stats.getMin());
        result.put("max", stats.getMax());
        result.put("average", stats.getAverage());

        return result;
    }


    //======== METHODS FOR DISPLAYING DATA =================

    // Displaying all employees in a formatted table
    public void displayAllEmployees() {
        displayFormattedReport(getAllEmployees(), "ALL EMPLOYEES");
    }

    //Generates a detailed formatted report using Stream API

    public void displayFormattedReport(List<Employee<T>> employeeList, String title) {
        if(employeeList.isEmpty()) {
            System.out.println("No employees to display");
            return;
        }

        //Calculate column widths
        int idWidth = employeeList.stream()
                .mapToInt(e -> e.getEmployeeId().toString().length())
                .max().orElse(10) + 2;

        int nameWidth = employeeList.stream()
                .mapToInt(e -> e.getName().length())
                .max().orElse(20) + 2;

        int deptWidth = employeeList.stream()
                .mapToInt(e -> e.getDepartment().length())
                .max().orElse(10) + 2;

        // Format strings
        String headerFormat = "%-" + idWidth + "s %-" + nameWidth + "s %-" + deptWidth + "s %12s %10s %8s %8s%n";
        String rowFormat = "%-" + idWidth + "s %-" + nameWidth + "s %-" + deptWidth + "s %,12.2f %10.1f %8d %8s%n";


        // Print report
        System.out.println("\n" + title);
        System.out.println("=".repeat(80));
        System.out.printf(headerFormat, "ID", "Name", "Department", "Salary", "Rating", "Exp", "Status");
        System.out.println("-".repeat(80));


        employeeList.stream()
                .sorted(Comparator.comparing((Employee e) -> e.getDepartment())
                        .thenComparing(e -> e.getName()))
                .forEach(e -> System.out.printf(rowFormat,
                        e.getEmployeeId(),
                        e.getName(),
                        e.getDepartment(),
                        e.getSalary(),
                        e.getPerformanceRating(),
                        e.getYearsOfExperience(),
                        e.isActive() ? "Active" : "Inactive"));

        System.out.println("=".repeat(80));
        System.out.println("Total employees: " + employeeList.size());

        // Add summary statistics of showing all employees
        if (employeeList.size() == employees.size()) {
            Map<String, Double> stats = getSalaryStatistics();
            System.out.printf("Salary Summary: Min = $%,.2f | Max = $%,.2f | Avg = $%,.2f%n",
                    stats.get("min"), stats.get("max"), stats.get("average"));
        }

    }

    public void displayDepartmentSummary() {
        System.out.println("\nDEPARTMENT SUMMARY");
        System.out.println("=".repeat(60));
        System.out.printf("%-15s %10s %15s %15s%n", "Department", "Employees", "Avg Salary", "Avg Rating");
        System.out.println("-".repeat(60));

        employees.values().stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summarizingDouble(Employee::getSalary)))
                .forEach((dept, stats) -> {
                    double avgRating = employees.values().stream()
                            .filter(e -> e.getDepartment().equals(dept))
                            .mapToDouble(Employee::getPerformanceRating)
                            .average()
                            .orElse(0);

                    System.out.printf("%-15s %10d %,15.2f %15.1f%n",
                            dept,
                            stats.getCount(),
                            stats.getAverage(),
                            avgRating);
                });

        System.out.println("=".repeat(60));
    }
}


