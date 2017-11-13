package es.miapp.mypetpics;


import com.google.analytics.tracking.android.EasyTracker;

import es.miapp.mypetpics.R.color;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;




/**
 * Clase para la actividad principal de la aplicación y carga las pestanyas del menú de navegación. 
 * Extiende de TabActivity
 * @see android.app.TabActivity
 * @version 1.0
 * @author Victoria Marcos
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	
	//private AdView mAdView;

	private static final int MENU_ABOUT = Menu.FIRST;
	private static final int MENU_QUIT = Menu.FIRST + 1;
	 
	//private static final String AD_UNIT_ID = "ca-app-pub-6628319787253203/4339611373";
	private Typeface drawingFont;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		

		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
	
		/*mAdView = new AdView(this);
		mAdView.setAdSize(AdSize.BANNER);
		mAdView.setAdUnitId(AD_UNIT_ID);*/
	

		drawingFont = Typeface.createFromAsset(getAssets(),"fonts/drawing.ttf");

		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

	        @Override
	        public void onTabChanged(String tabId) {
	       
	        }

		});

		Intent intent;
		Resources res = getResources();

		// Pestaña1
		intent = new Intent().setClass(this, InsertaFotoActivity.class);
		spec = tabHost
				.newTabSpec("Pestana1")
				//.setIndicator("INSERTA FOTO")
				.setIndicator(this.getString(R.string.tab1))	
			//	.setIndicator("inserta foto",res.getDrawable(R.drawable.pestana1_style))
				.setContent(intent);
	
				
		tabHost.addTab(spec);

		// Pestaña2
		intent = new Intent().setClass(this, VerFotosListActivity.class).addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

		spec = tabHost
				.newTabSpec("Pestana2")
				//.setIndicator("MIS FOTOS")
				.setIndicator(this.getString(R.string.tab2))	
				//.setIndicator("",res.getDrawable(R.drawable.pestana2_style))
				.setContent(intent);
		
		tabHost.addTab(spec);
		
		// Pestaña3
		intent = new Intent().setClass(this, BorrarFotosListActivity.class).addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

		spec = tabHost
				.newTabSpec("Pestana3")
				//.setIndicator("BORRAR FOTOS")
				.setIndicator(this.getString(R.string.tab3))				
				//.setIndicator("",res.getDrawable(R.drawable.pestana4_style))
				.setContent(intent);
		tabHost.addTab(spec);

		// Pestaña4
		intent = new Intent().setClass(this, InsertaAlbumActivity.class).addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

		spec = tabHost
				.newTabSpec("Pestana4")
				.setIndicator(this.getString(R.string.tab4))	
				//.setIndicator("MI ALBUM")
				//.setIndicator("",res.getDrawable(R.drawable.pestana4_style))
				.setContent(intent);
		tabHost.addTab(spec);
		
		
		// WebPlantillas
				/*intent = new Intent().setClass(this, WebPlantilla.class).addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
				spec = tabHost
						.newTabSpec("WebPlantilla")
						.setIndicator("PLANTILLAS")
						//.setIndicator("",res.getDrawable(R.drawable.pestana3_style))
						.setContent(intent);
				tabHost.addTab(spec);*/
				

	
		
		//background color tabHost
		// getTabHost().getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.pestana1_drawable_shape);//Pestaña1
		 getTabHost().getTabWidget().getChildAt(0).setBackgroundColor(color.fondoPrincipal);
		// getTabHost().getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.pestana2_drawable_shape);//Pestaña2
		 getTabHost().getTabWidget().getChildAt(1).setBackgroundColor(color.fondoPrincipal);//Pestaña2
		// getTabHost().getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.pestana3_drawable_shape);//Pestaña3
		 getTabHost().getTabWidget().getChildAt(2).setBackgroundColor(color.fondoPrincipal);//Pestaña3
		 getTabHost().getTabWidget().getChildAt(3).setBackgroundColor(color.fondoPrincipal);//Pestaña3
		// getTabHost().getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.pestana4_drawable_shape);//Pestaña4	
		// getTabHost().getTabWidget().getChildAt(3).setBackgroundColor(color.fondoPrincipal);
		 
	
		 LinearLayout layout = (LinearLayout) findViewById(R.id.layfinal);
		// layout.addView(mAdView);
	
		// AdRequest adRequest = new AdRequest.Builder().build();
		 
	
		// mAdView.loadAd(adRequest);
	}
	

	public void quit(){
		
		setResult(RESULT_OK);
		finish();
		
	}
	
	public void AcercaDe(){
		Intent intent = new Intent(this, AboutActivity.class);
		this.startActivity(intent);						
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ABOUT, 0, getString(R.string.about));
		menu.add(0, MENU_QUIT, 0, getString(R.string.exit));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ABOUT:
				AcercaDe();
				return true;
			case MENU_QUIT:
				quit();
				return true;
		}
		return false;
	}
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