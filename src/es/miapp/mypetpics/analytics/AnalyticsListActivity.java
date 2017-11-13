package es.miapp.mypetpics.analytics;

import android.app.ListActivity;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * Clase que configura Google Analytics y acceso a Topoos.
 * Extiende de ListActivity
 * @see android.app.ListActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class AnalyticsListActivity extends ListActivity {

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); 
		//AccessInterface.initializeTopoosSession(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); 
	}
}