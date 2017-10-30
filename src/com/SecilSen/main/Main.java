package com.SecilSen.main;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.SecilSen.agents.EventPlanner;
import com.SecilSen.agents.TravelPlanner;
import com.SecilSen.objects.Activity;
import com.SecilSen.objects.ActivityType;
import com.SecilSen.objects.BaseTypeObject;
import com.SecilSen.objects.Date;
import com.SecilSen.objects.EMessages;
import com.SecilSen.objects.EPreferences;
import com.SecilSen.objects.Hotel;
import com.SecilSen.objects.Message;

public class Main {
	
	public static ArrayList<Hotel> hotelsInTown = new ArrayList<Hotel>();
	public static ArrayList<Activity> activitiesInTown = new ArrayList<Activity>();
	public static BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(5);
	
	
	public static void setHotelsInTown(int month, int year){
		ArrayList<Date> availableDates = new ArrayList<Date>();
		for(int i = 1; i<=30; i++)
		{
			availableDates.add(new Date(i, month, year));
		}
		hotelsInTown.add(new Hotel("Hotel 1", 1, availableDates, 50, 3));
		ArrayList<Date> availableDates2 = new ArrayList<Date>();
		for(int i = 1; i<=30; i++)
		{
			if(i != 19 &&  i != 20)
			{
				availableDates2.add(new Date(i, month, year));
			}
		}
		hotelsInTown.add(new Hotel("Hotel 2", 2, availableDates2, 100, 4));
		ArrayList<Date> availableDates3 = new ArrayList<Date>();
		for(int i = 15; i<=25; i++)
		{
			availableDates3.add(new Date(i, month, year));
	
		}
		hotelsInTown.add(new Hotel("Hotel 3", 3, availableDates3, 25, 3));
		ArrayList<Date> availableDates4 = new ArrayList<Date>();
		for(int i = 15; i <= 19; i++)
		{
			availableDates4.add(new Date(i, month, year));
		}
		hotelsInTown.add(new Hotel("Hotel 4", 4, availableDates4, 150, 4));
		hotelsInTown.add(new Hotel("Hotel 5", 5, availableDates4, 130, 4));
		ArrayList<Date> availableDates5 = new ArrayList<Date>();
		for(int i = 15; i < 21; i++)
		{
			availableDates5.add(new Date(i, month, year));
		}
		hotelsInTown.add(new Hotel("Hotel 6", 6, availableDates5, 180, 5));
		
	}
	
	public static void setActivitiesInTown(int month, int year)
	{
		Activity activity1 = new Activity(ActivityType.ART, "Art Activity 1", 1, new Date(17, month, year));
		activitiesInTown.add(activity1);
		Activity activity2 = new Activity(ActivityType.SPORTS, "Sports Activity 1", 2, new Date(17, month, year));
		activitiesInTown.add(activity2);
		Activity activity3 = new Activity(ActivityType.SPORTS, "Sports Activity 2", 3, new Date(18, month, year));
		activitiesInTown.add(activity3);
	}
	

	public static void main(String[] args) {
		
		
		Date startDate = null, endDate = null;
		int initialStartDate = 0, minDate = 0;
		EnumMap<EPreferences, Object> preferences = new EnumMap<EPreferences, Object>(EPreferences.class);
		Scanner scan = new Scanner(System.in);
		System.out.println("Hello, user. Which city you would like to go?");
		String city = scan.nextLine();
		System.out.println("Please specify the date range that you would like to stay in "+city);
		System.out.println("Start Date (day)");
		int day = scan.nextInt();
		System.out.println("Start Date (month)");
		int month = scan.nextInt();
		System.out.println("Start Date (year)");
		int year = scan.nextInt();
		
		startDate = new Date(day, month, year);
		setHotelsInTown(month, year);
		setActivitiesInTown(month, year);
		
		System.out.println("End Date (day)");
		day = scan.nextInt();
		System.out.println("End Date (month)");
		month = scan.nextInt();
		System.out.println("End Date (year)");
		year = scan.nextInt();
		
		endDate = new Date(day, month, year);
		
		System.out.println("Your preferences:");
		System.out.println(EPreferences.MIN_DATE.getDescription());
		minDate = scan.nextInt();
		preferences.put(EPreferences.MIN_DATE, minDate);
		System.out.println(EPreferences.MIN_STAR.getDescription());
		int val = 0;
		val = scan.nextInt();
		preferences.put(EPreferences.MIN_STAR, val);
		System.out.println(EPreferences.IS_RESERVED_DATE.getDescription() + " (YES/NO)");
		String ans = scan.next();
		Activity currentEvent = null;
		if("yes".equalsIgnoreCase(ans))
		{
			System.out.println(EPreferences.RESERVED_DATE.getDescription() + " (day)");
			val = 0;
			val = scan.nextInt();
			preferences.put(EPreferences.RESERVED_DATE, new Date(val, month, year));
			currentEvent = new Activity(ActivityType.CONFERENCE_EVENT, "Conference speech", 1, new Date(val, month, year));
			System.out.println(EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED.getDescription() + "(YES/NO)");
			String answ = scan.next();
			if("yes".equalsIgnoreCase(answ))
			{
				if(val == startDate.day)
				{
					System.out.println("Would you like to arrive in Amsterdam at " + Integer.toString(val - 1) + "/" + startDate.month + "/" + startDate.year + "(YES/NO)");
					String answer = scan.next();
					if("yes".equalsIgnoreCase(answer))
					{
						initialStartDate = startDate.day;
						startDate.day = val - 1;
					}
					
				}
				preferences.put(EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED, true);
			}
			
			
			
		}
		System.out.println(EPreferences.ACTIVITY_PREFERENCE.getDescription() + "(Arts / Sports / -)");
		String str = scan.next();
		if("arts".equalsIgnoreCase(str))
		{
			preferences.put(EPreferences.ACTIVITY_PREFERENCE, ActivityType.ART);
		} else if ("sports".equalsIgnoreCase(str)){
			preferences.put(EPreferences.ACTIVITY_PREFERENCE, ActivityType.SPORTS);
		}
		System.out.println(EPreferences.MAX_BUDGET.getDescription());
		val = 0;
		val = scan.nextInt();
		preferences.put(EPreferences.MAX_BUDGET, val);
		System.out.println(EPreferences.ACTIVITY_COUNT.getDescription());
		val = 0;
		val = scan.nextInt();
		preferences.put(EPreferences.ACTIVITY_COUNT, val);
		if((Boolean)preferences.get(EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED) || 2 + val > minDate)
		{
			minDate = 2 + val;
			preferences.put(EPreferences.MIN_DATE, minDate);
		}
		
		System.out.println("TRAVEL IS PLANNING...");
		System.out.println();
		scan.close();
		//Plan the travel!
		final CyclicBarrier gate = new CyclicBarrier(3);
		new TravelPlanner(currentEvent, hotelsInTown, activitiesInTown, startDate, endDate, preferences, gate).start();
		new EventPlanner(gate).start();
		try {
			gate.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
