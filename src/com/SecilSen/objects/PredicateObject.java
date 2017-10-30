package com.SecilSen.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PredicateObject implements Comparable<PredicateObject>{
	private boolean trueOrFalse;
	protected int priority;
	protected EPredicates predicateType;
	protected BaseTypeObject content;
	protected List<Activity> contentList = Collections.synchronizedList(new ArrayList<Activity>());
	
	public PredicateObject(boolean trueOrFalse, int priority, EPredicates predicateType)
	{
		this.trueOrFalse = trueOrFalse;
		this.predicateType = predicateType;
		this.priority = priority;
	}
	

	public boolean isTrueOrFalse() {
		return trueOrFalse;
	}

	public void setTrueOrFalse(boolean trueOrFalse) {
		this.trueOrFalse = trueOrFalse;
	}


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public EPredicates getPredicateType() {
		return predicateType;
	}

	public void setPredicateType(EPredicates predicateType) {
		this.predicateType = predicateType;
	}
	

	public BaseTypeObject getContent() {
		return content;
	}

	public void setContent(BaseTypeObject content) {
		this.content = content;
	}
	

	public int compareTo(PredicateObject o) {
		return Integer.compare(o.priority, this.priority);
	}


	public List<Activity> getContentList() {
		return contentList;
	}


	public void setContentList(List<Activity> contentList) {
		this.contentList = contentList;
	}
	
	
	
	
}
