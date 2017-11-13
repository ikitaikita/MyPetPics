package es.miapp.mypetpics;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




import es.miapp.mypetpics.access.AccessInterface;
import es.miapp.mypetpics.analytics.AnalyticsListActivity;
import es.miapp.mypetpics.internal.AlbumFoto;
import es.miapp.mypetpics.internal.BookApaisado;
import es.miapp.mypetpics.internal.PersistenceSQL;
import es.miapp.mypetpics.internal.Plantilla;
import es.miapp.mypetpics.internal.Utils;


/**
 * Contenido de la actividad Pestaña 4 del menú que realiza la visualizacion de la lista Albumes. Solamente uno esta disponible en esta version
 * Extiende de AnalyticsListActivity
 * @see es.miapp.babybook.analytics.AnalyticsListActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class InsertaAlbumActivity extends AnalyticsListActivity implements OnClickListener{

	

	
	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
	public final int ALBUM_BORRADO_ERROR = -2;
	public final int ALBUM_BORRADO_OK = 2;
	public final int ALBUM_CREADO_OK = 3;
	public final int ALBUM_CREADO_ERROR = -3;
	public final int NO_ALBUMS = -4;
	
	
	private Handler handler = new Handler(new ResultMessageCallback());
	
	private List<AlbumFoto> m_listaAlbumes = null;
	private ArrayList<Plantilla> listaPlantillas=null;
	private AlbumFoto album=null;
	private String nombrePlantilla="";
	private Plantilla plantilla=null;
	private ProgressDialog pDialog = null;

	//private AsyncTaskDialog task;
	
	private ListView lv;
	private Button buttonA;
	private Button buttonD;
	private Button buttonE;
	private EditText tituloA;
	
	
	//private ImageThreadLoader imageLoader = new ImageThreadLoader();
	
	// *****Posicion GPS*****//

	
	//Imagen
	//private String imageHttpAddress = "http://192.254.226.126/appbabybook/imagenesbabybook/";
	//private Bitmap loadedImage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pestana4);
	
        

	
		//marquee=(TextView) findViewById(R.id.Title1);
		tituloA = (EditText) findViewById(R.id.tituloA);
		tituloA.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			
				checkRegisterButtonStatus(); 
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}

		});
		buttonA = (Button) findViewById(R.id.PublishA);		
		buttonA.setEnabled(false);
		buttonA.setOnClickListener(this);
		
		
		buttonD = (Button) findViewById(R.id.BorrarA);		
		buttonD.setEnabled(true);
		buttonD.setOnClickListener(this);
		
		buttonE = (Button) findViewById(R.id.EnviarA);		
		buttonE.setEnabled(true);
		buttonE.setOnClickListener(this);
		
		
		pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoAlbumes));
		
		 Thread thread = new Thread(new LoadAlbumsWorker());
		 thread.start();

	
		
		
		lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int fixedpos = arg2;

				
                
				Intent intent= new Intent(InsertaAlbumActivity.this,DetailAlbumActivity.class); 
				intent.putExtra("albumFoto", m_listaAlbumes.get(fixedpos));	
								
				startActivity(intent);
				
				
              
			}
		});
		
	}
	private class LoadAlbumsWorker implements Runnable {

		public void run() {

			int messageReturn = MESSAGE_OK;

			try {
				Thread.sleep(1000);
				listaPlantillas = AccessInterface.getPlantillas();
				m_listaAlbumes = (ArrayList<AlbumFoto>)getListaAlbumes();
				if(m_listaAlbumes.size()==1) album = m_listaAlbumes.get(0);
					
				checkRegisterButtonStatus();
		

			} catch (Exception e) {
				e.printStackTrace();
				messageReturn =  MESSAGE_ERROR;
			
			}

			handler.sendEmptyMessage(messageReturn);
		}
		
	
		
	}
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();
			

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(InsertaAlbumActivity.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case  MESSAGE_OK:
				
				setListAdapter(new Adapter(InsertaAlbumActivity.this,R.layout.lista_item, m_listaAlbumes));	
				//clean_view();
				break;
			
			case  ALBUM_CREADO_OK:
				Toast.makeText(InsertaAlbumActivity.this,
						getString(R.string.albumCreado), Toast.LENGTH_LONG)
						.show();
				
				setListAdapter(new Adapter(InsertaAlbumActivity.this,R.layout.lista_item, m_listaAlbumes));	
				
				clean_view();
				break;	
				
			case  ALBUM_CREADO_ERROR:
				Toast.makeText(InsertaAlbumActivity.this,
						getString(R.string.albumCreadoError), Toast.LENGTH_LONG)
						.show();
				
				break;	
			case ALBUM_BORRADO_OK:
				
				Toast.makeText(InsertaAlbumActivity.this,
						getString(R.string.OkBorrado), Toast.LENGTH_LONG)
						.show();
				
				setListAdapter(new Adapter(InsertaAlbumActivity.this,R.layout.lista_item, m_listaAlbumes));	
				checkRegisterButtonStatus();
				break;
				
			case  ALBUM_BORRADO_ERROR:
				
				Toast.makeText(InsertaAlbumActivity.this,
						getString(R.string.ErrorBorrado), Toast.LENGTH_LONG)
						.show();	
				break;
				
			case  NO_ALBUMS:
				
				Toast.makeText(InsertaAlbumActivity.this,
						getString(R.string.lista_vacia), Toast.LENGTH_LONG)
						.show();	
				break;

			}

			return true; // lo marcamos como procesado
		}
	}
	@Override
	public void onClick(View v) {
		
		Thread thread;
		switch (v.getId()) {
		
			case R.id.PublishA:
				
			pDialog = ProgressDialog.show(this,this.getString(R.string.INFORMACION),this.getString(R.string.crearAlbum));
			thread = new Thread(new AddBabyAlbum());
			thread.start();
			
			break;
			case R.id.BorrarA:
			
				pDialog = ProgressDialog.show(this,this.getString(R.string.espere),	this.getString(R.string.borrando));
			thread = new Thread(new DeleteAlbumBackground());
			thread.start();
			
			
			
			break;
			case R.id.EnviarA:
				
				
				enviarAlbum2();
				//break;
			default:
				break;
			}
		}


		
	public void clean_view() {
		
		tituloA.setText("");
		

	}

	
