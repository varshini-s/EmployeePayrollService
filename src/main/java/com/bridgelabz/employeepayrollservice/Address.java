package com.bridgelabz.employeepayrollservice;

public class Address 
{
	private int employeeId;
	private String  houseNumber;
	private String street;
	private String city;
	private String state;
	private String zip;
	
	public Address(int employeeId, String houseNumber, String street, String city, String state, String zip) 
	{
		
		this.employeeId = employeeId;
		this.houseNumber = houseNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	public int getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(int employeeId) 
	{
		this.employeeId = employeeId;
	}
	public String getHouseNumber() 
	{
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) 
	{
		this.houseNumber = houseNumber;
	}
	public String getStreet() 
	{
		return street;
	}
	public void setStreet(String street) 
	{
		this.street = street;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city) 
	{
		this.city = city;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getZip() 
	{
		return zip;
	}
	public void setZip(String zip) 
	{
		this.zip = zip;
	}




}
