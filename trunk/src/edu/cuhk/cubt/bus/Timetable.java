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
		
	private enum directionIndicatior{
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
	/* NOT understand
	Please focus on:
		1. Initiate the Routes in class [Data], << unable to do
		2. Put all the Route information to class [Route] << unable to do
		3. Modify [Route], or add new feature to [Route] if necessary.
		4. In Timetable.DirectionIndicatior(), use Poi.getByLocation and Poi.isCovered for checking <<dunno if it is true
		5. Change return type of findRoutesByLocationTime to Collection<Route>, after finish item(1-2)
*/
	public static /*Collection<Route>*/Collection<LinkedList<Poi>> findRoutesByLocationTime(long millis, Location location){
		Time time = new Time();
		time.set(millis); //any usage?	

		directionIndicatior direction;
		
		LinkedList<Integer> indicatorQueue = new LinkedList<Integer>();
		LinkedList<Integer> tmpQueue = new LinkedList<Integer>();
		Iterator<Integer> itr = indicatorQueue.iterator();
		Collection<LinkedList<Poi>> possibleRoutes = new LinkedList<LinkedList<Poi>>();
		
		direction = checkDirection(location); //enter first station only, how about check 2 stations?
		tmpQueue = checkTime(time);
		indicatorQueue = mapRoutingWithDirection(tmpQueue, direction);
		if(!indicatorQueue.isEmpty()){
			while(itr.hasNext()){
				int indicator = itr.next();
				possibleRoutes.add(routing(indicator));
			}	
			return possibleRoutes;
		}
		else
			return null; //unknown route		
	}
	
	
	private static directionIndicatior checkDirection(Location location){
		//Like that??
		//Poi target = Poi.getByLocation(location);
		//if(target.isCoverd(location)) <= what should location be? if(target.isCoverd("Train Station")) NOT possible of cuz
			//return directionIndicatior.UP;
		if((location.getLatitude() == 22.414361) && (location.getLongitude() == 114.210292)) //Train Station
			return directionIndicatior.UP;
		if((location.getLatitude() == 22.415306) && (location.getLongitude() == 114.208428)) //Chung Chi Teaching Blocks
			return directionIndicatior.UP;
		if((location.getLatitude() == 22.419737) && (location.getLongitude() == 114.206974)) //Sir Run Run Hall
			return directionIndicatior.UP;
		
		if((location.getLatitude() == 22.421279) && (location.getLongitude() == 114.207486)) //New Asia College
			return directionIndicatior.DOWN;
		if((location.getLatitude() == 22.422397) && (location.getLongitude() == 114.201395)) //Shaw College
			return directionIndicatior.DOWN;
		if((location.getLatitude() == 22.425152) && (location.getLongitude() == 114.207891)) //Residences No.10 and 11
			return directionIndicatior.DOWN;
		return directionIndicatior.UNDEFINED;
	}
	
	private static LinkedList<Integer> checkTime(Time time){ //already findRoutesByLocationTime, Extract all possible routes in particular time, neglect the direction
		
		LinkedList<Integer> indicatorQueue= new LinkedList<Integer>();
		
		//train station
		//Sunday 00 20 40
		if((time.weekDay == Time.SUNDAY)&&(time.minute == 00 || time.minute == 20 || time.minute == 40)){ 
			if((time.hour == 8 && time.minute == 40)||(time.hour == 9 && time.minute == 20)){ //08:40 or 09:20
				indicatorQueue = removeDuplicate(indicatorQueue,0);
			}if(time.hour == 13 && time.minute != 00){ //20 40
				indicatorQueue = removeDuplicate(indicatorQueue,0);
			}
			if(time.hour == 12 && time.minute != 20){ //00 40
				indicatorQueue = removeDuplicate(indicatorQueue,0);
			}
			if(time.hour == 13) //00 20 40
				indicatorQueue = removeDuplicate(indicatorQueue,7); //down
			if((time.hour == 10)||(time.hour == 11)||(time.hour >= 14 && time.hour <= 22)){ //00 20 40
				indicatorQueue = removeDuplicate(indicatorQueue,0); //up
				indicatorQueue = removeDuplicate(indicatorQueue,7); //down
			}
			if(time.hour == 23 && time.minute != 40){ //00 20
				indicatorQueue = removeDuplicate(indicatorQueue,0);
			}
		}
		
		//Sunday 10 30 50
		if((time.weekDay == Time.SUNDAY)&&(time.minute == 10 || time.minute == 30 || time.minute == 50)){
			if((time.hour == 11 || time.hour == 12 || (time.hour >= 14 && time.hour <=18)) && time.minute != 30) //10 50
				indicatorQueue = removeDuplicate(indicatorQueue,3);
			if(time.hour >= 20 && time.hour <= 22) //10 30 50
				indicatorQueue = removeDuplicate(indicatorQueue,3);
			if((time.hour == 23 || time.hour == 19) && time.minute == 10) //19:10 & 23:10
				indicatorQueue = removeDuplicate(indicatorQueue,3);
			if((time.hour == 12 && time.minute == 30) || ((time.hour >= 14 && time.hour <= 19) && (time.minute == 00 || time.minute == 30))) //00 30
				indicatorQueue = removeDuplicate(indicatorQueue,6); //down
		}
		
		//Weekday 23:25
		if((time.weekDay > 0)&&(time.hour == 23)&&(time.minute == 24)) 
			indicatorQueue = removeDuplicate(indicatorQueue,17);
		
		//Weekday before 09:00
		if((time.weekDay > 0)&&((time.hour == 7 && time.minute == 45)||(time.hour == 8 && time.minute == 15)||(time.hour == 9 && time.minute == 45)))
			indicatorQueue = removeDuplicate(indicatorQueue,1); //up
		
		if((time.weekDay > 0)&&((time.hour == 7 && time.minute == 30)||(time.hour == 8 && time.minute == 00)||(time.hour == 8 && time.minute == 30)))
			indicatorQueue = removeDuplicate(indicatorQueue,5); //down
		
		if((time.weekDay > 0)&&(time.hour == 8)&&(time.minute == 10))
			indicatorQueue = removeDuplicate(indicatorQueue,13); //SP1
		
		//Weekday 00 15 30 45
		if((time.weekDay > 0)&&(time.hour >= 9 && time.hour <= 17)&&(time.minute == 00 || time.minute == 15 || time.minute == 30 || time.minute == 45)){
			if(time.minute != 00)
				indicatorQueue = removeDuplicate(indicatorQueue,1); //up
			if(time.minute == 00)
				indicatorQueue = removeDuplicate(indicatorQueue,16); //up SP2
			if(time.minute != 30)
				indicatorQueue = removeDuplicate(indicatorQueue,5); //down
			if(time.minute == 30)
				indicatorQueue = removeDuplicate(indicatorQueue,15); //down SP2
		}
		
		//Weekday 00 20 40
		if((time.weekDay > 0)&&(time.minute == 00 || time.minute == 20 || time.minute == 40)){
			if(time.hour >= 9 && time.hour <= 17)
				indicatorQueue = removeDuplicate(indicatorQueue,2);
			if(time.hour >= 18 && time.hour <= 22)
				indicatorQueue = removeDuplicate(indicatorQueue,3); //up
			if(time.hour >= 18)
				indicatorQueue = removeDuplicate(indicatorQueue,6); // down
			if(((time.hour >= 19) && (time.hour <= 22))&&(time.minute != 00))
				indicatorQueue = removeDuplicate(indicatorQueue,6); //down
			if(((time.hour >= 19) && (time.hour <= 22))&&(time.minute == 00))
				indicatorQueue = removeDuplicate(indicatorQueue,14); //down SP4
			if(time.hour == 23 && time.minute == 00){ //23:00
				indicatorQueue = removeDuplicate(indicatorQueue,3); //up
				indicatorQueue = removeDuplicate(indicatorQueue,7); //down
			}
		}
		
		//Meet-class CC
		//Weekday every 20 => 00 20 40
		if((time.weekDay > 0)&&(time.minute == 00 || time.minute == 20 || time.minute == 40)){
			if(time.hour >= 9 && time.hour <= 16) //00 20 40
				indicatorQueue = removeDuplicate(indicatorQueue,4);
			if(time.hour == 17 && (time.minute == 00 || time.minute == 20)) //17:00 & 17:20
				indicatorQueue = removeDuplicate(indicatorQueue,4);
		}
		
		//Weekday 18
		if((time.weekDay > 0)&&(time.hour >= 9 && time.hour <= 17)&&(time.minute == 18))
			indicatorQueue = removeDuplicate(indicatorQueue,8); //down
		
		//Weekday 10 18
		if((time.weekDay > 0)&&(time.hour >= 9 && time.hour <= 17)&&(time.minute == 18 || time.minute == 10))
			indicatorQueue = removeDuplicate(indicatorQueue,9); //down
		
		//Additional
		//Weekday every 5 => 00 05 10 15 20 25 30 35 40 40 50 55
		if((time.weekDay > 0)&&((time.hour == 7 && time.minute == 45)||(time.hour == 8 && (time.minute == 00 || time.minute == 05 || time.minute == 10 || time.minute == 15 || time.minute == 20 || time.minute == 25 || time.minute == 30 || time.minute == 35 ||time.minute == 40 || time.minute == 45 || time.minute == 50 || time.minute == 55))||(time.hour == 9 && time.minute == 00))){
			indicatorQueue = removeDuplicate(indicatorQueue,10);
			indicatorQueue = removeDuplicate(indicatorQueue,11);
		}
		
		//Circular, no sat
		//Weekday 10 25 40
		if(((time.weekDay > 0)&&(time.weekDay < 5))&&(time.hour >= 9 && time.hour <= 18)&&(time.minute == 10 || time.minute == 25 || time.minute == 40))
			indicatorQueue = removeDuplicate(indicatorQueue,12); // circular
		//nothing matched
		//!!!!?!?!?
		return indicatorQueue;
	}
	private static LinkedList<Integer> removeDuplicate(
			LinkedList<Integer> indicatorQueue, int indicator) {
		if(!indicatorQueue.contains(indicator))
			indicatorQueue.add(indicator);
		return indicatorQueue;
	}
	private static LinkedList<Poi> routing(int indicator){
		LinkedList<Poi> predictedRoute = new LinkedList<Poi>();
		Poi stop = null;
		Hashtable<String, Poi> hashtable = Data.getPois();
	
		//maybe too exact?
		if((indicator == 0)||(indicator == 17))/*Mon-Sat; 23:25; Sun*/{
			//Predicted Route Weekday: 2300up; Sunday: up00
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
			stop = hashtable.get("Jockey Club Post-Graduate Hall"); //dun know exact time???
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			if(indicator == 0){
			stop = hashtable.get("Shaw College"); //eliminate in Weekday: 2300up
			predictedRoute.add(stop);
			}
			stop = hashtable.get("United College Staff Residence");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.15");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.10 and 11");
			predictedRoute.add(stop);
		}	
		
		if((indicator == 1)||(indicator ==16)){
			//Predicted Route Weekday: b40900up, normalup
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
			if(indicator == 16){
			stop = hashtable.get("Jockey Club Post-Graduate Hall");
			predictedRoute.add(stop);
			}
			stop = hashtable.get("University Sports Centre (Upward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
		}

		if(indicator == 2){
			//Predicted Route Weekday: Shaw up
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.10 and 11");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.15");
			predictedRoute.add(stop);
			stop = hashtable.get("United College Staff Residence");
			predictedRoute.add(stop);
			stop = hashtable.get("Chan Chun Ha Hostel");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
		}
		
		if(indicator == 3){
			//Predicted Route Weekday: after1800up; Sunday: up01
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
		}
		
		if(indicator == 4){
			//Predicted Route Weekday: Meet-class up
			stop = hashtable.get("Chung Chi Teaching Blocks");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("Fung King Hey Building");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
		}	
			
			
			
		if((indicator == 5)||(indicator == 15)){	
			//Predicted Route Weekday: b40900down, normaldown w.SP2
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			if(indicator == 15){
				stop = hashtable.get("Jockey Club Post-Graduate Hall"); //SP2
				predictedRoute.add(stop);
			}
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
		}
		
		if(indicator == 6){	
			//Predicted Route Weekday: after1800down; Sunday: down01
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
		}
		
		if(indicator == 7){
			//Predicted Route Weekday: after 2300down; Sunday: down00
			stop = hashtable.get("Residences No.10 and 11");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.15");
			predictedRoute.add(stop);
			stop = hashtable.get("United College Staff Residence");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Jockey Club Post-Graduate Hall"); //eliminate in Sunday: down00
			predictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
		}
			
		if(indicator == 8){
			//Predicted Route Weekday: Meet-class down NA
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Chung Chi Teaching Blocks");
			predictedRoute.add(stop);
		}	
		
		if(indicator == 9){
			//Predicted Route Weekday: Meet-class down shaw
			stop = hashtable.get("Shaw College");//shaw
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");//shaw
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Chung Chi Teaching Blocks");
			predictedRoute.add(stop);
		}	
			
			
			
		if(indicator == 10){	
			//Predicted Route Weekday: Additional first
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Upward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
		}
		
		if(indicator == 11){
			//Predicted Route Weekday: Additional Second  5 min each!!!!!?????!!!!!????
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
		}
		
		if(indicator == 12){
			//Predicted Route Weekday0-4: Circular, not on sat
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Sir Run Run Hall");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
		}
		
		if((indicator == 13)||(indicator == 14)){	
			//Predicted Route Weekday: after1800down SP4 =>14; b40900down SP1 =>13
			stop = hashtable.get("Shaw College");
			predictedRoute.add(stop);
			stop = hashtable.get("Residences No.3 and 4 ");
			predictedRoute.add(stop);
			stop = hashtable.get("New Asia College ");
			predictedRoute.add(stop);
			stop = hashtable.get("United college");
			predictedRoute.add(stop);
			stop = hashtable.get("University Administrative Building");
			predictedRoute.add(stop);
			stop = hashtable.get("Pentecostal Mission Hall Complex");
			predictedRoute.add(stop);
			stop = hashtable.get("University Sports Centre (Downward)");
			predictedRoute.add(stop);
			if(indicator == 13){
				stop = hashtable.get("Chung Chi Teaching Blocks");
				predictedRoute.add(stop);
			}
			if(indicator == 14){
				stop = hashtable.get("Jockey Club Post-Graduate Hall");
				predictedRoute.add(stop);
			}			
			stop = hashtable.get("Train Station");
			predictedRoute.add(stop);
		}
		return predictedRoute;
	
	}
	private static LinkedList<Integer> mapRoutingWithDirection(LinkedList<Integer> tmpQueue, directionIndicatior direction){
		int indicator;
		LinkedList<Integer> indicatorQueue = new LinkedList<Integer>();
		Iterator<Integer> itr = tmpQueue.iterator();
		while(itr.hasNext()){
			indicator = itr.next();
			if((direction == directionIndicatior.UP)&&(indicator <= 4 || indicator == 10 || indicator == 11))
				indicatorQueue.add(indicator);
			else if((direction == directionIndicatior.DOWN)&&(((indicator >= 5)||(indicator <= 9))||indicator == 12))
				indicatorQueue.add(indicator);
		}
		
		return indicatorQueue;
		}
}
