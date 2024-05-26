package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.lazy_implementation;

/******************************************************************************
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
import edu.princeton.cs.algs4.BoruvkaMST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.PrimMST;
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
// 目标：计算 图的最小生成树；
// 原理：对于任意切分结果中，最小横切边 总是属于MST；
// 思想：对于一个连通图，在对图结点进行BFS的过程中，标记结点为MST顶点&&向MST中添加其所关联的有效横切边
// 步骤：#1 把结点标记为”MST结点“ && 向”横切边优先队列“中添加横切边； #2 获取最小横切边，并添加到MST中； #3 对于横切边的“非树节点”，继续#1的操作。
// 一句话描述：在BFS的过程中，把图结点标记成为MST结点&&把它所关联的所有横切边添加到PQ中。然后把PQ中的最小横切边添加到MST中。当PQ为空时，算法就得到了图的MST
// 为什么叫做 延迟实现? 因为 有些无效的边(连接两个MST结点的边)会被添加到PQ中，需要等到(that's why)要删除它们的时候，才会检查边的有效性 - 对边的有效性进行延迟检查?!
public class LazyPrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private double weightOfMST;       // 最小展开树的权重
    private Queue<Edge> edgesInMSTQueue;     // MST中的边所构成的队列    用于表示MST
    private boolean[] vertexToIsMSTVertex;    // 顶点 -> 顶点是否属于MST的映射 用于记录 顶点是否已经被添加到MST中
    private MinPQ<Edge> crossEdgesPQ;      // 由横切边(边的一个端点在MST中)组成的优先队列  用于记录BFS中出现的横切边

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param weightedGraph the edge-weighted graph
     */
    public LazyPrimMST(EdgeWeightedGraph weightedGraph) {
        edgesInMSTQueue = new Queue<Edge>();
        crossEdgesPQ = new MinPQ<Edge>();
        vertexToIsMSTVertex = new boolean[weightedGraph.getVertexAmount()];

        // option01 - 直接从结点0开始在加权图中进行BFS（因为BFS的方式能够遍历 连通图中的每一个结点）
        prim(weightedGraph, 0);

        // option02 - 对于每一个结点，对其执行Prim算法 来 得到“图的最小生成树”
//        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
//            if (isNotMSTVertex(currentVertex))
//                prim(weightedGraph, currentVertex);

        // check optimality conditions
        assert check(weightedGraph);
    }

    private boolean isNotMSTVertex(int currentVertex) {
        return !vertexToIsMSTVertex[currentVertex];
    }

    // run Prim's algorithm
    private void prim(EdgeWeightedGraph weightedGraph, int startVertex) {
        // #0 把当前顶点 标记为”MST结点“ && 把当前顶点相关联的横切边 添加到优先队列中
        markVertexAsMSTAndAddItsCrossEdgesIntoPQ(weightedGraph, startVertex);

        while (!crossEdgesPQ.isEmpty()) {
            // #1 从”横切边优先队列“中，获取到 当前最小的横切边
            Edge minWeightEdge = crossEdgesPQ.delMin();
            // 如果”此最小横切边“已经是”MST边“了(因为先前添加的边导致后继横切边的两个顶点都已经变成MST顶点了)，则 跳过此横切边
            // 🐖 这里就是进行 延迟检查的地方 - 在从PQ中弹出最小边后，需要检查 这个边是不是一个有效的横切边
            if (isMSTEdge(minWeightEdge)) continue;

            // #2 把 “最小的横切边” 添加到 MST中（切分定理：最小横切边总是会属于MST）
            addEdgeInMST(minWeightEdge);

            // #3 把 最小横切边中所有的“非MST结点/图结点” 都添加到树中，成为”MST结点“
            addNonMSTVertexToMST(weightedGraph, minWeightEdge);
        }
    }

    private void addNonMSTVertexToMST(EdgeWeightedGraph weightedGraph, Edge minWeightEdge) {
        int oneVertex = minWeightEdge.eitherVertex(),
            theOtherVertex = minWeightEdge.theOtherVertexAgainst(oneVertex);

        // 如果顶点不是MST结点，说明它还没有被BFS处理过，则：#1 把它添加为MST顶点； #2 把它所有的横切边添加到优先队列中
        if (isNotMSTVertex(oneVertex)) {
            markVertexAsMSTAndAddItsCrossEdgesIntoPQ(weightedGraph, oneVertex);
        }

        if (isNotMSTVertex(theOtherVertex)) {
            markVertexAsMSTAndAddItsCrossEdgesIntoPQ(weightedGraph, theOtherVertex);               // theOtherVertex becomes part of tree
        }
    }

    private void addEdgeInMST(Edge minWeightEdge) {
        // #1 把横切边添加到表示MST的edge队列中
        // 切分定理：把 当前权值最小的横切边 作为 最小生成树中的边。
        edgesInMSTQueue.enqueue(minWeightEdge);

        // #2 为新添加的边 累计其权值
        weightOfMST += minWeightEdge.weight();
    }

    private boolean isMSTEdge(Edge minWeightEdge) {
        int oneVertex = minWeightEdge.eitherVertex(),
                theOtherVertex = minWeightEdge.theOtherVertexAgainst(oneVertex);        // two endpoints
        assert vertexToIsMSTVertex[oneVertex] || vertexToIsMSTVertex[theOtherVertex];

        return vertexToIsMSTVertex[oneVertex] && vertexToIsMSTVertex[theOtherVertex];
    }

    // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
    private void markVertexAsMSTAndAddItsCrossEdgesIntoPQ(EdgeWeightedGraph weightedGraph, int currentVertex) { // scan?? visit??
        // #1 把 当前顶点 标记为 “MST顶点”
        assert !vertexToIsMSTVertex[currentVertex];
        vertexToIsMSTVertex[currentVertex] = true;

        // #2 把 与当前节点相关联的所有graphEdge中的“横切边” 添加到 “横切边队列 crossEdgeQueue”中
        // 横切边的概念：连接 分属于两个集合(MSTVertex & graphVertex)的顶点的边
        for (Edge currentAssociatedGraphEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            int theOtherVertex = currentAssociatedGraphEdge.theOtherVertexAgainst(currentVertex);

            // 如果当前graphEdge是一个横切边(手段：它的另一个顶点 不是“MST顶点”)，则：把它添加到 ”横切边优先队列“中
            // 🐖 这里为 currentVertex所添加的横切边，并不一定最终用来构造MST。因为有些边已经不再是有效的横切边
            if (isNotMSTVertex(theOtherVertex)) {
                crossEdgesPQ.insert(currentAssociatedGraphEdge);
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
        return edgesInMSTQueue;
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

            for (Edge currentEdgeInMST : edgesInMSTQueue) {
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