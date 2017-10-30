package com.SecilSen.objects;

import java.util.ArrayList;
import java.util.EnumMap;

public class Hotel extends BaseTypeObject{
	
	private String hotelName;
	private int hotelId;
	private ArrayList<Date> avaliableDateList = new ArrayList<Date>();
	private int dailyPrice;
	private int star;
	private ArrayList<Date> reservedDates = new ArrayList<Date>();
	
	public Hotel(String hotelName, int hotelId, ArrayList<Date> availableDateList, int dailyPrice, int star)
	{
		this.hotelName = hotelName;
		this.hotelId = hotelId;
		this.avaliableDateList = availableDateList;
		this.dailyPrice = dailyPrice;
		this.star = star;
	}
	public boolean isHotelAvailableBetweenDates(Date startDate, Date endDate)
	{
		boolean controlStartDate = false;
	    for(Date d : this.avaliableDateList)
	    {
	    	if(startDate.toString().equalsIgnoreCase(d.toString()) && !controlStartDate)
	    	{
	    		controlStartDate = true;
	    		continue;
	    	}
	    	if(controlStartDate && endDate.toString().equals(d.toString()))
	    	{
	    		return true;
	    	}
	    }
		return false;
	}
	private boolean isHotelAvailableInObligatoryDate(Date obligatoryDate)
	{
		for(Date d : this.avaliableDateList)
		{
			if(d.toString().equals(obligatoryDate.toString()))
			{
				return true;
			}
		}
		return false;
	}
	public ArrayList<Date> checkHotelAvailability(Date startDate, Date endDate, EnumMap<EPreferences, Object> hotelPreferences)
	{
		ArrayList<Date> reservedDates = new ArrayList<Date>();
		Date obligatoryDate = (Date)hotelPreferences.get(EPreferences.RESERVED_DATE);
		if(obligatoryDate != null && !isHotelAvailableInObligatoryDate(obligatoryDate))
			return null;
		reservedDates.add(obligatoryDate);
		Boolean dateBeforeReserved = (Boolean)hotelPreferences.get(EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED);
		if(dateBeforeReserved)
		{
			reservedDates.add(new Date(obligatoryDate.day - 1, obligatoryDate.month, obligatoryDate.year));
		}
		Integer minDateInteger = null;
		Integer maxBudgetInteger = null;
		int minDate = 1, thresholdPrice = 0;
		if((minDateInteger = (Integer)hotelPreferences.get(EPreferences.MIN_DATE)) != null)
		{
			minDate = minDateInteger.intValue();
		}
		if((maxBudgetInteger = (Integer)hotelPreferences.get(EPreferences.MAX_BUDGET)) != null)
		{
			thresholdPrice = maxBudgetInteger.intValue();
		}

		ArrayList<Date> resultDates = new ArrayList<Date>();
		Date d = new Date(startDate.day, startDate.month, startDate.year);
		int howManyDays = Integer.MAX_VALUE;
		int maxDate = endDate.day - startDate.day + 1;
		int actualPrice = thresholdPrice;
		while(minDate <= maxDate)
		{
			while(d.day < endDate.day && d.day + minDate - 1 <= endDate.day)
			{
				int j = 0;
				if(reservedDates.size() > 0)
				{
					for(int i = d.day; i<d.day + minDate; i++)
					{
						for(Date date: reservedDates)
						{
							if(date.day == i)
							{
								j++;
							}
						}
						
					}
				}
				
				
				if( j == reservedDates.size())
				{
					Date endD = new Date(d.day + minDate - 1, d.month, d.year);
					boolean check = this.isHotelAvailableBetweenDates(d, endD);
					if(check && howManyDays > minDate && actualPrice > this.dailyPrice*minDate)
					{
						resultDates.clear();
						resultDates.add(new Date(d.day, d.month, d.year));
						resultDates.add(endD);
						howManyDays = minDate;
						actualPrice = this.dailyPrice*minDate;
					}
				}
				d.day ++;
			}
			minDate ++;
		}
		
		
		return resultDates.size() == 0 ? null : resultDates;
	}
	
	public int returnTotalPrice(int days)
	{
		return this.dailyPrice*days;
	}
	public String getHotelName() {
		return hotelName;
	}
	public int getHotelId() {
		return hotelId;
	}
	public ArrayList<Date> getAvaliableDateList() {
		return avaliableDateList;
	}
	
	public int getDailyPrice() {
		return dailyPrice;
	}
	public int getStar() {
		return star;
	}
	public ArrayList<Date> getReservedDates() {
		return reservedDates;
	}
	public void setReservedDates(ArrayList<Date> reservedDates) {
		this.reservedDates = reservedDates;
	}
	
	
	

}
