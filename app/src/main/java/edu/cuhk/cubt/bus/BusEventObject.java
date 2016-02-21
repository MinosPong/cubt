package edu.cuhk.cubt.bus;


public class BusEventObject{
	
	public static final int BUS_ENTER_EVENT = 10701;
	public static final int BUS_EXIT_EVENT = 10702;
	public static final int BUS_STOP_PASSED_EVENT = 10703;
	
	
	public BusEventObject(int event, Stop stop, long enterTime, long leaveTime){
		this.event = event;
		this.stop = stop;
		this.enterTime = enterTime;
		this.leaveTime = leaveTime;
	}
	private int event;
	private Stop stop;
	private long enterTime;
	private long leaveTime;
	
	public int getEvent() {		return event; }
	public Stop getStop() {		return stop; }
	public long getEnterTime() {return enterTime;}
	public long getLeaveTime() {return leaveTime;}
	public long getPeriod()	{	return leaveTime - enterTime;}
}
