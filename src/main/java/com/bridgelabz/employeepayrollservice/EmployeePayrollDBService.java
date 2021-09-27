package com.bridgelabz.employeepayrollservice;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class EmployeePayrollDBService 
{
	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement employeePayrollDataStatement;
	private PreparedStatement employeePayrollJoinDateStatement;


	public EmployeePayrollDBService()
	{

	}

	public static EmployeePayrollDBService getInstance()
	{
		if(employeePayrollDBService==null)
		{
			employeePayrollDBService=new EmployeePayrollDBService();
		}
		return  employeePayrollDBService;
	}
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
		String sql="SELECT * FROM employee_payroll";
		List<EmployeePayrollData> employeePayrollList=new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			employeePayrollList=this.getEmployeePayrollData(resultSet);

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
		String sql = String.format("UPDATE employee_payroll  SET basic_pay = %.2f WHERE name = '%s';",salary,name);
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

	public List<EmployeePayrollData> getEmployeePayrollData(String name)
	{

		List<EmployeePayrollData> employeePayrollList=null;
		if(this.employeePayrollDataStatement==null)
		{
			this.preparedStatementForEmployeeData();
		}
		try
		{
			employeePayrollDataStatement.setString(1,name);
			ResultSet resultSet= employeePayrollDataStatement.executeQuery();
			employeePayrollList=this.getEmployeePayrollData(resultSet);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet)
	{

		List<EmployeePayrollData> employeePayrollList=new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection())
		{

			while(resultSet.next())
			{
				int id=resultSet.getInt("id");
				String nameFromDB=resultSet.getString("name");
				double salary=resultSet.getDouble("basic_pay");
				LocalDate startDate= resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, nameFromDB, salary,startDate));
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return employeePayrollList;

	}


	private void preparedStatementForEmployeeData()
	{
		try {
			Connection connection = this.getConnection();
			String sql="SELECT * from employee_payroll WHERE name=?";
			employeePayrollDataStatement=connection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> getEmployeePayrollDataFromDBUsingStatement(String name)

	{
		String sql = String.format("SELECT * FROM employee_payroll  where name= '%s';",name);

		List<EmployeePayrollData> employeePayrollList=new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);

			employeePayrollList=this.getEmployeePayrollData(resultSet);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return employeePayrollList;
	}
	public Map<String, Double> getSalarySumBasedOnGender()
	{
		Map<String, Double> genderSalaryMap = new HashMap<String, Double>();
		
		String sql="SELECT SUM(basic_pay),gender FROM employee_payroll GROUP BY gender";
		
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);

			while(resultSet.next())
			{
				String gender=resultSet.getString("gender");
				double salarySum=resultSet.getDouble("SUM(basic_pay)");
				
				genderSalaryMap.put(gender, salarySum);
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return genderSalaryMap;
	}


	public int getEmployeeJoinCount(String startDate, String endDate)
	{
		List<EmployeePayrollData> employeePayrollList=null;
		if(this.employeePayrollJoinDateStatement==null)
		{
			this.preparedStatementToGetEmployeeJoinedCount();
		}
		try
		{
			employeePayrollJoinDateStatement.setDate(1,java.sql.Date.valueOf(startDate));
			employeePayrollJoinDateStatement.setDate(2,java.sql.Date.valueOf(endDate));

			ResultSet resultSet= employeePayrollJoinDateStatement.executeQuery();
			employeePayrollList=this.getEmployeePayrollData(resultSet);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollList.size();

	}

	private void preparedStatementToGetEmployeeJoinedCount()
	{
		try
		{
			Connection connection = this.getConnection();
			String sql="SELECT * FROM employee_payroll WHERE start BETWEEN ? AND ?";
			employeePayrollJoinDateStatement=connection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
