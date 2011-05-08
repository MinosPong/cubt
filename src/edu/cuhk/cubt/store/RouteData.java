package edu.cuhk.cubt.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.Route.OperationType;

public class RouteData {
	

	//Morning M
	public static final String RM_MTR_NA  = "Morning: MTR > NA ";
	public static final String RM_NA_MTR  = "Morning: NA > MTR";
	public static final String RM_MTR_SRR = "Morning: MTR > SRR";
	public static final String RM_SRR_SHAW = "Morning: SRR > SHAW";
	
	//Day D
	public static final String RD_MTR_NA  = "Day: MTR > NA"; //*****NEW ADD, SAME AS RM_MTR_NA
	public static final String RD_NA_MTR  = "Day: NA College > MTR"; //*****NEW ADD, SAME AS RM_NA_MTR
	public static final String RD_SHAW_C  = "Day: MTR > Shaw > MTR";
	
	//Evening E
	public static final String RE_SHAW_MTR  = "Evening: SHAW > MTR";
	public static final String RE_R11_MTR  = "Evening: R11 > MTR";
	public static final String RE_MTR_SHAW  = "Evening: University Station > Shaw College";
	public static final String RE_MTR_R11  = "Evening: University Station > University Residence Nos. 10-11";

	//Meet-Class C
	public static final String RC_NA_CC  = "Meet-Class: NA > CC";
	public static final String RC_SHAW_CC  = "Meet-Class: SHAW > CC";
	public static final String RC_SHAW_ADM = "Meet-Class: SHAW > ADM > SHAW";
	public static final String RC_CC_SHAW = "Meet-Class: CC > UC > NA > SHAW"; //*****NEW ADD
	
	
	//Holiday H
	public static final String RH_MTR_SHAW  = "Holiday: MTR > SHAW";
	public static final String RH_SHAW_MTR = "Holiday: SHAW > MTR";	//*****NEW ADD, SAME AS RE_SHAW_MTR
	public static final String RH_MTR_R11 = "Holiday: MTR > R11";
	public static final String RH_R11_MTR = "Holiday: R11 > MTR";  //*****NEW ADD
	
	public static final String ROUTE_13 = "Route 13";
	public static final String ROUTE_14 = "Route 14";
	public static final String ROUTE_15 = "Route 15";
	public static final String ROUTE_16 = "Route 16";
	
	
	private static Map<String,Route> routes = null;
	
	
	public static Map<String,Route> getRoutes(){
		if(routes == null){
			routes = new TreeMap<String, Route>();
			

			//Morning M
			routesInitHelper(routes, RM_MTR_NA, 
					"Weekday 0730-1800: University Station > NA College", 
					Route.TYPE_MORNING , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS});

			routesInitHelper(routes, RM_NA_MTR, 
					"Weekday 0730-1800: NA College > University Station", 
					Route.TYPE_MORNING , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});

