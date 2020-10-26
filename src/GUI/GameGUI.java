package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.BufferedInputStream;
import java.io.InputStream;

import Logica.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;

public class GameGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel contentPane;
	protected TimerPanel panelTimer;
	protected JButton[][] grilla;
	protected JToggleButton[][] grillaOpciones;
	protected int[][] grillaEquivalenciaNumerica;
	protected Game juego;
	protected int control;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameGUI ventana = new GameGUI();
					ventana.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameGUI() {
		setTitle("SUDOKU JOJO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1500, 1000);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(219, 112, 147));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelTablero = new JPanel();
		panelTablero.setBounds(10, 93, 857, 857);
		contentPane.add(panelTablero);
		panelTablero.setLayout(new GridLayout(9, 9, 0, 0));

		JLabel ZA_WARUDO = new JLabel(new ImageIcon(this.getClass().getResource("/Archivos/imagenes/ZA_WARUDO.gif")));
		ZA_WARUDO.setBounds(10, 93, 857, 857);

		JLabel ZA_WARUDO_FINAL = new JLabel(
				new ImageIcon(getClass().getResource("/Archivos/imagenes/finalZAWARUDO.png")));
		ZA_WARUDO_FINAL.setBounds(10, 93, 857, 857);

		JPanel panelOpciones = new JPanel();
		panelOpciones.setBorder(null);
		panelOpciones.setBackground(new Color(219, 112, 147));
		panelOpciones.setBounds(1066, 286, 280, 280);
		contentPane.add(panelOpciones);
		panelOpciones.setLayout(new GridLayout(3, 3, 5, 5));

		panelTimer = new TimerPanel();
		panelTimer.setBounds(1000, 11, 356, 100);
		contentPane.add(panelTimer);

		JLabel standSeleccionado = new JLabel("SELECCIONE SU STANDO");
		standSeleccionado.setHorizontalAlignment(SwingConstants.CENTER);
		standSeleccionado.setBounds(1066, 234, 280, 46);
		contentPane.add(standSeleccionado);

		crearElementos(panelTablero, panelOpciones, standSeleccionado);

		JButton botonPausa = new JButton("PARAR EL TIEMPO");
		botonPausa.setActionCommand("stop");
		botonPausa.setEnabled(false);
		botonPausa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Component boton = e.getComponent();
				String codigo = ((JButton) boton).getActionCommand();
				if (codigo == "stop") {
					botonPausa.setText("REANUDAR EL TIEMPO");
					panelTimer.stop();
					panelTablero.setVisible(false);

					botonPausa.setActionCommand("resume");

					try {

						InputStream audioSrc = getClass().getResourceAsStream("/Archivos/audio/ZA_WARUDO.wav");
						InputStream bufferedIn = new BufferedInputStream(audioSrc);

						Clip clip = AudioSystem.getClip();
						clip.open(AudioSystem.getAudioInputStream(bufferedIn));
						clip.start();
						contentPane.add(ZA_WARUDO);

						Timer t = new Timer(3500, new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								contentPane.remove(ZA_WARUDO);
								contentPane.add(ZA_WARUDO_FINAL);

							}
						});
						t.setRepeats(false);
						t.start();
						bufferedIn.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
				if (codigo == "resume") {
					botonPausa.setText("PARAR EL TIEMPO");
					panelTimer.start();
					panelTablero.setVisible(true);
					botonPausa.setActionCommand("stop");

					try {

						InputStream audioSrc = getClass().getResourceAsStream("/Archivos/audio/RESUME_TIME.wav");
						InputStream bufferedIn = new BufferedInputStream(audioSrc);

						Clip clip = AudioSystem.getClip();
						clip.open(AudioSystem.getAudioInputStream(bufferedIn));
						clip.start();
						contentPane.add(ZA_WARUDO);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					contentPane.remove(ZA_WARUDO);
					contentPane.remove(ZA_WARUDO_FINAL);
				}

			}
		});
		botonPausa.setBackground(Color.WHITE);
		botonPausa.setBounds(1226, 720, 135, 46);
		botonPausa.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		contentPane.add(botonPausa);

		JButton botonIniciar = new JButton("INICIAR");
		botonIniciar.setActionCommand("i");
		botonIniciar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (botonIniciar.isEnabled()) {
					Component boton = e.getComponent();
					String codigo = ((JButton) boton).getActionCommand();
					if (codigo == "i") {
						panelTimer.start();
						inicio();
						botonIniciar.setActionCommand("giveup");
						botonIniciar.setText("RENDIRSE");
						botonPausa.setEnabled(true);

					}
					if (codigo == "giveup") {
						rendirse();
					}
				}
			}
		});
		botonIniciar.setBounds(1066, 720, 135, 46);
		botonIniciar.setBackground(new Color(255, 255, 255));
		botonIniciar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		contentPane.add(botonIniciar);
	}

	public void inicio() {
		Cell celda;
		juego.crearJuego();
		for (int f = 0; f < grilla.length; f++)
			for (int c = 0; c < grilla[0].length; c++) {

				celda = juego.getCelda(f, c);

				if (celda.getValor() == 0)
					grilla[f][c].setEnabled(true);

				grilla[f][c].setBackground(new Color(255, 255, 255));
				grilla[f][c].setIcon(celda.getContenedorGrafico().getImagen());
				grilla[f][c].setDisabledIcon(celda.getContenedorGrafico().getImagen());
				reDimensionar(grilla[f][c], celda.getContenedorGrafico().getImagen());
			}
	}

	protected void rendirse() {
		int h = panelTimer.getHoras();
		int m = panelTimer.getMinutos();
		int s = panelTimer.getSegundos();
		String tiempo = h + ":" + m + ":" + s;
		panelTimer.stop();

		Component[] componentes = contentPane.getComponents();
		for (int i = 0; i < componentes.length; i++)
			componentes[i].setEnabled(false);
		JOptionPane.showMessageDialog(new JFrame(), "Yare yare daze, usted ha durado: " + tiempo, "Dialog",
				JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	protected void crearTablero(JPanel panelTablero) {
		grilla = new JButton[juego.getCantFilas()][juego.getCantColumnas()];
		for (int f = 0; f < grilla.length; f++)
			for (int c = 0; c < grilla[0].length; c++) {
				crearBoton(f, c);
				grilla[f][c].setEnabled(false);
				grilla[f][c].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						Component boton = e.getComponent();
						String codigo = ((JButton) boton).getActionCommand();
						logicaBoton(codigo, SwingUtilities.isRightMouseButton(e));
					}
				});
				panelTablero.add(grilla[f][c]);
			}
	}

	protected void crearOpciones(JPanel panelBotones, JLabel standSeleccionado) {
		grillaOpciones = new JToggleButton[juego.getCantElementos()][juego.getCantElementos()];

		for (int f = 0; f < grillaOpciones.length; f++)
			for (int c = 0; c < grillaOpciones[0].length; c++) {

				crearToggle(f, c);
				panelBotones.add(grillaOpciones[f][c]);

				grillaOpciones[f][c].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (SwingUtilities.isLeftMouseButton(e)) {
							Component botonT = e.getComponent();
							String codigo = ((JToggleButton) botonT).getActionCommand();
							logicaToggle(codigo, standSeleccionado);
						}

					}
				});
			}
	}

	public void crearElementos(JPanel panelTablero, JPanel panelOpciones, JLabel standSeleccionado) {
		juego = new Game();
		grillaEquivalenciaNumerica = new int[juego.getCantElementos()][juego.getCantElementos()];
		int aux = 1;
		for (int f = 0; f < juego.getCantElementos(); f++) {
			for (int c = 0; c < juego.getCantElementos(); c++) {
				grillaEquivalenciaNumerica[f][c] = aux;
				aux++;
			}
		}
		if (juego.getGrilla() != null) {
			String s1 = "Los elementos se eligen clickeando en el panel de la derecha primero.";
			String s2 = "Para borrar un casillero, presione click derecho.";
			String s = s1 + "\n" + s2;
			JOptionPane.showConfirmDialog(new JFrame(), "Consejos: \n" + s, "SUDOKU", JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			crearTablero(panelTablero);
			crearOpciones(panelOpciones, standSeleccionado);
		} else {
			JOptionPane.showConfirmDialog(new JFrame(), "SOLUCION INCORRECTA", "ERROR", JOptionPane.CLOSED_OPTION,
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}

	protected void crearBoton(int fila, int columna) {
		grilla[fila][columna] = new JButton();
		grilla[fila][columna].setActionCommand(fila + "," + columna);
		grilla[fila][columna].setEnabled(false);
		grilla[fila][columna].setBackground(Color.white);
		if (fila == 0 || fila == 3 || fila == 6) {
			if (columna == 0 || columna == 3 || columna == 6)
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.black));
			else if (columna == 8)
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.black));
			else
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.black));
		} else if (fila == 8) {
			if (columna == 0 || columna == 3 || columna == 6)
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.black));
			else if (columna == 8)
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.black));
			else
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.black));
		} else {
			if (columna == 0 || columna == 3 || columna == 6)
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.black));
			else if (columna == 8)
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.black));
			else
				grilla[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		}
	}

	protected void crearToggle(int fila, int columna) {
		int i = grillaEquivalenciaNumerica[fila][columna];
		grillaOpciones[fila][columna] = new JToggleButton(
				new ImageIcon(getClass().getResource("/Archivos/imagenes/i" + i + ".png")));
		grillaOpciones[fila][columna].setActionCommand(fila + "," + columna);
		grillaOpciones[fila][columna].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
	}

	protected void logicaBoton(String codigo, boolean clickDerecho) {
		String[] numeros = codigo.split(",");
		int f = Integer.parseInt(numeros[0]);
		int c = Integer.parseInt(numeros[1]);
		Cell celda = juego.getCelda(f, c);
		String[] repetidos;
		boolean ganado;
		int valorAnterior = celda.getValor();

		if (grilla[f][c].isEnabled()) 
			if (!clickDerecho)
				celda.setValor(juego.getBotonActivado());
			else
				celda.setValor(0);
		juego.actualizarContador(valorAnterior, celda.getValor());
		grilla[f][c].setIcon(celda.getContenedorGrafico().getImagen());
		reDimensionar(grilla[f][c], celda.getContenedorGrafico().getImagen());
		juego.buscarErrores(codigo);
		repetidos = juego.getErrores();
		actualizarImagenes(repetidos);
		if (repetidos == null)
			if (juego.getControl() == (juego.getCantFilas() * juego.getCantColumnas())) {
				ganado = juego.gano();
				if (ganado) {
					ganar();
				}
			}
	}

	protected void reDimensionar(JButton boton, ImageIcon grafico) {
		Image image = grafico.getImage();
		if (image != null) {
			Image newimg = image.getScaledInstance(boton.getWidth(), boton.getHeight(), java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
			boton.repaint();
		}
	}

	public void actualizarImagenes(String[] repetidas) {
		reset();
		if (repetidas != null)
			mostrarError(repetidas);
	}

	protected void reset() {
		for (int f = 0; f < juego.getCantFilas(); f++)
			for (int c = 0; c < juego.getCantColumnas(); c++) {
				juego.getCelda(f, c).setValor(juego.getCelda(f, c).getValor());
				reDimensionar(grilla[f][c], juego.getCelda(f, c).getContenedorGrafico().getImagen());
			}

	}

	protected void ganar() {
		int h = panelTimer.getHoras();
		int m = panelTimer.getMinutos();
		int s = panelTimer.getSegundos();
		String tiempo = h + ":" + m + ":" + s;
		panelTimer.stop();

		Component[] componentes = contentPane.getComponents();
		for (int i = 0; i < componentes.length; i++)
			componentes[i].setEnabled(false);
		JOptionPane.showMessageDialog(new JFrame(), "Usted ha ganado. \n Su tiempo fue de: " + tiempo,
				"Dialog", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	protected void mostrarError(String[] repetidas) {
		String[] numeros;
		int fila, columna;
		for (int i = 0; i < repetidas.length && repetidas[i] != null; i++) {
			numeros = repetidas[i].split(",");
			fila = Integer.parseInt(numeros[0]);
			columna = Integer.parseInt(numeros[1]);
			juego.getCelda(fila, columna).actualizarError(juego.getCelda(fila, columna).getValor());
			reDimensionar(grilla[fila][columna], juego.getCelda(fila, columna).getContenedorGrafico().getImagen());
		}
	}

	protected void logicaToggle(String codigo, JLabel standSeleccionado) {
		int seleccionado = juego.getBotonActivado();
		boolean encontre = false;
		String[] numeros = codigo.split(",");
		int fila = Integer.parseInt(numeros[0]);
		int columna = Integer.parseInt(numeros[1]);

		for (int fa = 0; fa < grillaEquivalenciaNumerica.length && !encontre; fa++) {
			for (int ca = 0; ca < grillaEquivalenciaNumerica.length && !encontre; ca++) {
				if (grillaEquivalenciaNumerica[fa][ca] == seleccionado) {
					encontre = true;
					grillaOpciones[fa][ca].setSelected(false);
					grillaOpciones[fa][ca].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
				}
			}
		}

		juego.setBotonActivado(grillaEquivalenciaNumerica[fila][columna]);
		grillaOpciones[fila][columna].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));

		seleccionado = juego.getBotonActivado();
		switch (seleccionado) {

		case 1:
			standSeleccionado.setText("『 BLACK SABBATH 』");
			break;

		case 2:
			standSeleccionado.setText("『 CREAM 』");
			break;

		case 3:
			standSeleccionado.setText("『 ECHOES ACT III 』");
			break;

		case 4:
			standSeleccionado.setText("『 GOLD EXPERIENCE 』");
			break;

		case 5:
			standSeleccionado.setText("『 HIGHWAY STAR 』");
			break;

		case 6:
			standSeleccionado.setText("『 KISS 』");
			break;

		case 7:
			standSeleccionado.setText("『 KING CRIMSON 』");
			break;

		case 8:
			standSeleccionado.setText("『 THE HAND 』");
			break;

		case 9:
			standSeleccionado.setText("『 WEATHER REPORT 』");
			break;

		default:
			standSeleccionado.setText("SELECCIONE SU STANDO");

		}

	}
}