/*private void enviarAlbum() {
	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText";
	if(album!=null)
	{
		 File file = new File(path, album.getUriAlbum());
	
		  
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);	
		
		sharingIntent.setType("text/plain");
		

		sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(file));
			    
			
		startActivity(Intent.createChooser(sharingIntent,"Compartir via"));
	}
	else 
	{
		Toast.makeText(InsertaAlbumActivity.this,
				getString(R.string.lista_vacia), Toast.LENGTH_LONG)
				.show();	
	}
	 
	 


}*/
private void enviarAlbum2() {
	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText";
	if(album!=null)
	{
		 File file = new File(path, album.getUriAlbum());
	
		  
		Intent emailIntent  = new Intent(Intent.ACTION_SEND);	
		
		emailIntent.setType("message/rfc822");
		
		//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "someone@gmail.com" });
		//emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Te mando mi BabyBook");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,R.string.enviar);
		emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(file));
			    
			
		startActivity(Intent.createChooser(emailIntent,getResources().getString(R.string.compartir)));
		
	
	}
	else 
	{
		Toast.makeText(InsertaAlbumActivity.this,
				getString(R.string.lista_vacia), Toast.LENGTH_LONG)
				.show();	
	}
	 
	 


}
	
	
private class DeleteAlbumBackground implements Runnable {
	
	
	public void run() {

		int messageReturn = ALBUM_BORRADO_OK;
		try {
			if(album!=null)
			{
				int idAlbum = album.getId();
				
				//ArrayList<String> listaNombreFotos = PersistenceSQL.getAllNameFotos(InsertaAlbumActivity.this,album.getId());
				PersistenceSQL.deleteAlbum(InsertaAlbumActivity.this, idAlbum);				
				PersistenceSQL.deleteFotoAlbum(InsertaAlbumActivity.this, idAlbum);
				/*if(listaNombreFotos!=null)
				{
					String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics";
					
					for ( int i = 0; i < listaNombreFotos.size() ; i ++ ) {
					    File filefoto = new File(pathfotos, listaNombreFotos.get(i));
					    filefoto.delete();
					}
				}*/
				
				m_listaAlbumes = (ArrayList<AlbumFoto>)getListaAlbumes();
			}else messageReturn =  NO_ALBUMS;
			
			
			
		} catch (Exception e) {
			messageReturn =  ALBUM_BORRADO_ERROR;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				if(album!=null)
				{
					/*ArrayList<String> listaNombreFotos = PersistenceSQL.getAllNameFotos(InsertaAlbumActivity.this,album.getId());
					if(listaNombreFotos!=null)						
					{
						Log.i("listaNombreFotos",listaNombreFotos.toString());
						if(listaNombreFotos.size()>0)
						{
							String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/babyfotos";
							for ( int i = 0; i < listaNombreFotos.size() ; i ++ ) {
							    File filefoto = new File(pathfotos, listaNombreFotos.get(i));
							    filefoto.delete();
							}
						}
					}*/
					
					
					String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText";
			    	File file = new File(path, album.getUriAlbum());
			   	  	file.delete();
				}
				


			}
		});

		handler.sendEmptyMessage(messageReturn);
	}
}
	
	private class AddBabyAlbum implements Runnable {
		private String tituloAlbum;
	
		@Override
		public void run() {
			int messageReturn = ALBUM_CREADO_OK;
			
			try {

					UUID uuid = UUID.randomUUID();
					String randomUUIDString = uuid.toString();
					randomUUIDString = randomUUIDString.replace("-", "");
					if (randomUUIDString.length() > 10) randomUUIDString = randomUUIDString.substring(0,9); 
					randomUUIDString = randomUUIDString + ".pdf";
					tituloAlbum = tituloA.getText().toString(); 
					if(createPDF(randomUUIDString,tituloAlbum ))
					{
						Date fechaActual = new Date();
						//Log.i("fecha actual: ", fechaActual.toString());
						
						AlbumFoto af = new AlbumFoto(randomUUIDString,tituloAlbum,"0",nombrePlantilla,fechaActual.toString());
						album =  af;
					
						if(onInsert(album));
						
						m_listaAlbumes = (ArrayList<AlbumFoto>)getListaAlbumes();
						
						
						messageReturn = ALBUM_CREADO_OK;
					}
					
	
				
				

			} catch (Exception e) {
				messageReturn =  ALBUM_CREADO_ERROR;
				e.printStackTrace();
			}
			handler.sendEmptyMessage(messageReturn);
		}

	}
	
	private boolean  onInsert(AlbumFoto af){
		
		int idAlbum = PersistenceSQL.insertAlbum(InsertaAlbumActivity.this, af);
		af.setId(idAlbum);
		return true;
		
		
	}
	  private void checkRegisterButtonStatus()
	    {
		  buttonA.setEnabled(tituloA.getText() != null && tituloA.getText().toString().length() > 0 && m_listaAlbumes.size()<1) ;
	    }
	  
	  

	
	  
	private List<AlbumFoto> getListaAlbumes()
	{
		
		 ArrayList<AlbumFoto> lista_albumes= PersistenceSQL.getAllAlbums(InsertaAlbumActivity.this);
		 return lista_albumes;
	}






	/**
	 * Clase adaptador para mostrar la lista de Albumes de fotos. Solamente aparece uno en esta version
	 * extiende de ArrayAdapter
	 * @see android.widget.ArrayAdapter<POI>
	 */
	private class Adapter extends ArrayAdapter<AlbumFoto> {

		private ArrayList<AlbumFoto> items;

		public Adapter(Context context, int textViewResourceId,
				List<AlbumFoto> items) {
			super(context, textViewResourceId, items);
			this.items = (ArrayList<AlbumFoto>) items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.lista_item_album, null);
			}

			final AlbumFoto p = items.get(position);
	
			v.setBackgroundColor(Color.parseColor("#8DC1C1"));
			if (p != null ) {

				final TextView titulo = (TextView) v
						.findViewById(R.id.tituloalbum);
				

				final TextView numPaginas = (TextView) v.findViewById(R.id.numFotos);
				

				if (numPaginas != null) {
				
					numPaginas.setText(p.getNumeroFotos());
				}

				
				if (titulo != null) {
				
					titulo.setText(p.getTitulo());
				}

			

			}

			return v;

		}

	}


	



	
	public boolean createPDF(String nombrePDF, String tituloPDF)
    {
       
		plantilla=Utils.obtenerPlantillaAleatoria(listaPlantillas);
		nombrePlantilla = plantilla.getNombre();
		//Log.i("nombrePlantilla: ",nombrePlantilla);
		String portada = plantilla.getPortada();
		//Log.i("portada: ",portada);
		
		return BookApaisado.createPDF(nombrePDF, tituloPDF,portada ); 
     
        
         
    } 
	
	/*private boolean uploadPDFFile(String nameFile, byte[] bitmap){       
		
		Log.i("uploadPDFFile","");
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(Constants.UPLOADPDF);	            
        MultipartEntity mpEntity = new MultipartEntity();           
        //ContentBody foto = new FileBody(file, "image/jpeg");
       // ByteArrayBody bab = new ByteArrayBody(bitmapdata, "prueba.jpg");
        ByteArrayBody bab = new ByteArrayBody(bitmap, "text/plain", nameFile);
       // ContentBody bin = new ByteArrayBody(bitmapdata, "myfile.dat");
       // mpEntity.addPart("fotoUp", foto);
        mpEntity.addPart("fileUp", bab);
        httppost.setEntity(mpEntity);
        try {
			httpclient.execute(httppost);
	        httpclient.getConnectionManager().shutdown();
	        return true;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
}*/
}	
