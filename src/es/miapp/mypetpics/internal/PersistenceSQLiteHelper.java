package es.miapp.mypetpics.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase para la gestion de la base de datos. Extiende de SQLiteOpenHelper
 * Crea las tablas de datos correspondientes
 * @see android.database.sqlite.SQLiteOpenHelper
 * @version 1.0
 * @author Victoria Marcos
 */
public class PersistenceSQLiteHelper extends SQLiteOpenHelper {

    //sentencias para crear las tablas de Poi y TABLE_VOTO
    String CREATE_FOTO_TABLE = "CREATE TABLE TABLE_FOTO (id_foto INTEGER PRIMARY KEY autoincrement not null, urlFoto TEXT, dias TEXT, meses TEXT, years TEXT, descrip TEXT, date TEXT); ";

	//String sqlCreateIndex_fotos_date = "CREATE INDEX index_fotos_date ON TABLE_FOTO ( date ASC );";
	
	String CREATE_ALBUM_TABLE = "CREATE TABLE TABLE_ALBUM (id_album INTEGER PRIMARY KEY autoincrement not null, url_album TEXT, titulo TEXT, numeroFotos TEXT,nombrePlantilla TEXT,fecha TEXT);";
	String CREATE_ALBUM_FOTO_TABLE = "CREATE TABLE TABLE_ALBUM_FOTO (id_album INTEGER, id_foto INTEGER);";
   
 
    public PersistenceSQLiteHelper(Context contexto, String nombre,
                               CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_FOTO_TABLE);       
        //db.execSQL(sqlCreateIndex_fotos_date); 
        db.execSQL(CREATE_ALBUM_TABLE);
        db.execSQL(CREATE_ALBUM_FOTO_TABLE);
        
        
        
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

     
        db.execSQL( "DROP TABLE IF EXISTS TABLE_FOTO ; ");
        db.execSQL("DROP TABLE IF EXISTS TABLE_ALBUM ;");
        db.execSQL("DROP TABLE IF EXISTS TABLE_ALBUM_FOTO ;");
 

        onCreate(db);
    }
}
