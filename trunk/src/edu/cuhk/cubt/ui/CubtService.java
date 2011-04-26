package edu.cuhk.cubt.ui;

import java.util.Hashtable;
import java.util.Iterator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.service.BusChangeMonitor;
import edu.cuhk.cubt.service.IServiceMonitor;
import edu.cuhk.cubt.service.LocationChangeMonitor;
import edu.cuhk.cubt.service.StopPassingMonitor;

public class CubtService extends Service{

    private final IBinder mBinder = new CubtServiceBinder();
	
	private static SCCMEngine mSccmEngine;
	
	private BusChangeMonitor busChangeMonitor;
	private LocationChangeMonitor locationChangeMonitor;
	
	private Hashtable<String,IServiceMonitor> serviceMonitors = new Hashtable<String,IServiceMonitor>();
	
	public SCCMEngine getSCCMEngine(){
		if(mSccmEngine == null){
			mSccmEngine = new SCCMEngine(this);
			mSccmEngine.startEngine();
		}
		return mSccmEngine;
	}
	
	@Override
    public IBinder onBind(Intent intent) {
    	return mBinder;
    }

    public class CubtServiceBinder extends Binder {
    	CubtService getService() {
            return CubtService.this;
        }
    }
	
    /**
     * onCreate() is called when the Service is create
     */
	@Override
	public void onCreate() {		
		super.onCreate();
		
		/* Start Sccm Engine */
		mSccmEngine = getSCCMEngine();
		
		/* Start service related component */
		
		serviceMonitors.put(BusChangeMonitor.class.getName(), new BusChangeMonitor());
		serviceMonitors.put(LocationChangeMonitor.class.getName(), new LocationChangeMonitor());
		serviceMonitors.put(StopPassingMonitor.class.getName(), new StopPassingMonitor());

		Iterator<IServiceMonitor> iterator = serviceMonitors.values().iterator();
		while(iterator.hasNext()){
			iterator.next().start(this);
		}
		
		createNotification();
	}

    /**
     * onDestory() is called when the Service is destory
     */
	@Override
	public void onDestroy() {
		

		/* Stop service related component*/

		Iterator<IServiceMonitor> iterator = serviceMonitors.values().iterator();
		while(iterator.hasNext()){
			iterator.next().stop(this);
		}
		
		serviceMonitors = null;
		
		/* stop the Sccm Engine */
		if(mSccmEngine != null){
			mSccmEngine.stopEngine();
			mSccmEngine = null;
		}
		
		/* Clear the Notification */
		clearNotification();
		super.onDestroy();
	}

	

	/**
	 * Notification ID
	 */
	private static final int NOTIFICATION = 1;
	
	/**
	 * Create Service Started Notification 
	 */
	private void createNotification(){
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = android.R.drawable.ic_notification_overlay;
		CharSequence tickerText = "CUBT Service Started";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "CUHK Bus Tracking Service";
		CharSequence contentText = "CUHK Bus Tracking Service is running";
		Intent notificationIntent = new Intent(this, TestUserStateActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(NOTIFICATION, notification);
	}
	
	/**
	 * Clear Service Started Notification
	 */
	private void clearNotification(){
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	mNotificationManager.cancel(NOTIFICATION);		
	}
	
	
}
