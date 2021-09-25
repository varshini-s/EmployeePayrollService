package com.bridgelabz.employeepayrollservice;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.IOService;

public class EmployeePayrollService 
{

	public enum IOService {CONSOLE_IO,FILE_IO,DB_IO,REST_IO}
	private List<EmployeePayrollData> employeePyrollList;
	
	EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
	
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

	public  List<EmployeePayrollData> readEmployeePayrollData(IOService ioService)
	{
		if(ioService.equals(IOService.FILE_IO))
		{
			this.employeePyrollList=new EmployeePayrollFileIOService().readData();
		}
		else if(ioService.equals(IOService.DB_IO))
		{
			this.employeePyrollList=new EmployeePayrollDBService().readData();
		}
		
		return employeePyrollList;
	}
	
	public long countEntries(IOService ioService) 
	{

		if(ioService.equals(IOService.FILE_IO))
		{
			return new EmployeePayrollFileIOService().countEntries();
		}
		
		return 0;
	}

	public void updateEmployeeSalary(String name, double salary) 
	{
		int result =employeePayrollDBService.updateEmployeeData(name, salary);
		if(result==0) return;
		EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
		if(employeePayrollData!=null) employeePayrollData.salary=salary;
		
	}

	private EmployeePayrollData getEmployeePayrollData(String name) 
	{
		EmployeePayrollData employeePayrollData;
		 employeePayrollData=this.employeePyrollList.stream()
							.filter(employeeDataItem->employeeDataItem.employeeName.equals(name))
							.findFirst()
							.orElse(null);
		
		return employeePayrollData;
		
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name)
	{
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollDBService.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	public Map<String, Double> getSalarySumBasedOnGender(IOService ioService) 
	{
		if(ioService.equals(IOService.DB_IO))
		{
			return employeePayrollDBService.getSalarySumBasedOnGender();
		}

		
		return null;
	}
}
