package com.SecilSen.objects;

public enum EPreferences {

	MIN_DATE("Minimum Dates?"),
	MIN_STAR("Minimum Star?"),
	MAX_BUDGET("Maximum Budget"),
	ACTIVITY_COUNT("How many activity would you like to attend during your stay?"),
	ACTIVITY_PREFERENCE("Activity Preference?"),
	IS_RESERVED_DATE("Do you have any reserved day for the event?"),
	RESERVED_DATE("Which day?"),
	IS_NIGHT_BEFORE_EVENT_RESERVED("Should the day before the event be kept free?");
	
	private String desc;
	private EPreferences(String desc)
	{
		this.desc = desc;
	}
	
	public String getDescription()
	{
		return this.desc;
	}
}
