package es.miapp.mypetpics.analytics;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;


/**
 * Clase que configura Google Analytics y extiende de Activity. 
 * @see android.app.Activity
 * @version 1.0
 * @author Victoria Marcos
 */
public class AnalyticsActivity extends Activity {

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); 
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); 
	}
}