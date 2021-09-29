package com.bridgelabz.employeepayrollservice;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class EmployeePayrollService 
{

	public enum IOService {CONSOLE_IO,FILE_IO,DB_IO,REST_IO}
	private List<Employee> employeePayrollList;




	EmployeePayrollDBService employeePayrollDBService ;

	public EmployeePayrollService()
	{

		employeePayrollDBService=EmployeePayrollDBService.getInstance();

	}

	public EmployeePayrollService(List<Employee> employeePyrollList) 
	{
		this.employeePayrollList=employeePyrollList;

	}

	public List<Employee> getEmployeePayrollList() {
		return employeePayrollList;
	}

	public void setEmployeePayrollList(List<Employee> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}


	public void readEmployee(Scanner consoleInputReader)
	{
		System.out.println("Enter employee id: ");
		int id=consoleInputReader.nextInt();
		System.out.println("Enter Employee name: ");
		String name=consoleInputReader.next();
		System.out.println("Enter employee salary: ");
		double salary=consoleInputReader.nextDouble();
		employeePayrollList.add(new Employee(id, name, salary));
	}

	public void writeEmployee(IOService ioService)
	{
		if(ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting employee payroll info to console:\n "+employeePayrollList);
		else if(ioService.equals(IOService.FILE_IO))
		{
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
		}

	}

	public void printData(IOService ioService) 
	{
		if(ioService.equals(IOService.FILE_IO))
		{
			new EmployeePayrollFileIOService().printData();
		}

	}

	public  List<Employee> readEmployee(IOService ioService)
	{
		if(ioService.equals(IOService.FILE_IO))
		{
			this.employeePayrollList=new EmployeePayrollFileIOService().readData();
		}
		else if(ioService.equals(IOService.DB_IO))
		{
			this.employeePayrollList=employeePayrollDBService.readData();
		}

		return employeePayrollList;
	}

	public long countEntries(IOService ioService) 
	{

		if(ioService.equals(IOService.FILE_IO))
		{
			return new EmployeePayrollFileIOService().countEntries();
		}

		return 0;
	}

	public void updateEmployeeSalary(String name, double salary) throws UserEntryException 
	{
		int result =employeePayrollDBService.updateEmployeeData(name, salary);
		if(result==0) return;
		Employee employee=this.getEmployee(name);
		if(employee!=null) 
		{
			employee.setSalary(salary);
			employee.getPayroll().setBasicPay(salary);
		}

	}

	private Employee getEmployee(String name) 
	{
		Employee Employee;
		Employee=this.employeePayrollList.stream()
				.filter(employeeDataItem->employeeDataItem.getEmployeeName().equals(name))
				.findFirst()
				.orElse(null);

		return Employee;

	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) throws UserEntryException
	{
		List<Employee> EmployeeList=employeePayrollDBService.getEmployee(name);
		return EmployeeList.get(0).equals(this.getEmployee(name));
	}

	public Map<String, Double> getSalarySumBasedOnGender(IOService ioService) 
	{
		if(ioService.equals(IOService.DB_IO))
		{
			return employeePayrollDBService.getSalarySumBasedOnGender();
		}


		return null;
	}
	public int getEmployeeJoinCount(IOService ioService, String startDate, String endDate)
	{
		if(ioService.equals(IOService.DB_IO))
		{
			return employeePayrollDBService.getEmployeeJoinCount( startDate,  endDate);
		}

		return 0;
	}


	public void addEmployeeToPayroll(String employeeName, String gender, double salary, LocalDate startDate,int companyId ) throws SQLException
	{
		employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(employeeName, gender, salary, startDate, companyId));
	}

	public void removeEmployeeFromList(int id)
	{
		for(int index=0;index<employeePayrollList.size();index++)
		{
			if(employeePayrollList.get(index).getEmployeeId()==id)
			{
				employeePayrollList.remove(index);
				break;
			}
		}

	}



	public List<Employee> removeEmployee(IOService ioService, int id) 
	{
		if(ioService.equals(IOService.DB_IO))
		{
			this.removeEmployeeFromList(id);
			return employeePayrollDBService.removeEmployee(id);
		}
		return null;


	}

	public void setUpDataBase() throws FileNotFoundException, SQLException 
	{
		employeePayrollDBService.setupDatabase();
		
	}
}
