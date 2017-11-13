package es.miapp.mypetpics;


import java.util.List;
import com.itextpdf.text.Document;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import es.miapp.mypetpics.internal.AlbumFoto;
import es.miapp.mypetpics.internal.PetPic;
import es.miapp.mypetpics.internal.BookApaisado;
import es.miapp.mypetpics.internal.Constants;
import es.miapp.mypetpics.internal.ImageThreadLoader;
import es.miapp.mypetpics.internal.PersistenceSQL;
import es.miapp.mypetpics.internal.PostImageLoadedListener;
import es.miapp.mypetpics.internal.Utils;




/**
 * Clase de actividad para la pantalla del detalle de la tapa y/o evento. 
 * Extiende de CustomActiviy e implementa OnClickListener para los botones de votacion y sharing, compartir
 * @see es.miapp.babybook.CustomActivity
 * @see android.view.View.OnClickListener
 * @version 1.0
 * @author Victoria Marcos
 */
public class DetailFotoActivity extends CustomActivity implements OnClickListener {

	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	private static final int NO_ALBUMES = -2;
	private static final int FOTO_YA_INSERTADA = -3;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Handler handler = new Handler(new ResultMessageCallback());
	private ProgressDialog progressDialog;
	private PetPic mBabyFoto;
	private String albumSeleccionado ="ninguno";
	private List<String> listaTitulos;

	//layout
	private ImageView photo;
	private TextView description;
	//private ImageView pointPlus;
	//private ImageView pointLess;
	//private ImageView share;
	private TextView cdias;
	private TextView cmeses;	
	private TextView cyears;
	
	private Button buttonT;
	//private RatingBar ratingBar;
	
	//private ImageButton gotoAlbum;
	private Document doc=null;
	
	private Activity activity;
	//private topoos.Objects.POI mPOI;
	//private float puntos_rating;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity = this;
		setContentView(R.layout.detail_foto_layout);
		photo = (ImageView) findViewById(R.id.babyPhoto);
		description = (TextView) findViewById(R.id.tapaDescription);
		cdias = (TextView) findViewById(R.id.ccdias);
	
		
	
