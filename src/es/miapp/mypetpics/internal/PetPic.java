package es.miapp.mypetpics.internal;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase Tapento que contiene la informacion necesaria. Tapa y evento son objetos de esta clase. 
 * @version 1.0
 * @author Victoria Marcos
 */
public class PetPic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String uriFoto;
	private String dias;
	private String meses;
	private String a�os;
	private String desc;
	private String DateRegister;
	private boolean enAlbum;
	
	
	public PetPic(String urlfoto, String diass, String mesess,String a�oss,String descrip, String date)
	{
		//id = idFoto;
		uriFoto = urlfoto;
		dias = diass;
		meses = mesess;
		a�os = a�oss;
		desc = descrip;
		DateRegister = date;
		enAlbum = false;
		
		
		
	}

	public int getId()
	{
		return id;
	}
	public void setId(int idNuevo)
	{
		id = idNuevo;
	}
	
	public String getUriFoto()
	{
		return uriFoto;
	}

	public String getDescription()
	{
		return desc;
	}
	
	public String getDate()
	{
		return DateRegister;
	}



	public String getDias() {
		return dias;
	}
	public String getMeses() {
		return meses;
	}
	
	public String getA�os() {
		return a�os;
	}

	public boolean isEnAlbum() {
		return enAlbum;
	}

	public void setEnAlbum(boolean enAlbum) {
		this.enAlbum = enAlbum;
	}
	
}
