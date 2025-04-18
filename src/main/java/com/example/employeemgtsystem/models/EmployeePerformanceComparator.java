package com.example.employeemgtsystem.models;


import java.util.Comparator;

public class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {

    @Override
    public int compare(Employee<T> o1, Employee<T> o2) {
        // Sort by highest performance first
        int ratingCompare = Double.compare(o2.getPerformanceRating(), o1.getPerformanceRating());

        //If ratings are equal, sort by experience
        return ratingCompare != 0 ? ratingCompare :
                Integer.compare(o2.getYearsOfExperience(), o1.getYearsOfExperience());

    }

}

