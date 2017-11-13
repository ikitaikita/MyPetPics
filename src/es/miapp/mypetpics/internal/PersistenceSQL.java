package es.miapp.mypetpics.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Clase para la gestion de la base de datos 
 * @version 1.0
 * @author Victoria Marcos
 */
public class PersistenceSQL {

	private static final String DBNAME = "DBPFOTOS"; 
	
	
	private static int CURRENT_BBDD_VERSION = 1;


	public static boolean isAddedFoto(int idAlbum, int idFoto, Context context) {
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				context, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();


		Cursor cursor = db.rawQuery("select * from TABLE_ALBUM_FOTO where id_album= "+ idAlbum + " and id_foto=" +idFoto , null);

		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				db.close();
				return true;
			} else {
				db.close();
				return false;
			}
		} else{
			db.close();
			return false;}

	}

	/**
	 * Inserta un nuevo elemento Tapento en la tabla Poi
	 * @param context
	 * @param tapento, objeto tapendo a insertar
	 */
	public static int insertFoto(Context context, PetPic d) {

		int ultId =0;
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(context,
				DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			db.beginTransaction();
			try {
					ContentValues cv = new ContentValues();
					
				

					//cv.put("id_foto", d.getId());
					cv.put("urlFoto", d.getUriFoto());
					cv.put("dias", d.getDias());
					cv.put("meses", d.getMeses());
					cv.put("years", d.getAños());
					cv.put("descrip", d.getDescription());
					cv.put("date", d.getDate());
					long ultimoId = db.insert("TABLE_FOTO", null, cv);
					ultId = (int)ultimoId;
										
					//db.insert("TABLE_FOTO", null, cv);

					db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}

			// Cerramos la base de datos
			db.close();
			return ultId;
		}
		return ultId;
	}

	/**
	 * Inserta un nuevo elemento Tapento en la tabla Poi
	 * @param context
	 * @param tapento, objeto tapendo a insertar
	 */
	public static int insertAlbum(Context context, AlbumFoto a) {

		int ultId =0;
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(context,
				DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			db.beginTransaction();
			try {
					ContentValues cv = new ContentValues();
					cv.put("url_album", a.getUriAlbum());
					cv.put("titulo", a.getTitulo());
					cv.put("numeroFotos", a.getNumeroFotos());
					cv.put("nombrePlantilla",a.getNombrePlantilla());
					cv.put("fecha", a.getDate());
					
					long ultimoId = db.insert("TABLE_ALBUM", null, cv);
					ultId = (int)ultimoId;
					db.setTransactionSuccessful();
					
					
			} finally {
				db.endTransaction();
				
			}

			// Cerramos la base de datos
			db.close();
			return ultId;
		}
		return ultId;
	}
	/*
	 * metodo que añade un voto a la tabla de votos
	 * @param poiId
	 * @param contexto
	 */
	public static void addFotoAlbum(int idAlbum, int idFoto, Context context) {

		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(context,
				DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id_album", idAlbum);	
		values.put("id_foto", idFoto);	
		db.insert("TABLE_ALBUM_FOTO", null, values);		
	
		db.close();

	}

	/*
	 * metodo para obtener la lista de ids de los elementos de la tabla TABLE_FOTO  que son las fotos insertadas por el usuario
	 * @param contexto
	 * @return lista de identificadores
	 */
	public static ArrayList<Integer> getallIdsFotos(Context ctx) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select id_foto from TABLE_FOTO ", null);

		if (cursor.moveToFirst()) {
			do {
				Integer id = cursor.getInt(0);
				IDsList.add(id);
			} while (cursor.moveToNext());
		}
		db.close();

		return IDsList;

	}
	
	/*
	 * metodo que añade un voto a la tabla de votos
	 * @param poiId
	 * @param contexto
	 */
	public static void actualizarRutaFicheroAlbum(int idAlbum, String uriNew, Context context, int numeroFotos) {

		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(context,
				DBNAME, null, CURRENT_BBDD_VERSION);
		String numero = String.valueOf(numeroFotos);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		db.execSQL("UPDATE TABLE_ALBUM SET url_album='"+uriNew+"', numeroFotos='"+numero+"' WHERE id_album="+idAlbum );
		
		db.close();

	}
	/*
	 * metodo para obtener la lista de ids de los elementos de la tabla TABLE_FOTO  que son las fotos insertadas por el usuario
	 * @param contexto
	 * @return lista de identificadores
	 */
	public static AlbumFoto getAlbum(Context ctx, String title) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		AlbumFoto af =null;
		//ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select id_album,url_album,titulo,numeroFotos,nombrePlantilla, fecha from TABLE_ALBUM where titulo='"+title+"'", null);

		if (cursor.moveToFirst()) {
			do {
				Integer idAlbum = cursor.getInt(0);
				String  url_album = cursor.getString(1);
				String  titulo = cursor.getString(2);
				String numeroFotos =  cursor.getString(3);
				String nombrePlantilla = cursor.getString(4);
				String fecha =  cursor.getString(5);
				af = new AlbumFoto(url_album,titulo,numeroFotos,nombrePlantilla,fecha);
				
				af.setId(idAlbum);
				
				
			} while (cursor.moveToNext());
		}
		db.close();

		return af;

	}
	
	public static boolean deleteAlbum(Context ctx, int idAlbum) {
		// Creamos una lista de enteros
		boolean devolver = false;
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getWritableDatabase();
		db.execSQL("delete from TABLE_ALBUM where id_album="+idAlbum );
		db.close();
		devolver=true;

		return devolver;

	}
	
	public static boolean deleteFoto(Context ctx, String uriFoto) {
		// Creamos una lista de enteros
		boolean devolver = false;
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getWritableDatabase();
		db.execSQL("delete from TABLE_FOTO where urlFoto='"+uriFoto+"'" );
		
		db.close();
		devolver=true;

		return devolver;

	}
	public static boolean deleteFotoAlbum(Context ctx, int idAlbum) {
		// Creamos una lista de enteros
		boolean devolver = false;
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getWritableDatabase();
		/*Cursor cursor = db.rawQuery("select id_foto from TABLE_ALBUM_FOTO where id_album="+idAlbum , null);
		if (cursor.moveToFirst()) {
			do {
				Integer idFoto = cursor.getInt(0);
				AccessInterface.deleteBabyFoto(idFoto);
				db.execSQL("delete from TABLE_FOTO where id_foto="+idFoto);
								
				
			} while (cursor.moveToNext());
		}*/
	
		db.execSQL("delete from TABLE_ALBUM_FOTO where id_album="+idAlbum );
		db.close();
		devolver=true;

		return devolver;

	}
	

	
	
	/*
	 * metodo para obtener la lista de ids de los elementos de la tabla TABLE_FOTO  que son las fotos insertadas por el usuario
	 * @param contexto
	 * @return lista de identificadores
	 */
	public static ArrayList<AlbumFoto> getAllAlbums(Context ctx) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<AlbumFoto> listaAlbumes = new ArrayList<AlbumFoto>();
		//ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select id_album,url_album,titulo,numeroFotos,nombrePlantilla,fecha from TABLE_ALBUM" , null);

		if (cursor.moveToFirst()) {
			do {
				Integer idAlbum = cursor.getInt(0);
				String url_album = cursor.getString(1);
				String titulo = cursor.getString(2);
				String numeroFotos =  cursor.getString(3);
				String nombrePlantilla =  cursor.getString(4);
				String fecha =  cursor.getString(5);
				AlbumFoto af = new AlbumFoto(url_album,titulo,numeroFotos,nombrePlantilla,fecha);				
				af.setId(idAlbum);
				listaAlbumes.add(af);
				
				
			} while (cursor.moveToNext());
		}
		db.close();

		return listaAlbumes;

	}
	public static ArrayList<PetPic> getAllFotos(Context ctx) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<PetPic> listaFotos = new ArrayList<PetPic>();
		//ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select id_foto,urlFoto,dias,meses,years,descrip,date from TABLE_FOTO" , null);

		if (cursor.moveToFirst()) {
			do {
				Integer idFoto = cursor.getInt(0);
				String urlFoto = cursor.getString(1);
				String dias = cursor.getString(2);
				String meses = cursor.getString(3);
				String years =  cursor.getString(4);
				String descrip =  cursor.getString(5);
				String date =  cursor.getString(6);
				PetPic bf = new PetPic(urlFoto,dias,meses,years,descrip,date);
				bf.setId(idFoto);
				
				listaFotos.add(bf);
				
				
			} while (cursor.moveToNext());
		}
		db.close();

		return listaFotos;

	}
	public static ArrayList<String> getAllNameFotos(Context ctx, int idAlbum) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<String> listaFotos = new ArrayList<String>();
		//ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select urlFoto from TABLE_FOTO where id_foto in(select id_foto from TABLE_ALBUM_FOTO where id_album="+idAlbum + ")" , null);

		if (cursor.moveToFirst()) {
			do {
				
				String urlFoto = cursor.getString(0);
						
				
				listaFotos.add(urlFoto);
				
				
			} while (cursor.moveToNext());
		}
		db.close();

		return listaFotos;

	}
	public static ArrayList<String> getAllNameFotosAlbum(Context ctx) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<String> listaFotos = new ArrayList<String>();
		//ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select urlFoto from TABLE_FOTO where id_foto in(select id_foto from TABLE_ALBUM_FOTO)" , null);

		if (cursor.moveToFirst()) {
			do {
				
				String urlFoto = cursor.getString(0);
						
				
				listaFotos.add(urlFoto);
				
				
			} while (cursor.moveToNext());
		}
		db.close();

		return listaFotos;

	}
	public static AlbumFoto getUnicAlbum(Context ctx)
	{
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);
		SQLiteDatabase db = usdbh.getReadableDatabase();
		AlbumFoto af = null;
		
		Cursor cursor = db.rawQuery("select id_album,url_album,titulo,numeroFotos,nombrePlantilla,fecha from TABLE_ALBUM" , null);

		if (cursor.moveToFirst()) {
			do {
				Integer idAlbum = cursor.getInt(0);
				String url_album = cursor.getString(1);
				String titulo = cursor.getString(2);
				String numeroFotos =  cursor.getString(3);
				String nombrePlantilla =  cursor.getString(4);
				String fecha =  cursor.getString(5);
				af = new AlbumFoto(url_album,titulo,numeroFotos,nombrePlantilla,fecha);				
				af.setId(idAlbum);
				
				
				
			} while (cursor.moveToNext());
		}
		db.close();

		return af;
		
	}
	
	public static int getNumeroFotos(Context ctx, int idAlbum)
	{
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		int numeroFotos =0;
		//ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select count(id_foto) from TABLE_ALBUM_FOTO where id_album="+idAlbum, null);

		if (cursor.moveToFirst()) {
			do {
				 numeroFotos = cursor.getInt(0);
				
				
				
			} while (cursor.moveToNext());
		}
		db.close();
		return numeroFotos;
	}
	/*
	 * metodo para obtener la lista de ids de los elementos de la tabla TABLE_FOTO  que son las fotos insertadas por el usuario
	 * @param contexto
	 * @return lista de identificadores
	 */
	public static ArrayList<Integer> getallIdsAlbums(Context ctx) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<Integer> IDsList = new ArrayList<Integer>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select id_album from TABLE_ALBUM ", null);

		if (cursor.moveToFirst()) {
			do {
				Integer id = cursor.getInt(0);
				IDsList.add(id);
			} while (cursor.moveToNext());
		}
		db.close();

		return IDsList;

	}
	
	
	
	
	/*
	 * metodo para obtener la lista de ids de los elementos de la tabla TABLE_FOTO  que son las fotos insertadas por el usuario
	 * @param contexto
	 * @return lista de identificadores
	 */
	public static List<String> getallTitleAlbumes(Context ctx) {
		// Creamos una lista de enteros
		
		PersistenceSQLiteHelper usdbh = new PersistenceSQLiteHelper(
				ctx, DBNAME, null, CURRENT_BBDD_VERSION);

		SQLiteDatabase db = usdbh.getReadableDatabase();
		ArrayList<String> listaTitleAlbumes = new ArrayList<String>();

		// Selcccion de todas las Query
		Cursor cursor = db.rawQuery("select titulo from TABLE_ALBUM ", null);

		if (cursor.moveToFirst()) {
			do {
				String tit = cursor.getString(0);
				listaTitleAlbumes.add(tit);
			} while (cursor.moveToNext());
		}
		db.close();

		return listaTitleAlbumes;

	}
	

	
	/**
	 * Return "YYYY-MM-DD HH:MM:SS.SSS"
	 * 
	 * @param cal
	 * @return
	 */
	private static String getDatetimeSQLiteFormat(Date cal) {


		return String.format("%4s", cal.getYear() + 1900).replace(' ', '0')
				+ "-"
				+ String.format("%2s", cal.getMonth() + 1).replace(' ',
						'0')
				+ "-"
				+ String.format("%2s", cal.getDate()).replace(
						' ', '0')
				+ " "
				+ String.format("%2s", cal.getHours()).replace(
						' ', '0')
				+ ":"
				+ String.format("%2s", cal.getMinutes()).replace(' ',
						'0')
				+ ":"
				+ String.format("%2s", cal.getSeconds()).replace(' ',
						'0')
				+ "."
				+ String.format("%3s", 0)
						.replace(' ', '3').substring(3);

	}


}
