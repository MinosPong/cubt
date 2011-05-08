package edu.cuhk.cubt.bus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import android.text.format.Time;

public class Route{	

	public enum OperationType{
		MORNING,
		DAY,
		EVENING,
		MEETCLASS,
		HOLIDAY,
		UNDEFINED
	};

	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_MORNING = 1;
	public static final int TYPE_DAY = 2;
	public static final int TYPE_EVENING = 3;
	public static final int TYPE_MEETCLASS = 4;
	public static final int TYPE_HOLIDAY = 5;
		
	
	Time start;
	Time end;
	int operationDay;
	
	private String name = "unKnown";
	private String description = "";
	
	private List<Poi> pois = new LinkedList<Poi>();
	private List<Stop> stops = new LinkedList<Stop>();
		
	public Route(String name, String description,int operationDay, Time start, Time end,  List<Poi> pois){
		//TODO
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		this.operationDay = operationDay;
		this.pois = pois;
		
		/* init the Stops List */
		Iterator<Poi> iter = pois.iterator();
		Poi poi;
		while(iter.hasNext()){
			 poi = iter.next();
			 if(poi instanceof Stop){
				stops.add((Stop)poi); 				 
			 }
		}
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public Time getStartTime(){
		return start;
	}
	
	public Time getEndTime(){
		return end;
	}
	
	public int getOperationDay(){
		return operationDay;
	}

	/**
	 * 
	 * @param stop
	 * @return null if no result
	 */
	public Stop getPreviousStop(Stop input){
		int index =stops.indexOf(input);
		if(index< 1 || index > stops.size()) return null;
		return stops.listIterator(index).previous();
	}
	
	/**
	 * 
	 * @param stop
	 * @return null if no result
	 */
	public Stop getNextStop(Stop input){
		int index =stops.indexOf(input);
		if(index< 0 || index >= stops.size() -1) return null;
		return stops.listIterator(index+1).next();
	}
	
	/**
	 * Returns whether bus route pass through specific POI or STOP
	 * @param poi
	 * @return whether bus route pass through specific POI or STOP
	 */
	public boolean isPassThrough(Poi poi){
		return pois.contains(poi);
	}
	
	public boolean isMatch(Iterator<Stop> inputStops){
		Iterator<Stop> routeStops = getStops();
		while(inputStops.hasNext()){
			boolean tmp = false;
			Stop inputStop = inputStops.next();
			while(routeStops.hasNext()){				
				Stop routeStop = routeStops.next();
				if(routeStop == inputStop){
					tmp=true;
					break;
				}
			}
			if(!tmp)return false;
		}		
		return true;
	}
	
	public Iterator<Poi> getPois(){
		return pois.iterator();
	}
	
	
	public Iterator<Stop> getStops(){		
		return stops.iterator();
	}
	
}

/*test*/
