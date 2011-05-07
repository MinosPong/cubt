package edu.cuhk.cubt.bus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.cuhk.cubt.store.RouteData;

public class RoutePrediction {

	
	public static List<Route> getRoutesByPassedStop(List<Stop> input){
		List<Route> results = new ArrayList<Route>();		
		
		Iterator<Route> routes = RouteData.getRoutes().values().iterator();
		
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
