package edu.cuhk.cubt.bus;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import android.location.Location;
import android.text.format.Time;

/**
 * An implementation of a Timetable.
 * @author PKM
 *
 */
public class Timetable {
	
	public enum OperationDay{
		WEEKDAY,
		SATURDAY,
		SUNDAY,
		HOLIDAY
	};
		
	private enum DirectionIndicatior{
		UP,DOWN,UNDEFINED
	};
	/**
	 * Returns all the routes on the given time and location
	 * 
	 * @param millis The UTC time
	 * @param location The location
	 * @author Kalyn
	 * @return Collection<Route> The possible route
	 */
	public static /*Collection<Route>*/Collection<LinkedList<Poi>> findRoutesByLocationTime(long millis, Location location){
		Time time = new Time();
		time.set(millis); //any usage?	

		DirectionIndicatior direction;
		
		LinkedList<Integer> IndicatorQueue = new LinkedList<Integer>();
		LinkedList<Integer> TmpQueue = new LinkedList<Integer>();
		Iterator<Integer> itr = IndicatorQueue.iterator();
		Collection<LinkedList<Poi>> PossibleRoutes = new LinkedList<LinkedList<Poi>>();
		
		direction = CheckDirection(location); //enter first station only, how about check 2 stations?
		TmpQueue = CheckTime(time);
		IndicatorQueue = MapRoutingWithDirection(TmpQueue, direction);
		if(!IndicatorQueue.isEmpty()){
			while(itr.hasNext()){
				int indicator = itr.next();
				//check the direction
				PossibleRoutes.add(Routing(indicator, time)); //return PossibleRoutes;
			}	
			return PossibleRoutes;
		}
		else
			return null; //unknown route		
	}
	
	
	private static DirectionIndicatior CheckDirection(Location location){
		//if(poi.getName() == "Train Station")
		if((location.getLatitude() == 22.414361) && (location.getLongitude() == 114.210292)) //Train Station
			return DirectionIndicatior.UP;
		if((location.getLatitude() == 22.415306) && (location.getLongitude() == 114.208428)) //Chung Chi Teaching Blocks
			return DirectionIndicatior.UP;
		if((location.getLatitude() == 22.419737) && (location.getLongitude() == 114.206974)) //Sir Run Run Hall
			return DirectionIndicatior.UP;
		
		if((location.getLatitude() == 22.421279) && (location.getLongitude() == 114.207486)) //New Asia College
			return DirectionIndicatior.DOWN;
		if((location.getLatitude() == 22.422397) && (location.getLongitude() == 114.201395)) //Shaw College
			return DirectionIndicatior.DOWN;
		if((location.getLatitude() == 22.425152) && (location.getLongitude() == 114.207891)) //Residences No.10 and 11
			return DirectionIndicatior.DOWN;
		return DirectionIndicatior.UNDEFINED;
	}
	
