package com.SecilSen.objects;

import java.util.ArrayList;

import com.SecilSen.agents.Agent;

public class Activity extends BaseTypeObject{
	
	public ActivityType activityType;
	public String desc;
	public int activityId;
	public ArrayList<Agent> registeredAgents;
	public Date activityDate;
	public Activity(ActivityType activityType, String desc, int activityId, Date activityDate) {
		super();
		this.activityType = activityType;
		this.desc = desc;
		this.activityId = activityId;
		this.activityDate = activityDate;
	}
	public ActivityType getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public ArrayList<Agent> getRegisteredAgents() {
		return registeredAgents;
	}
	public void setRegisteredAgents(ArrayList<Agent> registeredAgents) {
		this.registeredAgents = registeredAgents;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	
	
	
	
}
