package com.bridgelabz.employeepayrollservice;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bridgelabz.employeepayrollservice.UserEntryException.ExceptionType;

import java.time.LocalDate;

public class EmployeePayrollDBService 
{
	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement employeeDataStatement;
	private PreparedStatement employeePayrollUpdateDataStatement;


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

	public List<Employee> readData() 
	{
		String sql="SELECT * FROM employee";
		List<Employee> employeeList = getEmployeeDataUsingDB(sql);

		return employeeList;
	}


	public int updateEmployeeData(String name, double salary) throws UserEntryException 
	{
		this.updateEmployeeDataUsingPreparedStatement(name,salary);
		return 0;
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) throws UserEntryException 
	{
		String sql = String.format("UPDATE employee SET basic_pay = %.2f WHERE name = '%s';",salary,name);
		try (Connection connection = this.getConnection())
		{

			if(name.isEmpty())
			{
				throw new UserEntryException(ExceptionType.ENTERED_EMPTY,"Please enter valid Name");
			}
			Statement statement=connection.createStatement();
			return statement.executeUpdate(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		catch (NullPointerException e) 
		{
			throw new UserEntryException(ExceptionType.ENTERED_NULL,"Please enter valid Name");
		}
		return 0;
	}
	private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws UserEntryException 
	{
		if(this.employeePayrollUpdateDataStatement==null)
		{
			this.preparedStatementForUpdateEmployeeData();
		}
		try
		{
			if(name.isEmpty())
			{
				throw new UserEntryException(ExceptionType.ENTERED_EMPTY,"Please enter valid Name");
			}
			employeePayrollUpdateDataStatement.setDouble(1,salary);
			employeePayrollUpdateDataStatement.setString(2,name);


			return employeePayrollUpdateDataStatement.executeUpdate();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException e) 
		{
			throw new UserEntryException(ExceptionType.ENTERED_NULL,"Please enter valid Name");
		}


		return 0;
	}

	private void preparedStatementForUpdateEmployeeData()
	{
		try
		{
			Connection connection = this.getConnection();
			String sql="UPDATE employee  SET salary = ? WHERE name = ?;";

			employeePayrollUpdateDataStatement=connection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Employee> getEmployee(String name) throws UserEntryException
	{

		List<Employee> employeeList=null;
		if(this.employeeDataStatement==null)
		{
			this.preparedStatementForEmployeeData();
		}
		try
		{
			if(name.isEmpty())
			{
				throw new UserEntryException(ExceptionType.ENTERED_EMPTY,"Please enter valid Name");
			}
			employeeDataStatement.setString(1,name);
			ResultSet resultSet= employeeDataStatement.executeQuery();
			employeeList=this.getEmployee(resultSet);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException e) 
		{
			throw new UserEntryException(ExceptionType.ENTERED_NULL,"Please enter valid Name");
		}
		return employeeList;
	}

	private List<Employee> getEmployee(ResultSet resultSet)
	{

		List<Employee> employeeList=new ArrayList<Employee>();
		try (Connection connection = this.getConnection())
		{

			while(resultSet.next())
			{
				int id=resultSet.getInt("id");
				String nameFromDB=resultSet.getString("name");
				double salary=resultSet.getDouble("salary");
				LocalDate startDate= resultSet.getDate("start").toLocalDate();
				employeeList.add(new Employee(id, nameFromDB, salary,startDate));
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return employeeList;

	}


	private void preparedStatementForEmployeeData()
	{
		try 
		{
			Connection connection = this.getConnection();
			String sql="SELECT * from employee WHERE name=?";
			employeeDataStatement=connection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Employee> getEmployeeFromDBUsingStatement(String name)

	{
		String sql = String.format("SELECT * FROM employee  where name= '%s';",name);

		return this.getEmployeeDataUsingDB(sql);
	}

	private List<Employee> getEmployeeDataUsingDB(String sql) 
	{
		List<Employee> employeeList=new ArrayList<Employee>();
		try (Connection connection = this.getConnection())
		{
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);

			employeeList=this.getEmployee(resultSet);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return employeeList;
	}

	public Map<String, Double> getSalarySumBasedOnGender()
	{
		Map<String, Double> genderSalaryMap = new HashMap<String, Double>();

		String sql="SELECT SUM(salary),gender FROM employee GROUP BY gender";

		try (Connection connection = this.getConnection())
		{
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);

			while(resultSet.next())
			{
				String gender=resultSet.getString("gender");
				double salarySum=resultSet.getDouble("SUM(salary)");

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
		String sql = String.format("SELECT * FROM employee WHERE start BETWEEN '%s' AND '%s';",Date.valueOf(startDate),Date.valueOf(endDate));

		return this.getEmployeeDataUsingDB(sql).size();
	}



	public void deleteEmployeeData(int id) 
	{

		String sql = String.format("DELETE FROM employee WHERE id= '%d';",id);
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			statement.executeUpdate(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}


	public Employee addEmployeeToPayroll( String employeeName, String gender, double salary, LocalDate startDate,
										int companyId) throws SQLException 
	{
		int employeeId=-1;
		Connection connection=null;
		Employee employeeData=null;

		try 
		{
			connection=this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}

		try(Statement statement=connection.createStatement())
		{
			String sql=String.format("INSERT INTO employee(name,gender,salary,start,company_id) VALUES ('%s','%s','%s','%s','%s');", employeeName,gender,salary,startDate,companyId);
			int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(rowAffected==1)
			{
				ResultSet resultSet=statement.getGeneratedKeys();
				if(resultSet.next()) employeeId=resultSet.getInt(1);
			}

		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			try 
			{
				connection.rollback();

			} catch (Exception ex) 
			{

				ex.printStackTrace();
			}
		}
		try(Statement statement=connection.createStatement())
		{
			double deductions=salary*0.2;
			double taxablePay=salary-deductions;
			double tax=taxablePay*0.1;
			double netPay=salary-tax;

			String sql=String.format("INSERT INTO payroll(id,basic_pay,deductions,taxable_pay,tax,net_pay) VALUES "
								 + "('%s','%s','%s','%s','%s','%s');",employeeId,salary,deductions,taxablePay,tax,netPay);
			int rowAffected=statement.executeUpdate(sql);
			if(rowAffected==1)
			{
				employeeData = new Employee(employeeId, employeeName, salary, startDate);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			try 
			{
				connection.rollback();

			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}
		try 
		{
			connection.commit();

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
			{
				try 
				{
					connection.close();

				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		return employeeData;

	}


}
