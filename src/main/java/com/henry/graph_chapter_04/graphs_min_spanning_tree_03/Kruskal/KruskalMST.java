package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Kruskal;

/******************************************************************************
 *  Compilation:  javac KruskalMST.java
 *  Execution:    java  KruskalMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java MinPQ.java
 *                UF.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Kruskal's algorithm.
 *
 *  %  java KruskalMST tinyEWG.txt
 *  0-7 0.16000
 *  2-3 0.17000
 *  1-7 0.19000
 *  0-2 0.26000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 *  % java KruskalMST mediumEWG.txt
 *  168-231 0.00268
 *  151-208 0.00391
 *  7-157   0.00516
 *  122-205 0.00647
 *  8-152   0.00702
 *  156-219 0.00745
 *  28-198  0.00775
 *  38-126  0.00845
 *  10-123  0.00886
 *  ...
 *  10.46351
 *
 ******************************************************************************/

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * The {@code KruskalMST} class represents a data type for computing a
 * <em>minimum spanning tree</em> in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computes a <em>minimum
 * spanning forest</em>, which is the union of minimum spanning trees
 * in each connected component. The {@code weight()} method returns the
 * weight of a minimum spanning tree and the {@code edges()} method
 * returns its edges.
 * <p>
 * This implementation uses <em>Kruskal's algorithm</em> and the
 * union-find data type.
 * The constructor takes &Theta;(<em>E</em> log <em>E</em>) time in
 * the worst case.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>E</em>) extra space (not including the graph).
 * <p>
 * This {@code weight()} method correctly computes the weight of the MST
 * if all arithmetic performed is without floating-point rounding error
 * or arithmetic overflow.
 * This is the case if all edge weights are non-negative integers
 * and the weight of the MST does not exceed 2<sup>52</sup>.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * For alternate implementations, see {@link LazyPrimMST}, {@link PrimMST},
 * and {@link BoruvkaMST}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class KruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private double weightOfMST;                        // weight of MST
    private Queue<Edge> edgesQueueInMST = new Queue<Edge>();  // edges in MST

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param weightedGraph the edge-weighted graph
     */
    public KruskalMST(EdgeWeightedGraph weightedGraph) {

        // create array of edges, sorted by weight
        Edge[] edges = new Edge[weightedGraph.getEdgeAmount()];
        int currentSpot = 0;
        for (Edge currentEdge : weightedGraph.edges()) {
            edges[currentSpot++] = currentEdge;
        }
        Arrays.sort(edges);

        // run greedy algorithm
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (int currentEdgeCursor = 0; currentEdgeCursor < weightedGraph.getEdgeAmount() && edgesQueueInMST.size() < weightedGraph.getVertexAmount() - 1; currentEdgeCursor++) {
            Edge currentEdge = edges[currentEdgeCursor];
            int oneVertex = currentEdge.eitherVertex();
            int theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            // oneVertex-theOtherVertex does not create a cycle
            if (forest.findGroupIdOf(oneVertex) != forest.findGroupIdOf(theOtherVertex)) {
                forest.unionToSameComponent(oneVertex, theOtherVertex);     // merge oneVertex and theOtherVertex components
                edgesQueueInMST.enqueue(currentEdge);     // add edge currentEdge to mst
                weightOfMST += currentEdge.weight();
            }
        }

        // check optimality conditions
        assert check(weightedGraph);
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     *
     * @return the edges in a minimum spanning tree (or forest) as
     * an iterable of edges
     */
    public Iterable<Edge> edges() {
        return edgesQueueInMST;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     *
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        return weightOfMST;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph weightedGraph) {

        // check totalWeight weight
        double totalWeight = 0.0;
        for (Edge currentEdge : edges()) {
            totalWeight += currentEdge.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (Edge currentEdge : edges()) {
            int oneVertex = currentEdge.eitherVertex(), theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            if (forest.findGroupIdOf(oneVertex) == forest.findGroupIdOf(theOtherVertex)) {
                System.err.println("Not a forest");
                return false;
            }
            forest.unionToSameComponent(oneVertex, theOtherVertex);
        }

        // check that it is a spanning forest
        for (Edge currentEdge : weightedGraph.edges()) {
            int oneVertex = currentEdge.eitherVertex(), theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);
            if (forest.findGroupIdOf(oneVertex) != forest.findGroupIdOf(theOtherVertex)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge currentMSTEdge : edges()) {

            // all edges in MST except currentMSTEdge
            forest = new QuickFind(weightedGraph.getVertexAmount());
            for (Edge currentEdgeInMST : edgesQueueInMST) {
                int oneVertex = currentEdgeInMST.eitherVertex(),
                        theOtherVertex = currentEdgeInMST.theOtherVertexAgainst(oneVertex);

                if (currentEdgeInMST != currentMSTEdge)
                    forest.unionToSameComponent(oneVertex, theOtherVertex);
            }

            // check that currentMSTEdge is min weight edge in crossing cut
            for (Edge graphsCurrentEdge : weightedGraph.edges()) {
                int oneVertex = graphsCurrentEdge.eitherVertex(),
                        theOtherVertex = graphsCurrentEdge.theOtherVertexAgainst(oneVertex);

                if (forest.findGroupIdOf(oneVertex) != forest.findGroupIdOf(theOtherVertex)) {
                    if (graphsCurrentEdge.weight() < currentMSTEdge.weight()) {
                        System.err.println("Edge " + graphsCurrentEdge + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }


    /**
     * Unit tests the {@code KruskalMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph weightedGraph = new EdgeWeightedGraph(in);
        KruskalMST graphsMST = new KruskalMST(weightedGraph);
        for (Edge currentMSTEdge : graphsMST.edges()) {
            StdOut.println(currentMSTEdge);
        }
        StdOut.printf("%.5f\n", graphsMST.weight());
    }

}