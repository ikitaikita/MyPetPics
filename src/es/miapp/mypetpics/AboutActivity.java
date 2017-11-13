package es.miapp.mypetpics;


import es.miapp.mypetpics.analytics.AnalyticsActivity;
import android.os.Bundle;
import android.view.Window;

/**
 * Clase para la actividad de la app en el menu contextual de la aplicacion
 * @see es.miapp.babybook.analytics.AnalyticsActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class AboutActivity extends AnalyticsActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash);

	}

}
