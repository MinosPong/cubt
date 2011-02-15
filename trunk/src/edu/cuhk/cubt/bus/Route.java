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
