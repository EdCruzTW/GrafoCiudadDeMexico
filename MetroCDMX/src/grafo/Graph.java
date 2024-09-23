/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package grafo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación de un grafo ponderado con la posibilidad de elegir si será dirigido
 * o no, implementa los recorridos más útiles de un grafo, requiere de {@code Edge<F>}
 * para crear las aristas.
 * 
 * @param <F> Elementos que almacenará el grafo
 */
public class Graph<F> implements Serializable {
    
    /**
     * Arreglo de todos los nodos que se guardarán.
    */
    private ArrayList<Node<F>> node;
    
    /**
     * Define si el grafo será dirigido, esto afecta todos los recorridos,
     * agregar, y eliminar elementos.
     */
    private boolean directed;
    
    /**
     * Construye un grafo vacío, dirigido.
     * 
     */
    public Graph(){
        this(true);
    }
    
    /**
     * Construye un grafo vacío, con {@code directed} especificado.
     * 
     * @param directed {@code true} para un grafo dirigido.
     */
    public Graph(boolean directed){
        this.directed = directed;
        node = new ArrayList<>();
    }
    
    /**
     * 
     * @return {@code true} si no contiene ningún nodo
     */
    public boolean isEmpty(){
        return node.isEmpty();
    }
    
    /**
     * Devuelve el elemento en la posición dada.
     * 
     * @param index índice del nodo a buscar
     * @return elemento en posición específica
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public F get(int index) {
        return node.get(index).data;
    }
    
    /**
     * Devuelve el índice del elemento buscado.
     * 
     * @param item Elemento buscado dentro de la lista {@code node}
     * @return Índice del objeto especificado, o -1 si la
     * lista no contiene el elemento
     */
    public int indexOf(F item){
        for(int i = 0 ; i < node.size() ; i ++) {
            if(node.get(i).data.equals(item)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Devuelve {@code true} si la lista {@code node} contiene el elemento
     * especificado.
     * 
     * @param item Elemento a buscar
     * @return {@code true} si la lista contiene el elemento especificado
     */
    public boolean contains(F item){
        return indexOf(item) != -1;
    }
    
    /**
     * Devuelve el nodo {@code Node<F>} con el elemento especificado.
     * 
     * @param item Elemento a buscar
     * @return El nodo {@code Node<F>} que contiene el elemento especificado
     */
    private Node<F> getNode(F item) {
        for(Node<F> n : node) {
            if(n.data.equals(item)) {
                return n;
            }
        }
        return null;
    }
    
    /**
     * Devuelve la Arista que va desde {@code a} hasta {@code b}.
     * 
     * @param a El elemento de origen
     * @param b El elemento destino
     * @return La arista {@code Edge<F>} que va desde {@code a} hasta {@code b},
     * o {@code null} si no existe
     */
    private Edge<F> getEdge(F a, F b) {
        Node<F> n = getNode(a);
        if(n == null) {
            return null;
        }
        for(Edge<F> e : n.adjacent) {
            if(e.node.data.equals(b)) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * En caso de no contenerlo aún, agrega el elemento {@code item} al final de
     * la lista {@code node}.
     * 
     * @param item El elemento a ser agregado
     * @return {@code true} si el elemento no existe en la lista
     */
    public boolean addNode(F item){
        return addNode(node.size(), item);
    }
    
    /**
     * En caso de no contenerlo aún, agrega el elemento {@code item} a la lista
     * {@code node}, en el índice {@code i} especificado.
     * 
     * @param i El índice en donde debe ser almacenado
     * @param item El elemento a ser agregado
     * @return {@code true} si el elemento no existe en la lista
     */
    public boolean addNode(int i, F item){
        if(contains(item)) {
            return false;
        }
        Node<F> n = new Node<>(item);
        node.add(i, n);
        return true;
    }
    
    /**
     * Elimina el elemento especificado de la lista {@code node}, además
     * eliminando todas las aristas {@code Edge<F>} con otros nodos.
     * 
     * @param item El elemento a ser eliminado
     * @return {@code true} si el elemento ya era contenido en esta lista
     */
    public boolean removeNode(F item){
        Node<F> n = getNode(item);
        if(n == null) {
            return false;
        }
        for(Node<F> n1 : node) {
            removeEdge(getEdge(item, n1.data));
            removeEdge(getEdge(n1.data, item));
        }
        return node.remove(n);
    }
    
    /**
     * Crea y agrega una arista {@code Edge<F>} entre {@code a} y {@code b}.
     * 
     * @param a El elemento de partida
     * @param b El elemento destino
     * @param cost El costo entre estos dos elementos
     * @return {@code true} si la lista {@code node} contiene ambos elementos y
     * esta arista no existe aún
     */
    public boolean addEdge(F a, F b, int cost) {
        return addEdge(new Edge<>(a, b, cost));
    }
    
    /**
     * Agrega una arista {@code Edge<F>} previamente instanciada, los elementos
     * de esta lista {@code F} deben ser de la misma clase que los de {@code A}
     * y {@code B}
     * de la arista.
     * 
     * @param edge La arista a agregar
     * @return {@code true} si la lista {@code node} contiene ambos elementos de
     * de la arista ({@code A}, {@code B}), y ésta no existe aún
     */
    public boolean addEdge(Edge<F> edge){
        Node<F> nA, nB;
        if ((nA =getNode(edge.getA())) == null || (nB = getNode(edge.getB())) == null){
            return false;
        }
        edge.node = nB;
        if (nA.containsEdge(edge.getB())){
            return false;
        }
        nA.adjacent.add(edge);
        if (! directed && ! edge.getA().equals(edge.getB())){
            Edge<F> edge2 = null;
            try {
                edge2 = (Edge<F>) edge.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
            edge2.setA(edge.getB());
            edge2.setB(edge.getA());
            edge2.node = nA;
            nB.adjacent.add(edge2);
        }
        return true;
    }
    
    /**
     * Elimina, si es posible, la arista especificada, previamente instanciada.
     * 
     * @param edge La arista a eliminar
     * @return {@code true} si la lista contiene ambos elementos de la arista
     * ({@code A}, {@code B}).
     */
    public boolean removeEdge(Edge<F> edge){
        Node nA, nB;
        if ((nA =getNode(edge.getA())) == null || (nB = getNode(edge.getB())) == null){
            return false;
        }
        if(! nA.removeEdge(edge.getB())) {
            return false;
        }
        if(! directed && ! edge.getA().equals(edge.getB())) {
            if(! nB.removeEdge(edge.getA())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Devuelve una lista de todos los elementos {@code F} contenidos en la
     * lista{@code node}.
     * 
     * @param list Lista en donde se almacenarán los elementos
     * @return Lista de todos los elementos
     */
    public List<F> getObject(List<F> list) {
        for(Node<F> n : node) {
            list.add(n.data);
        }
        return list;
    }
    
    /**
     * Devuelve una lista de todas las aristas {@code Edge<F>} que existen en el
     * grafo.
     * 
     * @param list Lista en donde se deberán guardar los datos
     * @return Lista de todas las aristas que existen
     */
    public List<Edge<F>> getEdge(List<Edge<F>> list) {
        for(Node<F> n : node) {
            for(Edge<F> e : n.adjacent) {
                list.add(e);
            }
        }
        return list;
    }
    
    /**
     * Devuelve una lista de aristas {@code Edge<F>} que conforman la búsqueda
     * primero en profundidad (de manera recursiva), iniciando en el elemento
     * especificado.
     * 
     * @param item Elemento {@code F} en donde debe iniciar el recorrido
     * @param list Lista en donde se deberán guardar los datos
     * @return Lista con los elementos que conforman la búsqueda primero en 
     * profundidad
     */
    public List<Edge<F>> DFS(F item, List<Edge<F>> list){
        Node<F> n;
        if (isEmpty() || (n = getNode(item)) == null){
            return list;
        }
        reestartValues();
        return auxDFS(n, list);
    }
    
    /**
     * Método auxiliar para obtener la búsqueda primero en profundidad, este se
     * llamará recursivamente.
     * 
     * @param n El nodo para el que se debe ejecutar el método
     * @param list Lista en donde se deberán guardar los datos
     * @return Lista actualizando los datos de la búsqueda
     */
    private List<Edge<F>> auxDFS(Node<F> n, List<Edge<F>> list){
        n.visited = true;
        for (Edge<F> e : n.adjacent){
            if (! e.node.visited){
                list.add(getEdge(n.data, e.node.data));
                auxDFS(e.node, list);
            }
        }
        return list;
    }
    
    /**
     * Crea una lista de aristas {@code Edge<F} conteniendo los elementos que
     * conforman la búsqueda primero en anchura (utilizando una cola 
     * {@code Queue<>}), iniciando en el elemento especificado.
     * 
     * @param item El elemento {@code F} en donde se deberá iniciar la búsqueda
     * @param list Lista en donde se guardarán los datos
     * @return Lista con los elementos {@code F} que conforman la búsqueda
     * primero en anchura
     */
    public List<Edge<F>> BFS(F item, List<Edge<F>> list){
        Node<F> start;
        if (isEmpty() || (start = getNode(item)) == null){
            return list;
        }
        Queue<Node<F>> q = new LinkedList<>();
        reestartValues();
        q.offer(start);
        start.visited = true;
        while (! q.isEmpty()){
            Node<F> temp = q.poll();
            for (Edge<F> e : temp.adjacent){
                if (! e.node.visited){
                    list.add(getEdge(temp.data, e.node.data));
                    q.offer(e.node);
                    e.node.visited = true;
                }
            }
        }
        return list;
    }
    
    /**
     * Crea una lista de aristas {@code Edge<F>}, de ser posible, del camino más
     * corto desde el elemento {@code a} hacia el {@code b} utilizando el
     * algoritmo de Dijkstra, ambos elementos deberán estar contenidos en la
     * lista {@code node}, y deberá existir al menos un camino que conecte estos
     * dos elementos.
     * 
     * @param a El elemento {@code F} en donde iniciará el recorrido
     * @param b El elemento {@code F} en donde finalizará el recorrido
     * @param list Lista en donde los elementos deberán ser guardados
     * @return Lista conteniendo los elementos que conforman el algoritmo de
     * Dijkstra, o una lista vacía.
     */
    public List<Edge<F>> shortestPath(F a, F b, List<Edge<F>> list){
        Node nA, nB;
        if (isEmpty() || (nA = getNode(a)) == null || (nB = getNode(b)) == null){
            return list;
        }
        reestartValues();
        nA.dv = 0;
        while (remaining() && ! nB.visited){
            Node<F> temp = lowestCost();
            if (temp == null){
                return list;
            }
            temp.visited = true;
            for (Edge<F> e : temp.adjacent){
                if (! e.node.visited && temp.dv + e.cost < e.node.dv){
                    e.node.dv = temp.dv + e.cost;
                    e.node.pv = temp;
                }
            }
        }
        Node<F> tt = nB;
        int ind = list.size();
        while (tt.pv != null){
            list.add(ind, getEdge(tt.pv.data, tt.data));
            tt = tt.pv;
        }
        return list;
    }
    
    /**
     * Crea, de ser posible, una lista de aristas {@code Edge<F>} que conforman
     * el árbol de expansión mínima del grafo, el grafo deberá ser conexo (todos
     * sus vértices están conectados por lo menos por un camino), utiliza el
     * algoritmo de Prim.
     * 
     * @param item El elemento en donde se deberá iniciar el recorrido
     * @param list Lista en donde e guardarán los elementos
     * @return Lista contentiendo los elementos que conforman el árbol de
     * expansión mínima
     */
    public List<Edge<F>> minimumSpanningTree(F item, List<Edge<F>> list){
        Node<F> start;
        if (isEmpty() || (start = getNode(item)) == null){
            return list;
        }
        reestartValues();
        start.dv = 0;
        while (remaining()){
            Node<F> temp = lowestCost();
            if (temp == null){
                return list;
            }
            temp.visited = true;
            for (Edge<F> e : temp.adjacent){
                if (! e.node.visited && e.cost < e.node.dv){
                    e.node.dv = e.cost;
                    e.node.pv = temp;
                }
            }
        }
        for (Node<F> n : node){
            if (n.pv != null){
                list.add(getEdge(n.data, n.pv.data));
            }
        }
        return list;        
    }
    
    /**
     * Esta función se deberá llamar antes de iniciar cualquier recorrido,
     * para todos los nodos {@code Node<F>}, cambia el valor de visitado({@code
     * visited}) a {@code false}, su costo total({@code dv}) a {@code INFINITE}
     * y su predecesor({@code pv}) a {@code null}.
     */
    private void reestartValues(){
        for(Node<F> n : node){
            n.visited = false;
            n.dv = Integer.MAX_VALUE;
            n.pv = null;
        }
    }
    
    /**
     * Itera sobre todos los nodos contenidos en {@code node} hasta encontrar un
     * nodo no visitado({@code visited} == {@code false}), devuelve {@code true}
     * en caso de existir al menos un nodo no visitado.
     * 
     * @return {@code true} en caso de haber nodo(s) no visitados
     */
    private boolean remaining(){
        for (Node<F> n : node){
            if (! n.visited){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Devuelve el nodo {@code Node<F>} no visitado({@code visited} == {@code
     * false}) con el menor costo {@code dv}.
     * 
     * @return El nodo no visitado con el menor costo, {@code null} si no existe
     */
    private Node<F> lowestCost(){
        int min = Integer.MAX_VALUE;
        Node<F> n = null;
        for (Node<F> n1 : node){
            if (n1.dv < min && ! n1.visited){
                min = n1.dv;
                n = n1;
            }
        }
        return n;
    }
    
    /**
     * Esta función deberá llamarse solo después de un algorito que dependa de
     * costos({@code minimumSpanningTree()} o {@code shortestPath()}), devuelve
     * el costo {@code dv} del nodo especificado, En caso de {@code shortestPath}
     * el costo total de llegar del elemento {@code a} hacia {@code b}, en caso
     * de {@code minimumSpanningTree()} el costo del árbol de expansión mínima.
     * 
     * @param item El elemento{@code F} del cual se devolverá el costo
     * @return Costo({@code dv}) del elemento especificado
     */
    public int getDistance(F item) {
        return getNode(item).dv;
    }

    /**
     * Devuelve {@code true} si el grafo es dirigido.
     * 
     * @return {@code true} si el grafo es dirigido
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * Devuelve un {@code String} representando los de elementos({@code F})
     * contenidos en la lista de nodos({@code node}).
     * 
     * @return Representación de los nodos contenidos en el grafo
     */
    @Override
    public String toString() {
        String str = "";
        for(Node<F> n : node) {
            str += "{" + n.data.toString() + "} ";
        }
        return str;
    }
    
    /**
     * Esta clase representará las conexiones entre los elementos({@code F}) del
     * grafo, en un grafo dirigido el camino de {@code a} hacia {@code b} será
     * diferente al camino de {@code b} hacia {@code a}.
     * 
     * @param <F> Clase de los elementos que serán guardados, deberán ser de la
     * misma clase que los de {@code Graph<F>}
     */
    public static class Edge<F> implements Cloneable {

        /**
         * Elemento desde donde inicia la arista.
         */
        private F a;
        
        /**
         * Elemento donde termina la arista.
         */
        private F b;
        
        /**
         * Costo desde {@code a} a {@code b}.
         */
        private int cost;
        
        /**
         * Nodo que contiene el elemento donde termina la arista.
         */
        private Node<F> node;

        /**
         * Crea una arista con costo 1.
         * 
         * @param a Elemento donde inicia la arista
         * @param b Elemento donde termina la arista
         */
        public Edge(F a, F b) {
            this(a, b, 1);
        }
        
        /**
         * Crea una arista ponderada de costo {@code cost}, que irá desde el
         * elemento {@code a} hacia {@code b}.
         * 
         * @param a Elemento donde inicia la arista
         * @param b Elemento donde termina la arista
         * @param cost Costo desde {@code a} hacia {@code b}
         */
        public Edge(F a, F b, int cost) {
            this.a = a;
            this.b = b;
            this.cost = cost;
        }
        
        public F getA() {
            return a;
        }

        public void setA(F a) {
            this.a = a;
        }

        public F getB() {
            return b;
        }

        public void setB(F b) {
            this.b = b;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
        
    }
    
    /**
     * Clase que almacena un elemento {@code F} y sus nodos adyacentes
     * {@code Edge<F>}.
     * 
     * @param <F> Clase del elemento que almacenará
     */
    private static class Node<F> implements Serializable {
        
        /**
         * Elemento almacenado en este nodo.
         */
        F data;
        
        /**
         * Lista de nodos adyacentes.
         */
        ArrayList<Edge<F>> adjacent;
        
        /**
         * Variable que se usará en recorridos, inicialmente {@code false}.
         */
        boolean visited;
        
        /**
         * Costo total del nodo, usado en recorridos.
         */
        int dv;
        
        /**
         * Nodo predecesor a este, usado en recorridos.
         */
        Node<F> pv;
        
        /**
         * Crea un nodo con el elemento especificado.
         * 
         * @param data Elemento que almacenará el nodo
         */
        Node(F data){
            this.data = data;
            adjacent = new ArrayList<>();
        }
        
        /**
         * Devuelve {@code true} si la lista de nodos adyacentes({@code adjacent})
         * contiene el elemento especificado.
         * 
         * @param data Elemento a buscar
         * @return {@code true} si contiene {@code data} en los nodos adyacentes
         */
        boolean containsEdge(F data) {
            for(Edge<F> e : adjacent) {
                if(e.node.data.equals(data)) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * Elimina un elemento dentro de los nodos adyacentes({@code adjacent}).
         * 
         * @param data Elemento a ser eliminado
         * @return {@code true} si el elemento existe dentro de {@code adjacent}
         */
        boolean removeEdge(F data) {
            int i = 0;
            for(Edge<F> e : adjacent) {
                if(e.node.data.equals(data)) {
                    adjacent.remove(i);
                    return true;
                }
                i ++;
            }
            return false;
        }
        
    }
    
}

