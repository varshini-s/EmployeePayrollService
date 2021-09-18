package com.bridgelabz.employeepayrollservice;

public class EmployeePayrollData 
{
	public int id;
	public String employeeName;
	public double salary;
	
	public EmployeePayrollData(int id,String employeeName,double salary) 
	{
		this.id=id;
		this.employeeName=employeeName;
		this.salary=salary;
	}
	
	public String toString()
	{
		return "id="+id+",name='"+employeeName+'\''+", salary="+salary;
	}
	
}
