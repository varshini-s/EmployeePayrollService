package com.bridgelabz.employeepayrollservice;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Connection;

public class EmployeePayrollDBService 
{

	private Connection getConnection() throws SQLException 
	{
		String jdbcURL ="jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
		String username="root";
		String password="Demo@1234";
		Connection connection = null;
		System.out.println("Connecting to database"+jdbcURL);
		connection=DriverManager.getConnection(jdbcURL,username,password);
		System.out.println("Connection is successfull!!"+connection);


		return connection;
	}

	public List<EmployeePayrollData> readData() 
	{
		String sql="SELECT * FROM payroll JOIN employee ON payroll.id=employee.id";
		List<EmployeePayrollData> employeePayrollList=new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);

			while(resultSet.next())
			{
				int id=resultSet.getInt("id");
				String name=resultSet.getString("name");
				double salary=resultSet.getDouble("basic_pay");
				LocalDate startDate= resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary,startDate));
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) 
	{
		this.updateEmployeeDataUsingStatement(name,salary);
		return 0;
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) 
	{
		String sql = String.format("UPDATE payroll JOIN employee ON payroll.id=employee.id SET basic_pay = %.2f WHERE name = '%s';",salary,name);
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			return statement.executeUpdate(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

}
