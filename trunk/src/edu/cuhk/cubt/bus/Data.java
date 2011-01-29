package edu.cuhk.cubt.bus;

import java.util.Hashtable;

public class Data {

	/*   BEGIN POINT OF INTEREST INIT   */
	private static Hashtable<String, Poi> pois;
	
	private static final int TYPE_POI = 0;
	private static final int TYPE_STOP = 1;
	
	public static Hashtable<String, Poi> getPois(){
		if(pois == null){		
			poisInitHelper(pois,"Train Station",22.414361, 114.210292,50,TYPE_STOP);
			poisInitHelper(pois,"University Sports Centre (Upward)",22.417778, 114.210284,30,TYPE_STOP);
			poisInitHelper(pois,"University Sports Centre (Downward)",22.417793, 114.211290,30,TYPE_STOP);
			poisInitHelper(pois,"Sir Run Run Hall",22.419737, 114.206974,50,TYPE_STOP);
			poisInitHelper(pois,"Fung King Hey Building",22.419784, 114.203401,50,TYPE_STOP);
			poisInitHelper(pois,"United college",22.420463, 114.205311,50,TYPE_STOP);
			poisInitHelper(pois,"New Asia College ",22.421279, 114.207486,50,TYPE_STOP);
			poisInitHelper(pois,"University Administrative Building",22.418663,114.205284,50,TYPE_STOP);
			poisInitHelper(pois,"Pentecostal Mission Hall Complex",22.418358,114.209289,30,TYPE_STOP);
			poisInitHelper(pois,"Residences No.3 and 4 ",22.421604, 114.203136,30,TYPE_STOP);
			poisInitHelper(pois,"Shaw College",22.422397, 114.201395,50,TYPE_STOP);
			poisInitHelper(pois,"Residences No.10 and 11",22.425152, 114.207891,30,TYPE_STOP);
			poisInitHelper(pois,"Residences No.15",22.423766, 114.206700,30,TYPE_STOP);
			poisInitHelper(pois,"United College Staff Residence",22.423364, 114.205308,30,TYPE_STOP);
			poisInitHelper(pois,"Chan Chun Ha Hostel",22.421966, 114.204946,30,TYPE_STOP);
			poisInitHelper(pois,"Jockey Club Post-Graduate Hall",22.420002, 114.212384,40,TYPE_STOP);
			poisInitHelper(pois,"Chung Chi Teaching Blocks",22.415306, 114.208428,50,TYPE_STOP);
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
	
	
	
	
	
	
	
	
	
}
