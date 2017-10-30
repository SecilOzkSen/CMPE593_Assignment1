package com.SecilSen.objects;

public enum EPredicates {
	//Beliefs
	IS_BEST_AVAILABLE_HOTEL_FOUND("isBestAvailableHotelFound"), // corresponding desire : FindHotel
	IS_ACTIVITY_FOUND("isActivityFound"), // corresponding desire: findActivity
	IS_ACTIVITY_CANCELLED("isActivityCancelled"), //corresponding desire: CancelActivity
	IS_EVENT_DATE_CHANGED("isDateChanged"), //corresponding desire: CancelActivity if we have, CancelHotel if we have
	//Desires & intentions
	FIND_HOTEL("findHotel"),
	FIND_ACTIVITY("findActivity"),
	CANCEL_ACTIVITY("cancelActivity"),
	CANCEL_ALL("cancelAll");
	
	private String methodName;
	private EPredicates(String methodName)
	{
		this.methodName = methodName;
	}
	public String getMethodName() {
		return methodName;
	}
	
	public EPredicates findCorrespondingIntention()
	{
		switch (this) {
		case IS_BEST_AVAILABLE_HOTEL_FOUND:
			return FIND_HOTEL;
		case IS_ACTIVITY_FOUND:
			return FIND_ACTIVITY;
		case IS_ACTIVITY_CANCELLED:
			return CANCEL_ACTIVITY;
		case IS_EVENT_DATE_CHANGED:
			return CANCEL_ALL;

		default:
			return FIND_HOTEL;
		}
	}
	
	public int getPriority()
	{
		switch (this) {
		case IS_BEST_AVAILABLE_HOTEL_FOUND:
		case FIND_HOTEL:
			return 1000;
		case IS_ACTIVITY_FOUND:
		case FIND_ACTIVITY:
			return 10;
		case IS_ACTIVITY_CANCELLED:
		case CANCEL_ACTIVITY:
			return 100;
		case IS_EVENT_DATE_CHANGED:
		case CANCEL_ALL:
			return 10000;
		

		default:
			return 1;
		}
	}
	
}
