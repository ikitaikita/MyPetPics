package es.miapp.mypetpics;

import java.io.File;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import es.miapp.mypetpics.analytics.AnalyticsListActivity;
import es.miapp.mypetpics.internal.AlbumFoto;
import es.miapp.mypetpics.internal.PetPic;
import es.miapp.mypetpics.internal.Constants;
import es.miapp.mypetpics.internal.ImageThreadLoader;
import es.miapp.mypetpics.internal.PersistenceSQL;
import es.miapp.mypetpics.internal.PostImageLoadedListener;
import es.miapp.mypetpics.widget.PullToRefreshListView;
import es.miapp.mypetpics.widget.PullToRefreshListView.OnRefreshListener;



/**
 * Contenido de la actividad Pestaña 3 del menú que realiza la visualizacion de la lista las babyfotos subidas por el usuario
 * Extiende de AnalyticsListActivity   
 * @see  es.miapp.babybook.analytics.AnalyticsListActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class BorrarFotosListActivity extends AnalyticsListActivity implements OnClickListener {


	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	
	private ListView lv;
	private Button btnborrar;
	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
	public final int FOTOS_BORRADAS_OK = 2;
	public final int NO_FOTOS = -2;
	public final int FOTO_BORRADO_ERROR = -3;
	
	
	private Handler handler = new Handler(new ResultMessageCallback());
	
	private List<PetPic> m_listafotos = null;
	private List<String> m_listaNombrefotos = null;
	private List<String> m_nombresfotosborrar = null;
	
	private ProgressDialog pDialog = null;
	
	
	//Para las imagenes
	
	private Bitmap loadedImage;
	private Typeface drawingFont;
	private String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics/";
	private String pathfotosborrar = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics";



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.pestana3);
		
		btnborrar = (Button) findViewById(R.id.btnborrar);
		btnborrar.setEnabled(false);
		btnborrar.setOnClickListener(this);
		
		
		

		
		 pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoFotos));
		 
		
		 
		 Thread thread = new Thread(new LoadBabyFotosWorker());
		 thread.start();
		

		 lv=getListView();
		

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
               
				int fixedpos = arg2 - 1;
				
				
				Intent intent= new Intent(BorrarFotosListActivity.this,DetailFotoActivity2.class); 

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
		
		 ArrayList<String> lista_nombre_fotos= PersistenceSQL.getAllNameFotosAlbum(BorrarFotosListActivity.this);
		 return lista_nombre_fotos;
	}

	private List<PetPic> getListaFotos()
	{
		
		 ArrayList<PetPic> lista_fotos= PersistenceSQL.getAllFotos(BorrarFotosListActivity.this);
		 return lista_fotos;
	}

	

	/**
	 * Carga las Tapas y eventos en background. Implementa Runnable
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
				Toast.makeText(BorrarFotosListActivity.this,getString(R.string.ErrorCarga), Toast.LENGTH_LONG).show();
				break;
			case  MESSAGE_OK:
			
				setListAdapter(new Adapter(BorrarFotosListActivity.this,R.layout.lista_item_borrar, m_listafotos));			
				break;

			
		    case  FOTOS_BORRADAS_OK:
		    	
		    	m_listafotos = (ArrayList<PetPic>)getListaFotos();
				m_listaNombrefotos = (ArrayList<String>)getNombreFotosAlbum();			
			    setListAdapter(new Adapter(BorrarFotosListActivity.this,R.layout.lista_item_borrar, m_listafotos));			
			break;
			
		    case NO_FOTOS:
		    	Toast.makeText(BorrarFotosListActivity.this,getString(R.string.noFotos), Toast.LENGTH_LONG).show();
		    	break;

		    
			case FOTO_BORRADO_ERROR:
				Toast.makeText(BorrarFotosListActivity.this,getString(R.string.ErrorBorradoFotos), Toast.LENGTH_LONG).show();
		    	break;
	
		    }
			return true; // lo marcamos como procesado
		}
	}

	 private void checkRegisterButtonStatus()
	    {
	    	btnborrar.setEnabled(m_nombresfotosborrar.size()>0) ;
	        
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
				v = vi.inflate(R.layout.lista_item_borrar, null);
			}

			final PetPic p = items.get(position);
	
			v.setBackgroundColor(Color.parseColor("#B78EC3"));
			drawingFont = Typeface.createFromAsset(getAssets(),"fonts/drawing.ttf");
			
			if (p != null) {
				
				
				m_nombresfotosborrar = new ArrayList<String>();

				final TextView des = (TextView) v
						.findViewById(R.id.description);
				final ImageView imagen = (ImageView) v
						.findViewById(R.id.image);

				final TextView edad = (TextView) v.findViewById(R.id.edad);
				final CheckBox chkborrar = (CheckBox)v.findViewById(R.id.checkborrar);
				
				if(m_listaNombrefotos!=null)
				{
					if(m_listaNombrefotos.contains(p.getUriFoto())) 
						{
						chkborrar.setClickable(false);
						chkborrar.setEnabled(false);
						//chkborrar.setText("foto ya incluida en album. No se puede borrar.");
						chkborrar.setText(R.string.incluida);
						
						}
				
				}
				chkborrar.setClickable(false);
				chkborrar.setOnClickListener(new OnClickListener() {
					 
					  @Override
					  public void onClick(View v) {
				                //is chkIos checked?
						if (((CheckBox) v).isChecked()) {
							
							m_nombresfotosborrar.add(p.getUriFoto());
							checkRegisterButtonStatus();
							//Toast.makeText(BorrarFotosListActivity.this, "Ha seleccionado esta foto para borrar", Toast.LENGTH_LONG).show();
				 
						}else
						{
							if(m_nombresfotosborrar.contains(p.getUriFoto()))
							  m_nombresfotosborrar.remove(p.getUriFoto());
							checkRegisterButtonStatus();
							//Toast.makeText(BorrarFotosListActivity.this, "Ya no la va a borrar", Toast.LENGTH_LONG).show();
								
						}
						}
					});		
				
				

				if (edad != null) {
					//mas1.setText("+"+ p.getWarningcount().getDuplicated().toString());
					edad.setTypeface(drawingFont);
					String dias = "";
					String meses = "";
					String años ="";
					if(!p.getDias().equals("")) dias =p.getDias();
					if(!p.getMeses().equals("")) meses = p.getMeses();
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
								//Toast.makeText(getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.errcargaimg) +e.getMessage(), Toast.LENGTH_LONG).show();
								
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
	        	
	        
	        	
	        	setListAdapter(new Adapter(BorrarFotosListActivity.this, //Modificamos la lista mediante el adaptador
   					R.layout.lista_item_borrar, m_listafotos));
	        	
	        	((PullToRefreshListView) getListView()).onRefreshComplete();
	        		        	
	        	 super.onPostExecute(result);
	        }
	    }
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Thread thread;
		switch (v.getId()) {
		case R.id.btnborrar:
			pDialog = ProgressDialog.show(this,this.getString(R.string.INFORMACION),this.getString(R.string.borrandoFotos));
			thread = new Thread(new DeleteBabyFotos());
			thread.start();
			
			break;
		
			
		default:
			break;
		}
		
	}
	
	private class DeleteBabyFotos implements Runnable {
		
		
		public void run() {

			int messageReturn = FOTOS_BORRADAS_OK;
			try {
				if(m_nombresfotosborrar!=null)
				{
					for(int i=0; i<m_nombresfotosborrar.size(); i++)
					{
						
						if(PersistenceSQL.deleteFoto(BorrarFotosListActivity.this, m_nombresfotosborrar.get(i)));
						File filefoto = new File(pathfotosborrar, m_nombresfotosborrar.get(i));
					    filefoto.delete();
					}
					
					
					
				}else messageReturn =  NO_FOTOS;
				
				
				
			} catch (Exception e) {
				messageReturn =  FOTO_BORRADO_ERROR;
			}

		

			handler.sendEmptyMessage(messageReturn);
		}
	}



	
	
}
