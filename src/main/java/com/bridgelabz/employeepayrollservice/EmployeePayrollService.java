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
	

}
