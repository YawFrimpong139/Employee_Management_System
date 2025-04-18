package com.example.employeemgtsystem.models;


import java.util.Comparator;

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {

    @Override
    public int compare(Employee<T> o1, Employee<T> o2) {
        // TODO Auto-generated method stub
        return Double.compare(o2.getSalary(), o1.getSalary());
    }


}

