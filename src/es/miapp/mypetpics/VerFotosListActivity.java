package es.miapp.mypetpics;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import es.miapp.mypetpics.analytics.AnalyticsListActivity;
import es.miapp.mypetpics.internal.PetPic;
import es.miapp.mypetpics.internal.ImageThreadLoader;
import es.miapp.mypetpics.internal.PersistenceSQL;
import es.miapp.mypetpics.internal.PostImageLoadedListener;
import es.miapp.mypetpics.internal.Utils;
import es.miapp.mypetpics.widget.PullToRefreshListView;
import es.miapp.mypetpics.widget.PullToRefreshListView.OnRefreshListener;



/**
 * Contenido de la actividad Pestaña 2 del menú que realiza la visualizacion de la lista de las fotos subidas por el usuario
 * Extiende de AnalyticsListActivity   
 * @see  es.miapp.mypetpics.analytics.AnalyticsListActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class VerFotosListActivity extends AnalyticsListActivity  {


	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	
	private ListView lv;

	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
	private Handler handler = new Handler(new ResultMessageCallback());
	
	private List<PetPic> m_listafotos = null;
	private List<String> m_listaNombrefotos = null;
	
	private ProgressDialog pDialog = null;
	
	
	//Para las imagenes
	
	private Bitmap loadedImage;
	private Typeface drawingFont;
	private String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics/";
	



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.pestana2);
		
		
		

		
		 pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoFotos));
		 
		
		 
		 Thread thread = new Thread(new LoadBabyFotosWorker());
		 thread.start();
		

		 lv=getListView();
		

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
               
				int fixedpos = arg2 - 1;
				
				
				Intent intent= new Intent(VerFotosListActivity.this,DetailFotoActivity2.class); 

				intent.putExtra("babyFoto", m_listafotos.get(fixedpos));

                startActivity(intent);
                               
			}
		});
		
		


	}
	


	
	public void onStart(){
		super.onStart();
		((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Se actualiza la lista de babyfotos
            	new GetDataTask().execute();
            }
        });	
	}
	
	private List<String> getNombreFotosAlbum()
	{
		
		 ArrayList<String> lista_nombre_fotos= PersistenceSQL.getAllNameFotosAlbum(VerFotosListActivity.this);
		 return lista_nombre_fotos;
	}

	private List<PetPic> getListaFotos()
	{
		
		 ArrayList<PetPic> lista_fotos= PersistenceSQL.getAllFotos(VerFotosListActivity.this);
		 return lista_fotos;
	}

	

	/**
	 * Carga las fotos en background. Implementa Runnable
	 * @see java.lang.Runnable
	 * 
	 */
	private class LoadBabyFotosWorker implements Runnable {

		public void run() {

			int messageReturn = MESSAGE_OK;

			try {
				Thread.sleep(1000);
				m_listafotos = (ArrayList<PetPic>)getListaFotos();
				m_listaNombrefotos = (ArrayList<String>)getNombreFotosAlbum();
	


			} catch (Exception e) {
				messageReturn =  MESSAGE_ERROR;
			
			}

			handler.sendEmptyMessage(messageReturn);
		}
	}

	/**
	 * Metodo resultCalback LoadTapentosWorker
	 */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();
			

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(VerFotosListActivity.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case  MESSAGE_OK:
			
				setListAdapter(new Adapter(VerFotosListActivity.this,R.layout.lista_item, m_listafotos));			
				break;

			}

			return true; // lo marcamos como procesado
		}
	}



	/**
	 * Clase Adaptador para mostrar los items como deseamos
	 * extiende de ArrayAdapter
	 * @see android.widget.ArrayAdapter<POI>
	 */
	private class Adapter extends ArrayAdapter<PetPic> {

		private ArrayList<PetPic> items;

		public Adapter(Context context, int textViewResourceId,
				List<PetPic> items) {
			super(context, textViewResourceId, items);
			this.items = (ArrayList<PetPic>) items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.lista_item, null);
			}

			final PetPic p = items.get(position);
	
			v.setBackgroundColor(Color.parseColor("#B78EC3"));
			drawingFont = Typeface.createFromAsset(getAssets(),"fonts/drawing.ttf");
			
			if (p != null) {

				final TextView des = (TextView) v
						.findViewById(R.id.description);
				final ImageView imagen = (ImageView) v
						.findViewById(R.id.image);

				final TextView edad = (TextView) v.findViewById(R.id.edad);
				final TextView enAlbum = (TextView)v.findViewById(R.id.enAlbum);
				//chkborrar.setClickable(false);
				/*chkborrar.setOnClickListener(new OnClickListener() {
					 
					  @Override
					  public void onClick(View v) {
				                //is chkIos checked?
						if (((CheckBox) v).isChecked()) {
							Toast.makeText(VerFotosListActivity.this,
						 	   "Bro, try Android :)", Toast.LENGTH_LONG).show();
						}
				 
					  }
					});		*/
				if(m_listaNombrefotos!=null)
				{
					if(m_listaNombrefotos.contains(p.getUriFoto()))
						//enAlbum.setText("Ya esta en el Album");
						enAlbum.setText(R.string.inAlbum);
					
					//else enAlbum.setText("Aun no ha introducido en el Album");
					else enAlbum.setText(R.string.notinAlbum);
				}
				//else enAlbum.setText("Aun no ha introducido en el Album");				
				else enAlbum.setText(R.string.notinAlbum);
			

				if (edad != null) {
					//mas1.setText("+"+ p.getWarningcount().getDuplicated().toString());
					edad.setTypeface(drawingFont);
					String dias = "";
					String meses = "";
					String años ="";
					if(!p.getDias().equals("")) dias =p.getDias();
					if(!p.getMeses().equals("")) meses = Utils.getMonth(p.getMeses());
					if(!p.getAños().equals("")) años = p.getAños();
					
					edad.setText(dias + "/" + meses + "/" + años);
				}

				

				if (des != null) {
				
					des.setTypeface(drawingFont);
					des.setText(p.getDescription());
				}

				if (imagen != null) {
					
					    
					    	
					    	
					  
					    	imagen.setImageBitmap(null);
							Bitmap cachedImage = null;
							try {
								
								
								
								
								PostImageLoadedListener pill = new PostImageLoadedListener(imagen);							
								//String uri_imagen = Constants.URIFROMSERVER+ p.getUriFoto();
								//String uri_imagen = pathfotos+ p.getUriFoto();
								cachedImage = BitmapFactory.decodeFile(pathfotos+ p.getUriFoto());

							
								
								
								//cachedImage = imageLoader.loadImage(uri_imagen,pill);
						


							} catch (Exception e) {
								Toast.makeText(getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
					            e.printStackTrace();
								
							}
							if (cachedImage != null) {
								//imagen.setImageBitmap(cachedImage);
								//imagen.setImageBitmap(cachedImage);
								imagen.setImageBitmap(cachedImage);
								
								
							}
						
					    


				}

			}

			return v;

		}

	}


	/**
	 * Clase privada GetDataTask para la tarea asincrona de cargar la lista 
	 * extiende de AsyncTask
	 * @see android.os.AsyncTask<Void, Void, List<POI>>
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<PetPic>> {
		

	        @Override
	       protected List<PetPic> doInBackground(Void...params) {

	             	
	        	try {
	        		m_listafotos = (ArrayList<PetPic>)getListaFotos();
					
	        		
	        			
	        		
				} catch (Exception e) {
					e.printStackTrace();
				}

	        	return m_listafotos;
	        	
	        }

	        protected void onPostExecute(List<PetPic> result) {
	        	
	        
	        	
	        	setListAdapter(new Adapter(VerFotosListActivity.this, //Modificamos la lista mediante el adaptador
   					R.layout.lista_item, m_listafotos));
	        	
	        	((PullToRefreshListView) getListView()).onRefreshComplete();
	        		        	
	        	 super.onPostExecute(result);
	        }
	    }



	
	
}
