package com.SecilSen.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import com.SecilSen.main.Main;
import com.SecilSen.objects.Activity;
import com.SecilSen.objects.ActivityType;
import com.SecilSen.objects.Date;
import com.SecilSen.objects.EMessages;
import com.SecilSen.objects.EPredicates;
import com.SecilSen.objects.EPreferences;
import com.SecilSen.objects.Hotel;
import com.SecilSen.objects.Message;
import com.SecilSen.objects.PredicateObject;

public class TravelPlanner extends Agent {
	
	public ArrayList<Hotel> hotelsInTown = new ArrayList<Hotel>();
	public ArrayList<Activity> activitiesInTown = new ArrayList<Activity>();
	public Date startDate = null;
	public Date endDate = null;
	public EnumMap<EPreferences, Object> preferences = new EnumMap<EPreferences, Object>(EPreferences.class);
	//Plans
	public Hotel currentHotel = null;
	public List<Activity> currentActivities = Collections.synchronizedList(new ArrayList<Activity>());

	public Activity currentEvent = null;
	public CyclicBarrier gate;
	
	
	public TravelPlanner(Activity currentEvent, ArrayList<Hotel> hotelsInTown, ArrayList<Activity> activitiesInTown,  Date startDate, Date endDate, EnumMap<EPreferences, Object> preferences, CyclicBarrier gate) {
		super();
		this.currentEvent = currentEvent;
		this.hotelsInTown = hotelsInTown;
		this.activitiesInTown = activitiesInTown;
		this.startDate = startDate;
		this.endDate = endDate;
		this.preferences = preferences;
		this.gate = gate;
		
		System.out.println("I'm your travel planner.");
		System.out.println();
		printReservedDates();
		this.beliefs = initiateBeliefs();
		this.desires = initiateDesires();
		printCurrentBeliefs();
		printCurrentDesires();
		
		//this.intentions = initiateIntentions();
	}
	
	public ArrayList<PredicateObject> initiateBeliefs()
	{
		ArrayList<PredicateObject> blfs = new ArrayList<PredicateObject>();
		blfs.add(new PredicateObject(false, EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND.getPriority(), EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND));
		blfs.add(new PredicateObject(false, EPredicates.IS_ACTIVITY_FOUND.getPriority(), EPredicates.IS_ACTIVITY_FOUND));
		Collections.sort(blfs);
		return blfs;
		
	}
	public ArrayList<PredicateObject> initiateDesires()
	{
		ArrayList<PredicateObject> dsr = new ArrayList<PredicateObject>();
		dsr.add(new PredicateObject(true, EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND.getPriority(), EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND));
		dsr.add(new PredicateObject(true, EPredicates.IS_ACTIVITY_FOUND.getPriority(), EPredicates.IS_ACTIVITY_FOUND));
		Collections.sort(dsr);
		return dsr;
	}
	
	
	
	public Activity findAvailableActivity(Date date)
	{
		boolean control = false;
		Activity activity = null;
		ActivityType activityType;
		for(EPreferences preference : preferences.keySet())
		{
			if(preference == EPreferences.ACTIVITY_PREFERENCE)
			{
				control = true;
				break;
			}
		}
		if(control)
		{
			activityType = (ActivityType)preferences.get(EPreferences.ACTIVITY_PREFERENCE);
			for(Activity act : this.activitiesInTown)
			{
				if(act.getActivityDate().day == date.day)
				{
					if(activityType == act.activityType)
					{
						return act;
					}
					activity = act;
				}
			}
		}
		return activity;
	}
	public void cancelActivity(Activity activity, Date changedDate)
	{
		System.out.println("Activity cancelled");
		if(activity.getActivityType() == ActivityType.CONFERENCE_EVENT)
		{
			currentEvent.setActivityDate(changedDate);
			PredicateObject belief = findBelief(EPredicates.IS_EVENT_DATE_CHANGED);
			if(belief == null)
			{
				belief = new PredicateObject(true, EPredicates.IS_EVENT_DATE_CHANGED.getPriority(), EPredicates.IS_EVENT_DATE_CHANGED);
			}
			belief.setTrueOrFalse(true);
			this.updateBeliefs(belief);
			this.updateDesires(new PredicateObject(false, EPredicates.IS_EVENT_DATE_CHANGED.getPriority(), EPredicates.IS_EVENT_DATE_CHANGED), ACTION_ADD);
		} else {
			currentActivities.clear();
			PredicateObject belief = findBelief(EPredicates.IS_ACTIVITY_FOUND);
			if(belief == null)
			{
				belief = new PredicateObject(false, EPredicates.IS_ACTIVITY_FOUND.getPriority(), EPredicates.IS_ACTIVITY_FOUND);
			}
			belief.setTrueOrFalse(false);
			this.updateBeliefs(belief);
			this.updateDesires(new PredicateObject(true, EPredicates.IS_ACTIVITY_FOUND.getPriority(), EPredicates.IS_ACTIVITY_FOUND), ACTION_ADD);
		}
	}
	public void cancelHotel()
	{
		currentHotel = null;
		System.out.println("Hotel Cancelled");
		
		PredicateObject belief = findBelief(EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND);
		if(belief == null)
		{
			belief = new PredicateObject(false, EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND.getPriority(), EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND);
		}
		belief.setTrueOrFalse(false);
		this.updateBeliefs(belief);
		this.updateDesires(new PredicateObject(true, EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND.getPriority(), EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND), ACTION_ADD);
	}
	public void cancelAll()
	{	
		cancelHotel();
		synchronized (currentActivities) {
			for(Activity a: currentActivities)
			{
				if(a != null)
				{
					cancelActivity(a, null);
				}
				
			}
		}
		synchronized (this.beliefs) {
			for(PredicateObject p : this.beliefs)
			{
				if(p.getPredicateType() != EPredicates.IS_EVENT_DATE_CHANGED)
					p.setTrueOrFalse(false);
				else
					p.setTrueOrFalse(true);
			}
			printCurrentBeliefs();
		}
		synchronized (this.desires) {
			for(PredicateObject p: this.desires)
			{
				if(p.getPredicateType() == EPredicates.IS_EVENT_DATE_CHANGED)
					updateDesires(p, ACTION_REMOVE);
			}
		}
		
		
	}

