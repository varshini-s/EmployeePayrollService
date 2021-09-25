package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;
import java.util.Objects;

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
	


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayrollData other = (EmployeePayrollData) obj;
		return Objects.equals(employeeName, other.employeeName) && id == other.id
				&& Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary)
				&& Objects.equals(startDate, other.startDate);
	}

	public String toString()
	{
		return "id="+id+",name='"+employeeName+'\''+", salary="+salary;
	}
	
}
