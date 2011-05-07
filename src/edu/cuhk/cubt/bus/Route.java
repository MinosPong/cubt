package edu.cuhk.cubt.bus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.cuhk.cubt.bus.Timetable.OperationDay;

import android.text.format.Time;

public class Route{
	Time start;
	Time end;
	OperationDay operationDay;
	
	private String name = "unKnown";
	private String description = "";
	
	private List<Poi> pois = new LinkedList<Poi>();
	
	public Route(){
		//TODO
	}
	
	public Route(String name, String description,OperationDay operationDay, Time start, Time end,  List<Poi> pois){
		//TODO
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		this.operationDay = operationDay;
		this.pois = pois;
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
	
	public OperationDay getOperationDay(){
		return operationDay;
	}
	
	/**
	 * 
	 * @param stop
	 * @return null if no result
	 */
	public Stop getNextStop(Stop input){
		boolean matched = false;
		Iterator<Stop> stops = getStops();
		while(stops.hasNext()){
			Stop stop = stops.next();
			if(matched) return stop;
			
			if(input == stop){
				matched = true;
			}
		}
		return null;
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
		List<Stop> stops = new LinkedList<Stop>();
		Iterator<Poi> iter = pois.iterator();
		Poi poi;
		while(iter.hasNext()){
			 poi = iter.next();
			 if(poi instanceof Stop){
				stops.add((Stop)poi); 				 
			 }
		}
		return stops.iterator();
	}
	
}

/*test*/
