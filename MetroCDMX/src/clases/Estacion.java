/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package clases;

import extra.Colors;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Clase que modela las estaciones del metro de CDMX.
 */
public class Estacion {
    
    /**
     * Nombre de la estación.
     */
    private String nombre;
    
    /**
     * Color que se mostrará al pintar la estación.
     */
    private Color color;
    
    /**
     * Lineas de metro que pasan por la estacion.
     */
    private ArrayList<Integer> linea;
    
    /**
     * Coordenadas absolutas(no cambiarán) en X, Y de la estación, basadas en
     * un mapa de 800px * 800px.
     */
    private int x, y;
    
    /**
     * Radio absoluto(no cambiará) de la Estación.
     */
    public final int RADIUS = 7;
    
    /**
     * Coordenadas volátiles(cambiarán) de la estación, varían dependiendo del
     * zoom. En base a estas se dibujará la estación en pantalla.
     */
    private int xOnScreen, yOnScreen;
    
    /**
     * Radio volátil(cambiará) de la estación, varía dependiendo del zoom. En
     * base a esta se dibujará la estación.
     */
    private int radiusOnScreen;
    
    /**
     * Variable que determina la opacidad con la que se dibuja la estación,
     * cambiará si la estación está contenida o no luego de realizar un recorrido.
     */
    private boolean active;
    
