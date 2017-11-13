package es.miapp.mypetpics.internal;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
//import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;


public class BookApaisado {
	
	//private static String path = "C:\\Users\\vik\\Documents\\pruebasItext\\";
	private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText/";
	private static String pathfonts = "fonts/";
	private static String pathfotos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/petpics/";
	
	//private static Font fontHeader= new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	private  static BaseColor bcolor = new BaseColor(52,45,31);
	private  static BaseColor fcolor = new BaseColor(206,191,210);
	private  static BaseColor tcolor = new BaseColor(119,114,66);
	private  static BaseColor morado = new BaseColor(111,42,109);
	private  static BaseColor oliva = new BaseColor(26,58,12);
	
	private  static Font fontit = FontFactory.getFont(pathfonts+"million.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 26, Font.BOLDITALIC, tcolor);
	private  static Font fonbabytit = FontFactory.getFont(pathfonts+"hand.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20, Font.BOLD, bcolor);
	private  static Font fontheader = FontFactory.getFont(pathfonts+"hand.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.BOLD, bcolor);
	private  static Font fonthandafter = FontFactory.getFont(pathfonts+"hand.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.BOLD, oliva);
	private  static Font fontable = FontFactory.getFont(pathfonts+"drawing.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20, Font.BOLDITALIC, morado);
	private  static Font fontFooter= new Font(Font.FontFamily.COURIER,6, Font.BOLD);
	
	public static int izquierda = 1;
	public static int derecha =2;

	
	private static Rectangle pageSize = PageSize.A5.rotate();
	
	//Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLDITALIC, GrayColor.DARK_GRAY);
	
	
	public static final String TABLAMIZ = "marcoIzquierdo";
	public static final String TABLAMDE = "marcoDerecho";
	private static boolean masanchoqlargo = false;
	
	
	public BookApaisado(){
		
		
	}
	
	public static boolean createPDF(String nombrePDF, String tituloPDF, String portada)
    {
       
        //Rectangle pageSize = new Rectangle(200, 200); //ancho y alto
        
        //pageSize.setBackgroundColor(BaseColor.ORANGE);
       
        //Document doc = new Document(pageSize);
		Bitmap cachedImage = null;
        Document doc = new Document(pageSize, 60, 60,60, 60);
        
        File file = null;
         
         try {
                
                
                File dir = new File(path);
                    if(!dir.exists())
                        dir.mkdirs();
 
                //Log.i("PDFCreator" ,path);
                 
                     
                //File file = new File(dir, "sample.pdf");
                file = new File(dir, nombrePDF);
                FileOutputStream fOut = new FileOutputStream(file);
                PdfWriter writer = PdfWriter.getInstance(doc,fOut);
               // PdfWriter.getInstance(doc, fOut).setInitialLeading(5);
                  
                //open the document
                doc.open();
                doc.addTitle(tituloPDF);
                doc.addAuthor("autor");
                doc.addSubject("My Pet Pics");
                
                
                Paragraph p1 = new Paragraph(tituloPDF,fontit);
                p1.setAlignment(Paragraph.ALIGN_LEFT); 
                p1.setIndentationLeft(20);
                doc.add(p1);   
              
                 
                 Paragraph p2 = new Paragraph("My Pet Pics",fonbabytit);
                 p2.setAlignment(Paragraph.ALIGN_LEFT);
                 p2.setIndentationLeft(70);
                 doc.add(p2);
                 
                             
                 //añadir imagen de portada de fondo
                 //sacar imagen de servidor
                 String uri_portada = Constants.PORTADASFROMSERVER+ portada;
				 
				 URL portadaUrl = new URL(uri_portada);
		         HttpURLConnection conn = (HttpURLConnection) portadaUrl.openConnection();
		         conn.connect();
		         cachedImage = BitmapFactory.decodeStream(conn.getInputStream());	
		         ByteArrayOutputStream bos = new ByteArrayOutputStream();
				 cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
				 byte[] bitmapdata = bos.toByteArray();
                 
                 
                 
                 PdfContentByte canvas = writer.getDirectContentUnder();
                 Image image = Image.getInstance(bitmapdata);
                 image.scaleAbsolute(pageSize);
                 image.setAbsolutePosition(0, 0);
                 canvas.addImage(image);
               
                
          
                 doc.close();
                 
                 
                 return true;
                
                 
         } catch (DocumentException de) {
                de.printStackTrace();
         } catch (IOException e) {
                e.printStackTrace();
         } 
       
         return false;
         
    }

	public static String addPhotoAlbumNewPage(String uriAlbum, String nombreFoto, String descripcion, String afterPhoto , boolean hayfondo, String fondo, int position, boolean hayTabla,String formatoTabla, String marco) {
		Bitmap cachedImage = null;
		 String fichentrada = path+ uriAlbum;
		 UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			randomUUIDString = randomUUIDString.replace("-", "");
			if (randomUUIDString.length() > 10) randomUUIDString = randomUUIDString.substring(0,9); 
			randomUUIDString = randomUUIDString+"_mypetpicsbook"+ ".pdf";
		 
		 String fichsalida= path +  randomUUIDString;
		 String devuelve = "nada";
		 int rotation;
		
	
			
			try {
				 //Rectangle pageSize = new Rectangle(200, 200); //ancho y alto
				
				 Image image = Image.getInstance(pathfotos +nombreFoto);
				 //Log.i("ancho imagen entrada: ",String.valueOf(image.getScaledWidth()));
				 //Log.i("alto imagen entrada: ",String.valueOf(image.getScaledWidth()));
			     if(image.getScaledWidth()>image.getScaledHeight())
			     {
			    	 
			    	 masanchoqlargo = true;
			    	 formatoTabla = BookApaisado.TABLAMDE;
			    	 
			    	 
			     }    else masanchoqlargo =false;
		
				 Document document = new Document(pageSize,60, 60, 60, 60);
				 PdfReader reader = new PdfReader(fichentrada);
				 FileOutputStream outputStream = new FileOutputStream(fichsalida);
				 PdfWriter writer = PdfWriter.getInstance(document, outputStream);
				 int numPages = reader.getNumberOfPages();
				 document.open();				 
				 PdfContentByte cbb = writer.getDirectContent();
			
				for(int i=1; i<=numPages;i++)
				{
					
						
					PdfImportedPage page = writer.getImportedPage(reader, i); 
					rotation = reader.getPageRotation(i);
					  if (rotation == 90 || rotation == 270) 
                      { 
                          cbb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight()); 
                      } 
                      else 
                      { 
                          cbb.addTemplate(page, 1f, 0, 0, 1f, 0, 0); 
                      } 
					
					  if(i==1) document.newPage();
					  if(i<numPages) document.newPage();
						
					
					  if(i==numPages && i!=1 && position==izquierda  ) document.newPage();
					
					
				}
			
				 
				 
				 if(position==izquierda)
				 {
					 if(hayTabla)
					 {

						 if(formatoTabla.equals(TABLAMIZ))
						 {
							 PdfPTable table = crearTablaMIZ(nombreFoto, descripcion ,marco,Element.ALIGN_LEFT);
							 document.add(table);
						 }
						 
						 if(formatoTabla.equals(TABLAMDE))
						 {
							 PdfPTable table = crearTablaMDE(nombreFoto, descripcion,marco,Element.ALIGN_LEFT);
							 document.add(table);
						 }
						 
						 
					 }else
					 {
						 document.add(getHeader(descripcion,Element.ALIGN_LEFT));
						 //obtener foto
						 
						
						 
						/* String uri_imagen = Constants.URIFROMSERVER+ nombreFoto;
						 
						 URL imageUrl = new URL(uri_imagen);
				         HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				         conn.connect();
				         cachedImage = BitmapFactory.decodeStream(conn.getInputStream());
				         ByteArrayOutputStream bos = new ByteArrayOutputStream();
						 cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
						 
						 byte[] bitmapdata = bos.toByteArray();
						 Image foto = Image.getInstance(bitmapdata);*/
						 Image foto = Image.getInstance(pathfotos+ nombreFoto);
						 
			             foto.scaleToFit(250, 250);
			                //foto.setAbsolutePosition(120f, 90f);
			             foto.setAlignment(Element.ALIGN_LEFT);
			             document.add(foto);
					 }
					    
					   
		                document.add(getAfter(afterPhoto,Element.ALIGN_LEFT));
				 }
				 if(position==derecha)
				 {
					 float nuevoX = document.getPageSize().getWidth()/2 + 20;
					 float nuevoY = document.getPageSize().getHeight()/6;
					 hayTabla = false;
					
					
					 if(hayTabla)
					 {

						 if(formatoTabla.equals(TABLAMIZ))
						 {
							 
							 if(masanchoqlargo)
							 {
								 
								 cbb.beginText();
								 cbb.setFontAndSize(BaseFont.createFont(pathfonts+"hand.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED), 18);
								 
								
								 cbb.setTextMatrix(nuevoX, nuevoY);
								 cbb.setColorFill(bcolor);	
								 cbb.showText(descripcion + "   " + afterPhoto);
								 
								
								 cbb.endText();
								 
								 Image foto = Image.getInstance(path+nombreFoto);
					             foto.scaleToFit(200, 200);
					             
					             
					             foto.setAbsolutePosition(nuevoX, nuevoY+20);
					           
					             document.add(foto);
								
								
								
							 }
							 
							 else{
								 
								 PdfPTable table = crearTablaMIZ(nombreFoto, descripcion ,marco,Element.ALIGN_RIGHT);
								 table.setTotalWidth(200);
								 //table.writeSelectedRows(0,-1, nuevoX, nuevoY, cbb);
								 document.add(table);
							 }
							 
							
						 }
						 
						 if(formatoTabla.equals(TABLAMDE))
						 {
							 
							 if(masanchoqlargo)
							 {
								
								
								 cbb.beginText();
								 cbb.setFontAndSize(BaseFont.createFont(pathfonts+"hand.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED), 18);	
								
								 cbb.setTextMatrix(nuevoX, nuevoY);
								 cbb.setColorFill(bcolor);
								 cbb.showText(descripcion + "   " + afterPhoto);
								 cbb.endText();
								 
								 
								 Image foto = Image.getInstance(path+nombreFoto);
					             foto.scaleToFit(300, 200);
					             foto.setAbsolutePosition(nuevoX, nuevoY+20);
					             document.add(foto);
							     
								
							 }else
							 {
								 PdfPTable table = crearTablaMDE(nombreFoto, descripcion,marco,Element.ALIGN_RIGHT);
								 table.setTotalWidth(200);
								 //table.writeSelectedRows(0,-1, nuevoX, nuevoY, cbb);
								 
								 document.add(table);
							 }
							 
						 }
						 
						 
					 }else
					 {
						// document.add(getHeader(descripcion,Element.ALIGN_RIGHT));
						/* String uri_imagen = Constants.URIFROMSERVER+ nombreFoto;
						 
						 URL imageUrl = new URL(uri_imagen);
				         HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				         conn.connect();
				         cachedImage = BitmapFactory.decodeStream(conn.getInputStream());
				         ByteArrayOutputStream bos = new ByteArrayOutputStream();
						 cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
						 
						 byte[] bitmapdata = bos.toByteArray();
						 Image foto = Image.getInstance(bitmapdata);*/
						 //Log.i("nuevoX: ", String.valueOf(nuevoX));
						 //Log.i("nuevoY: ", String.valueOf(nuevoY));
						 cbb.beginText();
						 cbb.setFontAndSize(BaseFont.createFont(pathfonts+"hand.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED), 18);	
						
						 cbb.setTextMatrix(nuevoX, nuevoY);
						 cbb.setColorFill(bcolor);
						 cbb.showText(descripcion + "   " + afterPhoto);
						 cbb.endText();
						 
						 
						 
						 Image foto = Image.getInstance(pathfotos+ nombreFoto);
			             foto.scaleToFit(200, 200);
			             foto.setAbsolutePosition(nuevoX, nuevoY+20);
			            // foto.setAlignment(Element.ALIGN_RIGHT);
			             document.add(foto);
			               
					 }
					 
					 /*if(!masanchoqlargo)
					 {
						 document.add(getAfter(afterPhoto,Element.ALIGN_RIGHT)); //controlar
					 }*/
					 
		             document.add(getInformationFooter("My Pet Pics"));
				 }
                
                
               
				
                //document.add(getAfter(afterPhoto,position));          
                
				 
                if(hayfondo && (position==derecha))
                {
                	String uri_portada = Constants.PORTADASFROMSERVER+ fondo;
   				 
   				 	URL portadaUrl = new URL(uri_portada);
   				 	HttpURLConnection conn = (HttpURLConnection) portadaUrl.openConnection();
   				 	conn.connect();
   				 	cachedImage = BitmapFactory.decodeStream(conn.getInputStream());	
   				 	ByteArrayOutputStream bos = new ByteArrayOutputStream();
   				 	cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
   				 	byte[] bitmapdata = bos.toByteArray();
               	 
               	 PdfContentByte canvas = writer.getDirectContentUnder();
                    Image imgfondo = Image.getInstance(bitmapdata);
                    imgfondo.scaleAbsolute(pageSize);
                    imgfondo.setAbsolutePosition(0, 0);
                    canvas.addImage(imgfondo);
                }
                
                
				 document.close();
				 devuelve = randomUUIDString;
				 return devuelve;
			

			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (com.itextpdf.text.DocumentException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			//handler.sendEmptyMessage(messageReturn);
			return devuelve;
		
	}
	
	private static Paragraph getHeader(String header, int position) {
	     Paragraph paragraph = new Paragraph();
	     
	    Chunk chunk = new Chunk();
	  	   
	    paragraph.setAlignment(position);
	    
	    chunk.append( header + "\n");
	    //fontHeader.setColor(bcolor);
	    chunk.setFont(fontheader);
	    paragraph.add(chunk);
	    return paragraph;
	     }


	
	private static Paragraph getAfter(String after, int position) {
	     Paragraph paragraph = new Paragraph();
	    Chunk chunk = new Chunk();
	  paragraph.setAlignment(position);
	    chunk.append( after + "\n");
	    //fontHeader.setColor(bcolor);
	    chunk.setFont(fonthandafter);
	    paragraph.add(chunk);
	    return paragraph;
	     }
	private static Paragraph getInformationFooter(String informacion) {
	      Paragraph paragraph = new Paragraph();
	      Chunk chunk = new Chunk();
	     paragraph.setAlignment(Element.ALIGN_RIGHT);
	     
	     paragraph.setIndentationRight(20);
	    // paragraph.setAlignment(Element.ALIGN_BOTTOM);
	     chunk.append(informacion);
	     fontFooter.setColor(fcolor);
	     chunk.setFont(fontFooter);
	     paragraph.add(chunk);
	      return paragraph;
	       }
	

	public static PdfPTable crearTablaMIZ(String nombreImagen, String descripcion, String marco, int position ) throws DocumentException, BadElementException, MalformedURLException, IOException 
	{
		Bitmap cachedImage = null;
		 
        PdfPTable table = new PdfPTable(2);
       // table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(position);
       
        PdfPCell cell;
       
        
        //byte[] bitmapdata = bos.toByteArray();
       /* String uri_imagen = Constants.URIFROMSERVER+ nombreImagen;
		 
		 URL imageUrl = new URL(uri_imagen);
        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        cachedImage = BitmapFactory.decodeStream(conn.getInputStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
		 
		 byte[] bitmapdata = bos.toByteArray();
        Image image = Image.getInstance(bitmapdata);*/
        Image image = Image.getInstance(pathfotos+ nombreImagen);
        image.scaleToFit(200, 200);
        cell = new PdfPCell(image);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLDITALIC, GrayColor.DARK_GRAY);
        Paragraph p = new Paragraph(descripcion, fontable);
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(10);
        cell.addElement(p);
        
        String elmarco = Constants.PORTADASFROMSERVER+ marco;		 
        URL imageUrl = new URL(elmarco);
        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        cachedImage = BitmapFactory.decodeStream(conn.getInputStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
		 
		 byte[] bitmapdata = bos.toByteArray();
        Image image2 = Image.getInstance(bitmapdata);
        cell.setCellEvent(new ImageBackgroundEvent(image2));
        cell.setFixedHeight(image2.getScaledHeight() / image2.getScaledWidth());
        
       
        
        table.addCell(cell);
        return table;
    }

	public static PdfPTable crearTablaMDE(String nombreImagen, String descripcion, String marco, int position ) throws DocumentException, BadElementException, MalformedURLException, IOException 
	{
		Bitmap cachedImage = null;
        PdfPTable table = new PdfPTable(2);
       // table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
       
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(position);
        PdfPCell cell;
       
        //Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLDITALIC, GrayColor.DARK_GRAY);
        Paragraph p = new Paragraph(descripcion, fontable);
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(10);
        cell.addElement(p);
        
        
        String elmarco = Constants.PORTADASFROMSERVER+ marco;
		 
		 URL imageUrl = new URL(elmarco);
       HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
       conn.connect();
       cachedImage = BitmapFactory.decodeStream(conn.getInputStream());
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
		 
		 byte[] bitmapdata = bos.toByteArray();
        Image image2 = Image.getInstance(bitmapdata);
        
        cell.setCellEvent(new ImageBackgroundEvent(image2));
        cell.setFixedHeight(image2.getScaledHeight() / image2.getScaledWidth());
        table.addCell(cell);
        
       /* String uri_imagen = Constants.URIFROMSERVER+ nombreImagen;		 
		imageUrl = new URL(uri_imagen);
        conn = (HttpURLConnection) imageUrl.openConnection();
       conn.connect();
       cachedImage = BitmapFactory.decodeStream(conn.getInputStream());
       bos = new ByteArrayOutputStream();
		 cachedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
		 
		 bitmapdata = bos.toByteArray();
       Image image = Image.getInstance(bitmapdata);*/
        Image image = Image.getInstance(pathfotos+ nombreImagen);
        image.scaleToFit(200, 200);
        cell = new PdfPCell(image);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        return table;
    }

	
	

}
