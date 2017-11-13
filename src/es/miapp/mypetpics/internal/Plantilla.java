package es.miapp.mypetpics.internal;

import java.io.Serializable;
import java.util.ArrayList;

public class Plantilla implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String portada;	
	private String fondo1;
	private String fondo2;	
	private String fondo3;
	private String fondo4;
	private String marco1;
	private String marco2;
	
	public Plantilla(String nombre, String portada, String fondo1,String fondo2,String fondo3, String fondo4, String marco1, String marco2)
	{
		this.nombre = nombre;
		this.portada = portada;
		this.fondo1 = fondo1;
		this.fondo2 = fondo2;
		this.fondo3 = fondo3;
		this.fondo4 = fondo4;
		this.marco1 = marco1;
		this.marco2 = marco2;
		
		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPortada() {
		return portada;
	}
	public void setPortada(String portada) {
		this.portada = portada;
	}
	public String getFondo1() {
		return fondo1;
	}
	public void setFondo1(String fondo1) {
		this.fondo1 = fondo1;
	}
	public String getFondo2() {
		return fondo2;
	}
	public void setFondo2(String fondo2) {
		this.fondo2 = fondo2;
	}
	public String getFondo3() {
		return fondo3;
	}
	public void setFondo3(String fondo3) {
		this.fondo3 = fondo3;
	}
	public String getFondo4() {
		return fondo4;
	}
	public void setFondo4(String fondo4) {
		this.fondo4 = fondo4;
	}
	public String getMarco1() {
		return marco1;
	}
	public void setMarco1(String marco1) {
		this.marco1 = marco1;
	}
	public String getMarco2() {
		return marco2;
	}
	public void setMarco2(String marco2) {
		this.marco2 = marco2;
	}
	
	public ArrayList<String> getFondos()
	{
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(fondo1);
		lista.add(fondo2);
		lista.add(fondo3);
		lista.add(fondo4);
		return lista;
		
	}
	public ArrayList<String> getMarcos()
	{
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(marco1);
		lista.add(marco2);
	
		return lista;
		
	}

}
