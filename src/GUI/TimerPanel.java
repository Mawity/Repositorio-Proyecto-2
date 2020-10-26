package GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TimerPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected Timer timer;
	protected int sActual, mActual, hActual;
	protected ImageIcon[] imagenes;
	protected JLabel hh, h, mm, m, ss, s;
	
	public TimerPanel() {
		this.setBackground(Color.black);
		this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.white));
		JLabel espacio1 = new JLabel();
		JLabel espacio2 = new JLabel();
		hh = new JLabel();
		h = new JLabel();
		mm = new JLabel();
		m = new JLabel();
		ss = new JLabel();
		s = new JLabel();
		imagenes = setImagenes();
		organizar(espacio1, espacio2);
		sActual = 0;
		mActual = 0;
		hActual = 0;		
		timer = new Timer (1000, (ActionListener) new ActionListener ()
		{
			public void actionPerformed(ActionEvent e) {
				if(sActual<59) {
					sActual++;
				}	
				else if(mActual<59){
					mActual++;
					sActual=0;
					
				} else {
					hActual++;
					mActual = 0;
					sActual = 0;
				}
				s.setIcon(imagenes[sActual%10]);
				reDimensionar(s, imagenes[sActual%10]);
				ss.setIcon(imagenes[sActual/10]);
				reDimensionar(ss, imagenes[sActual/10]);
				m.setIcon(imagenes[mActual%10]);
				reDimensionar(m, imagenes[mActual%10]);
				mm.setIcon(imagenes[mActual/10]);
				reDimensionar(mm, imagenes[mActual/10]);
				h.setIcon(imagenes[hActual%10]);
				reDimensionar(h, imagenes[hActual%10]);
				hh.setIcon(imagenes[hActual/10]);
				reDimensionar(hh, imagenes[hActual/10]);
			}
		});
	}
	
	/**
	 * Agrega los diferentes labels al panel y se definen sus vistas gráficas.
	 * @param espacio1 - Label al que se le setea una imagen.
	 * @param espacio2 - Label al que se le setea una imagen.
	 */
	public void organizar(JLabel espacio1, JLabel espacio2) {	
		this.setLayout(new GridLayout(0, 8, 0, 0));
		
		hh.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.white));
		this.add(hh);
		
		h.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		this.add(h);
		
		espacio1.setIcon(imagenes[10]);
		espacio1.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		this.add(espacio1);
		
		mm.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		this.add(mm);
		m.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		this.add(m);
		
		espacio2.setIcon(imagenes[10]);
		espacio2.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		this.add(espacio2);
		ss.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		this.add(ss);
		
		s.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.white));
		this.add(s);
	}
	

	public void start() {
		timer.start();
	}
	

	public void stop() {
		timer.stop();
	}
	

	public void restart() {
		sActual = mActual = hActual = 0;
		timer.restart();
	}
	
	public int getSegundos() {
		return sActual;
	}
	
	public int getMinutos() {
		return mActual;
	}
	
	public int getHoras() {
		return hActual;
	}
	
	public ImageIcon[] setImagenes() {		
		ImageIcon[] toReturn = new ImageIcon[11];
		for(int i=0; i<11; i++)
			toReturn[i] = new ImageIcon(getClass().getResource("/Archivos/imagenes//"+i+".png"));
		return toReturn;
	}
	
	public ImageIcon[] getImagenes() {
		return imagenes;
	}
	
	protected void reDimensionar(JLabel label, ImageIcon grafico) {
		Image image = grafico.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(),  java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
			label.repaint();
		}
	}
}