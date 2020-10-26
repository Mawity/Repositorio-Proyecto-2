package GUI;

import javax.swing.ImageIcon;

public class GraphicContainer {
	protected ImageIcon imagen;
	protected String[] imagenes;
	protected String[] imagenesError;
	
	
	public GraphicContainer() {
		imagen = new ImageIcon();
		setImagenes();
		setImagenesError();
	}
	
	public void actualizar(int indice) {
		if (indice < imagenes.length) {
			ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagenes[indice]));
			imagen.setImage(imageIcon.getImage());
		}
	}
	
	public void actualizarError(int indice) { 
		if (indice < imagenesError.length) {
			ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagenesError[indice]));
			imagen.setImage(imageIcon.getImage());
		}
	}
	
	public ImageIcon getImagen() {
		return imagen;
	}
	
	public void setImagen(ImageIcon imagen) {
		this.imagen = imagen;
	}
	
	public String[] getImagenes() {
		return imagenes;
	}
	
	public void setImagenes() {
		imagenes = new String[10];
		for(int x=0; x<imagenes.length; x++) {
			imagenes[x] = "/Archivos/imagenes/i" + x + ".png";
		}
	}
	
	public void setImagenesError() {
		imagenesError = new String[10];
		for(int x=0; x<imagenesError.length; x++) {
			imagenesError[x] = "/Archivos/imagenes/ie" + x + ".png";
		}
	}
	

}