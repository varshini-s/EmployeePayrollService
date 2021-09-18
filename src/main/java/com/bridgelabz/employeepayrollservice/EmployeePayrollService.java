package com.bridgelabz.employeepayrollservice;

import java.util.List;
import java.util.Scanner;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.IOService;

public class EmployeePayrollService 
{

	public enum IOService {CONSOLE_IO,FILE_IO,DB_IO,REST_IO}
	private List<EmployeePayrollData> employeePyrollList;
	
	public EmployeePayrollService() {	}
		
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
	
	public void writeEmployeePayrollData(IOService ioService)
	{
		if(ioService.equals(IOService.CONSOLE_IO))
		System.out.println("\nWriting employee payroll info to console:\n "+employeePyrollList);
		else if(ioService.equals(IOService.FILE_IO))
		{
			new EmployeePayrollFileIOService().writeData(employeePyrollList);
		}
		
	}

	public void printData(IOService ioService) 
	{
		if(ioService.equals(IOService.FILE_IO))
		{
			new EmployeePayrollFileIOService().printData();
		}
		
	}

	public long readEmployeePayrollData(IOService ioService)
	{
		if(ioService.equals(IOService.FILE_IO))
		{
			this.employeePyrollList=new EmployeePayrollFileIOService().readData();
		}
		
		return employeePyrollList.size();
	}
	
	public long countEntries(IOService ioService) 
	{

		if(ioService.equals(IOService.FILE_IO))
		{
			return new EmployeePayrollFileIOService().countEntries();
		}
		
		return 0;
	}
}
