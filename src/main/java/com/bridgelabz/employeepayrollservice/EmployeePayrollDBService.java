package com.bridgelabz.employeepayrollservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.ScriptRunner;

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
	public Connection getConnection() throws SQLException 
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

	public void setupDatabase() throws SQLException, FileNotFoundException
	{
		Connection connection = this.getConnection();
		ScriptRunner sr = new ScriptRunner(connection);
		Reader reader = new BufferedReader(new FileReader("/home/varshini/Desktop/employee2.sql"));
		sr.runScript(reader);


	}

	public List<Employee> readData() 
	{

		String sql="SELECT * FROM employee JOIN payroll ON employee.id=payroll.id";
		List<Employee> employeeList = getEmployeeDataUsingDB(sql);

		return employeeList;
	}


	public int updateEmployeeData(String name, double salary) throws UserEntryException 
	{
		return this.updateEmployeeDataUsingPreparedStatement(name,salary);
	}

	private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws UserEntryException 
	{
		int count=0;
		if(this.employeePayrollUpdateDataStatement==null)
		{
			this.preparedStatementForUpdateEmployeeData();
		}

		Connection connection=null;

		try 
		{
			connection=this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}

		updateEmployeeSalary(name, salary, connection);
		count= updateEmployeePayroll(name, salary, connection);
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
		return count;
	}

	private int updateEmployeePayroll(String name, double salary, Connection connection) {
		try
		{
			double deductions=salary*0.2;
			double taxablePay=salary-deductions;
			double tax=taxablePay*0.1;
			double netPay=salary-tax;

			employeePayrollUpdateDataStatement.setDouble(1,salary);
			employeePayrollUpdateDataStatement.setDouble(2,deductions);
			employeePayrollUpdateDataStatement.setDouble(3,taxablePay);
			employeePayrollUpdateDataStatement.setDouble(4,tax);
			employeePayrollUpdateDataStatement.setDouble(5,netPay);
			employeePayrollUpdateDataStatement.setString(6,name);

			return employeePayrollUpdateDataStatement.executeUpdate();

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
		return 0;
	}

	private void updateEmployeeSalary(String name, double salary, Connection connection) {
		try(Statement statement=connection.createStatement())
		{
			updateSalaryInEmployeeTable(name,salary);

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
	}

	private void preparedStatementForUpdateEmployeeData()
	{
		try
		{
			Connection connection = this.getConnection();

			String sql ="Update payroll set  basic_pay =?, deductions=?, taxable_pay =?,"+
					"tax =?, net_pay =? where id =(Select id from employee where name=?);";

			employeePayrollUpdateDataStatement=connection.prepareStatement(sql);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private void updateSalaryInEmployeeTable(String name, double salary) 
	{

		String sql=String.format("UPDATE employee  SET salary = %.2f WHERE name = \"%s\";", salary,name);

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

				double deductions=resultSet.getDouble("deductions");
				double taxablePay=resultSet.getDouble("taxable_pay");
				double tax=resultSet.getDouble("tax");
				double netPay=resultSet.getDouble("net_pay");
				Payroll payroll = new Payroll( salary, deductions, taxablePay, tax, netPay);

				Employee employee = new Employee(id, nameFromDB, salary,startDate);
				employee.setPayroll(payroll);
				employeeList.add(employee);
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
			String sql="SELECT *  FROM employee JOIN payroll ON employee.id=payroll.id WHERE name=?";
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

	private Map<String, Double> getGivenOperationWithGenderMap(String operation) 
	{
		Map<String, Double> genderMap =new HashMap<String, Double>();

		String commonStringInQuery= ",gender FROM employee GROUP BY gender";
		String sql="SELECT "+operation+commonStringInQuery;
		
		try (Connection connection = this.getConnection())
		{
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next())
			{
				String gender=resultSet.getString("gender");
				double maxSalary=resultSet.getDouble(operation);
				genderMap.put(gender, maxSalary);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return genderMap;
		
	}
	public Map<String, Double> getSalarySumBasedOnGender()
	{

		String operation="SUM(salary)";
		return getGivenOperationWithGenderMap(operation);
	}
	
	public Map<String, Double> getMaxSalaryBasedOnGender() 
	{
		String operation= "MAX(salary)";
		return getGivenOperationWithGenderMap(operation);
	}
	

	public Map<String, Double> getMinSalaryBasedOnGender()
	{
		String operation= "MIN(salary)";
		return getGivenOperationWithGenderMap(operation);
	}

	public int getEmployeeJoinCount(String startDate, String endDate)
	{
		String sql = String.format("SELECT * FROM employee  JOIN payroll ON employee.id=payroll.id WHERE start BETWEEN '%s' AND '%s';",Date.valueOf(startDate),Date.valueOf(endDate));
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

		employeeId = insertToEmployeeTable(employeeName, gender, salary, startDate, companyId, employeeId, connection);
		employeeData = insertToPayrollTable(employeeName, salary, startDate, employeeId, connection, employeeData);
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

	private Employee insertToPayrollTable(String employeeName, double salary, LocalDate startDate, int employeeId,
			Connection connection, Employee employeeData) {
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
				Payroll payroll = new Payroll( salary, deductions, taxablePay, tax, netPay);
				employeeData = new Employee(employeeId, employeeName, salary, startDate);
				employeeData.setPayroll(payroll);
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
		return employeeData;
	}

	private int insertToEmployeeTable(String employeeName, String gender, double salary, LocalDate startDate,
			int companyId, int employeeId, Connection connection) {
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
		return employeeId;
	}

	public List<Employee> removeEmployee(int id)

	{
		String sql = String.format("UPDATE employee SET is_active=false WHERE id = '%d';",id);
		try (Connection connection = this.getConnection())
		{

			Statement statement=connection.createStatement();
			statement.executeUpdate(sql);
			return this.readData();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}





}
