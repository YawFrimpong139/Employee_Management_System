package com.example.employeemgtsystem.models;


import java.util.Comparator;

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {

    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        // Handle null employees
        if (emp1 == null && emp2 == null) return 0;
        if (emp1 == null) return 1;  // Nulls last
        if (emp2 == null) return -1; // Nulls first

        // Handle null salaries
        Double salary1 = emp1.getSalary();
        Double salary2 = emp2.getSalary();

        if (salary1 == null && salary2 == null) return 0;
        if (salary1 == null) return 1;
        if (salary2 == null) return -1;

        return Double.compare(salary2, salary1);

    }

}