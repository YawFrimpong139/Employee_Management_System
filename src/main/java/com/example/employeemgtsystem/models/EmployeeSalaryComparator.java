package com.example.employeemgtsystem.models;


import java.util.Comparator;

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {

    @Override
    public int compare(Employee<T> o1, Employee<T> o2) {
        // Handle null employees
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return 1;  // Nulls last
        if (o2 == null) return -1; // Nulls first

        // Handle null salaries
        Double salary1 = o1.getSalary();
        Double salary2 = o2.getSalary();

        if (salary1 == null && salary2 == null) return 0;
        if (salary1 == null) return 1;
        if (salary2 == null) return -1;

        return Double.compare(salary2, salary1);

    }

}