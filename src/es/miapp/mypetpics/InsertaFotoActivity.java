package es.miapp.mypetpics;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.widget.DatePicker.OnDateChangedListener;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.miapp.mypetpics.access.AccessInterface;
import es.miapp.mypetpics.internal.PersistenceSQL;
import es.miapp.mypetpics.internal.PetPic;





/**
 * Contenido de la actividad Pestaña 1 del menú que realiza la insercion de una tapa y/o evento
 * Extiende de CustomActivity e implementa el interfaz OnClickListener  
 * @see es.miapp.babybook.CustomActivity
 * @see android.view.View.OnClickListener
 * @version 1.0
 * @author Victoria Marcos
 */
public class InsertaFotoActivity extends CustomActivity implements
		android.view.View.OnClickListener   {


	
	private TextView marquee;



	private Button buttonT;
	

	private ImageView ImageView1;
	private static int TAKE_PICTURE = 1;
	private static final int SELECT_PICTURE = 2;
	private String nameCap = "";
	private String name = "";
	private String namePhoto ="";

	private EditText descripcion;
	private TextView tvDisplayDate;
	private DatePicker dpResult;
	private int year;
	private int month;
	private int day;
	
	//private EditText dias;
	//private EditText meses;
	//private EditText anys;
	
	private Typeface drawingFont;
	

	private ProgressDialog pd = null;
	

	// *****Imagen********//
	private byte[] bitmapdata;
	private ByteArrayOutputStream stream;

	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
	private Handler handler = new Handler(new ResultMessageCallback());
	 static int w = 250;
	 static int h = 250;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pestana1);
		setCurrentDateOnView();

		
		nameCap = Environment.getExternalStorageDirectory() + "/test.jpg";
		name = Environment.getExternalStorageDirectory()+"/";
		buttonT = (Button) findViewById(R.id.PublishBT);
		marquee = (TextView) findViewById(R.id.TapasList);
		buttonT.setEnabled(false);

		buttonT.setOnClickListener(this);
		ImageView1 = (ImageView) findViewById(R.id.ImageView1);
		drawingFont = Typeface.createFromAsset(getAssets(),"fonts/drawing.ttf");
		
		
		descripcion = (EditText) findViewById(R.id.DescriptionT);
		
		//dias = (EditText) findViewById(R.id.diasB);
		//meses = (EditText) findViewById(R.id.mesesB);
		//anys = (EditText) findViewById(R.id.anyosB);
		
		descripcion.setTypeface(drawingFont);
		tvDisplayDate.setTypeface(drawingFont);
		//dias.setTypeface(drawingFont);
		//meses.setTypeface(drawingFont);
		//anys.setTypeface(drawingFont);
		

		descripcion.addTextChangedListener(new TextWatcher() {
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
		
		
	
		
 

		stream = new ByteArrayOutputStream();



		ImageView1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {						
					
								
						GalleryCamSelection();

					}
				});
		
		dpResult.init(dpResult.getYear(), dpResult.getMonth(), dpResult.getDayOfMonth(),new OnDateChangedListener() {
			   
			   @Override
			   public void onDateChanged(DatePicker arg0, int selectedYear, int selectedMonth, int selectedDay) {
			    // TODO Auto-generated method stub
				   year= selectedYear;
		            month = selectedMonth + 1;
		            day = selectedDay;
		            tvDisplayDate.setText(new StringBuilder()
					// Month is 0 based, just add 1
					.append(day).append("/").append(month).append("/")
					.append(year).append(" "));
				   //tvDisplayDate.setText(" "+arg3+ " / "+ (arg2+1) + " / "+arg1);
			   }
			  } );
			
		
		

	}
	
    // Creando datepicker listener
    /*private DatePickerDialog.OnDateSetListener datePickerListener 
            = new DatePickerDialog.OnDateSetListener() {
        // Este metodo sera llamado cuando el cerremos el dialogo
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year= selectedYear;
            month = selectedMonth;
            day = selectedDay;
            // Actualizar a fecha seleccionada
         
            tvDisplayDate.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(month + 1).append("-").append(day).append("-")
			.append(year).append(" "));
 
            // Actualizar a fecha seleccionada
            dpResult.init(year, month, day, null);
         
	}
    };*/
	

	public void setCurrentDateOnView() {
		 
		tvDisplayDate = (TextView) findViewById(R.id.tvDate);
		dpResult = (DatePicker) findViewById(R.id.dpResult);
 
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		// set current date into textview
		tvDisplayDate.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(month + 1).append("-").append(day).append("-")
			.append(year).append(" "));
 
		// set current date into datepicker
		dpResult.init(year, month, day, null);
		
	}
 

	
	 /**
     * metodo que cambia el estado del boton insertar babyfoto
     */
    private void checkRegisterButtonStatus()
    {
    	buttonT.setEnabled(bitmapdata != null && descripcion.getText() != null && descripcion.getText().toString().length() > 0 ) ;
    	
        
    }
    



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == TAKE_PICTURE) {
			
			if (data != null) {
			
				if (data.hasExtra("data")) {
					
					

					Bitmap bitmap = ((Bitmap) data.getParcelableExtra("data"));
					//ImageView iv = (ImageView) findViewById(R.id.ImageView1);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 75, bos);
					bitmap.setDensity(72);
				    
					ImageView1.setImageBitmap(bitmap);
					
					
					//Bitmap bm  = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() ),(int) (bitmap.getHeight()),false);
				

					
				
					//bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, false);
					
					
					
					bitmapdata = bos.toByteArray();
					
					//bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
					

				}
				/*
				 * De lo contrario es una imagen completa
				 */
			} else {
				/*
				 * A partir del nombre del archivo ya definido lo buscamos y
				 * creamos el bitmap para el ImageView
				 */
				
				ImageView iv = (ImageView) findViewById(R.id.ImageView1);
				//iv.setImageBitmap(BitmapFactory.decodeFile(nameCap));
				iv.setImageBitmap(BitmapFactory.decodeFile(nameCap));
				
				
				// Guardar imagen en la galeria mediante mediascanner
				new MediaScannerConnectionClient() {
					private MediaScannerConnection msc = null;
					{
						msc = new MediaScannerConnection(
								getApplicationContext(), this);
						msc.connect();
					}

					public void onMediaScannerConnected() {
						msc.scanFile(nameCap, null);
					}

					public void onScanCompleted(String path, Uri uri) {
						msc.disconnect();
					}
				};
			}
		} else if (requestCode == SELECT_PICTURE) {
			Uri selectedImage = data.getData();

			String path = getRealPathFromURI(selectedImage);
			namePhoto = path;
			//Log.i("namePhoto: ",namePhoto);

			try {

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = (calculateInSampleSize(path, 480, 480));
				options.inDensity = 72;
				Bitmap bitmap = BitmapFactory.decodeFile(path, options);
				bitmap.compress(CompressFormat.JPEG, 75, bos);
				bitmapdata = bos.toByteArray();
				ImageView iv = (ImageView) findViewById(R.id.ImageView1);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
				iv.setImageBitmap(bitmap);

				
			} catch (Exception e) {

			}
		}
	

		checkRegisterButtonStatus();

	}

	public String getRealPathFromURI(Uri contentUri) {
		
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		android.database.Cursor cursor = managedQuery(contentUri, proj, // columns
																		// to
																		// return
				null, // clause rows
				null, // clause selection
				null); // order
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);

	}

	public static int calculateInSampleSize(String photoURI, int reqWidth,
			int reqHeight) {


		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoURI, options);

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}
	
	public static int calculateInSampleSize(Bitmap bitmap, int reqWidth,
			int reqHeight) {


		// Raw height and width of image
		final int height = bitmap.getHeight();
		final int width = bitmap.getWidth();
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

	/**
	 * Clase privada implementa Runnable para lanzar la actividad de añadir la PetPic al Sistema
	 */
	private class AddBabyFotoWorker implements Runnable {
		private String desc;
		private String diass = "";
		private String mesess="";
		private String años = "";
		//private int typeElement;
		
		private AddBabyFotoWorker(){
			//typeElement=type;
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			int messageReturn = MESSAGE_OK;
			
			try {
					UUID uuid = UUID.randomUUID();
					String randomUUIDString = uuid.toString();
					randomUUIDString = randomUUIDString.replace("-", "");
					if (randomUUIDString.length() > 10) randomUUIDString = randomUUIDString.substring(0,9); 
					randomUUIDString = randomUUIDString + ".jpg";
					
					
					//if(AccessInterface.uploadFoto(randomUUIDString,bitmapdata));
					if(AccessInterface.uploadFotoEnMovil(randomUUIDString,bitmapdata));
					{
						desc = descripcion.getText().toString();
						if(tvDisplayDate.getText().length()>0)
							{
							diass = String.valueOf(day);
							mesess = String.valueOf(month);
							años= String.valueOf(year);
							}
						
					
						Date fechaActual = new Date();
						//Log.i("fecha actual: ", fechaActual.toString());
						
						//if(onInsert(randomUUIDString,diass,mesess,años,desc,fechaActual)){}
						//Log.i("nombre foto: ", randomUUIDString);
						
						//int id = AccessInterface.onInsertFoto(randomUUIDString,diass,mesess,años,desc,fechaActual);
						//Log.i("ID insertado",String.valueOf(id));
						PetPic newBabyFoto = new PetPic(randomUUIDString, diass, mesess,años, desc, fechaActual.toString());
						if(onInsertFoto(newBabyFoto));
						//int id = PersistenceSQL.insertFoto(InsertaFotoActivity.this, newBabyFoto);
						//Log.i("ID Foto insertado",String.valueOf(id));
						//newBabyFoto.setId(id);
						messageReturn = MESSAGE_OK;
						
					
						
					}
		
				
				

			} catch (Exception e) {
				messageReturn = MESSAGE_ERROR;
				e.printStackTrace();
			}
			handler.sendEmptyMessage(messageReturn);
		}

	}
	
	private boolean  onInsertFoto(PetPic bf){
		
		int idFoto = PersistenceSQL.insertFoto(InsertaFotoActivity.this, bf);
		bf.setId(idFoto);
		return true;
		
		
	}

	
	/*private boolean uploadFoto(String nameFoto){       
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(Constants.UPLOAD);	            
        MultipartEntity mpEntity = new MultipartEntity();           
        //ContentBody foto = new FileBody(file, "image/jpeg");
       // ByteArrayBody bab = new ByteArrayBody(bitmapdata, "prueba.jpg");
        ByteArrayBody bab = new ByteArrayBody(bitmapdata, "image/jpeg", nameFoto);
       // ContentBody bin = new ByteArrayBody(bitmapdata, "myfile.dat");
       // mpEntity.addPart("fotoUp", foto);
        mpEntity.addPart("fotoUp", bab);
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
	
	/*private boolean  onInsert(String nameFoto, String dias, String meses, String anys, String descr, Date fecha){
		Log.i("ONINSERT ", "");
		Log.i("nameFoto: ", nameFoto);
		Log.i("dias: ", dias);
		Log.i("meses: ", meses);
		Log.i("años: ", anys);
		Log.i("desc: ", descr);
		Log.i("fecha: ", fecha.toString());
		HttpClient httpclient;
		
		HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Constants.ONINSERT); // Url del Servidor      
        //Añadimos nuestros datos
        List<NameValuePair> postValues = new ArrayList<NameValuePair>(5);
   
        postValues.add(new BasicNameValuePair("uri", nameFoto)); 
	    postValues.add(new BasicNameValuePair("dias", dias));
	    postValues.add(new BasicNameValuePair("meses", meses));
	    postValues.add(new BasicNameValuePair("anyos", anys));
	    postValues.add(new BasicNameValuePair("descrip", descr));
	    postValues.add(new BasicNameValuePair("fecha", fecha.toString()));
       

		try {
		   httppost.setEntity(new UrlEncodedFormEntity(postValues));
		   httpclient.execute(httppost);
		   
		   return true;
		  
	       
	       //return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}*/
	/*private int  onInsertFoto(String nameFoto, String dias, String meses, String anys, String descr, Date fecha)
	{
		Log.i("ONINSERT ", "ONINSERT");
		Log.i("nameFotoooooooooooo: ", nameFoto);
		Log.i("dias: ", dias);
		Log.i("meses: ", meses);
		Log.i("años: ", anys);
		Log.i("desc: ", descr);
		Log.i("fecha: ", fecha.toString());
		int idFoto=0;
		HttpClient httpclient;
		
		HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Constants.ONINSERTFOTO); // Url del Servidor      
        //Añadimos nuestros datos
        List<NameValuePair> postValues = new ArrayList<NameValuePair>(6);
        
   
        postValues.add(new BasicNameValuePair("uri", nameFoto)); 
	    postValues.add(new BasicNameValuePair("dias", dias));
	    postValues.add(new BasicNameValuePair("meses", meses));
	    postValues.add(new BasicNameValuePair("anyos", anys));
	    postValues.add(new BasicNameValuePair("descrip", descr));
	    postValues.add(new BasicNameValuePair("fecha", fecha.toString()));
       

		try {
		   httppost.setEntity(new UrlEncodedFormEntity(postValues));
		   HttpResponse resp = httpclient.execute(httppost);
		   
		   HttpEntity entity = resp.getEntity();
		   BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
		   InputStream iStream = buffer.getContent();
		                    
		   String aux = "";
		            
		   BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
		   new StringBuilder();
		   String line;
		   while ((line = r.readLine()) != null) {
		     aux += line;
		   }
		   JSONObject jsonObject = new JSONObject(aux);
		   JSONArray ids = jsonObject.getJSONArray("id");
		   for(int i = 0; i < ids.length(); i++) {
			   JSONObject id = ids.getJSONObject(i);
			   
			   idFoto = Integer.valueOf(id.getString("id"));
			
			     
			   
			    //m_restaurantes.add(res);
			    }
		   
		  
		   
		   return idFoto;
		  
	       
	       //return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idFoto;
		
	}
	 private StringBuilder inputStreamToString(InputStream is) {
		   String rLine = "";
		   StringBuilder pregunta = new StringBuilder();
		   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		 
		   try {
		    while ((rLine = rd.readLine()) != null) {
		     pregunta.append(rLine);
		    }
		   }
		 
		   catch (IOException e) {
		    // e.printStackTrace();
		    Toast.makeText(getApplicationContext(),
		      "Error..." + e.toString(), Toast.LENGTH_LONG).show();
		   }
		   return pregunta;
		  }*/
	 

	/**
	 * Metodo para gestionar el click del boton de publicar Tapa/evento
	 */

	@Override
	public void onClick(View v) {

		
			pd = ProgressDialog.show(this, getString(R.string.INFORMACION),
					getString(R.string.publicar));
			Runnable babyFotoWorker = new AddBabyFotoWorker();
			Thread thread = new Thread(babyFotoWorker);
			thread.start();
			
	}


	/**
	 * Metodo para lanzar la camara
	 */
	public void CaptureFoto() {

		
		/////
		//Uri output = Uri.fromFile(new File(name + "123456.jpg"));
		
		 
	    
		/////
		
		
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, nameCap);
		Uri mCapturedImageURI  = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent camaraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, output);

		startActivityForResult(camaraIntent, TAKE_PICTURE);
	}

	public void GallerySelection() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(intent, SELECT_PICTURE);
	}



	/**
	 * Metodo para limpiar la vista del usuario
	 */
	public void clean_view() {
		
		descripcion.setText("");
		tvDisplayDate.setText("inserta fecha");
		
	

		ImageView1.setImageBitmap(null);

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

	/**
	 * Metodo que nos muestra las opciones del Dialog
	 */
	/*private void showOneDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.sharing_title))
				.setTitle("")
				.setCancelable(false)
				.setNegativeButton(getString(R.string.sharing_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								clean_view();
							}
						})
				.setPositiveButton(getString(R.string.sharing_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
						
								Intent sharingIntent = new Intent(Intent.ACTION_SEND);
								sharingIntent.setType("text/plain");
																	
								
									sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TEXTO + mBabyFoto.getDescription());
									
															
								startActivity(Intent.createChooser(sharingIntent,"Compartir via"));


								clean_view();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}*/

	/**
	 * Metodo para seleccionar la imagen de la galeria/camara
	 */
	private void GalleryCamSelection() {

		final String[] items = { "Camara", "Galeria" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Selección");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {// Camara
					CaptureFoto();
				} else if (item == 1) {// Galeria
					GallerySelection();
				}

			}
		});

		builder.create();
		builder.show();

	}

	/**
	 * Metodo para gestionar el resultado de la carga de la imagen
	 */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {

			pd.dismiss();

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(InsertaFotoActivity.this, getString(R.string.NoPublicada),
						Toast.LENGTH_LONG).show();
				break;
			case MESSAGE_OK:
				
				showPersonToast("Su foto se ha registrado correctamente");
				//Toast.makeText(Pestana1.this, getString(R.string.Publicada),
						//Toast.LENGTH_LONG).show();
				clean_view();
				
				break;
				// Llamamos a showondialog
				//showOneDialog();
				
			}

			return true;
		}
	}






}
