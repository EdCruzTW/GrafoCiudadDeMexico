/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package clases;

import extra.Colors;
import grafo.Graph.Edge;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Clase que extiende {@code Edge<F>}, utiliza {@code Estacion}, se utiliza para
 * conectar estaciones.
 */
public class Via extends Edge<Estacion> {
    
    /**
     * Linea de metro a la que pertenece.
     */
    private int linea;
    
    /**
     * Grosor volátil(cambiará) de la linea.
     */
    private int grosor;
    
    /**
     * Color con el que se dibujará la línea basado en los colores originales de
     * las líneas del metro de CDMX.
     */
    private Color color;
    
    /**
     * Valor que determina la opacidad con la que se dibujará la linea entre
     * estaciones, cambiará si la estación está contenida o no luego de realizar
     * un recorrido.
     */
    private boolean active;
    
    /**
     * Crea una {@code Via} que conecta la {@code Estacion} {@code a} con la {@code b},
     * con un costo de {@code cost}.
     * Obtiene el color basándose en la paleta original del metro(Llama a {@code getColor()}
     * de {@code Metro}).
     * Agrega a las estaciones {@code a} y {@code b} las líneas de metro
     * correspondientes.
     * 
     * @param a {@code Estacion} origen de la arista.
     * @param b{@code Estacion} destino de la arista.
     * @param cost costo desde {@code a} hacia {@code b}.
     * @param linea Linea de metro a la que pertenece la {@code Via}.
     */
    public Via(Estacion a, Estacion b, int cost, int linea) {
        super(a, b, cost);
        this.linea = linea;
        color = Metro.getColor(linea);
        getA().addLinea(linea);
        getB().addLinea(linea);
        active = true;
        grosor = 5;
    }
    
    /**
     * Dibuja una línea recta entre la {@code Estacion} {@code a} y {@code b}, con
     * un grosor de {@code grosor}.
     * 
     * @param g objeto {@code Graphics2D} del panel en donde se dibujará.
     */
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(grosor));
        g.drawLine(getA().getxOnScreen(), getA().getyOnScreen(), getB().getxOnScreen(), getB().getyOnScreen());
    }
    
    /**
     * Dibuja una línea recta entre las dos estaciones {@code a} y {@code b} de
     * color {@code DIJKSTRA_COLOR} definido en {@code Colors}.
     * 
     * @param g objeto {@code Graphics2D} del panel en donde se dibujará.
     */
    public void drawCurrent(Graphics2D g) {
        g.setColor(Colors.DIJKSTRA_COLOR);
        g.setStroke(new BasicStroke(grosor + 2));
        g.drawLine(getA().getxOnScreen(), getA().getyOnScreen(), getB().getxOnScreen(), getB().getyOnScreen());
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(grosor - 1));
        g.drawLine(getA().getxOnScreen(), getA().getyOnScreen(), getB().getxOnScreen(), getB().getyOnScreen());
    }
    
    /**
     * Llamado cada vez que el zoom en pantalla cambia su valor.
     * Actualiza el valor de {@code grosor}.
     * 
     * @param zoom Valor nuevo de zoom.
     */
    public void zoomChanged(double zoom) {
        grosor = (int) (5 * zoom);
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Actualiza el valor de {@code active}.
     * Cambia la opacidad de la línea y la opacidad de las Estaciones que contiene.
     * 
     * @param active Valor nuevo de {@code active}.
     */
    public void setActive(boolean active) {
        int alpha = 15;
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), active ? 255 : alpha);
        getA().setActive(active, 2 * alpha);
        getB().setActive(active, 2 * alpha);
        this.active = active;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }
    
    /**
     * Devuelve el nombre de las estaciones contenidas ({@code a} y {@code b}).
     * 
     * @return El nombre de las estaciones contenidas.
     */
    @Override
    public String toString() {
        return getA().getNombre() + "-" + getB().getNombre();
    }
    
}
