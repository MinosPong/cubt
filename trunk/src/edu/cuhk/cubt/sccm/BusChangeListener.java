package edu.cuhk.cubt.sccm;

import edu.cuhk.cubt.bus.Stop;
import android.location.Location;

public interface BusChangeListener {

	public void busLocationChange(Location location);
	
	public void busEnterEvent(Stop busStop);
	
	public void busExitEvent(Stop busStop);
	
}
