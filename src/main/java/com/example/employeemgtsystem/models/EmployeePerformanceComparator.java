package com.example.employeemgtsystem.models;


import java.util.Comparator;

public class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {

    @Override
    public int compare(Employee<T> o1, Employee<T> o2) {
        // Handle null employees
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return 1;
        if (o2 == null) return -1;

        // Handle null ratings/experience
        Double rating1 = o1.getPerformanceRating();
        Double rating2 = o2.getPerformanceRating();
        Integer exp1 = o1.getYearsOfExperience();
        Integer exp2 = o2.getYearsOfExperience();

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

