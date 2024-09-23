/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package clases;

import grafo.Graph.Edge;
import grafo.Graph;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import visual.Window;

/**
 * Clase que administrará el grafo de estaciones, se encarga de dibujarlas, y de
 * administrar los eventos del mouse.
 */
public class Metro extends JPanel {
    
    /**
     * Linea A del metro de CDMX.
     */
    public static final int LINEA_A = 10;
    
    /**
     * Linea B del metro de CDMX.
     */
    public static final int LINEA_B = 11;
    
    /**
     * Tamaño original en el que se creó el grafo, se deberá ver de este tamaño
     * con un zoom de 1.0.
     */
    public static final int SIZE = 800;
    
    /**
     * Zoom mínimo en el que se dibujará el grafo.
     */
    public static final double MIN_ZOOM = 0.5;
    
    /**
     * Zoom máximo en el que se dibujará el grafo.
     */
    public static final double MAX_ZOOM = 2;
    
    /**
     * Valor del zoom actual.
     */
    private double zoom = 1;
    
    /**
     * Objeto que contendrá a este panel.
     */
    private Window parent;
    
    /**
     * Variable que decidirá si se dibujan los nombres y líneas de metro de las
     * estaciones.
     */
    private boolean mostrarNombres;
    
    /**
     * Objeto grafo que controla los recorridos.
     */
    private Graph<Estacion> metro;
    
    /**
     * Lista total de las estaciones, se dibujará siempre.
     * Usada para los eventos del mouse.
     */
    private ArrayList<Estacion> estacion;
    
    /**
     * Lista total de vias, se dibujará siempre.
     */
    private ArrayList<Edge<Estacion>> via;
    
    /**
     * Lista de estaciones que sean un "Destino" al hacer el recorrido de camino
     * más corto.
     */
    private ArrayList<Estacion> destinos;
    
    /**
     * Lista de distancias entre destinos al hacer el recorrido de camino más corto.
     */
    private ArrayList<Integer> costos;
    
    /**
     * Lista que contiene las Vias después de realizar un recorrido, se dibujará
     * sobre la lista total de Vias.
     */
    private ArrayList<Edge<Estacion>> viaRecorridos;
    
    /**
     * Estación que se actualizará cada vez que el mouse se mueve, null, si el
     * mouse no está sobre ninguna.
     */
    private Estacion currentEstacion;
    
    /**
     * Via que se mostrará después de iniciar el paso a paso luego de un recorrido,
     * se dibuja de un color diferente.
     */
    private Via currentVia;
    
    /**
     * Estación que se actualizará luego de hacer clic sobre alguna. Luego de 
     * seleccionarla, se podrá agregar a los destinos, o iniciar otro recorrido.
     */
    private Estacion selected;

    /**
     * Crea un panel con el metro específicado.
     * 
     * @param metro Grafo de CDMX.
     */
    public Metro(Graph<Estacion> metro) {
        this.metro = metro;
        initComponents();
        zoomChanged();
    }

    /**
     * Da formato al panel, inicializa objetos y agrega los eventos del mouse.
     */
    private void initComponents() {
        estacion = (ArrayList<Estacion>) metro.getObject(new ArrayList<>());
        via = (ArrayList<Edge<Estacion>>) metro.getEdge(new ArrayList<>());
        destinos = new ArrayList<>();
        costos = new ArrayList<>();
        viaRecorridos = new ArrayList<>();
        selected = currentEstacion = null;
        currentVia = null;
        mostrarNombres = true;
        
        setPreferredSize(new Dimension(SIZE, SIZE));
        setFocusable(false);
        setBackground(Color.white);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                metroMousePressed(evt);
            }
            @Override
            public void mouseMoved(MouseEvent evt) {
                metroMouseMoved(evt);
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }
    
    /**
     * Regresa el color de la línea basado en los colores del metro de CDMX.
     * 
     * @param linea línea de metro.
     * @return Color específico para cada línea.
     */
    public static Color getColor(int linea) {
        switch(linea) {
            case 1:
                return new Color(231,98,148,255);
            case 2:
                return new Color(0,100,168,255);
            case 3:
                return new Color(174,157,39,255);
            case 4:
                return new Color(111,183,174,255);
            case 5:
                return new Color(253,223,0,255);
            case 6:
                return new Color(204,0,51,255);
            case 7:
                return new Color(227,104,36,255);
            case 8:
                return new Color(0,156,108,255);
            case 9:
                return new Color(116,62,60,255);
            case 10:
                return new Color(163,39,192,255);
            case 11:
                return new Color(185,185,182,255);
            case 12:
                return new Color(185,158,81,255);
        }
        return Color.black;
    }
    
    /**
     * Llamado para mostrar nuevamente todas las estaciones, elimina los recorridos
     * y elimina la Estación seleccionada.
     */
    public void mostrarEstaciones() {
        for(Edge<Estacion> v : via) {
            ((Via) v).setActive(true);
        }
        currentVia = null;
        viaRecorridos.clear();
        destinos.clear();
        costos.clear();
        setSelected(null);
    }
    
