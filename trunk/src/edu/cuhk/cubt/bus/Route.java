package edu.cuhk.cubt.bus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.text.format.Time;

public class Route{
	Time start;
	Time end;
	int operationDay;
	
	private List<Poi> pois = new LinkedList<Poi>();
	
	public Route(){
		//TODO
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
