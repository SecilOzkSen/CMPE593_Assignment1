package com.SecilSen.objects;

public enum ActivityType {
	ART("Art"),
	SPORTS("Sports"),
	CONFERENCE_EVENT("Conference Event");
	private String desc;
	private ActivityType(String description)
	{
		this.desc = description;
	}
	public String getDesc() {
		return desc;
	}
	
	
}