	private static LinkedList<Integer> CheckTime(Time time){ //already findRoutesByLocationTime, Extract all possible routes in particular time, neglect the direction
		//if(direction == DirectionIndicatior.UP)
		//if(direction == DirectionIndicatior.DOWN)
		LinkedList<Integer> IndicatorQueue= new LinkedList<Integer>();
		
		//train station
		//Sunday 00 20 40
		if((time.weekDay == 6)&&(time.minute == 00 || time.minute == 20 || time.minute == 40)){ 
			if((time.hour == 8 && time.minute == 40)||(time.hour == 9 && time.minute == 20)){ //08:40 or 09:20
				RemoveDuplicate(IndicatorQueue,0);
			}if(time.hour == 13 && time.minute != 00){ //20 40
				RemoveDuplicate(IndicatorQueue,0);
			}
			if(time.hour == 12 && time.minute != 20){ //00 40
				RemoveDuplicate(IndicatorQueue,0);
			}
			if(time.hour == 13) //00 20 40
				RemoveDuplicate(IndicatorQueue,7); //down
			if((time.hour == 10)||(time.hour == 11)||(time.hour >= 14 && time.hour <= 22)){ //00 20 40
				RemoveDuplicate(IndicatorQueue,0); //up
				RemoveDuplicate(IndicatorQueue,7); //down
			}
			if(time.hour == 23 && time.minute != 40){ //00 20
				RemoveDuplicate(IndicatorQueue,0);
			}
		}
		
		//Sunday 10 30 50
		if((time.weekDay == 6)&&(time.minute == 10 || time.minute == 30 || time.minute == 50)){
			if((time.hour == 11 || time.hour == 12 || (time.hour >= 14 && time.hour <=18)) && time.minute != 30) //10 50
				RemoveDuplicate(IndicatorQueue,3);
			if(time.hour >= 20 && time.hour <= 22) //10 30 50
				RemoveDuplicate(IndicatorQueue,3);
			if((time.hour == 23 || time.hour == 19) && time.minute == 10) //19:10 & 23:10
				RemoveDuplicate(IndicatorQueue,3);
			if((time.hour == 12 && time.minute == 30) || ((time.hour >= 14 && time.hour <= 19) && (time.minute == 00 || time.minute == 30))) //00 30
				RemoveDuplicate(IndicatorQueue,6); //down
		}
		
		//Weekday 23:25
		if((time.weekDay < 6)&&(time.hour == 23)&&(time.minute == 24)) 
			RemoveDuplicate(IndicatorQueue,0);
		
		//Weekday before 09:00
		if((time.weekDay < 6)&&((time.hour == 7 && time.minute == 45)||(time.hour == 8 && time.minute == 15)||(time.hour == 9 && time.minute == 45)))
			RemoveDuplicate(IndicatorQueue,1); //up
		
		if((time.weekDay < 6)&&((time.hour == 7 && time.minute == 30)||(time.hour == 8 && time.minute == 00)||(time.hour == 8 && time.minute == 30)))
			RemoveDuplicate(IndicatorQueue,5); //down
		
		//Weekday 00 15 30 45
		if((time.weekDay < 6)&&(time.hour >= 9 && time.hour <= 17)&&(time.minute == 00 || time.minute == 15 || time.minute == 30 || time.minute == 45)){
			RemoveDuplicate(IndicatorQueue,1); //up
			RemoveDuplicate(IndicatorQueue,5); //down
		}
		
		//Weekday 00 20 40
		if((time.weekDay < 6)&&(time.minute == 00 || time.minute == 20 || time.minute == 40)){
			if(time.hour >= 9 && time.hour <= 17)
				RemoveDuplicate(IndicatorQueue,2);
			if(time.hour >= 18 && time.hour <= 22){
				RemoveDuplicate(IndicatorQueue,3); //up
				RemoveDuplicate(IndicatorQueue,6); // down
			}
			if(time.hour == 23 && time.minute == 00){ //23:00
				RemoveDuplicate(IndicatorQueue,3); //up
				RemoveDuplicate(IndicatorQueue,7); //down
			}
		}
		
		//Meet-class CC
		//Weekday every 20 => 00 20 40
		if((time.weekDay < 6)&&(time.minute == 00 || time.minute == 20 || time.minute == 40)){
			if(time.hour >= 9 && time.hour <= 16) //00 20 40
				RemoveDuplicate(IndicatorQueue,4);
			if(time.hour == 17 && (time.minute == 00 || time.minute == 20)) //17:00 & 17:20
				RemoveDuplicate(IndicatorQueue,4);
		}
		
		//Weekday 18
		if((time.weekDay < 6)&&(time.hour >= 9 && time.hour <= 17)&&(time.minute == 18))
			RemoveDuplicate(IndicatorQueue,8); //down
		
		//Weekday 10 18
		if((time.weekDay < 6)&&(time.hour >= 9 && time.hour <= 17)&&(time.minute == 18 || time.minute == 10))
			RemoveDuplicate(IndicatorQueue,9); //down
		
		//Additional
		//Weekday every 5 => 00 05 10 15 20 25 30 35 40 40 50 55
		if((time.weekDay < 6)&&((time.hour == 7 && time.minute == 45)||(time.hour == 8 && (time.minute == 00 || time.minute == 05 || time.minute == 10 || time.minute == 15 || time.minute == 20 || time.minute == 25 || time.minute == 30 || time.minute == 35 ||time.minute == 40 || time.minute == 45 || time.minute == 50 || time.minute == 55))||(time.hour == 9 && time.minute == 00))){
			RemoveDuplicate(IndicatorQueue,10);
			RemoveDuplicate(IndicatorQueue,11);
		}
		
		//Circular, no sat
		//Weekday 10 25 40
		if((time.weekDay < 5)&&(time.hour >= 9 && time.hour <= 18)&&(time.minute == 10 || time.minute == 25 || time.minute == 40))
			RemoveDuplicate(IndicatorQueue,12); // circular
		//nothing matched
		//!!!!?!?!?
		return IndicatorQueue;
	}
	private static LinkedList<Integer> RemoveDuplicate(
			LinkedList<Integer> IndicatorQueue, int indicator) {
		if(!IndicatorQueue.contains(indicator))
			IndicatorQueue.add(indicator);
		return IndicatorQueue;
	}
	private static LinkedList<Poi> Routing(int indicator, Time time){
		LinkedList<Poi> PredictedRoute = new LinkedList<Poi>();
		Poi stop = null;
		Hashtable<String, Poi> hashtable = Data.getPois();
	
		//maybe too exact?
		if(indicator == 0)/*Mon-Sat; 23:25; Sun*/{
			//Predicted Route Weekday: 2300up; Sunday: up00
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
			stop = hashtable.get("Jockey Club Post-Graduate Hall"); //dun know exact time???
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			if(time.weekDay == 6){
			stop = hashtable.get("Shaw College"); //eliminate in Weekday: 2300up
			PredictedRoute.add(stop);
			}
			stop = hashtable.get("United College Staff Residence");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.15");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.10 and 11");
			PredictedRoute.add(stop);
		}	
		
