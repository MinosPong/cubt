package edu.cuhk.cubt.bus;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Data {

	/*   BEGIN POINT OF INTEREST INIT   */
	private static Hashtable<String, Poi> pois;
	
	private static final int TYPE_POI = 0;
	private static final int TYPE_STOP = 1;
	
	public static final String STOP_MTR = "Train Station";
	public static final String STOP_SPU = "University Sports Centre (Upward)";
	public static final String STOP_SPD = "University Sports Centre (Downward)";
	public static final String STOP_SRR = "Sir Run Run Hall";
	public static final String STOP_FKH = "Fung King Hey Building";
	public static final String STOP_UCS = "United college";
	public static final String STOP_NAS = "New Asia College";
	public static final String STOP_ADM = "University Administrative Building";
	public static final String STOP_P5H = "Pentecostal Mission Hall Complex";
	public static final String STOP_R34 = "Residences No.3 and 4";
	public static final String STOP_SCS = "Shaw College";
	public static final String STOP_R11 = "Residences No.10 and 11";
	public static final String STOP_R15 = "Residences No.15";
	public static final String STOP_RUC = "United College Staff Residence";
	public static final String STOP_CCH = "Chan Chun Ha Hostel";
	public static final String STOP_PGH = "Jockey Club Post-Graduate Hall";
	public static final String STOP_CCS = "Chung Chi Teaching Blocks";
	//checkpoints
	public static final String STOP_CJC = "Jockey Club Post-Graduate Hall Checkpoint";
	public static final String STOP_CCC = "Chung Chi Teaching Blocks Checkpoint";
	public static final String STOP_CAB = "University Administrative Building Checkpoint";
	public static final String STOP_CNA = "New Asia College Checkpoint";
	public static final String STOP_CSC = "Shaw College Checkpoint";

	/**
	 * Load and init all the POIs, return in a Hashtable
	 * @return the Hash List of all POIs
	 */
	
	public static Hashtable<String, Poi> getPois(){
		if(pois == null){	
			pois = new Hashtable<String, Poi>();
			
			poisInitHelper(pois, STOP_MTR, 22.414361, 114.210292, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_SPU, 22.417778, 114.210284, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_SPD, 22.417793, 114.211290, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_SRR, 22.419737, 114.206974, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_FKH, 22.419784, 114.203401, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_UCS, 22.420463, 114.205311, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_NAS, 22.421279, 114.207486, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_ADM, 22.418663, 114.205284, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_P5H, 22.418358, 114.209289, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_R34, 22.421604, 114.203136, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_SCS, 22.422397, 114.201395, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_R11, 22.425152, 114.207891, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_R15, 22.423766, 114.206700, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_RUC, 22.423364, 114.205308, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_CCH, 22.421966, 114.204946, 30, TYPE_STOP);
			poisInitHelper(pois, STOP_PGH, 22.420002, 114.212384, 40, TYPE_STOP);
			poisInitHelper(pois, STOP_CCS, 22.415306, 114.208428, 50, TYPE_STOP);
			//Checkpoints
			poisInitHelper(pois, STOP_CJC, 22.417852, 114.212770, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_CCC, 22.416107, 114.210815, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_CAB, 22.419779, 114.204453, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_CNA, 22.420037, 114.206287, 50, TYPE_STOP);
			poisInitHelper(pois, STOP_CSC, 22.421792, 114.203315, 50, TYPE_STOP);
		}
		return pois;
	}
	
	private static void poisInitHelper(Hashtable<String, Poi> pois, 
			String name, double latitude, double longitude, float range, int type){
		switch(type){
		case TYPE_POI:
			pois.put(name, new Poi(name, latitude, longitude, range));
			break;
		case TYPE_STOP:
			pois.put(name, new Stop(name, latitude, longitude, range));
			break;
		}
	}

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
			pois.add(Poi.getByName(poiName));
		}


	}
	/*   END ROUTE INIT   */	
	
	
}
