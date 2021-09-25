package com.bridgelabz.employeepayrollservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Test
	public void givenFileOnReadingFromFileShouldMatchEmployeeCount()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		long entries = employeePayrollService.readEmployeePayrollData(IOService.FILE_IO).size();
		Assert.assertEquals(3, entries);

	}

	@Test
	public void  givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePyrollList = employeePayrollService.readEmployeePayrollData( IOService.DB_IO);
		Assert.assertEquals(3, employeePyrollList.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePyrollList = employeePayrollService.readEmployeePayrollData( IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}

	@Test
	public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeSalarySumWithGenderMap()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();

		Map<String, Double> expectedGenderSalaryMap = new HashMap<String, Double>();
		expectedGenderSalaryMap.put("F", 3000000.0);
		expectedGenderSalaryMap.put("M", 2000000.0);

		Map<String, Double> genderSalaryMap = employeePayrollService.getSalarySumBasedOnGender( IOService.DB_IO);

		Assert.assertEquals(expectedGenderSalaryMap, genderSalaryMap);

	}

}
