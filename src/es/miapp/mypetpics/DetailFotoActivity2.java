package es.miapp.mypetpics;


import java.io.File;
import java.util.Random;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import es.miapp.mypetpics.access.AccessInterface;
import es.miapp.mypetpics.internal.AlbumFoto;
import es.miapp.mypetpics.internal.PetPic;
import es.miapp.mypetpics.internal.BookApaisado;
import es.miapp.mypetpics.internal.ImageThreadLoader;
import es.miapp.mypetpics.internal.PersistenceSQL;
import es.miapp.mypetpics.internal.Plantilla;
import es.miapp.mypetpics.internal.Utils;





/**
 * Clase de actividad para la pantalla del detalle de la tapa y/o evento. 
 * Extiende de CustomActiviy e implementa OnClickListener para los botones de votacion y sharing, compartir
 * @see es.miapp.babybook.CustomActivity
 * @see android.view.View.OnClickListener
 * @version 1.0
 * @author Victoria Marcos
 */
public class DetailFotoActivity2 extends CustomActivity implements OnClickListener {

	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	private static final int NO_ALBUMES = -2;
	private static final int FOTO_YA_INSERTADA = -3;
	private static final int FOTOS_MAS_10 = -4;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Handler handler = new Handler(new ResultMessageCallback());
	private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText/";
	private String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics/";
	private String ficheroAntiguo;
	private ProgressDialog progressDialog;
	private PetPic mBabyFoto;
	