		if(indicator == 1){
			//Predicted Route Weekday: b40900up, normalup
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
		}

		if(indicator == 2){
			//Predicted Route Weekday: Shaw up
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.10 and 11");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.15");
			PredictedRoute.add(stop);
			stop = hashtable.get("United College Staff Residence");
			PredictedRoute.add(stop);
			stop = hashtable.get("Chan Chun Ha Hostel");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
		}
		
		if(indicator == 3){
			//Predicted Route Weekday: after1800up; Sunday: up01
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
		}
		
		if(indicator == 4){
			//Predicted Route Weekday: Meet-class up
			stop = hashtable.get("Chung Chi Teaching Blocks");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
		}	
			
			
			
		if(indicator == 5){	
			//Predicted Route Weekday: b40900down, normaldown
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
		}
		
		if(indicator == 6){	
			//Predicted Route Weekday: after1800down; Sunday: down01
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
		}
		
		if(indicator == 7){
			//Predicted Route Weekday: after 2300down; Sunday: down00
			stop = hashtable.get("Residences No.10 and 11");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.15");
			PredictedRoute.add(stop);
			stop = hashtable.get("United College Staff Residence");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Jockey Club Post-Graduate Hall"); //eliminate in Sunday: down00
			PredictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
		}
			
		if(indicator == 8){
			//Predicted Route Weekday: Meet-class down NA
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Chung Chi Teaching Blocks");
			PredictedRoute.add(stop);
		}	
		
		if(indicator == 9){
			//Predicted Route Weekday: Meet-class down shaw
			stop = hashtable.get("Shaw College");//shaw
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");//shaw
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Chung Chi Teaching Blocks");
			PredictedRoute.add(stop);
		}	
			
			
			
		if(indicator == 10){	
			//Predicted Route Weekday: Additional first
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			PredictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			PredictedRoute.add(stop);
		}
		
		if(indicator == 11){
			//Predicted Route Weekday: Additional Second  5 min each!!!!!?????!!!!!????
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
		}
		
		if(indicator == 12){
			//Predicted Route Weekday0-4: Circular, not on sat
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			PredictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			PredictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			PredictedRoute.add(stop);
			stop = hashtable.get("United college");
			PredictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			PredictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			PredictedRoute.add(stop);
		}
		return PredictedRoute;
	
	}
	private static LinkedList<Integer> MapRoutingWithDirection(LinkedList<Integer> TmpQueue, DirectionIndicatior direction){
		int indicator;
		LinkedList<Integer> IndicatorQueue = new LinkedList<Integer>();
		Iterator<Integer> itr = TmpQueue.iterator();
		while(itr.hasNext()){
			indicator = itr.next();
			if((direction == DirectionIndicatior.UP)&&(indicator <= 4 || indicator == 10 || indicator == 11))
				IndicatorQueue.add(indicator);
			else if((direction == DirectionIndicatior.DOWN)&&(((indicator >= 5)||(indicator <= 9))||indicator == 12))
				IndicatorQueue.add(indicator);
		}
		
		return IndicatorQueue;
		}
}
