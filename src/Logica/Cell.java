package Logica;

import GUI.GraphicContainer;

public class Cell {
	protected Integer valor;
	protected GraphicContainer contenedorGrafico;
	
	public Cell() {
		contenedorGrafico = new GraphicContainer();
		valor = null;
	}
	
	public void actualizarError(int valor) {
		contenedorGrafico.actualizarError(valor);
	}
	
	public int getCantElementos() {
		return contenedorGrafico.getImagenes().length;
	}
	
	public Integer getValor() {
		return valor; 
	}
	
	public void setValor(Integer valor) {
		if (valor!=null && valor < getCantElementos()) {
			this.valor = valor;
			contenedorGrafico.actualizar(this.valor);
		}
		else 
			this.valor = null;
	}
	
	public GraphicContainer getContenedorGrafico() {
		return contenedorGrafico;
	}
	
	public void setEntidadGrafica(GraphicContainer entidadGrafica) {
		this.contenedorGrafico = entidadGrafica;
	}
}