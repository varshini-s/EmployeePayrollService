package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		List<EmployeePayrollData> employeePayrollList = employeePayrollService.readEmployeePayrollData( IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollList.size());
	}

	@Test
	public void  givenEmployeePayrollInDB_WhenWrittenToDatabase_ShouldMatchEmployeeCount()
	{
		String dateString="2018-01-03";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date= LocalDate.parse(dateString, formatter);
		EmployeePayrollData[] arrayOfEmployees= { new EmployeePayrollData(5, "Sheldon", 100000.0,date)};
		
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));

		employeePayrollService.writeEmployeePayrollData( IOService.DB_IO);
		List<EmployeePayrollData> employeePayrollList = employeePayrollService.readEmployeePayrollData( IOService.DB_IO);

		Assert.assertEquals(4, employeePayrollList.size());
		
		employeePayrollService.deleteEmployeeData(IOService.DB_IO,5);
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws UserEntryException
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
		expectedGenderSalaryMap.put("M", 1300000.0);

		Map<String, Double> genderSalaryMap = employeePayrollService.getSalarySumBasedOnGender( IOService.DB_IO);

		Assert.assertEquals(expectedGenderSalaryMap, genderSalaryMap);

	}

	@Test
	public void givenDateRangeOfJoiningDate_ShouldReturnCountOfEmployeesJoinedFromDataBase()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String startDate="2018-01-02";
		String endDate="2019-11-30";

		LocalDate.parse(startDate, formatter);
		LocalDate.parse(endDate, formatter);
		int count=employeePayrollService.getEmployeeJoinCount(IOService.DB_IO,startDate,endDate);
		Assert.assertEquals(2,count);

	}

}
