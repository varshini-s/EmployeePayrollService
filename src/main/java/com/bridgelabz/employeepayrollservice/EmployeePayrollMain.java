package com.bridgelabz.employeepayrollservice;

import java.util.ArrayList;
import java.util.Scanner;

public class EmployeePayrollMain 
{

	public static void main(String[] args) 
	
	{
		System.out.println("----Welcome to employee payroll service----");
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInPutReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInPutReader);
		employeePayrollService.writeEmployeePayrollData();

	}

}
