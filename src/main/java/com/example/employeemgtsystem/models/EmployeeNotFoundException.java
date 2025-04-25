package com.example.employeemgtsystem.models;

import java.util.NoSuchElementException;

public class EmployeeNotFoundException extends Exception{
//    public EmployeeNotFoundException(String message, NoSuchElementException e){
//        super(message);
//    }
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
