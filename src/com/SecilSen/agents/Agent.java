package com.SecilSen.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.SecilSen.main.Main;
import com.SecilSen.objects.EPredicates;
import com.SecilSen.objects.Message;
import com.SecilSen.objects.PredicateObject;

public abstract class Agent extends Thread {
	public static String ACTION_REMOVE = "remove";
	public static String ACTION_ADD = "add";
	protected List<PredicateObject> beliefs = Collections.synchronizedList(new ArrayList<PredicateObject>());
	protected List<PredicateObject> desires = Collections.synchronizedList(new ArrayList<PredicateObject>());
	protected PredicateObject intention = null;
	public BlockingQueue<Message> messageQueue;
	public Agent()
	{
		this.messageQueue = Main.messageQueue;
		//Collections.sort(this.intentions);
	}
	
	
	protected PredicateObject findBelief(EPredicates predicate)
	{
		for(PredicateObject p : this.beliefs)
		{
			if(p.getPredicateType() == predicate)
				return p;
		}
		
		return null;
	}
	protected PredicateObject findDesire(EPredicates predicate)
	{
		for(PredicateObject p : this.desires)
		{
			if(p.getPredicateType() == predicate)
				return p;
		}
		
		return null;
	}

	public void updateBeliefs(PredicateObject updatedPredicateObject)
	{
		for(PredicateObject p : this.beliefs)
		{

			if(p.getPredicateType() == updatedPredicateObject.getPredicateType())
			{
				p = updatedPredicateObject;
				Collections.sort(this.beliefs);
				System.out.println();
				System.out.println("***** Beliefs Updated *****");
				printCurrentBeliefs();
				return;
			}
		}
		this.beliefs.add(updatedPredicateObject);
		Collections.sort(this.beliefs);
		System.out.println();
		System.out.println("***** Beliefs Updated *****");
		printCurrentBeliefs();
	}
	
	public boolean updateDesires(PredicateObject updatedPredicateObject, String removeOrAdd)
	{
		if(!this.desires.contains(updatedPredicateObject))
		{
			if(ACTION_ADD.equalsIgnoreCase(removeOrAdd))
			{
				this.desires.add(updatedPredicateObject);
				Collections.sort(this.desires);
				System.out.println();
				System.out.println("***** Desires Updated *****");
				printCurrentDesires();
				return true;
			} 
		} else {
			if(ACTION_ADD.equalsIgnoreCase(removeOrAdd))
			{
				for(PredicateObject obj : this.desires)
				{
					if(obj.getPredicateType() == updatedPredicateObject.getPredicateType())
					{
						obj.setTrueOrFalse(updatedPredicateObject.isTrueOrFalse());
					}
				}
			}
			else if(ACTION_REMOVE.equalsIgnoreCase(removeOrAdd))
			{
				this.desires.remove(updatedPredicateObject);
				Collections.sort(this.desires);
				System.out.println();
				System.out.println("***** Desires Updated *****");
				printCurrentDesires();
				return true;
			}
		}
		
		
		
		
		return false;
	
	}
	
	
	public abstract boolean generatePlan(PredicateObject predicateToBeInterpreted);
	public abstract ArrayList<PredicateObject> perceiveEnvironment();
	
	public void printCurrentBeliefs()
	{
		System.out.println("CURRENT BELIEFS: ");
		for(PredicateObject p : this.beliefs)
		{
			System.out.println("		->" + p.getPredicateType() + " -> " + p.isTrueOrFalse() + " -> " + "Priority: " + p.getPriority());
		}
	}
	public void printCurrentDesires()
	{
		System.out.println("CURRENT DESIRES: ");
		for(PredicateObject p : this.desires)
		{
			System.out.println("		->" +p.getPredicateType() + " -> " + p.isTrueOrFalse() + " -> " + "Priority: " + p.getPriority());
		}
	}
	public void printCurrentIntention()
	{
		System.out.println("***** Intention Updated *****");
		System.out.println("CURRENT INTENTION: ");
		System.out.println("		->" + intention.getPredicateType());
		System.out.println();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
	
	
	
	


}
