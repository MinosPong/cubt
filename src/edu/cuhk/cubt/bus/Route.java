package edu.cuhk.cubt.bus;

import java.util.ArrayList;
import java.util.List;

import android.text.format.Time;

public class Route {
	Time start;
	Time end;
	int operationDay;
	
	List<Stop> stops = new ArrayList<Stop>();
	
}
