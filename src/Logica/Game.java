package Logica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Game {

	protected static int cantFilas = 9;
	protected static int cantColumnas = 9;
	protected static int cantElementos = 3;
	protected Cell[][] grilla;
	protected int[][] grillaArchivo;
	protected Map<String, Integer> errores;
	protected int contador;
	protected int botonActivado;


	public Game() {
		boolean valido;
		botonActivado = 0;
		errores = new LinkedHashMap<String, Integer>();
		valido = leerArchivo();
		if (valido)
			valido = archivoEsCorrecto();
		if (valido)
			crearJuego();
		else
			grilla = null;
	}

	protected boolean leerArchivo() {
		boolean toReturn = true;
		String linea = "";
		String[] auxiliar;
		int fila = 0;
		int numero;
		try {
			InputStream in = Game.class.getClassLoader().getResourceAsStream("Archivos/texto/Sudoku.txt");
			InputStreamReader inr =new InputStreamReader(in);
			BufferedReader bfr = new BufferedReader(inr);
			grillaArchivo = new int[cantFilas][cantColumnas];
			while ((linea = bfr.readLine()) != null && toReturn) {
				auxiliar = linea.split(" ");
				if (auxiliar.length == cantColumnas)
					for (int i = 0; i < auxiliar.length && toReturn; i++) {
						numero = Integer.parseInt(auxiliar[i]);
						if (numero > 0 && numero <= 9)
							grillaArchivo[fila][i] = numero;
						else
							toReturn = false;
					}
				else
					toReturn = false;
				fila++;
			}
			bfr.close();
			toReturn = toReturn == true ? fila == cantFilas : toReturn;
		} catch (IOException e) {
			e.printStackTrace();
			toReturn = false;
		}
		return toReturn;
	}

	public Cell getCelda(int f, int c) {
		return grilla[f][c];
	}

	public int getCantFilas() {
		return cantFilas;
	}

	public int getCantColumnas() {
		return cantColumnas;
	}
	
	public int getCantElementos() {
		return cantElementos;
	}

	public Cell[][] getGrilla() {
		return grilla;
	}

	public int getControl() {
		return contador;
	}
	
	public int getBotonActivado() {
		return botonActivado;
	}
	

	public boolean archivoEsCorrecto() {
		boolean toReturn = true;
		for (int i = 0, j = 0; i < cantFilas && j < cantColumnas && toReturn; i++, j++)
			toReturn = chequearFila(i, grillaArchivo) && chequearColumna(j, grillaArchivo)
					&& chequearPanel(i, grillaArchivo);
		return toReturn;
	}

	protected boolean chequearFila(int fila, int m[][]) {
		boolean toReturn;
		String[] arregloControl = new String[cantColumnas];
		int valor;
		for (int c = 0; c < cantColumnas; c++) {
			valor = m[fila][c];
			if (valor != 0) {
				toReturn = arregloControl[valor - 1] == null;
				if (!toReturn) {
					errores.put(fila + "," + c, valor);
					errores.put(arregloControl[valor - 1], valor);
				}
				arregloControl[valor - 1] = fila + "," + c;
			}
		}
		return errores.isEmpty();
	}

	protected boolean chequearColumna(int columna, int m[][]) {
		boolean toReturn;
		String[] arregloControl = new String[cantFilas];
		int valor;
		for (int f = 0; f < cantFilas; f++) {
			valor = m[f][columna];
			if (valor != 0) {
				toReturn = arregloControl[valor - 1] == null;
				if (!toReturn) {
					errores.put(f + "," + columna, valor);
					errores.put(arregloControl[valor - 1], valor);
				}
				arregloControl[valor - 1] = f + "," + columna;
			}
		}
		return errores.isEmpty();
	}

	protected boolean chequearPanel(int panel, int m[][]) {
		boolean toReturn;
		String[] arr = new String[(cantFilas+cantColumnas)/2];
		int f, c;
		if(panel%3==0) {
			f = panel;
			c = 0;
		}
		else if(panel%3==1) {
			f = panel-1;
			c = 3;
		}
		else {
			f = panel-2;
			c = 6;
		}
		int valor;		
		for(int i=f; i<f+3; i++)
			for(int j=c; j<c+3; j++) {
				valor = m[i][j];
				if(valor!=0) {
					toReturn = arr[valor-1] == null;
					if(!toReturn) {
						errores.put(i+","+j, valor);
						errores.put(arr[valor-1], valor);
					}
					arr[valor-1] = i+","+j;
				}
			}		
		return errores.isEmpty();
	}

	public void crearJuego() {
		Random rand = new Random();
		int value;
		int valor;
		contador = 0;
		grilla = new Cell[cantFilas][cantColumnas];
		for (int f = 0; f < cantFilas; f++) {
			for (int c = 0; c < cantColumnas; c++) {
				grilla[f][c] = new Cell();
				valor = 0;
				value = rand.nextInt(2);
				if (value == 0) { 
					valor = grillaArchivo[f][c];
					contador++;
				}
				grilla[f][c].setValor(valor);
			}
		}
	}
	
	public void setBotonActivado(int botonActivado) {
		this.botonActivado = botonActivado;
	}

	public void actualizarContador(int valorAnterior, int valorActual) {
		
		if(valorAnterior==0 && valorActual!=0) {
			contador++;
		}else if(valorAnterior!=0 && valorActual==0){
			contador--;
		}
		
		
	}

	public void buscarErrores(String indice) {
		int[][] matrizAux = new int[cantFilas][cantColumnas];
		String[] erroresBorrar;
		int pos = 0;
		for (int f = 0; f < cantFilas; f++)
			for (int c = 0; c < cantColumnas; c++)
				matrizAux[f][c] = grilla[f][c].getValor();
		for (int i = 0, j = 0; i < cantFilas && j < cantColumnas; i++, j++) {
			chequearFila(i, matrizAux);
			chequearColumna(j, matrizAux);
			chequearPanel(i, matrizAux);
		}
		erroresBorrar = new String[errores.size()];
		for (String clave : errores.keySet())
			if (!seRepite(clave, matrizAux))
				erroresBorrar[pos++] = clave;
		for (int i = 0; i < erroresBorrar.length && erroresBorrar[i] != null; i++)
			errores.remove(erroresBorrar[i]);
	}

	public String[] getErrores() {
		String[] toReturn;
		toReturn = errores.size() == 0 ? null : new String[errores.size()];
		int pos = 0;
		for (String indice : errores.keySet())
			toReturn[pos++] = indice;
		return toReturn;
	}

	protected boolean seRepite(String indice, int[][] m) {
		String[] coordenadas = indice.split(",");
		int fila = Integer.parseInt(coordenadas[0]);
		int columna = Integer.parseInt(coordenadas[1]);
		boolean toReturn = false;
		int valor = m[fila][columna];
		for (int f = 0; f < cantFilas && !toReturn && valor != 0; f++)
			if (f != fila)
				toReturn = grilla[f][columna].getValor() == valor ? true : false;
		for (int c = 0; c < cantColumnas && !toReturn && valor != 0; c++) 
			if (c != columna) 
				toReturn = grilla[fila][c].getValor() == valor ? true : false;
		toReturn = !toReturn ? seRepiteEnPanel(fila, columna, m) : toReturn;
		return toReturn;
	}

 	protected boolean seRepiteEnPanel(int fila, int columna, int[][] m) {
 		boolean toReturn = false;
 		int f = fila - fila%3; 
		int c = columna - columna%3; 
		int valor = m[fila][columna];
		for(int i=f; i<f+3 && !toReturn && valor!=0; i++)
			for(int j=c; j<c+3 && !toReturn; j++)
				if(i!=fila && j!=columna) 
					toReturn = m[i][j]==valor ? true : toReturn;
		return toReturn;
 	}
	
	public boolean gano() {
		return errores.isEmpty();
	}

}