	@Override
	public boolean generatePlan(PredicateObject predicateToBeInterpreted) {
		switch (predicateToBeInterpreted.getPredicateType()) {
		case FIND_HOTEL:
			currentHotel = findAvailableHotel();
			if(currentHotel != null)
			{
				printHotelInformation();
				predicateToBeInterpreted.setContent(currentHotel);
				predicateToBeInterpreted.setTrueOrFalse(true);
				PredicateObject belief = findBelief(EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND);
				belief.setTrueOrFalse(true);
				this.updateBeliefs(belief);
				PredicateObject desire = findDesire(EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND);
				desire.setTrueOrFalse(true);
				this.updateDesires(desire, ACTION_REMOVE);
				this.intention = null;
				return true;
			}
			break;
		case FIND_ACTIVITY:	
			ArrayList<Date> activityDateList = new ArrayList<Date>();
			Boolean control = (Boolean)preferences.get(EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED);
			Date hotelStartDate = currentHotel.getReservedDates().get(0), hotelEndDate = currentHotel.getReservedDates().get(currentHotel.getReservedDates().size() -1);
			for(int i = hotelStartDate.day; i <= hotelEndDate.day; i++)
			{
				if(control.booleanValue())
				{
					if((i != currentEvent.getActivityDate().day) &&(i != currentEvent.getActivityDate().day - 1))
					{
						activityDateList.add(new Date(i, hotelStartDate.month, hotelEndDate.year));
					}
				}
				else if(i != currentEvent.getActivityDate().day)
				{
					activityDateList.add(new Date(i, hotelStartDate.month, hotelEndDate.year));
				} 
			}
			for(Date d: activityDateList)
			{
				Activity a = findAvailableActivity(d);
				if(a != null)
				{
					currentActivities.add(a);
				}
			}
			if(currentActivities != null && currentActivities.size() > 0)
			{
				predicateToBeInterpreted.setContentList(currentActivities);
				predicateToBeInterpreted.setTrueOrFalse(true);
				PredicateObject blf = findBelief(EPredicates.IS_ACTIVITY_FOUND);
				blf.setTrueOrFalse(true);
				PredicateObject desire = findDesire(EPredicates.IS_ACTIVITY_FOUND);
				desire.setTrueOrFalse(true);
				this.updateDesires(desire, ACTION_REMOVE);
				this.intention = null;
				printActivityInformation();
				return true;
			}
			return true;
		case CANCEL_ACTIVITY:
			for(Activity a : currentActivities)
			{
				cancelActivity(a, null);
			}
			return true;
		case CANCEL_ALL:
			cancelAll();
			predicateToBeInterpreted.setTrueOrFalse(true);
			PredicateObject blf = findBelief(EPredicates.IS_EVENT_DATE_CHANGED);
			blf.setTrueOrFalse(false);
			PredicateObject desire = findDesire(EPredicates.IS_EVENT_DATE_CHANGED);
			Date newDate = (Date)preferences.get(EPreferences.RESERVED_DATE);
			newDate.day = currentEvent.getActivityDate().day;
			desire.setTrueOrFalse(false);
			this.updateBeliefs(blf);
			this.updateDesires(desire, ACTION_REMOVE);
			this.intention = null;
			return true;
		default:
			break;
		}
		return false;
	}
	
	public EnumMap<EPreferences, Object> findHotelPreferences()
	{
		EnumMap<EPreferences, Object> hotelPreferences = new EnumMap<EPreferences, Object>(EPreferences.class);
		for(EPreferences preference : this.preferences.keySet())
		{
			Object value = this.preferences.get(preference);
			//If a preference value is null, then user doesn't have that preference(false value)
			if((preference  == EPreferences.MAX_BUDGET || preference == EPreferences.MIN_DATE || preference == EPreferences.MIN_STAR || preference == EPreferences.RESERVED_DATE || preference == EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED ) && value != null)
			{
				if(hotelPreferences == null)
				{
					hotelPreferences = new EnumMap<EPreferences, Object>(EPreferences.class);
				}
				hotelPreferences.put(preference, value);
			}
		}
		
		return hotelPreferences;
	}
	
