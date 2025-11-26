package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah;

/******************************************************************************
 *  Compilation:  javac EdgeWeightedGraph.java
 *  Execution:    java EdgeWeightedGraph filename.txt
 *  Dependencies: Bag.java Edge.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  An edge-weighted undirected graph, implemented using adjacency lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java EdgeWeightedGraph tinyEWG.txt
 *  8 16
 *  0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000
 *  1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000
 *  2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000
 *  3: 3-6 0.52000  1-3 0.29000  2-3 0.17000
 *  4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000
 *  5: 1-5 0.32000  5-7 0.28000  4-5 0.35000
 *  6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
 *  7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
 *
 ******************************************************************************/

import com.henry.basic_chapter_01.collection_types.bag.via_linked_node.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

/**
 * The {@code EdgeWeightedGraph} class represents an edge-weighted
 * graph of vertices named 0 through <em>V</em> – 1, where each
 * undirected edge is of type {@link Edge} and has a real-valued weight.
 * It supports the following two primary operations: add an edge to the graph,
 * iterate over all of the edges incident to a vertex. It also provides
 * methods for returning the degree of a vertex, the number of vertices
 * <em>V</em> in the graph, and the number of edges <em>E</em> in the graph.
 * Parallel edges and self-loops are permitted.
 * By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 * adjacency list of <em>v</em> twice and contributes two to the degree
 * of <em>v</em>.
 * <p>
 * This implementation uses an <em>adjacency-lists representation</em>, which
 * is a vertex-indexed array of {@link Bag} objects.
 * It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 * the number of edges and <em>V</em> is the number of vertices.
 * All instance methods take &Theta;(1) time. (Though, iterating over
 * the edges returned by {@link #getAssociatedEdgesOf(int)} takes time proportional
 * to the degree of the vertex.)
 * Constructing an empty edge-weighted graph with <em>V</em> vertices takes
 * &Theta;(<em>V</em>) time; constructing an edge-weighted graph with
 * <em>E</em> edges and <em>V</em> vertices takes
 * &Theta;(<em>E</em> + <em>V</em>) time.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int vertexAmount;
    private int edgeAmount;
    private Bag<Edge>[] vertexToAssociatedVertexes; // 记录 vertex -> 它所关联的所有边 的映射

    /**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param vertexAmount the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedGraph(int vertexAmount) {
        if (vertexAmount < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");

        this.vertexAmount = vertexAmount;
        this.edgeAmount = 0;

        vertexToAssociatedVertexes = (Bag<Edge>[]) new Bag[vertexAmount];
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToAssociatedVertexes[currentVertex] = new Bag<Edge>();
        }
    }

    /**
     * Initializes a random edge-weighted graph with {@code V} vertices and <em>E</em> edges.
     *
     * @param vertexAmount the number of vertices
     * @param edgeAmount   the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
    public EdgeWeightedGraph(int vertexAmount, int edgeAmount) {
        this(vertexAmount);
        if (edgeAmount < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for (int currentEdgeCursor = 0; currentEdgeCursor < edgeAmount; currentEdgeCursor++) {
            int vertexV = StdRandom.uniform(vertexAmount);
            int vertexW = StdRandom.uniform(vertexAmount);
            // 随机的权重
            double weight = 0.01 * StdRandom.uniform(0, 100);

            Edge newEdge = new Edge(vertexV, vertexW, weight);
            addEdge(newEdge);
        }
    }

    /**
     * Initializes an edge-weighted graph from an input stream.
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
    public EdgeWeightedGraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");

        try {
            vertexAmount = in.readInt();
            vertexToAssociatedVertexes = (Bag<Edge>[]) new Bag[vertexAmount];
            for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
                vertexToAssociatedVertexes[currentVertex] = new Bag<Edge>();
            }

            int edgeAmount = in.readInt();
            if (edgeAmount < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
            for (int currentEdgeCursor = 0; currentEdgeCursor < edgeAmount; currentEdgeCursor++) {
                int vertexV = in.readInt();
                int vertexW = in.readInt();

                validateVertex(vertexV);
                validateVertex(vertexW);

                double weight = in.readDouble();
                Edge newEdge = new Edge(vertexV, vertexW, weight);
                addEdge(newEdge);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedGraph constructor", e);
        }

    }

    /**
     * Initializes a new edge-weighted graph that is a deep copy of {@code G}.
     *
     * @param weightedGraph the edge-weighted graph to copy
     */
    public EdgeWeightedGraph(EdgeWeightedGraph weightedGraph) {
        this(weightedGraph.getVertexAmount());
        this.edgeAmount = weightedGraph.getEdgeAmount();

        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<Edge> edgeStack = new Stack<Edge>();
            for (Edge currentAssociatedEdge : weightedGraph.vertexToAssociatedVertexes[currentVertex]) {
                edgeStack.push(currentAssociatedEdge);
            }

            for (Edge currentEdge : edgeStack) {
                vertexToAssociatedVertexes[currentVertex].add(currentEdge);
            }
        }
    }


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int getVertexAmount() {
        return vertexAmount;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
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
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param passedEdge the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(Edge passedEdge) {
        int vertexV = passedEdge.eitherVertex();
        int vertexW = passedEdge.theOtherVertexAgainst(vertexV);
        validateVertex(vertexV);
        validateVertex(vertexW);

        vertexToAssociatedVertexes[vertexV].add(passedEdge);
        vertexToAssociatedVertexes[vertexW].add(passedEdge);
        edgeAmount++;
    }

    /**
     * Returns the edges incident on vertex {@code v}.
     *
     * @param passedVertex the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> getAssociatedEdgesOf(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToAssociatedVertexes[passedVertex];
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param passedVertex the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degreeOf(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToAssociatedVertexes[passedVertex].size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<Edge> edges() {
        Bag<Edge> edgeCollection = new Bag<Edge>();

        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            int selfLoops = 0;
            for (Edge currentAssociatedEdge : getAssociatedEdgesOf(currentVertex)) {
                // 只计入 另一个结点更大的边 - 这样冲抵 邻接表中，同一条边会被记录两次的事实
                if (currentAssociatedEdge.theOtherVertexAgainst(currentVertex) > currentVertex) {
                    edgeCollection.add(currentAssociatedEdge);
                }
                // 如果存在自环的话，则：只计入一条边 (self loops will be consecutive)
                else if (currentAssociatedEdge.theOtherVertexAgainst(currentVertex) == currentVertex) {
                    if (selfLoops % 2 == 0) {
                        edgeCollection.add(currentAssociatedEdge);
                    }
                    selfLoops++;
                }
            }
        }
        return edgeCollection;
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder graphStr = new StringBuilder();
        graphStr.append(vertexAmount + " " + edgeAmount + NEWLINE);
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            graphStr.append(currentVertex + ": ");
            for (Edge currentAssociatedEdge : vertexToAssociatedVertexes[currentVertex]) {
                graphStr.append(currentAssociatedEdge + "  ");
            }
            graphStr.append(NEWLINE);
        }
        return graphStr.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedGraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(in);
        StdOut.println(edgeWeightedGraph);
    }

}