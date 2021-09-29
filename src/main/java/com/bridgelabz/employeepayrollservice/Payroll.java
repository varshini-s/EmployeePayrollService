package com.bridgelabz.employeepayrollservice;

import java.util.Objects;

public class Payroll 
{
	private double basicPay;
	private double deductions;
	private double taxablePay;
	private double tax;
	private double netPay;
	
	public Payroll( double basicPay, double deductions, double taxablePay, double tax, double netPay) 
	{
		
		this.basicPay = basicPay;
		this.deductions = deductions;
		this.taxablePay = taxablePay;
		this.tax = tax;
		this.netPay = netPay;
	}

	public double getBasicPay() 
	{
		return basicPay;
	}
	public void setBasicPay(double basicPay) 
	{
		this.basicPay = basicPay;
	}
	public double getDeductions() 
	{
		return deductions;
	}
	public void setDeductions(double deductions) 
	{
		this.deductions = deductions;
	}
	public double getTaxablePay() 
	{
		return taxablePay;
	}
	public void setTaxablePay(double taxablePay) 
	{
		this.taxablePay = taxablePay;
	}
	public double getTax() 
	{
		return tax;
	}
	public void setTax(double tax)
	{
		this.tax = tax;
	}
	public double getNetPay() 
	{
		return netPay;
	}
	public void setNetPay(double netPay)
	{
		this.netPay = netPay;
	}

	@Override
	public String toString() {
		return "Payroll [basicPay=" + basicPay + ", deductions=" + deductions + ", taxablePay=" + taxablePay + ", tax="
				+ tax + ", netPay=" + netPay + "]";
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payroll other = (Payroll) obj;
		return Double.doubleToLongBits(basicPay) == Double.doubleToLongBits(other.basicPay)
				&& Double.doubleToLongBits(deductions) == Double.doubleToLongBits(other.deductions)
				&& Double.doubleToLongBits(netPay) == Double.doubleToLongBits(other.netPay)
				&& Double.doubleToLongBits(tax) == Double.doubleToLongBits(other.tax)
				&& Double.doubleToLongBits(taxablePay) == Double.doubleToLongBits(other.taxablePay);
	}

	
	


}
