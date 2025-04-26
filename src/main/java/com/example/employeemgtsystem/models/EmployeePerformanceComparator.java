package com.example.employeemgtsystem.models;


import java.util.Comparator;

public class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {

    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        // Handle null employees
        if (emp1 == null && emp2 == null) return 0;
        if (emp1 == null) return 1;
        if (emp2 == null) return -1;

        // Handle null ratings/experience
        Double rating1 = emp1.getPerformanceRating();
        Double rating2 = emp2.getPerformanceRating();
        Integer exp1 = emp1.getYearsOfExperience();
        Integer exp2 = emp2.getYearsOfExperience();

        // Compare ratings with null checks
        int ratingCompare = 0;
        if (rating1 != null && rating2 != null) {
            ratingCompare = Double.compare(rating2, rating1);
        } else if (rating1 == null && rating2 != null) {
            ratingCompare = 1;
        } else if (rating1 != null && rating2 == null) {
            ratingCompare = -1;
        }

        if (ratingCompare != 0) return ratingCompare;

        // Compare experience with null checks
        if (exp1 != null && exp2 != null) {
            return Integer.compare(exp2, exp1);
        } else if (exp1 == null && exp2 != null) {
            return 1;
        } else if (exp1 != null && exp2 == null) {
            return -1;
        }
        return 0;
    }

}