    /**
     * Llamado para agregar el camino más corto entre dos Estacioes, normalmente
     * se llamará más de una vez, dependiendo del número de destinos.
     * 
     * @param a Estación origen.
     * @param b Estación destino.
     * @return {@code true} si el Recorrido se completa con éxito(no debería regresar
     * {@code false} nunca).
     */
    public boolean addDijkstra(Estacion a, Estacion b) {
        int size = viaRecorridos.size();
        viaRecorridos = (ArrayList<Edge<Estacion>>)metro.shortestPath(a, b, viaRecorridos);
        return viaRecorridos.size() != size;
    }
    
    /**
     * Regresa la distancia de un recorrido de camino más corto, debe ser llamado
     * inmediatamente después de ser ejecutado.
     * 
     * @return Distancia entre las dos estaciones.
     */
    public int distancia() {
        if(viaRecorridos.isEmpty()) {
            return 0;
        }
        int total = 0;
        for(Integer c : costos) {
            total += c;
        }
        return total;
    }
    
    /**
     * Devuelve el número de estaciones luego de un recorrido de camino más corto,
     * debe ser llamado inmediatamente después de realizarlo.
     * 
     * @return Número de estaciones que tomará el camino.
     */
    public int estaciones() {
        if(viaRecorridos.isEmpty()) {
            return 0;
        }
        return viaRecorridos.size();
    }
    
    /**
     * Devuelve el número de transbordes luego de realizar un recorrido de camino
     * más corto, debe ser llamado inmediatamente después de ser realizado.
     * 
     * @return El número de transbordes que se deberán hacer.
     */
    public int transbordes() {
        if(viaRecorridos.isEmpty()) {
            return 0;
        }
        int total = 0;
        int prev = ((Via) viaRecorridos.get(0)).getLinea();
        for(Edge<Estacion> v : viaRecorridos) {
            if(((Via) v).getLinea() != prev) {
                total++;
                prev = ((Via) v).getLinea();
            }
        }
        return total;
    }
    
    /**
     * Vacía las listas de los recorridos, se deberá llamar antes de hacer un recorrido
     * de cualquier tipo.
     */
    public void reestartRecorrido() {
        viaRecorridos.clear();
        destinos.clear();
        costos.clear();
    }
    
    /**
     * Variable utilizada para actualizar el valor de la Via actual, usada en recorridos.
     */
    private int currentViaIndex;
    
    /**
     * Variable usada para determinar la dirección en que se debe mover la Via actual.
     */
    private int mod;
    
    /**
     * Solo puede ser llamado luego de realizar un recorrido, para mostrar más a
     * detalle dicho recorrido.
     */
    public void iniciarPasoAPaso() {
        currentVia = (Via) viaRecorridos.get(0);
        currentViaIndex = 0;
        repaint();
    }
    
    /**
     * Solo puede ser llamado luego de iniciar el recorrido paso a paso, cambia
     * el valor de la Via actual.
     * 
     * @param mod Dirección a la que se moverá la Via actual.
     */
    public void mostrarSiguiente(int mod) {
        if(viaRecorridos.isEmpty()) {
            return;
        }
        int ind = currentViaIndex + (this.mod == mod ? mod : 0);
        this.mod = mod;
        if(ind < 0 || ind >= viaRecorridos.size()) {
            return;
        }
        currentVia = (Via) viaRecorridos.get(ind);
        currentViaIndex = ind;
        repaint();
    }
    
    /**
     * Sirve para agregar un destino en un recorrido de camino más corto, se usará
     * para identificar las Estaciones normales de los destinos.
     * 
     * @param d Estación que se agregará a los destinos.
     */
    public void addDestino(Estacion d) {
        destinos.add(d);
        costos.add(metro.getDistance(d));
    }
    
    /**
     * Oculta todas las Estaciones y Vias, para después mostrar solo las contenidas
     * en el recorrido realizado.
     */
    public void mostrarRecorrido() {
        for(Edge<Estacion> v : via) {
            ((Via) v).setActive(false);
        }
        currentVia = null;
        for(Edge<Estacion> v : viaRecorridos) {
            ((Via) v).setActive(true);
        }
        repaint();
    }
    
    /**
     * Inicia un recorrido de árbol de expansión mínima.
     * 
     * @param e Estacion en donde se deberá iniciar el recorrido.
     */
    public void iniciarPrim(Estacion e) {
        viaRecorridos = (ArrayList<Edge<Estacion>>)metro.minimumSpanningTree(e, new ArrayList<>());
        mostrarRecorrido();
    }
    
    /**
     * Inicia un recorrido de búsqueda primero en profundidad.
     * 
     * @param e Estación en donde se deberá iniciar el recorrido.
     */
    public void iniciarDFS(Estacion e) {
        viaRecorridos = (ArrayList<Edge<Estacion>>)metro.DFS(e, new ArrayList<>());
        mostrarRecorrido();
    }
    
