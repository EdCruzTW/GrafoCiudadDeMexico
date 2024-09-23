/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package extra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase que muestra la información después de obtener el camino más corto entre
 * dos {@code Estacion}.
 * 
 * Muestra la distancia en metros, el número de estaciones que se tendrá que
 * viajar y el número de transbordes.
 */
public class InfoRecorrido extends JPanel {
    
    /**
     * Crea un nuevo panel.
     */
    public InfoRecorrido() {
        initComponents();
    }
    
    /**
     * Da formato al panel, agrega tres {@code JLabel}, en los que se mostrarán
     * los datos.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setFocusable(false);
        setPreferredSize(new Dimension(220, 100));
        setBackground(Color.white);
        distancia = new JLabel();
        distancia.setFont(new Font("Century Gothic", 0, 14));
        distancia.setBackground(Color.white);
        distancia.setHorizontalAlignment(JLabel.CENTER);
        distancia.setPreferredSize(new Dimension(220, 33));
        
        estaciones = new JLabel();
        estaciones.setFont(new Font("Century Gothic", 0, 14));
        estaciones.setBackground(Color.white);
        estaciones.setHorizontalAlignment(JLabel.CENTER);
        estaciones.setPreferredSize(new Dimension(220, 33));
        
        transbordes = new JLabel();
        transbordes.setFont(new Font("Century Gothic", 0, 14));
        transbordes.setBackground(Color.white);
        transbordes.setHorizontalAlignment(JLabel.CENTER);
        transbordes.setPreferredSize(new Dimension(220, 33));
        
        add(distancia, BorderLayout.NORTH);
        add(estaciones, BorderLayout.CENTER);
        add(transbordes, BorderLayout.SOUTH);
        
        endRecorrido();
    }
    
    /**
     * Este método se deberá llamar cada vez que se muestren todas las estaciones.
     */
    public void endRecorrido() {
        distancia.setText("Distancia: -");
        estaciones.setText("Estaciones: -");
        transbordes.setText("Transbordes: -");
        updateUI();
    }
    
    /**
     * Este método se deberá llamar cada vez que se inicie un recorrido de camino
     * más corto.
     * 
     * @param dist La distancia en metros desde la {@code Estacion} a hacia b
     * @param n El número de estaciones que se viajará
     * @param t el número de transbordes que se tendrán que hacer
     */
    public void startRecorrido(int dist, int n, int t) {
        distancia.setText("Distancia: " + dist + " m.");
        estaciones.setText("Estaciones: " + n);
        transbordes.setText("Transbordes: " + t);
        updateUI();
    }
    
    private JLabel distancia;
    private JLabel estaciones;
    private JLabel transbordes;
    
}
