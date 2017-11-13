package es.miapp.mypetpics.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.miapp.mypetpics.internal.PetPic;
import es.miapp.mypetpics.internal.Constants;
import es.miapp.mypetpics.internal.Plantilla;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
//import android.util.Log;

/**
 * Clase para acceso a plataforma Topoos y diferentes operaciones que se realizan con esta plataforma 
 * @version 1.0
 * @author Victoria Marcos
 */
public class AccessInterface {
	
	//public static final String TOPOOS_ADMIN_APP_TOKEN = "1a2699e6-4ecf-4ccd-886e-1e90b15f451e";
	//public static final String URL_SERVER = "http://192.254.226.126/appbabybook/datos.class.php?tipo=";
	public static final String obtener_plantillas = "obtener_plantillas";
	public static final String obtener_plantillaX="obtener_plantillaX";
	private static String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics/";

	
	private List<PetPic> getListaFotos2()
	{
		 ArrayList<PetPic> lista_fotos = new ArrayList<PetPic>();
		 
		
		 String url_foto,dias,meses,years,fecha,descrip="";
		 int id_foto=0;
		 //String lati = "", lon = "";
		
		 try {
			   
			   
			   HttpGet httpGet = new HttpGet(Constants.url);
			   HttpClient httpClient = new DefaultHttpClient();
			   HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
			   HttpEntity entity = response.getEntity();
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
			   JSONArray babyFotos = jsonObject.getJSONArray("datos");
			  
			   for(int i = 0; i < babyFotos.length(); i++) {
				   JSONObject babyFoto = babyFotos.getJSONObject(i);
				   
				    id_foto = Integer.valueOf(babyFoto.getString("id_foto"));
				    url_foto=babyFoto.getString("url_foto");
				    dias = babyFoto.getString("dias");
				    meses = babyFoto.getString("meses");
				    years=babyFoto.getString("years");
				    fecha= babyFoto.getString("fecha");
				    descrip=babyFoto.getString("descrip");
				   
				    
				    PetPic bf = new PetPic(url_foto,dias,meses,years,descrip,fecha);
				    bf.setId(id_foto);
				    lista_fotos.add(bf);
				     
				   
				    //m_restaurantes.add(res);
				    }
			 

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return lista_fotos;
	}
	
	
	public static int  onInsertFoto(String nameFoto, String dias, String meses, String anys, String descr, Date fecha)
	{
		/*Log.i("ONINSERT ", "ONINSERT");
		Log.i("nameFotoooooooooooo: ", nameFoto);
		Log.i("dias: ", dias);
		Log.i("meses: ", meses);
		Log.i("años: ", anys);
		Log.i("desc: ", descr);
		Log.i("fecha: ", fecha.toString());*/
		int idFoto=0;
		HttpClient httpclient;
		
		HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Constants.ONINSERTFOTO); // Url del Servidor      
        //Añadimos nuestros datos
        List<NameValuePair> postValues = new ArrayList<NameValuePair>(6);
        
        /*
         * $uriFoto = $_REQUEST['uri'];
   $dias = $_REQUEST['dias'];
   $meses =  $_REQUEST['meses'];
   $anyos= $_REQUEST['anyos'];
   $descrip= $_REQUEST['desc'];
         */
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
	public static boolean uploadFoto(String nameFoto, byte[] bitmap){       
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(Constants.UPLOAD);	            
        MultipartEntity mpEntity = new MultipartEntity();           
        //ContentBody foto = new FileBody(file, "image/jpeg");
       // ByteArrayBody bab = new ByteArrayBody(bitmapdata, "prueba.jpg");
        ByteArrayBody bab = new ByteArrayBody(bitmap, "image/jpeg", nameFoto);
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
}

	public static boolean uploadFotoEnMovil(String nameFoto, byte[] bitmapbyte){       
		 		
		 
		 try{
			 File imagesFolder = new File(pathfotos);
			 if(!imagesFolder.exists())
				 imagesFolder.mkdirs();
			 //Log.i("pathfotos " ,pathfotos);
			 File image = new File(imagesFolder, nameFoto);
			 FileOutputStream fos = null;
			 fos = new FileOutputStream(image);
			 Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapbyte , 0, bitmapbyte .length);
			 bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
		     fos.flush();
		     //Log.i("devuelve image.getAbsolutePath()",image.getAbsolutePath());
		     return true;
		 }catch (Exception e) {
             e.printStackTrace();
        } 
        

       
		 
       
        return false;
}   
    public static Plantilla getPlantillaX(String nombrePlantilla)
    {
    	 Plantilla plan=null;
		 String nombre,portada,fondo1,fondo2,fondo3,fondo4,marco1,marco2="";
		 try {
			   
			   
			  
			   HttpGet httpGet = new HttpGet(Constants.URL_SERVER+obtener_plantillaX +"&nombreP="+nombrePlantilla);
			   HttpClient httpClient = new DefaultHttpClient();
			   HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
			   HttpEntity entity = response.getEntity();
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
			   JSONArray plantillas = jsonObject.getJSONArray("datos_plantillaX");
			  
			   for(int i = 0; i < plantillas.length(); i++) {
				   JSONObject plantilla = plantillas.getJSONObject(i);
				   
				    nombre = plantilla.getString("nombre");
				    portada=plantilla.getString("portada");
				    fondo1 = plantilla.getString("fondo1");
				    fondo2 = plantilla.getString("fondo2");
				    fondo3=plantilla.getString("fondo3");
				    fondo4= plantilla.getString("fondo4");
				    marco1=plantilla.getString("marco1");
				    marco2=plantilla.getString("marco2");
				   
				    
				    plan = new Plantilla(nombre,portada,fondo1,fondo2,fondo3,fondo4,marco1,marco2);
				   
				     
				   
				    //m_restaurantes.add(res);
				    }
			   /*
			    * Guardamos en respuesta, el json_encode() que nos da el servicio WEB PHP
			    * Si respuesta es != de null es por que trae el objeto json con datos
			    * y utilizamos el metodo inputStreamToString, para descomponerlo y poder
			    * mostrarlo en el ListView.
			    */

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return plan;
    }
  
    public static ArrayList<Plantilla> getPlantillas()
	{
		 ArrayList<Plantilla> lista_plantillas = new ArrayList<Plantilla>();
		 
		
		 String nombre,portada,fondo1,fondo2,fondo3,fondo4,marco1,marco2="";
		
		 //String lati = "", lon = "";
		
		 try {
			   
			   
			   HttpGet httpGet = new HttpGet(Constants.URL_SERVER+obtener_plantillas);
			   HttpClient httpClient = new DefaultHttpClient();
			   HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
			   HttpEntity entity = response.getEntity();
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
			   JSONArray plantillas = jsonObject.getJSONArray("datos_plantillas");
			  
			   for(int i = 0; i < plantillas.length(); i++) {
				   JSONObject plantilla = plantillas.getJSONObject(i);
				   
				    nombre = plantilla.getString("nombre");
				    portada=plantilla.getString("portada");
				    fondo1 = plantilla.getString("fondo1");
				    fondo2 = plantilla.getString("fondo2");
				    fondo3=plantilla.getString("fondo3");
				    fondo4= plantilla.getString("fondo4");
				    marco1=plantilla.getString("marco1");
				    marco2=plantilla.getString("marco2");
				   
				    
				    Plantilla plan = new Plantilla(nombre,portada,fondo1,fondo2,fondo3,fondo4,marco1,marco2);
				    lista_plantillas.add(plan);
				     
				   
				    //m_restaurantes.add(res);
				    }
			   /*
			    * Guardamos en respuesta, el json_encode() que nos da el servicio WEB PHP
			    * Si respuesta es != de null es por que trae el objeto json con datos
			    * y utilizamos el metodo inputStreamToString, para descomponerlo y poder
			    * mostrarlo en el ListView.
			    */

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return lista_plantillas;
	}
    
	public static boolean  deleteBabyFoto(int idBabyFoto){
		
		String idFoto = String.valueOf(idBabyFoto);
		HttpClient httpclient;
		
		
		
		HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Constants.ONDELETE); // Url del Servidor      
        //Añadimos nuestros datos
        List<NameValuePair> postValues = new ArrayList<NameValuePair>(1);
        
        /*
         * $uriFoto = $_REQUEST['uri'];
   $dias = $_REQUEST['dias'];
   $meses =  $_REQUEST['meses'];
   $anyos= $_REQUEST['anyos'];
   $descrip= $_REQUEST['desc'];
         */
        postValues.add(new BasicNameValuePair("idFoto", idFoto)); 

       

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
		
	}
    

  
    



}
