package es.miapp.mypetpics.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase Tapento que contiene la informacion necesaria. Tapa y evento son objetos de esta clase. 
 * @version 1.0
 * @author Victoria Marcos
 */
public class AlbumFoto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String titulo;
	private String uriAlbum;
	private ArrayList<Integer> listaFotos; //contiene la lista de ids de las fotos que componen el album
	private String DateRegister;
	private String numeroFotos;
	private String nombrePlantilla;
	
	
	public AlbumFoto( String urlAlbum, String tit, String numFotos, String nombrePlantilla, String date)
	{
		
		this.uriAlbum = urlAlbum;
		this.titulo = tit;		
		this.DateRegister = date;
		//listaFotos = new ArrayList<Integer>();
		this.numeroFotos =numFotos;
		this.setNombrePlantilla(nombrePlantilla);
		
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int idNew)
	{
		id=idNew;
	}
	
	public String getUriAlbum()
	{
		return uriAlbum;
	}


	
	public String getDate()
	{
		return DateRegister;
	}



	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String tit) {
		this.titulo = tit;
	}
	public void insertarFoto(int idBf)
	{
		
		if(!listaFotos.contains(idBf)) listaFotos.add(idBf);
	}
	
	public boolean existeFoto(int idBf)
	{
		if(listaFotos.contains(idBf))return true;
		else return false;
	}
	
	public String getNumFotos()
	{
		return String.valueOf(listaFotos.size());
		
	}
	public String getListaFotos()
	{
		return listaFotos.toString();
	}

	public String getNumeroFotos() {
		return numeroFotos;
	}

	public void setNumeroPaginas(String numFotos) {
		this.numeroFotos = numeroFotos;
	}

	public String getNombrePlantilla() {
		return nombrePlantilla;
	}

	public void setNombrePlantilla(String nombrePlantilla) {
		this.nombrePlantilla = nombrePlantilla;
	}
	
	
}