	private Plantilla plantilla;
	private AlbumFoto album =null;
	private Typeface drawingFont;
	private Typeface handFont;
	//layout
	private ImageView photo;
	private TextView description;
	private TextView cdias;
	private Button buttonT;
	private File imgFile;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_foto_layout);
		photo = (ImageView) findViewById(R.id.babyPhoto);
		description = (TextView) findViewById(R.id.tapaDescription);
		cdias = (TextView) findViewById(R.id.ccdias);
		
		//fechaFoto = (TextView) findViewById(R.id.fechaFoto);
	
		mBabyFoto = (PetPic) getIntent().getExtras().getSerializable("babyFoto");
		buttonT = (Button) findViewById(R.id.PublishBT);
		buttonT.setEnabled(true);
		buttonT.setOnClickListener(this);
		album=PersistenceSQL.getUnicAlbum(this); 
		//if(album != null) Log.i("album del sistema", album.getTitulo());
	    
		description.setMovementMethod(ScrollingMovementMethod.getInstance());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		
		if (mBabyFoto != null) {

			

			
				photo.setImageBitmap(null);
				Bitmap cachedImage = null;
			
				try {
					

					//PostImageLoadedListener pill = new PostImageLoadedListener(photo);
					//String uri_imagen = Constants.URIFROMSERVER+ mBabyFoto.getUriFoto();
					//cachedImage = imageLoader.loadImage(uri_imagen,pill);
					imgFile = new  File(pathfotos+ mBabyFoto.getUriFoto());
					if(imgFile.exists()){
						cachedImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					}
					//cachedImage = BitmapFactory.decodeFile(pathfotos+ mBabyFoto.getUriFoto());
					
					//cachedImage = imageLoader.loadImage(topoos.Images.Operations.GetImageURIThumb(mBabyFoto.getUriFoto(),topoos.Images.Operations.SIZE_LARGE), pill);
					drawingFont = Typeface.createFromAsset(getAssets(),"fonts/drawing.ttf");
					description.setTypeface(drawingFont);
					description.setTextSize(25);
					
					description.setText(mBabyFoto.getDescription());
					handFont  = Typeface.createFromAsset(getAssets(),"fonts/hand.ttf");
					cdias.setTypeface(handFont);
					cdias.setTextSize(25);
					String dias = "";
					String meses = "";
					String años ="";
					if(!mBabyFoto.getDias().equals("")) dias = mBabyFoto.getDias();
					if(!mBabyFoto.getMeses().equals("")) meses = Utils.getMonth(mBabyFoto.getMeses());
					if(!mBabyFoto.getAños().equals("")) años = mBabyFoto.getAños();
					
					cdias.setText( dias + "/" + meses + "/" + años);
					
				} catch (Exception e) {
					
				}
				if (cachedImage != null) {
					photo.setImageBitmap(cachedImage);
					//photo.setImageURI(Uri.fromFile(imgFile));
				}
			boolean added = false;
			if(album!=null) 
				added = PersistenceSQL.isAddedFoto(album.getId(), mBabyFoto.getId(), this);
			setAddedButtonsStatus(added);

		}
	}
	/**
	 * metodo que establece el estado del botón de insertar foto
	 * @param boolean isAdded
	 */
	private void setAddedButtonsStatus(boolean isAdded) {
		if (isAdded) {
			buttonT.setEnabled(false);			

		} else {
			buttonT.setEnabled(true);
			

		}
	}

	/**
	 * Metodo que nos muestra las opciones del Dialog
	 */
	/*private void showAlbumDialog() {
		
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
									AlertDialog.Builder mensaseListaElementos = new AlertDialog.Builder(DetailFotoActivity2.this);
									  mensaseListaElementos.setTitle("Selecciona un Album");
									  mensaseListaElementos.setCancelable(false);
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
									

									
										
						
							
								}
							});
		}else{
			builder.setMessage("No ha creado ningun algun previamente");
			
		}
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	

private void showAlbumDialogUnic() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
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
									
									Intent intent = new Intent(DetailFotoActivity2.this, AddFotoBackground.class);
								
									   startActivity(intent);
								

							
								}
							
		
			
					});
  
		
		AlertDialog alert = builder.create();
		alert.show();
	}*/



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
				Toast.makeText(DetailFotoActivity2.this, "Errooooor",
						Toast.LENGTH_LONG).show();
				break;
			case WORKER_MSG_OK:
				//setVoteButtonsStatus(true);
				setAddedButtonsStatus(true);
				File ficheroAborrar = new File(path +ficheroAntiguo);
				if(ficheroAborrar.exists()) ficheroAborrar.delete();
				Toast.makeText(DetailFotoActivity2.this, "FOTO INSERTADA",
						Toast.LENGTH_LONG).show();
				break;
			case NO_ALBUMES:
				//setVoteButtonsStatus(true);
				
				Toast.makeText(DetailFotoActivity2.this, "Aun no has creado ningún Album",
						Toast.LENGTH_LONG).show();
				break;
				
			case FOTO_YA_INSERTADA:
				//setVoteButtonsStatus(true);
				
				Toast.makeText(DetailFotoActivity2.this, "Esta foto ya ha sido insertada",
						Toast.LENGTH_LONG).show();
				break;
			
			case FOTOS_MAS_10:
			//setVoteButtonsStatus(true);
			
			Toast.makeText(DetailFotoActivity2.this, "No puedes insertar más fotos",
					Toast.LENGTH_LONG).show();
			break;
			}

			progressDialog.dismiss();
			return true; 
		}
	}


	/**
	 * clase privada que añade foto
	 */
	private class AddFotoBackground implements Runnable {
		
		public void run() {
			

			int messageReturn = WORKER_MSG_OK;
			try {
				
				
				
				 //AlbumFoto af = PersistenceSQL.getAlbum(DetailFotoActivity2.this, albumSeleccionado);
				 //Log.i("af: ", af.toString());
					 if(album!=null)
					 {
						 String uriAlbum = album.getUriAlbum();
						  String nombrePlantilla = album.getNombrePlantilla();
						 
						  int numeroFotos = PersistenceSQL.getNumeroFotos(DetailFotoActivity2.this, album.getId());
						  if(numeroFotos<=11)
						  {
							  String nuevoFichero =  addPhotoAlbum(uriAlbum, mBabyFoto, numeroFotos +1,nombrePlantilla);
								
								 
								 if(!nuevoFichero.equals("nada"))
								  {
									  
									  PersistenceSQL.actualizarRutaFicheroAlbum(album.getId(), nuevoFichero, DetailFotoActivity2.this,numeroFotos +1);
									  PersistenceSQL.addFotoAlbum(album.getId(), mBabyFoto.getId(), DetailFotoActivity2.this);
									  mBabyFoto.setEnAlbum(true);
									  ficheroAntiguo = uriAlbum;
									  
									  
									  
								  }
								 messageReturn = WORKER_MSG_OK;
								 
						  }else messageReturn = FOTOS_MAS_10;
						 
					 }else messageReturn = NO_ALBUMES;
					 
					 
					  
					//existe fichero en la ruta del movil
					   
					
					 
				
				
		
				
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


	


	 

	 
		private String addPhotoAlbum(String uriAlbum, PetPic bf, int num, String nombrePlantilla) {
			//private String tituloAlbum;
			 String modificado = "nada";
			 String dias ="";
			 String meses ="";
			 String años = "";
			 if(!bf.getDias().equals("")) dias = bf.getDias();
			 if(!bf.getMeses().equals("")) meses = Utils.getMonth(bf.getMeses());
			 if(!bf.getAños().equals("")) años = bf.getMeses();
			 String afterPhoto = dias+"/"+ meses+"/"+años;
			
			 int position;
			 if(!Utils.esPar(num))
			 {
				 position =BookApaisado.izquierda;
			 }else position=BookApaisado.derecha;
			 
			 plantilla = AccessInterface.getPlantillaX(nombrePlantilla);
			 
			
			 
			 String fondo = Utils.obtenerAleatorio(plantilla.getFondos());
			 String marco = Utils.obtenerAleatorio(plantilla.getMarcos());
			
		
			 boolean hayMarco = getRandomBoolean();
		
			 //String tipoCelda = getRandomString();
			 String tipoCelda = BookApaisado.TABLAMDE;
			
			 
			 
					 
			 modificado = BookApaisado.addPhotoAlbumNewPage(uriAlbum, bf.getUriFoto(), bf.getDescription(), afterPhoto, true, fondo, position, hayMarco, tipoCelda, marco);
			 return modificado;
			
		}
	 
		private boolean getRandomBoolean() {
		    Random random = new Random();
		    return random.nextBoolean();
		}
		
		private String getRandomString()
		{
			 Random random = new Random();
			 boolean ran = random.nextBoolean();
			 if(ran ==true) return BookApaisado.TABLAMDE;
			 else return BookApaisado.TABLAMIZ;
		}

		@Override
		public void onClick(View v) {
			
			
			Thread thread;
			switch (v.getId()) {
			case R.id.PublishBT:
				progressDialog = ProgressDialog.show(this,
						this.getString(R.string.espere),
						this.getString(R.string.insertandoFoto));
				thread = new Thread(new AddFotoBackground());
				thread.start();
				break;
			
			}

					
					
			
			
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