package com.example.erp.controller.employee;

import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

import com.example.erp.entity.employee.EmployeeAttendance;

public class TimeDifferenceCalculator {
    public static void main(String[] args) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    	// Assuming you have the EmployeeAttendance object properly initialized
    	EmployeeAttendance employee = new EmployeeAttendance();
    	String inTimeStr = employee.getInTime();
    	String outTimeStr = employee.getOutTime();

    	LocalTime inTime = LocalTime.parse(inTimeStr, formatter);
    	LocalTime outTime = LocalTime.parse(outTimeStr, formatter);

    	// Calculate the duration between inTime and outTime
    	Duration duration = Duration.between(inTime, outTime);

    	// Convert the duration to hours and minutes
    	long minutes = duration.toMinutes();
    	long hours = minutes / 60;
    	minutes = minutes % 60;

    	// Format the working hours and minutes as a String
    	String formattedDuration = String.format("%02d:%02d", hours, minutes);

    	// Set the formatted duration to the employee's working hour
    	employee.setWorkingHour(formattedDuration);

    	// Print the time difference
    	System.out.println("Time difference: " + hours + " hours and " + minutes + " minutes.");
    }
}
