package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.eager_implementation;

/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                IndexMinPQ.java UF.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Prim's algorithm.
 *
 *  %  java PrimMST tinyEWG.txt
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  4-5 0.35000
 *  5-7 0.28000
 *  6-2 0.40000
 *  0-7 0.16000
 *  1.81000
 *
 *  % java PrimMST mediumEWG.txt
 *  1-72   0.06506
 *  2-86   0.05980
 *  3-67   0.09725
 *  4-55   0.06425
 *  5-102  0.03834
 *  6-129  0.05363
 *  7-157  0.00516
 *  ...
 *  10.46351
 *
 *  % java PrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code PrimMST} class represents a data type for computing a
 * <em>minimum spanning tree</em> in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computes a <em>minimum
 * spanning forest</em>, which is the union of minimum spanning trees
 * in each connected component. The {@code weight()} method returns the
 * weight of a minimum spanning tree and the {@code edges()} method
 * returns its edges.
 * <p>
 * This implementation uses <em>Prim's algorithm</em> with an indexed
 * binary heap.
 * The constructor takes &Theta;(<em>E</em> log <em>V</em>) time in
 * the worst case, where <em>V</em> is the number of
 * vertices and <em>E</em> is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the
 * edge-weighted graph).
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
 * For alternate implementations, see {@link LazyPrimMST}, {@link KruskalMST},
 * and {@link BoruvkaMST}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class PrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private Edge[] vertexToItsMinEdgeToMST;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] vertexToItsMinEdgeWeight;      // distTo[v] = weight of shortest such edge
    private boolean[] vertexToIsTreeVertex;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> vertexToItsMinEdgeWeightPQ;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param weightedGraph the edge-weighted graph
     */
    public PrimMST(EdgeWeightedGraph weightedGraph) {
        vertexToItsMinEdgeToMST = new Edge[weightedGraph.getVertexAmount()];
        vertexToItsMinEdgeWeight = new double[weightedGraph.getVertexAmount()];
        vertexToIsTreeVertex = new boolean[weightedGraph.getVertexAmount()];
        vertexToItsMinEdgeWeightPQ = new IndexMinPQ<Double>(weightedGraph.getVertexAmount());

        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
            // 从 当前“树节点” 到 “非树节点”的最短边 - 初始化为 正无穷大
            vertexToItsMinEdgeWeight[currentVertex] = Double.POSITIVE_INFINITY;

        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)      // run from each vertex to find
            if (isNotMSTVertex(currentVertex)) {
                prim(weightedGraph, currentVertex);      // minimum spanning(展开) forest
            }

        // check optimality conditions
        assert check(weightedGraph);
    }

    private boolean isNotMSTVertex(int currentVertex) {
        return !vertexToIsTreeVertex[currentVertex];
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph weightedGraph, int passedVertex) {
        vertexToItsMinEdgeWeight[passedVertex] = 0.0;
        vertexToItsMinEdgeWeightPQ.insert(passedVertex, vertexToItsMinEdgeWeight[passedVertex]);

        while (!vertexToItsMinEdgeWeightPQ.isEmpty()) {
            int vertexOfMinEdgeWeight = vertexToItsMinEdgeWeightPQ.delMin();
            scan(weightedGraph, vertexOfMinEdgeWeight);
        }
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph weightedGraph, int passedVertex) {
        vertexToIsTreeVertex[passedVertex] = true;

        for (Edge currentEdge : weightedGraph.getAssociatedEdgesOf(passedVertex)) {
            int theOtherVertex = currentEdge.theOtherVertexAgainst(passedVertex);

            if (vertexToIsTreeVertex[theOtherVertex]) continue;         // v-theOtherVertex is obsolete edge

            if (currentEdge.weight() < vertexToItsMinEdgeWeight[theOtherVertex]) { // ???
                vertexToItsMinEdgeWeight[theOtherVertex] = currentEdge.weight();
                vertexToItsMinEdgeToMST[theOtherVertex] = currentEdge;

                if (vertexToItsMinEdgeWeightPQ.contains(theOtherVertex))
                    // i -> key | vertex -> weight, so it is decreaseWeight(xxx, ooo)
                    vertexToItsMinEdgeWeightPQ.decreaseKey(theOtherVertex, vertexToItsMinEdgeWeight[theOtherVertex]);
                else
                    vertexToItsMinEdgeWeightPQ.insert(theOtherVertex, vertexToItsMinEdgeWeight[theOtherVertex]);
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     *
     * @return the edges in a minimum spanning tree (or forest) as
     * an iterable of edges
     */
    public Iterable<Edge> edgesInMST() {
        Queue<Edge> edgesInMST = new Queue<Edge>();

        for (int currentVertex = 0; currentVertex < vertexToItsMinEdgeToMST.length; currentVertex++) {
            Edge minEdgeToMST = vertexToItsMinEdgeToMST[currentVertex];
            if (minEdgeToMST != null) {
                edgesInMST.enqueue(minEdgeToMST);
            }
        }

        return edgesInMST;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     *
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        double weight = 0.0;

        for (Edge currentEdge : edgesInMST())
            weight += currentEdge.weight();

        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph weightedGraph) {

        // check weight
        double totalWeight = 0.0;
        for (Edge currentEdge : edgesInMST()) {
            totalWeight += currentEdge.weight();
        }

        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (Edge currentEdge : edgesInMST()) {
            int oneVertex = currentEdge.eitherVertex(),
                    theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            if (isInSameTree(forest, oneVertex, theOtherVertex)) {
                System.err.println("Not a forest");
                return false;
            }

            forest.unionToSameComponent(oneVertex, theOtherVertex);
        }

        // check that it is a spanning forest
        for (Edge currentEdge : weightedGraph.edges()) {

            int oneVertex = currentEdge.eitherVertex(),
                    theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            if (isNotInSameTree(forest, oneVertex, theOtherVertex)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge currentMSTEdge : edgesInMST()) {

            // all edges in MST except currentMSTEdge
            forest = new QuickFind(weightedGraph.getVertexAmount());
            for (Edge currentEdgeInMST : edgesInMST()) {

                int oneVertex = currentEdgeInMST.eitherVertex(),
                        theOtherVertex = currentEdgeInMST.theOtherVertexAgainst(oneVertex);

                if (currentEdgeInMST != currentMSTEdge)
                    forest.unionToSameComponent(oneVertex, theOtherVertex);
            }

            // #5 check that currentMSTEdge is min weight edge in crossing cut
            for (Edge currentEdge : weightedGraph.edges()) {
                int oneVertex = currentEdge.eitherVertex(),
                        theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

                if (isNotInSameTree(forest, oneVertex, theOtherVertex)) {
                    if (currentEdge.weight() < currentMSTEdge.weight()) {
                        System.err.println("Edge " + currentEdge + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    private boolean isNotInSameTree(QuickFind forest, int oneVertex, int theOtherVertex) {
        return forest.findGroupIdOf(oneVertex) != forest.findGroupIdOf(theOtherVertex);
    }

    private boolean isInSameTree(QuickFind forest, int oneVertex, int theOtherVertex) {
        return forest.findGroupIdOf(oneVertex) == forest.findGroupIdOf(theOtherVertex);
    }

    /**
     * Unit tests the {@code PrimMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph weightedGraph = new EdgeWeightedGraph(in);

        PrimMST graphMST = new PrimMST(weightedGraph);
        for (Edge currentEdge : graphMST.edgesInMST()) {
            StdOut.println(currentEdge);
        }

        StdOut.printf("%.5f\n", graphMST.weight());
    }


}