package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.lazy_implementation.exe;

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
// 延迟实现：在获取“最小横切边”时，才去判断 队列中的边 是否是 有效横切边；
// 特征：会在队列中保存 已经失效的边。在处理稠密图时，队列中无效边的数量会很大
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

        // option02 - 对于每一个结点，对其执行Prim算法 来 得到 各个无向加权图的MST 所组成的森林
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
        markVertexAsMSTAndAddItsCrossEdgesIntoPQ(weightedGraph, startVertex);

        while (!crossEdgesPQ.isEmpty()) {
            Edge minWeightEdge = crossEdgesPQ.delMin();
            // 🐖 随着MST节点的添加，原本的横切边可能变得无效（连接了两个MST顶点）。需要跳过这样无效的边
            if (bothEndsAreMSTVertex(minWeightEdge)) continue;

            addEdgeInMST(minWeightEdge);

            // #3 把 最小横切边中所有的“非MST结点/图结点” 都添加到树中，成为”MST结点“
            repeatOnNonMSTVertex(weightedGraph, minWeightEdge);
        }
    }

    private void repeatOnNonMSTVertex(EdgeWeightedGraph weightedGraph, Edge minWeightEdge) {
        int oneVertex = minWeightEdge.eitherVertex(),
            theOtherVertex = minWeightEdge.theOtherVertexAgainst(oneVertex);

        if (isNotMSTVertex(oneVertex)) {
            markVertexAsMSTAndAddItsCrossEdgesIntoPQ(weightedGraph, oneVertex);
        }

        if (isNotMSTVertex(theOtherVertex)) {
            markVertexAsMSTAndAddItsCrossEdgesIntoPQ(weightedGraph, theOtherVertex);
        }
    }

    private void addEdgeInMST(Edge minWeightEdge) {
        edgesInMSTQueue.enqueue(minWeightEdge);
        weightOfMST += minWeightEdge.weight();
    }

    private boolean bothEndsAreMSTVertex(Edge minWeightEdge) {
        int oneVertex = minWeightEdge.eitherVertex(),
            theOtherVertex = minWeightEdge.theOtherVertexAgainst(oneVertex);        // two endpoints

        assert vertexToIsMSTVertex[oneVertex] || vertexToIsMSTVertex[theOtherVertex];
        return vertexToIsMSTVertex[oneVertex] && vertexToIsMSTVertex[theOtherVertex];
    }

    // 把 当前节点所关联的所有横切边 都添加到 横切边优先队列 中
    private void markVertexAsMSTAndAddItsCrossEdgesIntoPQ(EdgeWeightedGraph weightedGraph, int currentVertex) {
        assert !vertexToIsMSTVertex[currentVertex];
        vertexToIsMSTVertex[currentVertex] = true;

        for (Edge currentAssociatedGraphEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            int theOtherVertex = currentAssociatedGraphEdge.theOtherVertexAgainst(currentVertex);

            // 🐖 在这里添加时，currentAssociatedGraphEdge是一条 有效的横切边。但随着MST节点的添加，它可能会变成 连接MST节点的边 而失效
            // 所以 从优先队列中 取出边后，需要额外 添加对边是否是 连接两个MST节点的边 的校验
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

        // #1 检查图的权重
        double totalWeight = 0.0;
        // 累加得到 MST树中各个边（手段：edgesOfMST()）的权重之和
        for (Edge currentEdge : edgesOfMST()) {
            totalWeight += currentEdge.weight();
        }
        // 验证 weightOfMST() 返回的值 是否 与上述累计的结果 相等
        if (Math.abs(totalWeight - weightOfMST()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weightOfMST());
            return false;
        }

        /* #2 检查MST是非循环的(acyclic) */
        // 初始化 得到一个 分散的森林
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());

        /* 按照MST的特征(不存在环)可知：在被添加到同一组之前，任何边的两个顶点应该都分属于不同的组 */
        // 对于MST树中的每一条边..
        for (Edge currentEdge : edgesOfMST()) {
            // 得到 边的两个端点
            int oneVertex = currentEdge.eitherVertex(),
                theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            /* 在把端点添加到同一组中之前，检查 它们是不是 已经在同一个组中了 */
            // 如果 边的两个端点 属于同一个组，说明 MST中存在有环，则：
            if (isInSameGroup(forest, oneVertex, theOtherVertex)) {
                // 算法生成的MST 不是有效的MST，返回false 表示 检查未通过
                System.err.println("Not a forest");
                return false;
            }

            // 把 边的两个端点 添加到 同一个组中
            forest.unionToSameComponent(oneVertex, theOtherVertex);
        }
        /* 如果MST是正确的，则：在for循环结束后，所有的节点 会都已经在 同一个组中了 */

        // #3 check that it is a “spanning forest”(展开的森林)
        for (Edge currentEdge : weightedGraph.edges()) {
            // 获取到 当前边的两个端点
            int oneVertex = currentEdge.eitherVertex(),
                theOtherVertexAgainst = currentEdge.theOtherVertexAgainst(oneVertex);

            // 如果 其两个端点 不在同一个组中，说明???
            if (notInSameGroup(forest, oneVertex, theOtherVertexAgainst)) {
                // 则：返回false 来 表示其不是一个 spanning tree
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // #4 check that it is a minimal spanning forest (cut optimality conditions)
        // 检查 它是“最小展开森林”（cut最优条件??）
        for (Edge currentMSTEdge : edgesOfMST()) {

            // all edges in MST except currentMSTEdge
            // 重新创建一片森林 森林中都是分散的树节点
            forest = new QuickFind(weightedGraph.getVertexAmount());

            for (Edge currentEdgeInMST : edgesInMSTQueue) {
                // 获取到MST边的两个端点
                int oneVertex = currentEdgeInMST.eitherVertex(),
                    theOtherVertex = currentEdgeInMST.theOtherVertexAgainst(oneVertex);

                // 如果 这两条边 不相同，说明 ???，则：
                if (currentEdgeInMST != currentMSTEdge) {
                    // 把两个端点 合并到同一个组中
                    forest.unionToSameComponent(oneVertex, theOtherVertex);
                }
            }

            // 检查 currentMSTEdge 是 横切边中的最小权重边
            for (Edge currentEdge : weightedGraph.edges()) {
                int oneVertex = currentEdge.eitherVertex(),
                    theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

                // 如果xxx，说明 当前边 是一条横切边，则：
                if (notInSameGroup(forest, oneVertex, theOtherVertex)) {
                    // 比较 该横切边 与 MST中的横切边的权重大小，如果 该横切边更小，说明 MST的横切边 错误，则：
                    if (currentEdge.weight() < currentMSTEdge.weight()) {
                        // 返回false 来 表示MST边的错误
                        System.err.println("Edge " + currentEdge + " violates cut optimality conditions");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // 判断 节点v 和 节点w 是否在同一个组中
    private boolean isInSameGroup(QuickFind forest, int oneVertex, int theOtherVertex) {
        return forest.findGroupIdOf(oneVertex) == forest.findGroupIdOf(theOtherVertex);
    }

    private boolean notInSameGroup(QuickFind groups, int oneVertex, int theOtherVertexAgainst) {
        return groups.findGroupIdOf(oneVertex) != groups.findGroupIdOf(theOtherVertexAgainst);
    }


    /**
     * Unit tests the {@code LazyPrimMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 文件名 -> 文件流
        In in = new In(args[0]);
        // 文件流 -> 无向加权图对象
        EdgeWeightedGraph weightedGraph = new EdgeWeightedGraph(in);
        // 图对象 -> 图中的MST
        LazyPrimMST graphsMST = new LazyPrimMST(weightedGraph);

        // 打印图的MST的所有边
        // 问：一幅 无向加权连通图 就只有一个MST吗？
        for (Edge currentEdge : graphsMST.edgesOfMST()) {
            StdOut.println(currentEdge);
        }

        StdOut.printf("%.5f\n", graphsMST.weightOfMST());
    }
}