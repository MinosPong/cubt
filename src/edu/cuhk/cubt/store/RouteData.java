package edu.cuhk.cubt.store;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.Timetable.OperationDay;

public class RouteData {
	

	public static final String ROUTE_0  = "Route 0";
	public static final String ROUTE_1  = "Route 1";
	public static final String ROUTE_2  = "Route 2";
	public static final String ROUTE_3  = "Route 3";
	public static final String ROUTE_4  = "Route 4";
	public static final String ROUTE_5  = "Route 5";
	public static final String ROUTE_6  = "Route 6";
	public static final String ROUTE_7  = "Route 7";
	public static final String ROUTE_8  = "Route 8";
	public static final String ROUTE_9  = "Route 9";
	public static final String ROUTE_10 = "Route 10";
	public static final String ROUTE_11 = "Route 11";
	public static final String ROUTE_12 = "Route 12";
	public static final String ROUTE_13 = "Route 13";
	public static final String ROUTE_14 = "Route 14";
	public static final String ROUTE_15 = "Route 15";
	public static final String ROUTE_16 = "Route 16";
	public static final String ROUTE_17 = "Route 17";
	
	
	private static Hashtable<String,Route> routes = null;
	
	
	public static Hashtable<String,Route> getRoutes(){
		if(routes == null){
			routes = new Hashtable<String, Route>();
			
			routesInitHelper(routes, ROUTE_0, 
					"Predicted Route Weekday: 2300up; Sunday: up00", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_PGH,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS,
					PoiData.STOP_RUC,
					PoiData.STOP_R15,
					PoiData.STOP_R11});

			routesInitHelper(routes, ROUTE_1, 
					"Route Weekday: b40900up", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS});

			routesInitHelper(routes, ROUTE_2, 
					"Weekday: Shaw up", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_SCS,
					PoiData.STOP_R11,
					PoiData.STOP_R15,
					PoiData.STOP_RUC,
					PoiData.STOP_CCH,
					PoiData.STOP_SCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});	


			routesInitHelper(routes, ROUTE_3, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});
			

			routesInitHelper(routes, ROUTE_4, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_CCS,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});


			routesInitHelper(routes, ROUTE_5, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});
			

			routesInitHelper(routes, ROUTE_6, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});
			
/*
			routesInitHelper(routes, ROUTE_6, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});
	*/		
			

			routesInitHelper(routes, ROUTE_7, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_R11,
					PoiData.STOP_R15,
					PoiData.STOP_RUC,
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_PGH, //eliminate in Sunday: down00
					PoiData.STOP_MTR});
			
			

			routesInitHelper(routes, ROUTE_8, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_CCS});
			

			routesInitHelper(routes, ROUTE_9, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,//shaw
					PoiData.STOP_R34,//shaw
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_CCS});
			
/*
			routesInitHelper(routes, ROUTE_9, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,//shaw
					PoiData.STOP_R34,//shaw
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_CCS});
*/			

			routesInitHelper(routes, ROUTE_10, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});
			

			routesInitHelper(routes, ROUTE_11, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SRR,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_ADM,
					PoiData.STOP_SRR});
			

			routesInitHelper(routes, ROUTE_12, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_ADM,
					PoiData.STOP_SRR,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});
			

			routesInitHelper(routes, ROUTE_13, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_CCS,
					PoiData.STOP_MTR,});
			
			
			routesInitHelper(routes, ROUTE_14, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_PGH,
					PoiData.STOP_MTR,});

			routesInitHelper(routes, ROUTE_15, 
					"Route Weekday: after1800up; Sunday: up01", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_PGH,
					PoiData.STOP_MTR});
			
			
			routesInitHelper(routes, ROUTE_16, 
					"Predicted Route Weekday: 2300up; Sunday: up00", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_PGH,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS});
			
			routesInitHelper(routes, ROUTE_17, 
					"Predicted Route Weekday: 2300up; Sunday: up00", 
					OperationDay.WEEKDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_PGH,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS,
					PoiData.STOP_R34,
					PoiData.STOP_RUC,
					PoiData.STOP_R15,
					PoiData.STOP_R11});
			
			
			
		}			
		return routes;
	}
	

	public static Route getRouteByName(String routeName){
		 return getRoutes().get(routeName);
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
	 */
	private static void routesInitHelper(Hashtable<String, Route> routes,
			String name, String description, OperationDay operationDay, int startTime, int endTime, String[] poisName){
		
		//TODO Change the helper function to fit you and make it more easy to read and modify.
		//TODO Also change the constructor of Route such that you can fit your input
		//TODO poisInitHelper may help you understand more how to do it
		//more to know: if data come from database, helper function may help you to map it and create objects
		List<Poi> pois = new LinkedList<Poi>();
		for(String poiName : poisName){
			pois.add(PoiData.getByName(poiName));
		}

		Route route = new Route(name, description, operationDay, null, null, pois);
		routes.put(name, route);


	}
}
