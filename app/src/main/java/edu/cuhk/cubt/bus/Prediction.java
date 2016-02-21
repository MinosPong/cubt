package edu.cuhk.cubt.bus;

import java.util.Collection;
import java.util.Date;

import edu.cuhk.cubt.store.PoiData;

import android.location.Location;
import android.text.format.Time;

//This piece of rubbish had never been tested

public class Prediction{

	Boolean cc = false;
	Boolean uab = false;
	Boolean na = false;
	Boolean fkh = false;
	Boolean r34 = false;
	Boolean uc = false;
	
	String currentPoi = null;
	String lastPoi = null;
	
	public void paramReset() //The method for resetting all the parameters in this class 
	{
		cc = false;
		uab = false;
		na = false;
		fkh = false;
		r34 = false;
		uc = false;
		currentPoi = null;
		lastPoi = null;	
	}
	
	public String theNextStop(long millis, Location location){ 	//The next stop method starts

		//get the current time in hour
		Time time = new Time();
		time.set(millis);
		int hour = time.hour;
		
		
		//get the past and current POI
		Poi poi = PoiData.getPoiByLocation(location);
		String getPoi = poi.getName();
		if (getPoi.equals(currentPoi)) 
		{
			lastPoi = currentPoi;
			currentPoi = getPoi;
		};
		
		//indicator for ever-passed algorithm
		if (getPoi=="Chung Chi Teaching Blocks"){cc=true;}
		if (getPoi=="University Administrative Building"){uab=true;}
		if (getPoi=="New Asia College"){na=true;}
		if (getPoi=="Fung King Hey Building"){fkh=true;}
		if (getPoi=="Residences No.3 and 4"){r34=true;}
		if (getPoi=="United college"){uc=true;}
		
		
        if ((hour<=18) && (time.weekDay>=1)) //before 1800, Monday to Saturday
		{
        	//Checkpoint has the highest priority
        	if (getPoi=="Jockey Club Post-Graduate Hall Checkpoint") {return "Jockey Club Post-Graduate Hall";}
        	else if (getPoi=="Chung Chi Teaching Blocks Checkpoint") {return "Chung Chi Teaching Blocks";}
        	else if (getPoi=="University Administrative Building Checkpoint") {return "University Administrative Building";}
        	else if (getPoi=="New Asia College Checkpoint") {return "New Asia College";}
        	else if (getPoi=="Shaw College Checkpoint") {return "Shaw College";}
        	//last stop algorithm
        	else if (lastPoi.equals("Residences No.15") && (currentPoi=="United College Staff Residence")){return "Chan Chun Ha Hostel";}
			else if (lastPoi.equals("Residences No.10 and 11") && currentPoi.equals("Residences No.15")){return "United College Staff Residence";}
			else if (lastPoi.equals(null) && currentPoi.equals("Shaw College")){return "Residences No.3 and 4";}
			else if (lastPoi.equals("Residences No.3 and 4") && currentPoi.equals("Shaw College")){return "Residences No.3 and 4";}
			else if (lastPoi.equals("Chan Chun Ha Hostel") && currentPoi.equals("Shaw College")){return "University Administrative Building";}
			else if (lastPoi.equals("United college") && currentPoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastPoi.equals("Shaw College") && currentPoi.equals("Residences No.3 and 4")){return "New Asia College";}
			else if (lastPoi.equals("New Asia College") && currentPoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastPoi.equals("United college") && currentPoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastPoi.equals("Sir Run Run Hall") && currentPoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastPoi.equals("Residences No.3 and 4") && currentPoi.equals("University Administrative Building")){return "Sir Run Run Hall";}
			else if (lastPoi.equals("Shaw College") && currentPoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastPoi.equals("Train Station") && currentPoi.equals("Jockey Club Post-Graduate Hall")){return "University Sports Centre (Upward)";}
			else if (lastPoi.equals("University Sports Centre (Downward)") && currentPoi.equals("Jockey Club Post-Graduate Hall")){return "Train Station";}
			//ever-passed algorithm
			else if ((cc==true) && currentPoi.equals("New Asia College")){return "United college";}
			else if ((cc==true) && currentPoi.equals("New Asia College")){return "Residences No.3 and 4";}
			else if ((uab==true) && currentPoi.equals("United college")){return "Residences No.3 and 4";}
			else if ((cc==true) && currentPoi.equals("United college")){return "Residences No.3 and 4";}
			else if ((uab==false) && (na==false) && currentPoi.equals("United college")){return "New Asia College";}
			else if ((uab==false) && (na==true) && currentPoi.equals("United college")){return "University Administrative Building";}
			else {return null;}
		}
		
		else if ((hour>18) && (time.weekDay>=1)) //after 1800, Monday to Saturday
		{
        	//Checkpoint has the highest priority
        	if (getPoi=="Jockey Club Post-Graduate Hall Checkpoint") {return "Jockey Club Post-Graduate Hall";}
        	else if (getPoi=="Chung Chi Teaching Blocks Checkpoint") {return "Chung Chi Teaching Blocks";}
        	else if (getPoi=="University Administrative Building Checkpoint") {return "University Administrative Building";}
        	else if (getPoi=="New Asia College Checkpoint") {return "New Asia College";}
        	else if (getPoi=="Shaw College Checkpoint") {return "Shaw College";}
			//last stop algorithm
        	else if (lastPoi.equals("Residences No.3 and 4") && currentPoi.equals("United College Staff Residence")){return "Residences No.15";}
			else if (lastPoi.equals("Residences No.15") && currentPoi.equals("United College Staff Residence")){return "Shaw College";}
			else if (lastPoi.equals("Residences No.10 and 11") && currentPoi.equals("Residences No.15")){return "United College Staff Residence";}
			else if (lastPoi.equals("Residences No.3 and 4") && currentPoi.equals("Shaw College")){return "Residences No.3 and 4";}
			else if (lastPoi.equals("United College Staff Residence") && currentPoi.equals("Shaw College")){return "Residences No.3 and 4 ";}
			else if (lastPoi.equals("United college") && currentPoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastPoi.equals("Shaw College") && currentPoi.equals("Residences No.3 and 4")){return "New Asia College";}
			else if (lastPoi.equals("New Asia College") && currentPoi.equals("Residences No.3 and 4")){return "United College Staff Residence";}
			else if (lastPoi.equals("United college") && currentPoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastPoi.equals("Train Station") && currentPoi.equals("Jockey Club Post-Graduate Hall")){return "University Sports Centre (Upward)";}
			else if (lastPoi.equals("University Sports Centre (Downward)") && currentPoi.equals("Jockey Club Post-Graduate Hall")){return "Train Station";}
			//ever-passed algorithm
			else if ((fkh==false) && currentPoi.equals("New Asia College")){return "United college";}
			else if ((fkh==true) && (r34==false)&& currentPoi.equals("New Asia College")){return "Residences No.3 and 4";}
			else if ((fkh==true) && (r34==true) && currentPoi.equals("New Asia College")){return "United college";}
			else if ((fkh==false) && (r34==false) && currentPoi.equals("United college")){return "Residences No.3 and 4";}
			else if ((fkh==false) && (r34==true) && currentPoi.equals("United college")){return "University Administrative Building";}
			else if ((fkh==true) && (r34==false) && currentPoi.equals("United college")){return "New Asia College";}
			else if ((fkh==true) && (r34==true) && currentPoi.equals("United college")){return "University Administrative Building";}
			else {return null;}	
		}
        
		else //Sunday
		{
        	//Checkpoint has the highest priority
        	if (getPoi=="Jockey Club Post-Graduate Hall Checkpoint") {return "Jockey Club Post-Graduate Hall";}
        	else if (getPoi=="Chung Chi Teaching Blocks Checkpoint") {return "Chung Chi Teaching Blocks";}
        	else if (getPoi=="University Administrative Building Checkpoint") {return "University Administrative Building";}
        	else if (getPoi=="New Asia College Checkpoint") {return "New Asia College";}
        	else if (getPoi=="Shaw College Checkpoint") {return "Shaw College";}
        	//last stop algorithm
        	else if (lastPoi.equals("Shaw College") && (currentPoi=="United College Staff Residence")){return "Residences No.15";}
			else if (lastPoi.equals("Residences No.15") && currentPoi.equals("United College Staff Residence")){return "Shaw College";}
			else if (lastPoi.equals("United College Staff Residence") && currentPoi.equals("Residences No.15")){return "Residences No.10 and 11";}
			else if (lastPoi.equals("Residences No.10 and 11") && currentPoi.equals("Residences No.15")){return "United College Staff Residence";}
			else if (lastPoi.equals("Residences No.3 and 4") && currentPoi.equals("Shaw College")){return "United College Staff Residence";}
			else if (lastPoi.equals("United College Staff Residence") && currentPoi.equals("Shaw College")){return "United College Staff Residence";}
			else if (lastPoi.equals("New Asia College") && currentPoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastPoi.equals("United college") && currentPoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastPoi.equals("Shaw College") && currentPoi.equals("Residences No.3 and 4")){return "New Asia College";}
			else if (lastPoi.equals("United college") && currentPoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastPoi.equals("Train Station") && currentPoi.equals("Jockey Club Post-Graduate Hall")){return "University Sports Centre (Upward)";}
			else if (lastPoi.equals("University Sports Centre (Downward)") && currentPoi.equals("Jockey Club Post-Graduate Hall")){return "Train Station";}
			//ever-passed algorithm
			else if ((uc==true) && currentPoi.equals("New Asia College")){return "Residences No.3 and 4";}
			else if ((uc==false) && currentPoi.equals("New Asia College")){return "United college";}
			else if ((r34==false) && (na==false) && currentPoi.equals("United college")){return "New Asia College";}
			else if ((na==true) && (r34==false) && currentPoi.equals("United college")){return "Residences No.3 and 4 ";}
			else if ((na==true) && (r34==true) && currentPoi.equals("United college")){return "University Administrative Building";}
			else {return null;}	
		}
        
					
	} //The next stop method ends
	
}