    /**
     * Constructor que se usará para comparar dos estaciones ({@code get()}).
     * 
     * @param nombre Nombre de la estación
     */
    private Estacion(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Crea una estación con las variables absolutas de la estación.
     * 
     * @param nombre Nombre de la estación.
     * @param x Coordenada en X absoluta de la estación.
     * @param y Coordenada en Y absoluta de la estación.
     */
    public Estacion(String nombre, int x, int y) {
        this.nombre = nombre;
        xOnScreen = this.x = x;
        yOnScreen = this.y = y;
        radiusOnScreen = RADIUS;
        linea = new ArrayList<>();
        color = Color.BLACK;
        active = true;
    }
    
    /**
     * Devuelve una estación con el nombre especificado, se usará para comparar
     * dos estaciones.
     * 
     * @param nombre Nombre de la estacion.
     * @return nueva Estacion con el nombre especificado.
     */
    public static Estacion get(String nombre) {
        return new Estacion(nombre);
    }
    
    /**
     * Dibuja la estación usando las coordenadas, {@code xOnScreen}, {@code yOnScreen}
     * y el radio {@code radiusOnScreen}.
     * La circunferencia será de color negro, y un poco más ancho en caso de que
     * más de una linea de metro pase por ésta, en otro caso, será del color de
     * la linea.
     * 
     * @param g objeto {@code Graphics2D} del panel en donde se dibujará.
     */
    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(esMultiEstacion() ? 2 : 1));
        g.fillOval(xOnScreen - radiusOnScreen, yOnScreen - radiusOnScreen, 2 * radiusOnScreen, 2 * radiusOnScreen);
        g.setColor(color);
        g.drawOval(xOnScreen - radiusOnScreen, yOnScreen - radiusOnScreen, 2 * radiusOnScreen, 2 * radiusOnScreen);
    }
    
    /**
     * Este método se deberá llamar cada vez que el zoom en pantalla cambie,
     * actualizará los valores de {@code xOnScreen}, {@code yOnScreen} y
     * {@code radiusOnScreen}
     * 
     * @param zoom El valor del nuevo zoom.
     */
    public void zoomChanged(double zoom) {
        xOnScreen = (int) (zoom * x);
        yOnScreen = (int) (zoom * y);
        radiusOnScreen = (int) (zoom * RADIUS);
    }
    
    /**
     * Devolverá {@code true} en caso de que el mouse esté dentro de la circunferencia
     * que se dibuja en pantalla.
     * 
     * @param evtX Coordenada en X del {@code MouseEvent}.
     * @param evtY Coordenada en Y del {@code MouseEvent}.
     * @return {@code true} si el mouse está dentro de la circunferencia.
     */
    public boolean mouseIn(int evtX, int evtY) {
        return Math.sqrt(Math.pow(xOnScreen - evtX, 2) + Math.pow(yOnScreen - evtY, 2)) <= radiusOnScreen;
    }
    
    /**
     * Método para mostrar el nombre y líneas de metro que pasan por la estación.
     * Usada cuando el mouse pasa por encima de una estación, y al realizar un
     * recorrido paso a paso.
     * Dibuja un réctangulo con bordes redondeados, y por encima el texto.
     * 
     * @param g objeto {@code Graphics2D} del panel en donde se dibujará.
     */
    public void drawNombre(Graphics2D g) {
        g.setFont(new Font("Century Gothic", 1, 14));
        FontMetrics metrics  = g.getFontMetrics();
        int width = metrics.stringWidth(nombre);
        boolean mitadX = x > Metro.SIZE / 2;
        boolean mitadY = y > Metro.SIZE / 2;
        g.setColor(Color.black);
        g.fillRoundRect(mitadX ? xOnScreen - width - 30 : xOnScreen, mitadY ? yOnScreen - 40 : yOnScreen, width + 30, 40, 8, 8);
        g.setColor(Color.white);
        g.drawString(nombre, xOnScreen + (mitadX ? - width - 15 : 15), yOnScreen + (mitadY ? -6 : 14));
        g.setFont(new Font("Century Gothic", 0, 14));
        
        int widthLinea = metrics.stringWidth(lineaToString());
        int pos = (width + 30) / 2 - (widthLinea / 2);
        g.drawString(lineaToString(), xOnScreen + (mitadX ? - widthLinea - pos : pos), yOnScreen + (mitadY ? -26 : 34));
    }
    
    /**
     * Dibuja una circunferencia de color rojo, cuando una estación es seleccionada
     * (se hace clic sobre ella).
     * 
     * @param g objeto {@code Graphics2D} del panel en donde se dibujará.
     */
    public void drawSelected(Graphics2D g) {
        g.setColor(Color.red);
        g.fillOval(xOnScreen - radiusOnScreen, yOnScreen - radiusOnScreen, 2 * radiusOnScreen, 2 * radiusOnScreen);
    }
    
    /**
     * Dibuja circunferencias de color {@code DIJKSTRA_COLOR} cuando una estación
     * es un destino / parada.
     * 
     * @param g objeto {@code Graphics2D} del panel en donde se dibujará.
     */
    public void drawDestino(Graphics2D g) {
        g.setColor(Colors.DIJKSTRA_COLOR);
        g.fillOval(xOnScreen - radiusOnScreen, yOnScreen - radiusOnScreen, 2 * radiusOnScreen, 2 * radiusOnScreen);
    }
    
    /**
     * Devuelve {@code true} si por la estación pasa más de una línea de metro.
     * 
     * @return {@code true} si pasa más de una línea.
     */
    public boolean esMultiEstacion() {
        return linea.size() > 1;
    }
    
    /**
     * Agrega, si aún no la contiene, la línea de metro especificada, llamado en
     * el constructor de {@Code Via}.
     * 
     * @param linea la Linea que se agregará.
     */
    public void addLinea(int linea) {
        if(this.linea.contains(linea)) {
            return;
        }
        this.linea.add(linea);
        color = esMultiEstacion() ? Color.black : Metro.getColor(linea);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxOnScreen() {
        return xOnScreen;
    }

    public int getyOnScreen() {
        return yOnScreen;
    }

    public int getRadiusOnScreen() {
        return radiusOnScreen;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Cambia el valor de {@code active} y la opacidad del color, llamado en
     * {@code setActive()} de {@code Via}.
     * 
     * @param active Valor nuevo de {@code active}.
     * @param alpha Valor nuevo de la opacidad.
     */
    public void setActive(boolean active, int alpha) {
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (active ? 255 : alpha));
        this.active = active;
    }

    /**
     * Devuelve el nombre y líneas de metro que pasan por la estación.
     * 
     * @return nombre y líneas de metro.
     */
    @Override
    public String toString() {
        return nombre + "," + lineaToString();
    }
    
    /**
     * Una lista de líneas de metro.
     * 
     * @return línea(s) de metro.
     */
    public String lineaToString() {
        if(linea.isEmpty()) {
            return "";
        }
        String str = linea.get(0) + "";
        for(int i = 1; i < linea.size(); i++) {
            int l = linea.get(i);
            str += ", " + (l == 10 ? "A" : l == 11 ? "B" : l + "");
        }
        return str;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    /**
     * Devuelve verdadero si ambas estaciones tienen el mismo nombre.
     * 
     * @param obj Otra {@code Estacion} a comparar.
     * @return {@code true} si el nombre es el mismo.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estacion other = (Estacion) obj;
        return Objects.equals(this.nombre, other.nombre);
    }
    
}