	public Hotel findAvailableHotel()
	{
		EnumMap<EPreferences, Object> hotelPreferences = findHotelPreferences();
		Hotel currentSelection = null;
		int selectionsTotalPrice = Integer.MAX_VALUE; 
		Integer star = (Integer)hotelPreferences.get(EPreferences.MIN_STAR);
		for(Hotel h : this.hotelsInTown)
		{
			if(h.getStar() < star.intValue())
				continue;
			ArrayList<Date> availableDates = h.checkHotelAvailability(this.startDate, this.endDate, hotelPreferences);
			h.setReservedDates(availableDates);
			if(availableDates == null || availableDates.size() < 2)
				continue;
			else if(!hotelPreferences.containsKey(EPreferences.MAX_BUDGET))
				return h;
			int hotelsTotalPrice = (Math.abs((availableDates.get(1).day - availableDates.get(0).day + 1))) * h.getDailyPrice();
			if(hotelsTotalPrice < selectionsTotalPrice)
			{
				currentSelection = h;
				selectionsTotalPrice = hotelsTotalPrice;
			}
		}
		
		return currentSelection;
	}


	@Override
	public ArrayList<PredicateObject> perceiveEnvironment() {
		ArrayList<PredicateObject> changedPredicates = null;
		
		while(true)
		{
			boolean control = false;
			for(Message m : Main.messageQueue)
			{
				if(m != null)
				{
					control = true;
					break;
				}
			}
			if(!control)
				break;
			try {
				Message message = Main.messageQueue.poll(10, TimeUnit.SECONDS);
				PredicateObject po = null;
				if(message.getMessage() == EMessages.HOTEL_CANCELLED)
				{
					po = new PredicateObject(false, EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND.getPriority(), EPredicates.IS_BEST_AVAILABLE_HOTEL_FOUND);
				} else if(message.getMessage() == EMessages.ACTIVITY_CANCELLED)
				{
					po = new PredicateObject(false, EPredicates.IS_ACTIVITY_CANCELLED.getPriority(), EPredicates.IS_ACTIVITY_CANCELLED);
					po.setContent(message.getContent());
				} else if(message.getMessage() == EMessages.DATE_CHANGED)
				{
					currentEvent = (Activity)message.getContent();
					po = new PredicateObject(true, EPredicates.IS_EVENT_DATE_CHANGED.getPriority(), EPredicates.IS_EVENT_DATE_CHANGED);
					po.setContent(message.getContent());
				}
				if(po != null)
				{
					if(changedPredicates == null)
					{
						changedPredicates = new ArrayList<PredicateObject>();
					}
					changedPredicates.clear();
					changedPredicates.add(po);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return changedPredicates;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			gate.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true)
		{
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.desires.size() > 0)
			{
				PredicateObject desire = desires.get(0);
				PredicateObject newIntention = new PredicateObject(false, desire.getPriority(), desire.getPredicateType().findCorrespondingIntention());
				this.intention = newIntention;
				printCurrentIntention();
				newIntention.setContent(desire.getContent());
				this.generatePlan(newIntention);
				
			}
			
			ArrayList<PredicateObject> changedPredicates = perceiveEnvironment();
			
			if((changedPredicates != null && changedPredicates.size() > 0))
			{
				for(PredicateObject p : changedPredicates)
				{
					this.updateBeliefs(p);
					PredicateObject correspondingDesire = new PredicateObject(!p.isTrueOrFalse(), p.getPriority(), p.getPredicateType());
					correspondingDesire.setContent(p.getContent());
					this.updateDesires(correspondingDesire, ACTION_ADD);
					
				}
					
			}
			
		}
	}
	
	public void printHotelInformation()
	{
		System.out.println(currentHotel.getHotelName() + " is reserved between " + currentHotel.getReservedDates().get(0).toString() + " and " + currentHotel.getReservedDates().get(1).toString()+ ".");
	}
	public void printReservedDates()
	{
		System.out.println("You have a speech in " + currentEvent.getActivityDate().toString());
		if(preferences.containsKey(EPreferences.IS_NIGHT_BEFORE_EVENT_RESERVED))
		{
			Date newDate = new Date(currentEvent.getActivityDate().day - 1, currentEvent.getActivityDate().month, currentEvent.getActivityDate().year);
			System.out.println("Since you want to practice the night before your speech, Date: " + newDate.toString() + " is also reserved.");
		}
	}
	public void printActivityInformation()
	{
		for (Activity a  : currentActivities) {
			System.out.println("For " + a.getDesc() + ", ticket is bought. The activity is at " + a.getActivityDate().toString());
		}
		
	}
	
	

}
