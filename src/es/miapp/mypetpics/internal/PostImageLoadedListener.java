package es.miapp.mypetpics.internal;

import com.itextpdf.text.Image;

import android.graphics.Bitmap;
import android.widget.ImageView;
import es.miapp.mypetpics.internal.ImageThreadLoader.ImageLoadedListener;

/**
 * Clase para la gestion del oyente de la carga de imagenes
 * Implementa la interfaz ImageLoadedListener
 * @see es.miapp.babybook.internal.ImageThreadLoader.ImageLoadedListener
 * @version 1.0
 * @author Victoria Marcos
 */
public class PostImageLoadedListener implements ImageLoadedListener {
	private ImageView m_image = null;
	private Image i_image = null;

	public PostImageLoadedListener(ImageView image) {
		m_image = image;
	}
	public PostImageLoadedListener(Image image) {
		i_image = image;
	}


	@Override
	public void imageLoaded(Bitmap imageBitmap) {
		m_image.setImageBitmap(imageBitmap);
	}
}
