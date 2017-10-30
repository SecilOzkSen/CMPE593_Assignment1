package com.SecilSen.objects;

public class Date extends BaseTypeObject{
	public int day;
	public int month;
	public int year;
	
	public Date(int day, int month, int year) {
		super();
		this.day = day;
		this.month = month;
		this.year = year;
	}

	@Override
	public String toString() {
		return this.day + "/" + this.month + "/" + this.year;
	}
	
	
}
