package com.bridgelabz.employeepayrollservice;

import java.util.Arrays;

import org.junit.Test;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.IOService;

import org.junit.Assert;

public class EmployeePayrollServiceTest 
{

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries()
	{
		EmployeePayrollData[] arrayOfEmployees= {
				new EmployeePayrollData(1, "Sheldon", 100000.0),
				new EmployeePayrollData(2, "Penny",200000.0 ),
				new EmployeePayrollData(3,"Ted",300000.0)
				
		};
		
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		Assert.assertEquals(3, entries);
		
	}
	

	
}