    /**
     * Inicia un recorrido de búsqueda primero en anchura.
     * 
     * @param e Estación en donde se deberá iniciar el recorrido.
     */
    public void iniciarBFS(Estacion e) {
        viaRecorridos = (ArrayList<Edge<Estacion>>)metro.BFS(e, new ArrayList<>());
        mostrarRecorrido();
    }
    
    /**
     * Se llamará cada vez que el zoom cambie, ya sea por el deslizador, o por las 
     * flechas "Arriba" y "Abajo".
     * Reposicionará las estacioones, su tamaño y el grosor de las Vias.
     */
    private void zoomChanged() {
        setPreferredSize(new Dimension((int) (SIZE * zoom), (int) (SIZE * zoom)));
        for(Estacion e : metro.getObject(new ArrayList<>())){ 
            e.zoomChanged(zoom);
        }
        for(Edge<Estacion> v : via) {
            ((Via) v).zoomChanged(zoom);
        }
        repaint();
        updateUI();
    }
    
    /**
     * Se llama cada vez que el mouse es presionado, si se presiona con el botón
     * izquierdo, actualizará el valor de la estación seleccionada.
     * Si se hace clic en la Estación actulmente seleccionada, se deseleccionará.
     * 
     * @param evt Evento del mouse.
     */
    private void metroMousePressed(MouseEvent evt) {
        if(evt.getButton() == MouseEvent.BUTTON1) {
            if(currentEstacion == null) {
                return;
            }
            if(currentEstacion.equals(selected)) {
                setSelected(null);
                return;
            }
            setSelected(currentEstacion);
        }
    }

    /**
     * Actualiza el valor de la estación seleccionada, llama además a {@code parent}
     * y actualiza el valor de la Estación seleccionada en éste.
     * 
     * @param selected La estación sobre la cual se hizo clic.
     */
    private void setSelected(Estacion selected) {
        this.selected = selected;
        parent.updateSelected(selected);
        repaint();
    }
    
    /**
     * Llamado cada vez que el mouse se mueve sobre el panel, actualiza el valor
     * de la Estación actual({@code null} si el mouse no está sobre ninguna).
     * 
     * @param evt 
     */
    private void metroMouseMoved(MouseEvent evt) {
        for(Estacion e : estacion) {
            if(e.mouseIn(evt.getX(), evt.getY())) {
                if(e.equals(currentEstacion)) {
                    return;
                }
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                currentEstacion = e;
                repaint();
                return;
            }
        }
        if(currentEstacion != null) {
            setCursor(Cursor.getDefaultCursor());
            repaint();
            currentEstacion = null;
        }
    }
    
    /**
     * Método usado para dibujar las estaciones.
     * 
     * @param g Objeto {@code Graphics} en el que se dibujarán las estaciones.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D)g);
    }
    
    /**
     * Método que maneja la forma y orden en que se dibujan las Estaciones y Vias.
     * 
     * Primero se dibuja la lista de Vías totales.
     * 
     * Después la via actual(En caso de estar dentro del paso a paso de un recorrido).
     * 
     * Por encima de éstas, la lista de Estaciones totales.
     * 
     * Si hay alguna Estación seleccionada, se dibujará encima.
     * 
     * Luego se dibujarán los destinos / paradas del recorrido de camino más corto.
     * 
     * Después, si es que se deben mostrar los nombres, y si el mouse está sobre
     * una estación se dibuja {@code currentEstacion}.
     * 
     * Por encima de todas, si actualmente se muestran los nombres y el recorrido
     * paso a paso está habilitado, se dibujará, dependiendo de la dirección el
     * nombre de la Estación especificada.
     * 
     * @param g Objeto {@code Graphics} en el que se dibujarán las estaciones.
     */
    public void draw(Graphics2D g) {
        for(Edge<Estacion> e : via) {
            ((Via) e).draw(g);
        }
        if(currentVia != null) {
            currentVia.drawCurrent(g);
        }
        for(Estacion e : estacion) {
            e.draw(g);
        }
        if(selected != null) {
            selected.drawSelected(g);
        }
        for(Estacion e : destinos) {
            e.drawDestino(g);
        }
        if(mostrarNombres && currentEstacion != null) {
            currentEstacion.drawNombre(g);
        }
        if(mostrarNombres && currentVia != null) {
            if(mod > 0) {
                currentVia.getB().drawNombre(g);
            } else {
                currentVia.getA().drawNombre(g);
            }
        }
    }

    /**
     * Se actualiza el valor que decide si se dibujan los nombre, o no.
     * 
     * @param mostrarNombres Nuevo valor de {@code mostrarNombre}.
     */
    public void setMostrarNombres(boolean mostrarNombres) {
        this.mostrarNombres = mostrarNombres;
        repaint();
    }

    /**
     * Actualiza el valor de zoom y redimensiona los elementos del panel.
     * 
     * @param zoom Nuevo valor de {@code zoom}.
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        zoomChanged();
    }

    public Estacion getSelected() {
        return selected;
    }

    public void setParent(Window parent) {
        this.parent = parent;
    }
    
}
