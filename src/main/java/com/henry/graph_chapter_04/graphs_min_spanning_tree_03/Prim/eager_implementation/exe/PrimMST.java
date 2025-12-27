package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.eager_implementation.exe;

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
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah.EdgeWeightedGraph;
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
// 目标：计算出 加权无向连通图 的最小生成树MST(minimum spanning tree)
// 概念：对于MST来说，原始图中的每一个图结点，都会有 它的“连接到MST的最小边”。
// 思想：通过记录 每个结点 距离MST的最小边(如果有的话)，就能够得到 MST本身
// 步骤：① 初始化一个图节点作为MST节点；② 借助索引PQ，获取到 连接到MST的最小边的图节点；③ 把该图节点标记为MST节点，并 更新其所有的邻居图节点；
// 最终，使用 算法结束后的vertexToItsMinCrossEdgeToMST[] 来 得到 MST本身(集合形式)
// 即时实现：在 把 结点 添加到 结点PQ 前，先验证 结点的有效性
public class PrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    // 结点->“其连接到MST的最小边” 的映射    用于从中得到MST本身
    private Edge[] vertexToItsMinCrossEdgeToMST;

    // 结点->“其连接到MST的最小边”的权重值 的映射；   用于 快速获取到 节点的最小边权重值； 应用：判断 当前边 是不是 更小的边
    private double[] vertexToItsMinCrossEdgeWeightToMST;
    // 结点->“其连接到MST的最小边”的权重值 的映射；   用于 快速获取到 拥有最小边权重值的节点，以及 快速更新 节点的最小边权重值； 应用：获取 最小横切边
    private IndexMinPQ<Double> vertexToItsMinCrossEdgeWeightPQ;

    // 结点->结点是不是MST结点 的映射；  用于 判断给定的边 是不是 有效横切边
    private boolean[] vertexToIsMSTVertex;

    // 作用：① 初始化成员变量；② 计算出 加权图的一个MST或森林
    // 参数：加权图
    public PrimMST(EdgeWeightedGraph weightedGraph) {
        // ① 初始化 所有的 成员变量
        initCapacity(weightedGraph);

        // 对 vertex -> itsMinCrossEdgeWeight 的数组 进行 元素初始化
        initItems(weightedGraph);

        // 对于图中的每一个顶点...
        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
            // 如果 该节点 还不是 MST结点，说明 它所在的连通分量 还没有 被搜索过，则：
            if (isNotMSTVertex(currentVertex)) {
                // 以 它 作为 “起始顶点”，执行 prim算法 来 得到 它 所在的连通分量图的一个MST
                prim(weightedGraph, currentVertex);
            }

        // check optimality conditions
        assert check(weightedGraph);
    }

    private void initItems(EdgeWeightedGraph weightedGraph) {
        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++)
            // 从 当前“树节点” 到 MST的权重最小的边的权重 - 初始化为 正无穷大
            vertexToItsMinCrossEdgeWeightToMST[currentVertex] = Double.POSITIVE_INFINITY;
    }

    // 完成 数组成员变量的 容量初始化
    private void initCapacity(EdgeWeightedGraph weightedGraph) {
        vertexToItsMinCrossEdgeToMST = new Edge[weightedGraph.getVertexAmount()];
        vertexToItsMinCrossEdgeWeightToMST = new double[weightedGraph.getVertexAmount()];
        vertexToIsMSTVertex = new boolean[weightedGraph.getVertexAmount()];
        vertexToItsMinCrossEdgeWeightPQ = new IndexMinPQ<Double>(weightedGraph.getVertexAmount());
    }

    private boolean isNotMSTVertex(int currentVertex) {
        return !vertexToIsMSTVertex[currentVertex];
    }

    //

    /**
     * 在 指定的无向加权连通图 中，以 指定顶点 作为 起始顶点 来 执行prim算法
     *
     * @param weightedGraph 指定的加权图
     * @param startVertex   指定的起始节点
     */
    private void prim(EdgeWeightedGraph weightedGraph,
                      int startVertex) {
        /* 对 startVertex节点相关的成员变量 做必要的初始化 */
        // 初始化 起始节点 ”到MST的最小边的权重值“ 为0 (因为把它作为MST节点)
        vertexToItsMinCrossEdgeWeightToMST[startVertex] = 0.0;
        // 初始化 起始节点 在索引优先队列中的 条目：index:节点 -> key:节点 连接到MST的 最小边的权重
        vertexToItsMinCrossEdgeWeightPQ.insert(startVertex, vertexToItsMinCrossEdgeWeightToMST[startVertex]);

        // 如果 横切边优先队列中 还存在有“横切边”（可能已经无效），说明 仍旧存在有 图节点，则：
        // 需要 ① 把 最小横切边 添加为 MST的边；② 把 图节点 添加为 MST树节点；
        while (!vertexToItsMinCrossEdgeWeightPQ.isEmpty()) {
            // 获取到 队列所有节点中，距离MST最近的 那个节点
            // 手段：minPQ.delMin(); 因为该方法 返回的是 index
            // 🐖 这里从PQ中出队 “连接到MST最小权重的节点”后，不需要像 lazyPrim那样进行校验（因为 在添加时，已经确保 是最小边了）
            int vertexWithMinEdgeWeight = vertexToItsMinCrossEdgeWeightPQ.delMin();

            // 对于此结点：① 标记为“树结点”； ② 对于其所有相邻的“图结点”，更新其 属性值，并将之添加/更新到优先队列中
            markVertexAsMSTAndUpdateNeighborGraphVertex(weightedGraph, vertexWithMinEdgeWeight);
        }
    }


    /**
     * 把 指定加权图中的 指定节点 添加为 MST节点，并 更新其 所有相邻图节点的 距离MST的最小边
     * @param weightedGraph 指定的加权图
     * @param givenVertex  指定的图节点
     */
    private void markVertexAsMSTAndUpdateNeighborGraphVertex(EdgeWeightedGraph weightedGraph,
                                                             int givenVertex) {
        // #1 把 传入的图结点 标记为 “树结点”
        vertexToIsMSTVertex[givenVertex] = true;

        // #2 对于 该树节点 在图中 所关联的所有边...
        for (Edge currentAssociatedEdge : weightedGraph.getAssociatedEdgesOf(givenVertex)) {
            // 获取到 该关联边 的另一个端点
            int theOtherVertexInEdge = currentAssociatedEdge.theOtherVertexAgainst(givenVertex);

            // 如果 该端点 也是 MSTVertex，说明 该关联边 不是 横切边，则：
            if (isMSTVertex(theOtherVertexInEdge)) {
                // 直接跳过 该关联边
                continue;
            }

            /* 否则，另一个节点 会是一个图节点，说明 当前关联边 是一个 横切边，则：(在MST中 添加了新的节点后) 尝试更新 该图节点 连接到MST的最小边(及权重) */
            // 如果 该横切边的权重 小于 先前记录的 此结点的“连接到MST的最小边”的权重，说明 找到了 此节点的 连接到MST的更小边，则：
            if (hasSmallerWeightThanRecorded(theOtherVertexInEdge, currentAssociatedEdge)) {
                // 更新 此图结点(theOtherVertexInEdge)的 所有属性
                updateGraphVertexProperties(theOtherVertexInEdge, currentAssociatedEdge);
            }
        }
    }

    /**
     * 根据 指定图节点到当前MST的最小边 来 更新 该节点的 成员属性
     * @param givenGraphVertex    指定的图节点
     * @param newMinCrossEdge   该节点到当前MST的最小边
     */
    private void updateGraphVertexProperties(int givenGraphVertex, Edge newMinCrossEdge) {
        /* #1 更新 数组成员变量 */
        vertexToItsMinCrossEdgeToMST[givenGraphVertex] = newMinCrossEdge;
        vertexToItsMinCrossEdgeWeightToMST[givenGraphVertex] = newMinCrossEdge.weight();
        // #2 更新 索引优先队列 成员变量
        updatePQEntryFor(givenGraphVertex);
    }

    // 🐖 即时实现的特征：对于每一个图节点，minPQ 只为其维护了 单一的一条 到MST的最小边
    private void updatePQEntryFor(int passedVertex) {
        // 如果 此结点 已经存在于 优先队列中...
        if (vertexToItsMinCrossEdgeWeightPQ.contains(passedVertex)) {
            // 则：更新 优先队列中 此结点所对应的 最小边的权重值
            // 🐖 之所以 使用 索引优先队列，就是为了 这里 能够 方便地更新 结点（作为索引）所对应的“连接到MST的最小边”的权重(作为key/优先级)
            vertexToItsMinCrossEdgeWeightPQ.changeKey(passedVertex, vertexToItsMinCrossEdgeWeightToMST[passedVertex]);
        } else { // 如果 此结点 还不存在于 优先队列中...
            // 则：添加 entry(index, key) index:vertex; key: minEdgeWeight;
            vertexToItsMinCrossEdgeWeightPQ.insert(passedVertex, vertexToItsMinCrossEdgeWeightToMST[passedVertex]);
        }
    }

    /**
     * 判断 对于 给定的图节点，经过 给定的关联边 连接到 MST时，是否 会得到 更小的连接边?
     * @param givenGraphVertex  给定的图节点
     * @param givenReachToMSTEdge  给定的关联边（图节点，经过此关联边 连接到 MST）
     * @return
     */
    private boolean hasSmallerWeightThanRecorded(int givenGraphVertex,
                                                 Edge givenReachToMSTEdge) {
        double currentRecordedMinEdge = vertexToItsMinCrossEdgeWeightToMST[givenGraphVertex];
        double givenEdgesWeight = givenReachToMSTEdge.weight();

        return currentRecordedMinEdge > givenEdgesWeight;
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

        for (int currentVertex = 0; currentVertex < vertexToItsMinCrossEdgeToMST.length; currentVertex++) {
            Edge minEdgeToMST = vertexToItsMinCrossEdgeToMST[currentVertex];
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