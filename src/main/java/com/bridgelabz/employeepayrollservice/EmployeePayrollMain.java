package com.bridgelabz.employeepayrollservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.IOService;

public class EmployeePayrollMain 
{

	public static void main(String[] args) 
	
	{
		System.out.println("----Welcome to employee payroll service----");
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInPutReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInPutReader);
		employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
		

	}

}
