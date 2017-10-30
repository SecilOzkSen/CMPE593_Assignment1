package com.SecilSen.agents;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import com.SecilSen.main.Main;
import com.SecilSen.objects.Activity;
import com.SecilSen.objects.BaseTypeObject;
import com.SecilSen.objects.PredicateObject;

public class ActivityPlanner extends Agent{

	protected ArrayList<Activity> activityList = new ArrayList<Activity>();
	
	public ActivityPlanner() {
		super();
		

	}
	
	public void changeActivity(Activity a)
	{
		
	}
	
	@Override
	public void run() {
		super.run();
	}
	
	public void changeActivity()
	{
		
	}
	
	
	
	@Override
	public boolean generatePlan(PredicateObject predicateToBeInterpreted) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ArrayList<PredicateObject> perceiveEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