			routesInitHelper(routes, RM_MTR_SRR, 
					"Before 9:00 a.m.: University Station > Sir Run Run Shaw Hall > University Station", 
					Route.TYPE_MORNING , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});			

			routesInitHelper(routes, RM_SRR_SHAW, 
					"Before 9:00 a.m.: Y.C. Liang Hall > Shaw College > Y.C. Liang Hall ", 
					Route.TYPE_MORNING , 0, 0, new String[]{
					PoiData.STOP_SRR,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_ADM,
					PoiData.STOP_SRR});

			//Day D
			routesInitHelper(routes, RD_MTR_NA, 
					"Weekday 0730-1800: University Station > NA College", 
					Route.TYPE_DAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS});

			routesInitHelper(routes, RD_SHAW_C, 
					"Weekday 0900-1800: University Station > Shaw College > University Station", 
					Route.TYPE_DAY , 0, 0, new String[]{
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

			routesInitHelper(routes, RD_NA_MTR, 
					"Weekday 0730-1800: NA College > University Station", 
					Route.TYPE_DAY , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});
			

			//Evening E
			routesInitHelper(routes, RE_MTR_R11, 
					"Weekday 1800-2330: University Station > University Residence Nos. 10-11", 
					Route.TYPE_EVENING , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_PGH, //<<---suppose no??
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS,
					PoiData.STOP_R34,
					PoiData.STOP_RUC,
					PoiData.STOP_R15,
					PoiData.STOP_R11});
			
			routesInitHelper(routes, RE_MTR_SHAW, 
					"Weekday 1800-2330: University Station > Shaw College", 
					Route.TYPE_EVENING , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});

			routesInitHelper(routes, RE_SHAW_MTR, 
					"Weekday 1800-2330: Shaw College > University Station", 
					Route.TYPE_EVENING , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});

			routesInitHelper(routes, RE_R11_MTR, 
					"Weekday 2325: University Residence Nos. 10-11 > University Station", 
					Route.TYPE_EVENING , 0, 0, new String[]{
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
			

			//Meet-Class C
			routesInitHelper(routes, RC_NA_CC, 
					"Meet-class: NA College > Chung Chi Teaching Blocks", 
					Route.TYPE_MEETCLASS , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_CCS});

			routesInitHelper(routes, RC_SHAW_CC, 
					"Meet-class: Shaw College > Chung Chi Teaching Blocks ", 
					Route.TYPE_MEETCLASS , 0, 0, new String[]{
					PoiData.STOP_SCS,//shaw
					PoiData.STOP_R34,//shaw
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_CCS});

			routesInitHelper(routes, RC_SHAW_ADM, 
					"Meet-class: Shaw College > University Administration Building > Shaw College ", 
					Route.TYPE_MEETCLASS , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_ADM,
					PoiData.STOP_SRR,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});			

			routesInitHelper(routes, RC_CC_SHAW, 
					"Meet Class: CC > Shaw", 
					Route.TYPE_MEETCLASS , 0, 0, new String[]{
					PoiData.STOP_CCS,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});

			
			//Holiday H
			routesInitHelper(routes, RH_SHAW_MTR, 
					"Holiday: Shaw College > University Station", 
					Route.TYPE_HOLIDAY , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_MTR});

			routesInitHelper(routes, RH_R11_MTR, 
					"Holiday: University Residence Nos. 10-11 > University Station", 
					Route.TYPE_HOLIDAY , 0, 0, new String[]{
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
					PoiData.STOP_MTR});
			
			routesInitHelper(routes, RH_MTR_R11, 
					"Holiday: University Station > University Residence Nos. 10-11", 
					Route.TYPE_HOLIDAY , 0, 0, new String[]{
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

			routesInitHelper(routes, RH_MTR_SHAW, 
					"Holiday 0800-2330: University Station > Shaw College", 
					Route.TYPE_HOLIDAY , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS,
					PoiData.STOP_R34,
					PoiData.STOP_SCS});
			
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
			routesInitHelper(routes, ROUTE_13, 
					"", 
					Route.TYPE_UNDEFINED , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_PGH,
					PoiData.STOP_MTR});		
			
			routesInitHelper(routes, ROUTE_14, 
					"SCS -> NAS -> PGH -> MTR", 
					Route.TYPE_UNDEFINED , 0, 0, new String[]{
					PoiData.STOP_SCS,
					PoiData.STOP_R34,
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_PGH,
					PoiData.STOP_MTR});

			routesInitHelper(routes, ROUTE_15, 
					"NA College > PGH > University Station", 
					Route.TYPE_UNDEFINED , 0, 0, new String[]{
					PoiData.STOP_NAS,
					PoiData.STOP_UCS,
					PoiData.STOP_ADM,
					PoiData.STOP_P5H,
					PoiData.STOP_SPD,
					PoiData.STOP_PGH,
					PoiData.STOP_MTR});
			
			
			routesInitHelper(routes, ROUTE_16, 
					"University Station > PGH > NA College", 
					Route.TYPE_UNDEFINED , 0, 0, new String[]{
					PoiData.STOP_MTR,
					PoiData.STOP_PGH,
					PoiData.STOP_SPU,
					PoiData.STOP_SRR,
					PoiData.STOP_FKH,
					PoiData.STOP_UCS,
					PoiData.STOP_NAS});
			
			

			
		}			
		return routes;
	}
	

	public static Route getRouteByName(String routeName){
		 return getRoutes().get(routeName);
	}
	
	public static Collection<Route> getRoutesByOperationType(int type){		
		Collection<Route> results = new ArrayList<Route>();
		Iterator<Route> routes = getRoutes().values().iterator();		
		while(routes.hasNext()){
			Route route = routes.next();
			if(route.getOperationDay() == type){
				results.add(route);
			}
		}
		return results;
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
	private static void routesInitHelper(Map<String, Route> routes,
			String name, String description, int operationType, int startTime, int endTime, String[] poisName){
		
		//TODO Change the helper function to fit you and make it more easy to read and modify.
		//TODO Also change the constructor of Route such that you can fit your input
		//TODO poisInitHelper may help you understand more how to do it
		//more to know: if data come from database, helper function may help you to map it and create objects
		List<Poi> pois = new LinkedList<Poi>();
		for(String poiName : poisName){
			pois.add(PoiData.getByName(poiName));
		}

		Route route = new Route(name, description, operationType, null, null, pois);
		routes.put(name, route);


	}
}
