package com.bridgelabz.employeepayrollservice;

import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService 
{

	public enum IOService {CONSOLE_IO,FILE_IO,DB_IO,REST_IO}
	private List<EmployeePayrollData> employeePyrollList;
		
	public EmployeePayrollService(List<EmployeePayrollData> employeePyrollList) 
	{
		this.employeePyrollList=employeePyrollList;
	
	}
	
	public void readEmployeePayrollData(Scanner consoleInputReader)
	{
		System.out.println("Enter employee id: ");
		int id=consoleInputReader.nextInt();
		System.out.println("Enter Employee name: ");
		String name=consoleInputReader.next();
		System.out.println("Enter employee salary: ");
		double salary=consoleInputReader.nextDouble();
		employeePyrollList.add(new EmployeePayrollData(id, name, salary));
	}
	

}
