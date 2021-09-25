package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;

public class EmployeePayrollData 
{
	public int id;
	public String employeeName;
	public double salary;
	public LocalDate startDate;
	
	public EmployeePayrollData(int id,String employeeName,double salary) 
	{
		this.id=id;
		this.employeeName=employeeName;
		this.salary=salary;
	}
	
	public EmployeePayrollData(int id, String employeeName, double salary, LocalDate startDate)
	{
		this(id, employeeName, salary);
		this.startDate=startDate;
		
	}

	public String toString()
	{
		return "id="+id+",name='"+employeeName+'\''+", salary="+salary;
	}
	
}
