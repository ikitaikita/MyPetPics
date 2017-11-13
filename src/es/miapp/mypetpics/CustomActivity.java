package es.miapp.mypetpics;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;

/**
 * Clase de la que extienden ciertas actividades.
 * Extiende de clase Activity
 * @see android.app.Activity
 * @version 1.0
 * @author Victoria Marcos
 */
public class CustomActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
	}
	

	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); 
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
