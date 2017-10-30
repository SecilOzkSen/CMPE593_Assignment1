package com.SecilSen.agents;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import com.SecilSen.main.Main;
import com.SecilSen.objects.Activity;
import com.SecilSen.objects.ActivityType;
import com.SecilSen.objects.BaseTypeObject;
import com.SecilSen.objects.Date;
import com.SecilSen.objects.EMessages;
import com.SecilSen.objects.Message;
import com.SecilSen.objects.PredicateObject;

public class EventPlanner extends Agent{
	
	public CyclicBarrier gate;

	public EventPlanner(CyclicBarrier gate) {
		super();
		this.gate = gate;
	}
	
	public void changeEvent(Activity event, int day)
	{
		if(event.getActivityType() == ActivityType.CONFERENCE_EVENT)
		{
			Date date = event.getActivityDate();
			date.day = day;
		}
	}
	@Override
	public void run() {
		super.run();
		try {
			gate.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Message message = new Message(EMessages.DATE_CHANGED, new Activity(ActivityType.CONFERENCE_EVENT, "Conference Event", 1, new Date(19, 11, 2017)));
		Main.messageQueue.add(message);
		System.out.println();
		System.out.println("------------------------------------------------------------------");
		System.out.println("WAIT... SPEECH DATE CHANGED TO 19TH!");
		System.out.println("------------------------------------------------------------------");
		System.out.println();
	}

	@Override
	public ArrayList<PredicateObject> perceiveEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean generatePlan(PredicateObject predicateToBeInterpreted) {
		// TODO Auto-generated method stub
		return false;
	}

}
