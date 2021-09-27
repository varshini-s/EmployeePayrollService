package com.bridgelabz.employeepayrollservice;


public class UserEntryException extends Exception
{
	enum ExceptionType 
	{
		ENTERED_NULL, ENTERED_EMPTY
	}

	ExceptionType type;

	public UserEntryException(ExceptionType type, String message) 
	{

		super(message);
		this.type = type;

	}

}
