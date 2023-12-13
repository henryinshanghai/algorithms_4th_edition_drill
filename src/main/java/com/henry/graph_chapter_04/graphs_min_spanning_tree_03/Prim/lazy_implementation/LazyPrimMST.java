package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.lazy_implementation; /******************************************************************************
 *  Compilation:  javac LazyPrimMST.java
 *  Execution:    java LazyPrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                MinPQ.java UF.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using a lazy version of Prim's
 *  algorithm.
 *
 *  %  java LazyPrimMST tinyEWG.txt
 *  0-7 0.16000
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 *  % java LazyPrimMST mediumEWG.txt
 *  0-225   0.02383
 *  49-225  0.03314
 *  44-49   0.02107
 *  44-204  0.01774
 *  49-97   0.03121
 *  202-204 0.04207
 *  176-202 0.04299
 *  176-191 0.02089
 *  68-176  0.04396
 *  58-68   0.04795
 *  10.46351
 *
 *  % java LazyPrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code LazyPrimMST} class represents a data type for computing a
 * <em>minimum spanning tree</em> in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computes a <em>minimum
 * spanning forest</em>, which is the union of minimum spanning trees
 * in each connected component. The {@code weight()} method returns the
 * weight of a minimum spanning tree and the {@code edges()} method
 * returns its edges.
 * <p>
 * This implementation uses a lazy version of <em>Prim's algorithm</em>
 * with a binary heap of edges.
 * The constructor takes &Theta;(<em>E</em> log <em>E</em>) time in
 * the worst case, where <em>V</em> is the number of vertices and
 * <em>E</em> is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>E</em>) extra space in the worst case
 * (not including the edge-weighted graph).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * For alternate implementations, see {@link PrimMST}, {@link KruskalMST},
 * and {@link BoruvkaMST}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class LazyPrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private double weightOfMST;       // total weight of MST
    private Queue<Edge> edgesInMST;     // edges in the MST
    private boolean[] vertexToIsInMST;    // marked[v] = true iff v on tree
    private MinPQ<Edge> crossEdgesPQ;      // edges with one endpoint in tree

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param weightedGraph the edge-weighted graph
     */
    public LazyPrimMST(EdgeWeightedGraph weightedGraph) {
        edgesInMST = new Queue<Edge>();
        crossEdgesPQ = new MinPQ<Edge>();
        vertexToIsInMST = new boolean[weightedGraph.getVertexAmount()];

        // option01 - 直接从结点0开始BFS（BFS能够遍历 连通图中的每一个结点）
        prim(weightedGraph, 0);

        // option02 - 对于每一个结点，对其执行Prim算法 来 得到“图的最小生成树”
//        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
//            if (isNotMSTVertex(currentVertex))
//                prim(weightedGraph, currentVertex);

        // check optimality conditions
        assert check(weightedGraph);
    }

    private boolean isNotMSTVertex(int currentVertex) {
        return !vertexToIsInMST[currentVertex];
    }

    // run Prim's algorithm
    private void prim(EdgeWeightedGraph weightedGraph, int currentVertex) {
        // #1 向MST中添加 结点 && 向优先队列中添加 横切边
        addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, currentVertex);

        while (!crossEdgesPQ.isEmpty()) {
            // 获取到 当前最小的横切边
            Edge minWeightEdge = crossEdgesPQ.delMin();

            if (isMSTEdge(minWeightEdge)) continue;
            // #2 把 “最小的横切边” 添加到 MST中（切分定理：最小横切边总是会属于MST）
            addEdgeInMST(minWeightEdge);

            // #3 把 边中“非树结点”，添加到树中，成为树节点
            addNonMSTVertexToMST(weightedGraph, minWeightEdge);
        }
    }

    private void addNonMSTVertexToMST(EdgeWeightedGraph weightedGraph, Edge minWeightEdge) {
        int oneVertex = minWeightEdge.eitherVertex(),
                theOtherVertex = minWeightEdge.theOtherVertexAgainst(oneVertex);        // two endpoints

        if (isNotMSTVertex(oneVertex)) {
            addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, oneVertex);               // oneVertex becomes part of tree
        }
        if (isNotMSTVertex(theOtherVertex)) {
            addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, theOtherVertex);               // theOtherVertex becomes part of tree
        }
    }

    private void addEdgeInMST(Edge minWeightEdge) {
        // 切分定理：把 当前权值最小的横切边 作为 最小生成树中的边。
        edgesInMST.enqueue(minWeightEdge);                            // add minWeightEdge to MST

        // 累计权值
        weightOfMST += minWeightEdge.weight();
    }

    private boolean isMSTEdge(Edge minWeightEdge) {
        int oneVertex = minWeightEdge.eitherVertex(),
                theOtherVertex = minWeightEdge.theOtherVertexAgainst(oneVertex);        // two endpoints
        assert vertexToIsInMST[oneVertex] || vertexToIsInMST[theOtherVertex];

        return vertexToIsInMST[oneVertex] && vertexToIsInMST[theOtherVertex];
    }

    // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
    private void addVertexInMSTAndAddItsCrossEdgesInPQ(EdgeWeightedGraph weightedGraph, int currentVertex) { // scan?? visit??
        assert !vertexToIsInMST[currentVertex];
        vertexToIsInMST[currentVertex] = true;

        // 收集 与当前节点相关联的所有“横切边” - 两个集合：树结点 & 非树节点
        for (Edge currentAssociatedEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            int theOtherVertex = currentAssociatedEdge.theOtherVertexAgainst(currentVertex);

            // 如果边的另一个顶点 不是“树顶点”，说明 找到了一个横切边，则：把它添加到 优先队列中
            if (isNotMSTVertex(theOtherVertex)) {
                crossEdgesPQ.insert(currentAssociatedEdge);
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     *
     * @return the edges in a minimum spanning tree (or forest) as
     * an iterable of edges
     */
    public Iterable<Edge> edgesOfMST() {
        return edgesInMST;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     *
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weightOfMST() {
        return weightOfMST;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph weightedGraph) {

        // #1 check weight
        double totalWeight = 0.0;
        for (Edge currentEdge : edgesOfMST()) {
            totalWeight += currentEdge.weight();
        }
        if (Math.abs(totalWeight - weightOfMST()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weightOfMST());
            return false;
        }

        // #2 check that it is acyclic(非循环的??)
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());

        for (Edge currentEdge : edgesOfMST()) {
            int oneVertex = currentEdge.eitherVertex(),
                theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);
            if (isInSameTree(forest, oneVertex, theOtherVertex)) {
                System.err.println("Not a forest");
                return false;
            }
            forest.unionToSameComponent(oneVertex, theOtherVertex);
        }

        // #3 check that it is a spanning(展开) forest
        for (Edge currentEdge : weightedGraph.edges()) {
            int oneVertex = currentEdge.eitherVertex(),
                theOtherVertexAgainst = currentEdge.theOtherVertexAgainst(oneVertex);

            if (notInSameTree(forest, oneVertex, theOtherVertexAgainst)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // #4 check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge currentMSTEdge : edgesOfMST()) {

            // all edges in MST except currentMSTEdge
            forest = new QuickFind(weightedGraph.getVertexAmount());

            for (Edge currentEdgeInMST : edgesInMST) {
                int oneVertex = currentEdgeInMST.eitherVertex(),
                    theOtherVertex = currentEdgeInMST.theOtherVertexAgainst(oneVertex);

                if (currentEdgeInMST != currentMSTEdge) {
                    forest.unionToSameComponent(oneVertex, theOtherVertex);
                }
            }

            // #5 check that currentMSTEdge is min weight edge in crossing cut
            for (Edge currentEdge : weightedGraph.edges()) {
                int oneVertex = currentEdge.eitherVertex(),
                    theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

                if (notInSameTree(forest, oneVertex, theOtherVertex)) {
                    if (currentEdge.weight() < currentMSTEdge.weight()) {
                        System.err.println("Edge " + currentEdge + " violates cut optimality conditions");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isInSameTree(QuickFind forest, int oneVertex, int theOtherVertex) {
        return forest.findGroupIdOf(oneVertex) == forest.findGroupIdOf(theOtherVertex);
    }

    private boolean notInSameTree(QuickFind groups, int oneVertex, int theOtherVertexAgainst) {
        return groups.findGroupIdOf(oneVertex) != groups.findGroupIdOf(theOtherVertexAgainst);
    }


    /**
     * Unit tests the {@code LazyPrimMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph weightedGraph = new EdgeWeightedGraph(in);
        LazyPrimMST graphsMST = new LazyPrimMST(weightedGraph);

        for (Edge currentEdge : graphsMST.edgesOfMST()) {
            StdOut.println(currentEdge);
        }

        StdOut.printf("%.5f\n", graphsMST.weightOfMST());
    }
}