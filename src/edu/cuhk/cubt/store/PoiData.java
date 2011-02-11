package edu.cuhk.cubt.store;

import java.util.Hashtable;
import java.util.Iterator;

import android.location.Location;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Stop;

public class PoiData {


	/*   BEGIN POINT OF INTEREST INIT   */

	private static Hashtable<String, Poi> pois;

	public static final int TYPE_POI = 0;
	public static final int TYPE_STOP = 1;
	public static final int TYPE_CHECKPOINT = 2;
	
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
	public static final String CHECKPOINT_CJC = "Jockey Club Post-Graduate Hall Checkpoint";
	public static final String CHECKPOINT_CCC = "Chung Chi Teaching Blocks Checkpoint";
	public static final String CHECKPOINT_CAB = "University Administrative Building Checkpoint";
	public static final String CHECKPOINT_CNA = "New Asia College Checkpoint";
	public static final String CHECKPOINT_CSC = "Shaw College Checkpoint";

	public static void poisInitHelper(Hashtable<String, Poi> pois, 
			String name, double latitude, double longitude, float range, int type){
		switch(type){
		case PoiData.TYPE_POI:
			pois.put(name, new Poi(name, latitude, longitude, range));
			break;
		case PoiData.TYPE_STOP:
			pois.put(name, new Stop(name, latitude, longitude, range));
			break;
		case TYPE_CHECKPOINT:
			pois.put(name, new Stop(name, latitude, longitude, range));
			break;
		}
	}

	
	
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
			poisInitHelper(pois, CHECKPOINT_CJC, 22.417852, 114.212770, 50, TYPE_CHECKPOINT);
			poisInitHelper(pois, CHECKPOINT_CCC, 22.416107, 114.210815, 50, TYPE_CHECKPOINT);
			poisInitHelper(pois, CHECKPOINT_CAB, 22.419779, 114.204453, 50, TYPE_CHECKPOINT);
			poisInitHelper(pois, CHECKPOINT_CNA, 22.420037, 114.206287, 50, TYPE_CHECKPOINT);
			poisInitHelper(pois, CHECKPOINT_CSC, 22.421792, 114.203315, 50, TYPE_CHECKPOINT);
		}
		return pois;
	}



	public static Stop getStopByLocation(Location dest){
		//TODO
		Poi poi = PoiData.getPoiByLocation(dest);
		if(poi instanceof Stop)
			return (Stop)poi;
		return null;
	}



	/**
	 * Returns the Poi that represent the input Location dest,
	 * return null if no Poi matched
	 * @param dest the Location to be check
	 * @return the Poi that represent the input Location dest,
	 * return null if no Poi matched
	 */
	public static Poi getPoiByLocation(Location dest){
		//TODO
		Iterator<Poi> pois = getPois().values().iterator();
		
		while(pois.hasNext()){
			Poi poi = pois.next();
			if(poi.isCovered(dest))
				return poi;
		}
		return null;
	}



	/**
	 * Returns the Poi that represent the input name,
	 * return null if no Poi matched
	 * @param name the name of the Poi
	 * @return the Poi that represent the input name,
	 * return null if no Poi matched
	 */
	public static Poi getByName(String name){
		return getPois().get(name);
	}
	


}
