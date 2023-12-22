package com.henry.graph_chapter_04.shortest_paths_04;

/******************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph digraph.txt
 *  Dependencies: Bag.java DirectedEdge.java
 *  Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 ******************************************************************************/

import com.henry.basic_chapter_01.collection_types.bag.via_linked_node.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

/**
 * The {@code EdgeWeightedDigraph} class represents an edge-weighted
 * digraph of vertices named 0 through <em>V</em> - 1, where each
 * directed edge is of type {@link DirectedEdge} and has a real-valued weight.
 * It supports the following two primary operations: add a directed edge
 * to the digraph and iterate over all edges incident from a given vertex.
 * It also provides methods for returning the indegree or outdegree of a
 * vertex, the number of vertices <em>V</em> in the digraph, and
 * the number of edges <em>E</em> in the digraph.
 * Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an <em>adjacency-lists representation</em>, which
 * is a vertex-indexed array of {@link Bag} objects.
 * It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 * the number of edges and <em>V</em> is the number of vertices.
 * All instance methods take &Theta;(1) time. (Though, iterating over
 * the edges returned by {@link #associatedEdgesOf(int)} takes time proportional
 * to the outdegree of the vertex.)
 * Constructing an empty edge-weighted digraph with <em>V</em> vertices
 * takes &Theta;(<em>V</em>) time; constructing an edge-weighted digraph
 * with <em>E</em> edges and <em>V</em> vertices takes
 * &Theta;(<em>E</em> + <em>V</em>) time.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int vertexAmount;                // number of vertices in this digraph
    private int edgeAmount;                      // number of edges in this digraph
    private Bag<DirectedEdge>[] vertexToItsAssociatedEdges;    // adj[v] = adjacency list for vertex v
    private int[] vertexToItsIndegree;             // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param vertexAmount the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(int vertexAmount) {
        if (vertexAmount < 0)
            throw new IllegalArgumentException("Number of vertices in a Digraph must be non-negative");
        this.vertexAmount = vertexAmount;
        this.edgeAmount = 0;
        this.vertexToItsIndegree = new int[vertexAmount];
        vertexToItsAssociatedEdges = (Bag<DirectedEdge>[]) new Bag[vertexAmount];

        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++)
            vertexToItsAssociatedEdges[currentVertex] = new Bag<DirectedEdge>();
    }

    /**
     * Initializes a random edge-weighted digraph with {@code V} vertices and <em>E</em> edges.
     *
     * @param vertexAmount the number of vertices
     * @param edgeAmount   the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
    public EdgeWeightedDigraph(int vertexAmount, int edgeAmount) {
        this(vertexAmount);
        if (edgeAmount < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be non-negative");

        for (int currentEdgeCursor = 0; currentEdgeCursor < edgeAmount; currentEdgeCursor++) {
            int vertexV = StdRandom.uniform(vertexAmount);
            int vertexW = StdRandom.uniform(vertexAmount);
            double weight = 0.01 * StdRandom.uniform(100);

            DirectedEdge currentEdge = new DirectedEdge(vertexV, vertexW, weight);
            addEdge(currentEdge);
        }
    }

    /**
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedDigraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.vertexAmount = in.readInt();
            if (vertexAmount < 0)
                throw new IllegalArgumentException("number of vertices in a Digraph must be non-negative");
            vertexToItsIndegree = new int[vertexAmount];
            vertexToItsAssociatedEdges = (Bag<DirectedEdge>[]) new Bag[vertexAmount];
            for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
                vertexToItsAssociatedEdges[currentVertex] = new Bag<DirectedEdge>();
            }

            int edgeAmount = in.readInt();
            if (edgeAmount < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
            for (int currentEdgeCursor = 0; currentEdgeCursor < edgeAmount; currentEdgeCursor++) {
                int vertexV = in.readInt();
                int vertexW = in.readInt();

                validateVertex(vertexV);
                validateVertex(vertexW);

                double weight = in.readDouble();
                addEdge(new DirectedEdge(vertexV, vertexW, weight));
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e);
        }
    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of {@code G}.
     *
     * @param weightedDigraph the edge-weighted digraph to copy
     */
    public EdgeWeightedDigraph(EdgeWeightedDigraph weightedDigraph) {
        this(weightedDigraph.getVertexAmount());
        this.edgeAmount = weightedDigraph.getEdgeAmount();
        for (int currentVertex = 0; currentVertex < weightedDigraph.getVertexAmount(); currentVertex++)
            this.vertexToItsIndegree[currentVertex] = weightedDigraph.indegree(currentVertex);

        for (int currentVertex = 0; currentVertex < weightedDigraph.getVertexAmount(); currentVertex++) {
            // associatedEdgesStack so that adjacency list is in same order as original
            Stack<DirectedEdge> associatedEdgesStack = new Stack<DirectedEdge>();
            for (DirectedEdge currentAssociatedEdge : weightedDigraph.vertexToItsAssociatedEdges[currentVertex]) {
                associatedEdgesStack.push(currentAssociatedEdge);
            }

            for (DirectedEdge edgeOnStackTop : associatedEdgesStack) {
                vertexToItsAssociatedEdges[currentVertex].add(edgeOnStackTop);
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int getVertexAmount() {
        return vertexAmount;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int getEdgeAmount() {
        return edgeAmount;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int passedVertex) {
        if (passedVertex < 0 || passedVertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + passedVertex + " is not between 0 and " + (vertexAmount - 1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param passedEdge the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *                                  and {@code V-1}
     */
    public void addEdge(DirectedEdge passedEdge) {
        int departVertex = passedEdge.departVertex();
        int terminalVertex = passedEdge.terminalVertex();
        validateVertex(departVertex);
        validateVertex(terminalVertex);

        vertexToItsAssociatedEdges[departVertex].add(passedEdge);
        vertexToItsIndegree[terminalVertex]++;

        edgeAmount++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param passedVertex the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> associatedEdgesOf(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsAssociatedEdges[passedVertex];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param passedVertex the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsAssociatedEdges[passedVertex].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param passedVertex the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsIndegree[passedVertex];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> edgesBag = new Bag<DirectedEdge>();
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            for (DirectedEdge currentAssociatedEdge : associatedEdgesOf(currentVertex)) {
                edgesBag.add(currentAssociatedEdge);
            }
        }

        return edgesBag;
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder graphStr = new StringBuilder();
        graphStr.append(vertexAmount + " " + edgeAmount + NEWLINE);
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            graphStr.append(currentVertex + ": ");
            for (DirectedEdge currentAssociatedEdge : vertexToItsAssociatedEdges[currentVertex]) {
                graphStr.append(currentAssociatedEdge + "  ");
            }
            graphStr.append(NEWLINE);
        }
        return graphStr.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph weightedDigraph = new EdgeWeightedDigraph(in);
        StdOut.println(weightedDigraph);
    }

}