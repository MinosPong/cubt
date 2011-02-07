package edu.cuhk.cubt.bus;

import java.util.Collection;
import android.location.Location;
import android.text.format.Time;

//This piece of rubbish had never been tested

public class prediction{

	Boolean cc = false;
	Boolean uab = false;
	Boolean na = false;
	Boolean fkh = false;
	Boolean r34 = false;
	
	String currentpoi = null;
	String lastpoi = null;
	
	public void paramreset() //The method for resetting all the parameters in this class 
	{
		cc = false;
		uab = false;
		na = false;
		fkh = false;
		r34 = false;
		currentpoi = null;
		lastpoi = null;	
	}
	
	public String thenextstop(long millis, Location location){ 	//The next stop method starts

		//get the current time in hour
		Time time = new Time();
		time.set(millis);
		String timestring = time.toString();
		int hour = Integer.parseInt(Character.toString(timestring.charAt(9)))*10 + Integer.parseInt(Character.toString(timestring.charAt(10)));
		
		//get the past and current POI
		Poi poi = Poi.getByLocation(location);
		String getpoi = poi.getName();
		if (getpoi.equals(currentpoi)) 
		{
			lastpoi = currentpoi;
			currentpoi = getpoi;
		};
		
		//indicator for ever-passed algorithm
		if (getpoi=="Chung Chi Teaching Blocks"){cc=true;}
		if (getpoi=="University Administrative Building"){uab=true;}
		if (getpoi=="New Asia College"){na=true;}
		if (getpoi=="Fung King Hey Building"){fkh=true;}
		if (getpoi=="Residences No.3 and 4"){r34=true;}
		
		
        if (hour<18) //before 1800
		{
        	//last stop algorithm
			if (lastpoi.equals("Residences No.15") && (currentpoi=="United College Staff Residence")){return "Chan Chun Ha Hostel";}
			else if (lastpoi.equals("Residences No.10 and 11") && currentpoi.equals("Residences No.15")){return "United College Staff Residence";}
			else if (lastpoi.equals(null) && currentpoi.equals("Shaw College")){return "Residences No.3 and 4";}
			else if (lastpoi.equals("Residences No.3 and 4") && currentpoi.equals("Shaw College")){return "Residences No.3 and 4";}
			else if (lastpoi.equals("Chan Chun Ha Hostel") && currentpoi.equals("Shaw College")){return "University Administrative Building";}
			else if (lastpoi.equals("United college") && currentpoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastpoi.equals("Shaw College") && currentpoi.equals("Residences No.3 and 4")){return "New Asia College";}
			else if (lastpoi.equals("New Asia College") && currentpoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastpoi.equals("United college") && currentpoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastpoi.equals("Sir Run Run Hall") && currentpoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastpoi.equals("Residences No.3 and 4") && currentpoi.equals("University Administrative Building")){return "Sir Run Run Hall";}
			else if (lastpoi.equals("Shaw College") && currentpoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastpoi.equals("Train Station") && currentpoi.equals("Jockey Club Post-Graduate Hall")){return "University Sports Centre (Upward)";}
			else if (lastpoi.equals("University Sports Centre (Downward)") && currentpoi.equals("Jockey Club Post-Graduate Hall")){return "Train Station";}
			//ever-passed algorithm
			else if ((cc==true) && currentpoi.equals("New Asia College")){return "United college";}
			else if ((cc==true) && currentpoi.equals("New Asia College")){return "Residences No.3 and 4";}
			else if ((uab==true) && currentpoi.equals("United college")){return "Residences No.3 and 4";}
			else if ((cc==true) && currentpoi.equals("United college")){return "Residences No.3 and 4";}
			else if ((uab==false) && (na==false) && currentpoi.equals("United college")){return "New Asia College";}
			else if ((uab==false) && (na==true) && currentpoi.equals("United college")){return "University Administrative Building";}
			else {return null;}
		}
		
		else //after 1800
		{
			//last stop algorithm
			if (lastpoi.equals("Residences No.3 and 4") && currentpoi.equals("United College Staff Residence")){return "Residences No.15";}
			else if (lastpoi.equals("Residences No.15") && currentpoi.equals("United College Staff Residence")){return "Shaw College";}
			else if (lastpoi.equals("Residences No.10 and 11") && currentpoi.equals("Residences No.15")){return "United College Staff Residence";}
			else if (lastpoi.equals("Residences No.3 and 4") && currentpoi.equals("Shaw College")){return "Residences No.3 and 4";}
			else if (lastpoi.equals("United College Staff Residence") && currentpoi.equals("Shaw College")){return "Residences No.3 and 4 ";}
			else if (lastpoi.equals("United college") && currentpoi.equals("Residences No.3 and 4")){return "Shaw College";}
			else if (lastpoi.equals("Shaw College") && currentpoi.equals("Residences No.3 and 4")){return "New Asia College";}
			else if (lastpoi.equals("New Asia College") && currentpoi.equals("Residences No.3 and 4")){return "United College Staff Residence";}
			else if (lastpoi.equals("United college") && currentpoi.equals("University Administrative Building")){return "Pentecostal Mission Hall Complex";}
			else if (lastpoi.equals("Train Station") && currentpoi.equals("Jockey Club Post-Graduate Hall")){return "University Sports Centre (Upward)";}
			else if (lastpoi.equals("University Sports Centre (Downward)") && currentpoi.equals("Jockey Club Post-Graduate Hall")){return "Train Station";}
			//ever-passed algorithm
			else if ((fkh==false) && currentpoi.equals("New Asia College")){return "United college";}
			else if ((fkh==true) && (r34==false)&& currentpoi.equals("New Asia College")){return "Residences No.3 and 4";}
			else if ((fkh==true) && (r34=true) && currentpoi.equals("New Asia College")){return "United college";}
			else if ((fkh==false) && (r34=false) && currentpoi.equals("United college")){return "Residences No.3 and 4";}
			else if ((fkh==false) && (r34=true) && currentpoi.equals("United college")){return "University Administrative Building";}
			else if ((fkh==true) && (r34=false) && currentpoi.equals("United college")){return "New Asia College";}
			else if ((fkh==true) && (r34=true) && currentpoi.equals("United college")){return "University Administrative Building";}
			else {return null;}	
		}
					
	} //The next stop method ends
	
}
