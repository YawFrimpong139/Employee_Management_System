package com.example.employeemgtsystem.models;

import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.logging.Logger;



public class EmployeeDatabase<T> {



    private static final Logger logger = Logger.getLogger(EmployeeDatabase.class.getName());
    private final Map<T, Employee<T>> employees;
    private final String idNotNull = "Employee ID cannot be Null";


    private static final Set<String> VALID_DEPARTMENTS = Set.of(
            "HR", "Finance", "IT", "Marketing", "Sales", "Operations"
    );
    public EmployeeDatabase() {
        this.employees = new HashMap<>();
    }

    //============== METHODS FOR CRUD OPERATIONS ===================

    // This is the method to add Employee to our hash map
    public void addEmployee(Employee<T> employee) {
        try{
            Objects.requireNonNull(employee, "Employee cannot be null");   //"Employee cannot be null"

            if(employees.containsKey(employee.getEmployeeId())) {
                throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " already exist");
            }
            employees.put(employee.getEmployeeId(), employee);
        }catch(RuntimeException e){
            logger.severe("Failed to add employee: " + e.getMessage());
            throw e;

        }
    }

    private void validateEmployeeData(Employee<T> employee) throws InvalidNameException, InvalidSalaryException, InvalidDepartmentException {
        //Validate the name
        if(employee.getName() == null || employee.getName().trim().isEmpty()){
            throw new InvalidNameException("Employee name cannot be empty or null");
        }

        //Validate Employee Salary
        if(employee.getSalary() < 0){
            throw new InvalidSalaryException("Employee Salary cannot be negative" + employee.getSalary());
        }

        //Validate Departments
        if(!VALID_DEPARTMENTS.contains(employee.getDepartment())){
            throw new InvalidDepartmentException(
                    "Invalid department: " + employee.getDepartment() +
                            "Valid departments are " + VALID_DEPARTMENTS
            );
        }

        //Validate Performance Rating
        if (employee.getPerformanceRating() < 0 || employee.getPerformanceRating() > 10) {
            throw new IllegalArgumentException(
                    "Performance rating must be between 0 and 10: " + employee.getPerformanceRating()
            );
        }
    }

    //This is also the method to read our data from the hash map
    public Employee<T> getEmployee(T employeeId) throws EmployeeNotFoundException{

        Objects.requireNonNull(employeeId, "Employee ID cannot be null");

        Employee<T> employee = employees.get(employeeId);
        if (employee == null) {
            String errorMessage = "Employee with ID " + employeeId + " cannot be found";
            logger.warning(errorMessage);
            throw new EmployeeNotFoundException(errorMessage);
        }
        return employee;


    }

    //This creates a copy of all the Employee<T> objects in the Map, for easier manipulation like sorting,
    //filtering and other operations
    //This makes them modify(eg Sorting, filtering etc) without affecting the data in the map
    public List<Employee<T>> getAllEmployees(){
        return new ArrayList<>(employees.values());
    }


    public void UpdateEmployeeDetails(T employeeId, String field, Object newValue) throws InvalidNameException, InvalidSalaryException, InvalidDepartmentException, EmployeeNotFoundException{

        try {
            Objects.requireNonNull(employeeId, idNotNull);   //"Employee ID cannot be null"
            Objects.requireNonNull(field, "Field name cannot be null");
            Employee<T> employee = getEmployee(employeeId);


            switch (field.toLowerCase()) {
                case "name":
                    if (newValue == null || ((String) newValue).trim().isEmpty()) {
                        throw new InvalidNameException("Employee name cannot be empty or null");
                    }
                    employee.setName((String) newValue);
                    break;

                case "department":
                    String dept = (String) newValue;
                    if (!VALID_DEPARTMENTS.contains(dept)) {
                        throw new InvalidDepartmentException(
                                "Invalid department: " + dept +
                                        ". Valid departments are: " + VALID_DEPARTMENTS
                        );
                    }
                    employee.setDepartment(dept);
                    break;
                case "salary":
                    if (!(newValue instanceof Number)) {
                        throw new IllegalArgumentException("Salary must be a number");
                    }
                    double salary = ((Number) newValue).doubleValue();
                    if (salary < 0) {
                        throw new InvalidSalaryException("Salary cannot be negative: " + salary);
                    }
                    employee.setSalary(salary);
                    break;

                case "performancerating":
                    double rating = (double) newValue;
                    if (rating < 0 || rating > 10) {
                        throw new IllegalArgumentException("Performance rating must be between 0 and 10");
                    }
                    employee.setPerformanceRating(rating);
                    break;
                case "yearsofexperience":
                    int years = (int) newValue;
                    if (years < 0) {
                        throw new IllegalArgumentException("Years of experience cannot be negative");
                    }
                    employee.setYearsOfExperience(years);
                    break;
                case "isactive":
                    employee.setActive((boolean) newValue);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field name: " + field);

            }
        } catch (ClassCastException e) {
            String errorMsg = "Invalid type for field " + field + ": " + e.getMessage();
            logger.severe(errorMsg);
            throw new IllegalArgumentException(errorMsg, e);
        }catch(RuntimeException e){
            logger.severe("Error updating employee: " + e.getMessage());
            throw e;
        }
    }


    //This is the method to remove an employee
    public Employee<T> removeEmployee(T employeeId) throws EmployeeNotFoundException{

        Objects.requireNonNull(employeeId, "Employee ID cannot be null");

        try {
            // This will throw NoSuchElementException if employee not found
            Employee<T> employee = getEmployee(employeeId);
            employees.remove(employeeId);
            return employee;
        } catch (NoSuchElementException e) {
            String errorMessage = "Employee with ID " + employeeId + " not found";
            logger.warning(errorMessage);
            throw new EmployeeNotFoundException(errorMessage, e);
        }

    }


    //============ METHODS FOR FILTERING AND SORTING ==========================

    // Search by department

    public List<Employee<T>> searchEmployeesByDepartment(String department) throws InvalidDepartmentException{
        try {
            Objects.requireNonNull(department, "Department cannot be null");

            if (!VALID_DEPARTMENTS.contains(department)) {
                throw new InvalidDepartmentException(
                        "Invalid department: " + department +
                                ". Valid departments are: " + VALID_DEPARTMENTS
                );
            }

            return employees.values().stream()
                    .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            logger.warning("Error searching by department: " + e.getMessage());
            throw e;
        }
    }


    //Search by Name

    public List<Employee<T>> searchEmployeesByName(String namePart) throws InvalidNameException{
//        return employees.values().stream()
//                .filter(e -> e.getName().toLowerCase().contains(namePart.toLowerCase()))
//                .collect(Collectors.toList());
        try{
            Objects.requireNonNull(namePart, "Name cannot be null");

            if(namePart.trim().isEmpty()){
                throw new InvalidNameException("Name search term cannot be empty or whitespace");
            }

            return employees.values().stream()
                    .filter(employee -> {
                        String employeeName = Objects.requireNonNullElse(employee.getName(), "");
                        return employeeName.toLowerCase()
                                .contains(namePart.toLowerCase());
                    })
                    .collect(Collectors.toList());
        }catch(RuntimeException e){
            logger.severe("Name search failed for term '" + namePart + "': " + e.getMessage());
            throw e;
        }
    }

    //Search by Performance Rating

    public List<Employee<T>> getHighPerformers(double minRate){

        return employees.values().stream()
                .filter(e -> e.getPerformanceRating() >= minRate)
                .collect(Collectors.toList());
    }

    // Search Employees by Salary range
    public List<Employee<T>> getEmployeesBySalaryRange(double minSalary, double maxSalary){
        try {
            if (minSalary < 0 || maxSalary < 0) {
                throw new IllegalArgumentException("Salary values cannot be negative");
            }
            if (minSalary > maxSalary) {
                throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
            }

            if (employees.isEmpty()) {
                logger.info("Attempted to search salary range in empty employee database");
                return new ArrayList<>();
            }

            return employees.values().stream()
                    .filter(Objects::nonNull) // Filter out null employees
                    .filter(e -> {
                        Double salary = e.getSalary();
                        return salary != null && salary >= minSalary && salary <= maxSalary;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error searching by salary range: " + e.getMessage());
            throw new RuntimeException("Salary range search failed", e);
        }
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
        try {
            if (employees.isEmpty()) {
                logger.info("Attempted to sort empty employee list");
                return new ArrayList<>();
            }

            return employees.values().stream()
                    .filter(Objects::nonNull) // Remove null employees
                    .sorted(Comparator.nullsLast(Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error sorting by years of experience: " + e.getMessage());
            throw new RuntimeException("Sorting failed", e);
        }
//        return employees.values().stream()
//                .sorted()
//                .collect(Collectors.toList());
    }


    // Sorting by Salary (Highest first)

    public List<Employee<T>> sortBySalary(){
        try {
            if (employees.isEmpty()) {
                logger.info("Attempted to sort empty employee list by salary");
                return new ArrayList<>();
            }

            return employees.values().stream()
                    .filter(Objects::nonNull) // Remove null employees
                    .sorted(new EmployeeSalaryComparator<>())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error sorting by salary: " + e.getMessage());
            throw new RuntimeException("Salary sorting failed", e);
        }
    }

    // Sorting by Performance Rating

    public List<Employee<T>> sortByPerformance(){
        try {
            if (employees.isEmpty()) {
                logger.info("Attempted to sort empty employee list by performance");
                return new ArrayList<>();
            }

            return employees.values().stream()
                    .filter(Objects::nonNull) // Remove null employees
                    .sorted(new EmployeePerformanceComparator<>())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error sorting by performance: " + e.getMessage());
            throw new RuntimeException("Performance sorting failed", e);
        }
    }


    // Custom Sorting with Comparator

    public List<Employee<T>> sortEmployees(Comparator<Employee<T>> comparator){
        Objects.requireNonNull(comparator, "Comparator cannot be null");

        try {
            if (employees.isEmpty()) {
                logger.info("Attempted to sort empty employee list with custom comparator");
                return new ArrayList<>();
            }

            return employees.values().stream()
                    .filter(Objects::nonNull) // Remove null employees
                    .sorted(Comparator.nullsLast(comparator))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error in custom sorting: " + e.getMessage());
            throw new RuntimeException("Custom sorting failed", e);
        }
    }



    //======================= SALARY MANAGEMENT================================

    // Method to give a percentage raise when an employee meets the minimum performance rating
    public List<Employee<T>> salaryRaiseToHighPerformers(double minRating, double raisePercentage){

        try {
            if (minRating < 0 || minRating > 10) {
                throw new IllegalArgumentException("Minimum rating must be between 0 and 10");
            }
            if (raisePercentage < 0) {
                throw new IllegalArgumentException("Raise percentage cannot be negative");
            }

            return employees.values().stream()
                    .filter(e -> e.getPerformanceRating() >= minRating)
                    .peek(e -> {
                        try {
                            double newSalary = e.getSalary() * (1 + raisePercentage / 100);
                            if (newSalary < 0) {
                                throw new InvalidSalaryException("Calculated salary is negative");
                            }
                            e.setSalary(newSalary);
                        } catch (RuntimeException ex) {
                            logger.severe("Error applying salary raise: " + ex.getMessage());
                            throw ex;
                        } catch (InvalidSalaryException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            logger.severe("Error in salary raise process: " + e.getMessage());
            throw e;
        }

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


