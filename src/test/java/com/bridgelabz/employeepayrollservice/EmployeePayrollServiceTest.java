package com.bridgelabz.employeepayrollservice;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.IOService;

import org.junit.Assert;
import org.junit.Before;

public class EmployeePayrollServiceTest 
{
	
	@Before
	public void test() throws SQLException, FileNotFoundException
	{
		
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.setUpDataBase();
		
	}

		@Test
		public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries()
		{
			Employee[] arrayOfEmployees= {
					new Employee(1, "Sheldon", 100000.0),
					new Employee(2, "Penny",200000.0 ),
					new Employee(3,"Ted",300000.0)
	
			};
	
			EmployeePayrollService employeePayrollService;
			employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
			employeePayrollService.writeEmployee(IOService.FILE_IO);
			employeePayrollService.printData(IOService.FILE_IO);
			long entries = employeePayrollService.countEntries(IOService.FILE_IO);
			Assert.assertEquals(3, entries);
	
		}
	
		@Test
		public void givenFileOnReadingFromFileShouldMatchEmployeeCount()
		{
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			long entries = employeePayrollService.readEmployee(IOService.FILE_IO).size();
			Assert.assertEquals(3, entries);
	
		}
	
		@Test
		public void  givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount()
		{
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			List<Employee> employeePayrollList = employeePayrollService.readEmployee( IOService.DB_IO);
			Assert.assertEquals(3, employeePayrollList.size());
		}
	
	
		
		@Test
		public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws UserEntryException
		{
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			employeePayrollService.readEmployee( IOService.DB_IO);
			employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
			boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
			Assert.assertTrue(result);
		}
	
		@Test
		public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeSalarySumWithGenderMap()
		{
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
	
	
			Map<String, Double> genderSalaryMap = employeePayrollService.getSalarySumBasedOnGender( IOService.DB_IO);
	
			Assert.assertEquals(genderSalaryMap.get("F"),(Double)300000.0);
			Assert.assertEquals(genderSalaryMap.get("M"),(Double)1300000.0);
	
	
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
		
		@Test
		public void givenNewEmployee_WhenAddedShouldSyncWithDB() throws SQLException, UserEntryException
		{
			
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			employeePayrollService.readEmployee(IOService.DB_IO);
			employeePayrollService.addEmployeeToPayroll("aaa", "M", 10000.00, LocalDate.now(), 1);
			boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("aaa");
			Assert.assertTrue(result);
			
			
		}
		
		@Test
		public void whenEmployeeIsRemoved_DatabaseShouldContainEmployeeDetailAsInactive() 
		{
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			int count = employeePayrollService.readEmployee( IOService.DB_IO).size();
			List<Employee> employeeListFromDBEmployees=	employeePayrollService .removeEmployee(IOService.DB_IO,1);
			List<Employee> employeeListInMemory = employeePayrollService.getEmployeePayrollList();
			Assert.assertEquals(employeeListFromDBEmployees.size(), count);
			Assert.assertEquals(employeeListInMemory.size(), count-1);
	
		}



}
