package edu.cuhk.cubt.bus;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import edu.cuhk.cubt.store.PoiData;

public class Data {

	/*   BEGIN POINT OF INTEREST INIT   */
	
	/*   END POINT OF INTEREST INIT   */
	

	/*   BEGIN ROUTE INIT   */	
	
	/**
	 * defines the Route ID number here
	 * Reuse the key(variable name) instead of integer for identify
	 */
	public static final int ROUTE_UNKNOWN = 0;
	
	private static Hashtable<Integer, Route> routes;
	
	public static Hashtable<Integer, Route> getRoutes(){
		if(routes == null){
			routes = new Hashtable<Integer, Route>();
			
		}
		return routes;
	}
	
	/**
	 * Change the helper function to fit you and make it more easy to read and modify.
	 * Also change the constructor of Route such that you can fit your input
	 * @param routes
	 * @param id
	 * @param name
	 * @param operatingDays Change and make it to a format that you can easily used.
	 * @param startTime
	 * @param endTime
	 * @param pois
	 * 
	 * @author Kayln
	 */
	private static void routesInitHelper(Hashtable<Integer, Poi> routes,
			int id, String name, int[] operatingDays, int startTime, int endTime, String[] poisName){
		
		//TODO Change the helper function to fit you and make it more easy to read and modify.
		//TODO Also change the constructor of Route such that you can fit your input
		//TODO poisInitHelper may help you understand more how to do it
		//more to know: if data come from database, helper function may help you to map it and create objects
		
		List<Poi> pois = new LinkedList<Poi>();
		for(String poiName : poisName){
			pois.add(PoiData.getByName(poiName));
		}


	}
	/*   END ROUTE INIT   */	
	
	
}
