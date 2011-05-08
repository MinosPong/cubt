package edu.cuhk.cubt.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.text.format.Time;

import edu.cuhk.cubt.bus.Route.OperationDay;
import edu.cuhk.cubt.store.RouteData;

public class RoutePrediction {

	
	public static List<Route> getRoutesByPassedStop(long mtime, List<Stop> input){
		Time time = new Time();
		time.set(mtime);
		Collection<Route> routes = new ArrayList<Route>();
		if(time.weekDay == Time.SUNDAY){
			routes.addAll(RouteData.getRoutesByOperationType(OperationDay.HOLIDAY));
		}else if(time.hour < 9){
			routes.addAll(RouteData.getRoutesByOperationType(OperationDay.MORNING));			
		}else if(time.hour >= 18){
			routes.addAll(RouteData.getRoutesByOperationType(OperationDay.EVENING));				
		}else{
			routes.addAll(RouteData.getRoutesByOperationType(OperationDay.DAY));	
			routes.addAll(RouteData.getRoutesByOperationType(OperationDay.MEETCLASS));	
		}
		return getPossibleRoute(routes.iterator(), input);	
	}
	
	
	public static List<Route> getRoutesByPassedStop(List<Stop> input){
		return getPossibleRoute(RouteData.getRoutes().values().iterator(), input);		
	}	
	
	private static List<Route> getPossibleRoute(Iterator<Route> routes, List<Stop> input){
		List<Route> results = new ArrayList<Route>();		
		
		while(routes.hasNext()){
			Route route = routes.next();
			if(route.isMatch(input.iterator())){
				results.add(route);
			}
		}
		return results;
	}
	
	public static List<Stop> getPossibleNextStop(Stop stop){		
		return getPossibleNextStop(RouteData.getRoutes().values().iterator(), stop);	
	}
	
	/**
	 * 
	 * @param routes The list of routes
	 * @param stop The stop before the return stop
	 * @return
	 */
	private static List<Stop> getPossibleNextStop(Iterator<Route> routes, Stop stop){
		List<Stop> results = new ArrayList<Stop>();
		
		while(routes.hasNext()){
			Route route = routes.next();
			Stop next = route.getNextStop(stop);
			if(next != null && !results.contains(next)){
				results.add(next);
			}
		}
				
		return results;
	}
	
	
}
