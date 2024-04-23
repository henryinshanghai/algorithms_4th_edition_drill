package com.henry.graph_chapter_04.no_direction_graph_01.represent_graph; /******************************************************************************
 *  Compilation:  javac Graph.java        
 *  Execution:    java Graph input.txt
 *  Dependencies: Bag.java StackViaNodeTemplate.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  A graph, implemented using an array of sets.
 *  Parallel edges and self-loops allowed.
 *
 *  % java Graph tinyG.txt
 *  13 vertices, 13 edges 
 *  0: 6 2 1 5 
 *  1: 0 
 *  2: 0 
 *  3: 5 4 
 *  4: 5 6 3 
 *  5: 3 4 0 
 *  6: 0 4 
 *  7: 8 
 *  8: 7 
 *  9: 11 10 12 
 *  10: 9 
 *  11: 9 12 
 *  12: 11 9 
 *
 *  % java Graph mediumG.txt
 *  250 vertices, 1273 edges 
 *  0: 225 222 211 209 204 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15 
 *  1: 220 203 200 194 189 164 150 130 107 72 
 *  2: 141 110 108 86 79 51 42 18 14 
 *  ...
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 *  The {@code Graph} class represents an undirected graph of vertices
 *  named 0 through <em>V</em> – 1.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the vertices adjacent to a vertex. It also provides
 *  methods for returning the degree of a vertex, the number of vertices
 *  <em>V</em> in the graph, and the number of edges <em>E</em> in the graph.
 *  Parallel edges and self-loops are permitted.
 *  By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 *  adjacency list of <em>v</em> twice and contributes two to the degree
 *  of <em>v</em>.
 *  <p>
 *  This implementation uses an <em>adjacency-lists representation</em>, which
 *  is a vertex-indexed array of {@link Bag} objects.
 *  It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 *  the number of edges and <em>V</em> is the number of vertices.
 *  All instance methods take &Theta;(1) time. (Though, iterating over
 *  the vertices returned by {@link #adjacentVertexesOf(int)} takes time proportional
 *  to the degree of the vertex.)
 *  Constructing an empty graph with <em>V</em> vertices takes
 *  &Theta;(<em>V</em>) time; constructing a graph with <em>E</em> edges
 *  and <em>V</em> vertices takes &Theta;(<em>E</em> + <em>V</em>) time. 
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int vertexAmount;
    private int edgesAmount;
    private Bag<Integer>[] vertexToItsAdjacentVertexes;

    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     * param V the number of vertices
     *
     * @param  vertexAmount number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public Graph(int vertexAmount) {
        if (vertexAmount < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.vertexAmount = vertexAmount;
        this.edgesAmount = 0;
        vertexToItsAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];

        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToItsAdjacentVertexes[currentVertex] = new Bag<Integer>();
        }
    }

    /**
     * Initializes a graph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     * @throws IllegalArgumentException if the input stream is in the wrong format
     */
    public Graph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.vertexAmount = in.readInt();
            if (vertexAmount < 0) throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            vertexToItsAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];
            for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
                vertexToItsAdjacentVertexes[currentVertex] = new Bag<Integer>();
            }

            int edgeAmount = in.readInt();
            if (edgeAmount < 0) throw new IllegalArgumentException("number of edges in a Graph must be non-negative");

            for (int currentEdgeCursor = 0; currentEdgeCursor < edgeAmount; currentEdgeCursor++) {
                int vertexV = in.readInt();
                int vertexW = in.readInt();
                validateVertex(vertexV);
                validateVertex(vertexW);
                addEdge(vertexV, vertexW);
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }


    /**
     * Initializes a new graph that is a deep copy of {@code G}.
     *
     * @param  passedGraph the graph to copy
     * @throws IllegalArgumentException if {@code G} is {@code null}
     */
    public Graph(Graph passedGraph) {
        this.vertexAmount = passedGraph.vertexAmount();
        this.edgesAmount = passedGraph.edgeAmount();
        if (vertexAmount < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");

        // update adjacency lists
        vertexToItsAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToItsAdjacentVertexes[currentVertex] = new Bag<Integer>();
        }

        for (int currentVertex = 0; currentVertex < passedGraph.vertexAmount(); currentVertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> adjacentVertexesInReverseOrder = new Stack<Integer>();

            for (int currentAdjacentVertex : passedGraph.vertexToItsAdjacentVertexes[currentVertex]) {
                adjacentVertexesInReverseOrder.push(currentAdjacentVertex);
            }

            for (int currentAdjacentVertex : adjacentVertexesInReverseOrder) {
                vertexToItsAdjacentVertexes[currentVertex].add(currentAdjacentVertex);
            }
        }
    }

    /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int vertexAmount() {
        return vertexAmount;
    }

    /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int edgeAmount() {
        return edgesAmount;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= vertexAmount)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertexAmount -1));
    }

    /**
     * Adds the undirected edge v-w to this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        edgesAmount++;
        vertexToItsAdjacentVertexes[v].add(w);
        vertexToItsAdjacentVertexes[w].add(v);
    }


    /**
     * Returns the vertices adjacent to vertex {@code v}.
     *
     * @param  passedVertex the vertex
     * @return the vertices adjacent to vertex {@code v}, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> adjacentVertexesOf(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsAdjacentVertexes[passedVertex];
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(int v) {
        validateVertex(v);
        return vertexToItsAdjacentVertexes[v].size();
    }


    /**
     * Returns a string representation of this graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(vertexAmount + " vertices, " + edgesAmount + " edges " + NEWLINE);
        for (int v = 0; v < vertexAmount; v++) {
            s.append(v + ": ");
            for (int w : vertexToItsAdjacentVertexes[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }


    /**
     * Unit tests the {@code Graph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        StdOut.println(G);
    }

}