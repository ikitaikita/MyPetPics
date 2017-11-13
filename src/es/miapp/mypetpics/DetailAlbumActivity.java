package es.miapp.mypetpics;

import java.io.File;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import es.miapp.mypetpics.internal.AlbumFoto;
import es.miapp.mypetpics.internal.ImageThreadLoader;



/**
 * Contenido de la actividad que visualiza la vista del detalle del Album previamente creado
 * Extiende de CustomActivity   
 * @see  es.miapp.babybook.analytics.AnalyticsListActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class DetailAlbumActivity extends CustomActivity implements OnClickListener  {


	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	
	
	
	//private ListView lv;

	public final int ALBUM_BORRADO_OK = 1;
	public final int ALBUM_BORRADO_ERROR = -1;
	
	
	
	
	//private List<AlbumFoto> m_listaAlbumes = null;
	
	private AlbumFoto album;
	private TextView tituloAlbum;
	
	private TextView numeroPaginas;
	private Button buttonA;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.detail_album_layout);
		tituloAlbum= (TextView) findViewById(R.id.tituloAlbum);
		
		numeroPaginas= (TextView) findViewById(R.id.numeroPaginas);
		
		buttonA = (Button) findViewById(R.id.MostrarA);
		buttonA.setEnabled(true);
		buttonA.setOnClickListener(this);

		
		album = (AlbumFoto) getIntent().getExtras().getSerializable("albumFoto");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		if (album != null) 
		{
			tituloAlbum.setText(album.getTitulo());
			
			numeroPaginas.setText(album.getNumeroFotos()+" fotos");
		}
		

		 
		

	}

	@Override
	public void onClick(View v) {
		
		
		switch (v.getId()) {
		case R.id.MostrarA:
			showPdf();
			break;
		
			
		default:
			break;
		}
		
			
		
	}
	

	

	
	void showPdf()
	 {
	  
		 String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText";

	 
     
	  File file = new File(path, album.getUriAlbum());
	  Uri pathfichero = Uri.fromFile(file);
	  Intent intent = new Intent(Intent.ACTION_VIEW); 
	  intent.setDataAndType(pathfichero, "application/pdf"); 
	  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	  
	  try { 
          startActivity(intent); 
      }  
      catch (ActivityNotFoundException e) { 
    	     	  
          Toast.makeText(DetailAlbumActivity.this, "No dispone de ninguna aplicación para ver el documento PDF", Toast.LENGTH_SHORT).show();
    	
      } 
	 }
	


	
}
