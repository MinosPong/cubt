package edu.cuhk.cubt.store;

import edu.cuhk.cubt.bus.Poi;

public class PoiHistory extends CircularBuffer<Poi>{

	public static final int DEFAULT_POI_HISTORY_SIZE_LIMIT = 20;	//buffer size
	
	public PoiHistory(){
		this(DEFAULT_POI_HISTORY_SIZE_LIMIT);
	}
	
	public PoiHistory(int bufferSize) {
		super(bufferSize);
	}

}