		mBabyFoto = (PetPic) getIntent().getExtras().getSerializable("babyFoto");
		buttonT = (Button) findViewById(R.id.PublishBT);
		buttonT.setEnabled(true);
		buttonT.setOnClickListener(this);

	
		description.setMovementMethod(ScrollingMovementMethod.getInstance());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		
		if (mBabyFoto != null) {

			

			
				photo.setImageBitmap(null);
				Bitmap cachedImage = null;
			
				try {
					

					PostImageLoadedListener pill = new PostImageLoadedListener(
							photo);
					String uri_imagen = Constants.URIFROMSERVER+ mBabyFoto.getUriFoto();
					cachedImage = imageLoader.loadImage(uri_imagen,pill);
					//cachedImage = imageLoader.loadImage(topoos.Images.Operations.GetImageURIThumb(mBabyFoto.getUriFoto(),topoos.Images.Operations.SIZE_LARGE), pill);
					
					description.setText(mBabyFoto.getDescription());
					cdias.setText(mBabyFoto.getDias());
					
					cmeses.setText(mBabyFoto.getMeses());
					cyears.setText(mBabyFoto.getAños());
					

				} catch (Exception e) {
					
				}
				if (cachedImage != null) {
					photo.setImageBitmap(cachedImage);
				}
			

			//boolean voted = PersistenceSQL.isVotedTV(mBabyFoto.getId(), this);
			//setVoteButtonsStatus(voted);

		}
	}

	/**
	 * Metodo que nos muestra las opciones del Dialog
	 */
	private void showAlbumDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(!listaTitulos.isEmpty())
		{
			
			builder.setMessage(getString(R.string.introAlbum))
					.setTitle("")
					.setCancelable(false)
					.setNegativeButton(getString(R.string.intro_cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									//clean_view();
								}
							})
					.setPositiveButton(getString(R.string.intro_ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									
									
									final CharSequence[] cs = listaTitulos.toArray(new CharSequence[listaTitulos.size()]);
									
									

									
										 runOnUiThread(new Runnable() {
											  public void run() {
											   // Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show();
											    AlertDialog.Builder mensaseListaElementos = new AlertDialog.Builder(DetailFotoActivity.this);
												  mensaseListaElementos.setTitle("Selecciona un Album");
												  mensaseListaElementos.setItems(cs, 
												        new DialogInterface.OnClickListener() 
												       {
													  public void onClick(DialogInterface dialog, int item) 
												         {
												          // Toast.makeText(getApplicationContext(),"Opción elegida: " + cs[item], Toast.LENGTH_SHORT).show();
												           albumSeleccionado = (String) cs[item];
												           Log.i("albumSeleccionado",albumSeleccionado);
												         }
												      }).show();
												  //mensaseListaElementos.show();
												  
											  }
											});
									
									
									 //Thread.sleep(200);
									
										
									
									
									
								/*	Intent intent = new Intent(getApplicationContext(),DetailAlbumActivity.class);
							
									intent.putExtra("babyFoto", mBabyFoto);			

									startActivity(intent);*/
							
								}
							});
		}else{
			builder.setMessage("No ha creado ningun algun previamente");
			
		}
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	




	/**
	 * metodo que establece el estado de los botones de votacion
	 * @param boolean isVoted
	 */
	/*private void setVoteButtonsStatus(boolean isVoted) {
		if (isVoted) {
			pointLess.setEnabled(false);
			pointPlus.setEnabled(false);

		} else {
			pointLess.setEnabled(true);
			pointPlus.setEnabled(true);

		}
	}*/
	
	
	/**
	 * metodo para obtener los puntos y poder generar posteriormente el nivel
	 * @param POI
	 * @return puntos
	 */
	/*private static float obtenerRating(int pos, int neg)
	{
		float retorno = 0;
		int puntosTotales = pos - neg;		
	    if(puntosTotales<=0)retorno = 0;
	    else if(puntosTotales<10)retorno=1;
	    else if(puntosTotales<20)retorno=2;
	    else if(puntosTotales<30)retorno=3;
	    else if(puntosTotales>40)retorno=4;
	
		return retorno;
		
	
	}//END puntosPOI*/
    /**
     * Clase privada para la gestion de la actividad DetailFotoActivity
     * @author Victoria Marcos
     *
     */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {

			// Cerramos la pantalla de progreso

			switch (arg0.what) {
			case WORKER_MSG_ERROR:
				Toast.makeText(DetailFotoActivity.this, "Errooooor",
						Toast.LENGTH_LONG).show();
				break;
			case WORKER_MSG_OK:
				//setVoteButtonsStatus(true);
				
				Toast.makeText(DetailFotoActivity.this, "FOTO INSERTADA",
						Toast.LENGTH_LONG).show();
				break;
			case NO_ALBUMES:
				//setVoteButtonsStatus(true);
				
				Toast.makeText(DetailFotoActivity.this, "No tiene ningun album",
						Toast.LENGTH_LONG).show();
				break;
			}

			progressDialog.dismiss();
			return true; 
		}
	}


	/**
	 * metodo que añade voto negativo 
	 */
	private class AddFotoBackground implements Runnable {
		
		public void run() {

			int messageReturn = WORKER_MSG_OK;
			try {
				
				
				
				 AlbumFoto af = PersistenceSQL.getAlbum(DetailFotoActivity.this, albumSeleccionado);
				 Log.i("af: ", af.toString());
				 if(!PersistenceSQL.isAddedFoto(af.getId(), mBabyFoto.getId(), DetailFotoActivity.this)) 
					 {
					  String uriAlbum = af.getUriAlbum();
					  String numPaginas = af.getNumeroFotos();
					  int num = Integer.valueOf( numPaginas);
					  int numeroNuevo = 0;
					  
					  Log.i("nameFile en insertarFotoEnPDF: ",uriAlbum);
					 
					 
					  
					//existe fichero en la ruta del movil
					   
					 String nuevoFichero =  addPhotoAlbum(uriAlbum, mBabyFoto, num);
					 Log.i("nuevoFichero: ",nuevoFichero);
					 
					 if(!nuevoFichero.equals("nada"))
					  {
						  Log.i("nuevoFichero PDF",nuevoFichero);
						  if(!Utils.esPar(num)) numeroNuevo = num +1;
						  else numeroNuevo = num;
						  int numero = Integer.valueOf(numeroNuevo);
						  PersistenceSQL.actualizarRutaFicheroAlbum(af.getId(), nuevoFichero, DetailFotoActivity.this,numero);
						  PersistenceSQL.addFotoAlbum(af.getId(), mBabyFoto.getId(), DetailFotoActivity.this);
						  
					  }
					 
					 }
				 else messageReturn = FOTO_YA_INSERTADA;
				
				messageReturn = WORKER_MSG_OK;
		
				
			} catch (Exception e) {
				e.printStackTrace();
				messageReturn = WORKER_MSG_ERROR;
			}

			/*runOnUiThread(new Runnable() {
				public void run() {
					
					showPersonToast("foto insertada en el album");
					//nVotesMinus.setText(String.valueOf(mBabyFoto.getWarningcount().getClosed() + 1));

				}
			});*/

			handler.sendEmptyMessage(messageReturn);
		}
	}

	private void showPersonToast(String textoAMostrar)
    {
    	Context context = getApplicationContext();
    	CharSequence text = textoAMostrar;
    	int duration = Toast.LENGTH_LONG;
    	 
    	LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.custom_toast,
    	        (ViewGroup) findViewById(R.id.toast_layout_root));
    	 
    	TextView textToast = (TextView) layout.findViewById(R.id.text_toast);
    	textToast.setText(text);
    	 
    	Toast toast = new Toast(context);
    	toast.setDuration(duration);
    	toast.setView(layout);
    	toast.show();
    }
	
	


	 

	 
		private String addPhotoAlbum(String uriAlbum, PetPic bf, int num) {
			//private String tituloAlbum;
			 String modificado = "nada";
			 String dias ="";
			 String meses ="";
			 String años = "";
			 if(!bf.getDias().equals("")) dias = bf.getDias() +"dias ";
			 if(!bf.getMeses().equals("")) meses = bf.getMeses() + " meses ";
			 if(!bf.getAños().equals("")) años = bf.getMeses() + " anyos";
			 String afterPhoto = "tengo" + dias+ meses + años;
			
			 int position;
			 if(!Utils.esPar(num))
			 {
				 position =BookApaisado.izquierda;
			 }else position=BookApaisado.derecha;
			 
			
			 
			 modificado = BookApaisado.addPhotoAlbumNewPage(uriAlbum, bf.getUriFoto(), bf.getDescription(), afterPhoto, false, null, position, true, BookApaisado.TABLAMDE, "fondoceldanaranja.png");
			 return modificado;
			
		}
	 
	

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			listaTitulos= PersistenceSQL.getallTitleAlbumes(DetailFotoActivity.this);
			showAlbumDialog();
			Thread thread;
			if(!albumSeleccionado.equals("ninguno"))
			{	
				progressDialog = ProgressDialog.show(this,
						this.getString(R.string.espere),
						this.getString(R.string.insertandoFoto));
				thread = new Thread(new AddFotoBackground());
				thread.start();
				
			}
			
		
			
			
			
			
					//showListaAlbumsDialog(listaTitulos);
					//albumSeleccionado = "prueba";
					
					
			
			
		}

	/**
	 * metodo que añade voto positivo
	 */
	/*private class AddPositiveVoteBackground implements Runnable {
		
		public void run() {

			int messageReturn = WORKER_MSG_OK;
			try {

				AccessInterface.AddPositiveVote(DetailFotoActivity.this, mBabyFoto.getId());

				PersistenceSQL.addVoteTV(mBabyFoto.getId(), DetailFotoActivity.this);
				
			} catch (Exception e) {
				messageReturn = WORKER_MSG_ERROR;
			}

			runOnUiThread(new Runnable() {
				public void run() {
					nVotesPlus.setText(String.valueOf(mBabyFoto.getWarningcount()
							.getDuplicated() + 1));


				}
			});

			handler.sendEmptyMessage(messageReturn);
		}
	}*/
	

	
	


	//@Override
	/*public void onClick(View v) {
		Thread thread;
		switch (v.getId()) {

		case R.id.votesPlus:
			progressDialog = ProgressDialog.show(this,
					this.getString(R.string.espere),
					this.getString(R.string.evotacion));
			//thread = new Thread(new AddPositiveVoteBackground());
			//thread.start();
			break;
		case R.id.votesMinus:
			progressDialog = ProgressDialog.show(this,
					this.getString(R.string.espere),
					this.getString(R.string.evotacion));
			thread = new Thread(new AddNegativeVoteBackground());
			thread.start();
			break;

		case R.id.share:
			showOneDialog();
		default:
			break;
		}

	}*/

	/**
	 * Metodo para mostrar cuadro de dialogo de informacion compartir 
	 */
	/*private void showOneDialog() {

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);	
		
		sharingIntent.setType("text/plain");
		
											
		
	
	
			
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TEXTO + mBabyFoto.getDescription());
			    
				
			
			
		
		
		
		startActivity(Intent.createChooser(sharingIntent,"Compartir via"));


	}*/

}