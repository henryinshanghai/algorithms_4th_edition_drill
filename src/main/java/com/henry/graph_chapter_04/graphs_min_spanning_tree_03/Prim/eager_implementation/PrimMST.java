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
import edu.princeton.cs.algs4.BoruvkaMST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.LazyPrimMST;
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
// 目标：计算出 图的最小生成树MST(minimum spanning tree)
// 原理：对于MST来说，原始图中的每一个结点，都会有它的“距离MST的最小边”。
// 思想：通过记录每个结点 距离MST的最小边(如果有的话)，就能够得到MST本身
// 步骤：#1 把某一个结点的“距离MST的最小边”设置为0 & 把vertex -> itsMinEdge添加到优先队列中；#2 从队列中获取“最小的”结点；
// #3 把结点更新为“树结点” & 更新“theOtherVertex结点“ 它”距离MST的最小边”。
public class PrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private Edge[] vertexToItsMinEdgeToMST;     // 用于维护 结点->“其连接到MST的最小边”的映射    应用：获取到MST本身
    private double[] vertexToItsMinEdgeWeight;      // 用于维护 结点->“其连接到MST的最小边”的权重值 的映射   手段：数组
    private boolean[] vertexToIsMSTVertex;     // 用于记录 结点->结点是不是MST结点 的映射
    private IndexMinPQ<Double> vertexToItsMinEdgeWeightPQ; // 用于记录 结点->“其连接到MST的最小边”的权重值 的映射 手段：索引优先队列 应用：快速获取到minEdgeWeight

    // 计算出 加权图的一个MST或森林
    // 参数：加权图
    public PrimMST(EdgeWeightedGraph weightedGraph) {
        instantiateVariables(weightedGraph);

        initVertexToMinEdgeWeightArr(weightedGraph);

        // 对于图中的每一个顶点
        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
            // 如果它还不是MST结点，说明它所在的连通分量还没有被BFS过，则：
            if (isNotMSTVertex(currentVertex)) {
                // 以它作为“起始顶点”，执行prim算法 来 得到它所在的连通分量图的一个MST
                prim(weightedGraph, currentVertex);
            }

        // check optimality conditions
        assert check(weightedGraph);
    }

    private void initVertexToMinEdgeWeightArr(EdgeWeightedGraph weightedGraph) {
        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
            // 从 当前“树节点” 到 “非树节点”的最短边 - 初始化为 正无穷大
            vertexToItsMinEdgeWeight[currentVertex] = Double.POSITIVE_INFINITY;
    }

    private void instantiateVariables(EdgeWeightedGraph weightedGraph) {
        vertexToItsMinEdgeToMST = new Edge[weightedGraph.getVertexAmount()];
        vertexToItsMinEdgeWeight = new double[weightedGraph.getVertexAmount()];
        vertexToIsMSTVertex = new boolean[weightedGraph.getVertexAmount()];
        vertexToItsMinEdgeWeightPQ = new IndexMinPQ<Double>(weightedGraph.getVertexAmount());
    }

    private boolean isNotMSTVertex(int currentVertex) {
        return !vertexToIsMSTVertex[currentVertex];
    }

    // 在加权图中，以指定顶点作为起始顶点 来 执行prim算法
    private void prim(EdgeWeightedGraph weightedGraph, int startVertex) {
        // #1 初始化 当前顶点的 ”到MST的最小边的权重值“为0
        vertexToItsMinEdgeWeight[startVertex] = 0.0;
        // #2 初始化 当前顶点 在索引优先队列中的entry
        vertexToItsMinEdgeWeightPQ.insert(startVertex, vertexToItsMinEdgeWeight[startVertex]);

        while (!vertexToItsMinEdgeWeightPQ.isEmpty()) {
            // #3 从优先队列中，获取到拥有最小的“距离MST最小边的权重值”权重值的 那个结点
            int vertexWithMinEdgeWeight = vertexToItsMinEdgeWeightPQ.delMin();

            // #4 对于此结点：① 标记为“树结点”； ② 对于其所有相邻的“图结点”，更新其 属性值，并将之添加/更新到优先队列中
            markVertexAsMSTAndUpdateNeighborGraphVertex(weightedGraph, vertexWithMinEdgeWeight);
        }
    }

    // scan vertex v
    private void markVertexAsMSTAndUpdateNeighborGraphVertex(EdgeWeightedGraph weightedGraph, int passedVertex) {
        // #1 把结点 标记为 “树结点”
        vertexToIsMSTVertex[passedVertex] = true;

        // #2 对于 结点 所有关联的边...
        for (Edge currentAssociatedGraphEdge : weightedGraph.getAssociatedEdgesOf(passedVertex)) {
            // 获取到 当前graphEdge的另一个顶点
            int theGraphVertex = currentAssociatedGraphEdge.theOtherVertexAgainst(passedVertex);

            // ① 如果 另一个顶点 也是 MSTVertex，说明 当前graphEdge已经是一个MSTEdge，则：
            if (isMSTVertex(theGraphVertex)) {
                // 直接跳过它
                continue;
            }

            /* 否则，说明 当前graphEdge是一个横切边 */
            // ② 如果 当前边的权值 小于 vertexToItsMinEdgeWeight数组中所记录的 此结点的“连接MST的最小边”的权重，说明 当前边是“连接MST的更小边”，则：
            if (hasSmallerWeightThanRecorded(currentAssociatedGraphEdge, theGraphVertex)) {
                // 更新 此图结点(theGraphVertex)的所有属性
                updateGraphVertexProperties(currentAssociatedGraphEdge, theGraphVertex);
            }
        }
    }

    private void updateGraphVertexProperties(Edge currentAssociatedGraphEdge, int theGraphVertex) {
        // Ⅰ 更新 图结点(theOtherVertex)的 “其连接到MST的最小边”
        vertexToItsMinEdgeToMST[theGraphVertex] = currentAssociatedGraphEdge;
        // Ⅱ 更新 图结点的 “连接到MST的最小边”的权值
        vertexToItsMinEdgeWeight[theGraphVertex] = currentAssociatedGraphEdge.weight();
        // Ⅲ 更新 图结点 在索引优先队列中所对应的entry
        updatePQEntryFor(theGraphVertex);
    }

    private void updatePQEntryFor(int passedVertex) {
        // 如果此结点 已经存在于 优先队列中...
        if (vertexToItsMinEdgeWeightPQ.contains(passedVertex))
            // 则：更新优先队列中 此结点所对应的 最小边的权重值
            // 🐖 使用索引优先队列，是为了能够 方便地更新 结点所对应的“连接到MST的最小边”的权重
            // 这里使用 vertex 来 作为索引（vertex -> weight）
            vertexToItsMinEdgeWeightPQ.decreaseKey(passedVertex, vertexToItsMinEdgeWeight[passedVertex]);
        else // 如果 此结点还不存在于 优先队列中...
            // 则：把 结点 -> 结点对应的 最小边的权重值 添加到 优先队列中
            vertexToItsMinEdgeWeightPQ.insert(passedVertex, vertexToItsMinEdgeWeight[passedVertex]);
    }

    private boolean hasSmallerWeightThanRecorded(Edge currentAssociatedGraphEdge, int neighborGraphVertex) {
        return currentAssociatedGraphEdge.weight() < vertexToItsMinEdgeWeight[neighborGraphVertex];
    }

    private boolean isMSTVertex(int theOtherVertex) {
        return vertexToIsMSTVertex[theOtherVertex];
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