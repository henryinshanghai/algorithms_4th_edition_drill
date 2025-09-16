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
import edu.princeton.cs.algs4.BoruvkaMST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LazyPrimMST;
import edu.princeton.cs.algs4.PrimMST;
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

// 目标：获取到 加权无向图的最小生成树MST
// 原理：最小横切边 一定属于MST
// 思想：向MST中 逐个地、分散地 添加 当前权重最小的边，直到 MST被完全构建
// 步骤：#1 把边 按照权重排序； #2 创建一个forest对象（N个节点，每个结点都是一棵树）；
// #3 判断 引入当前边是否会导致环 。如果不会，则：添加到MST（队列）中，并 合并边的两个顶点
// 一句话描述：对于 排序后的图中的边序列，如果 当前边添加到MST中不会引入环，则
// #1 合并 由该边连接的两个分量； #2 把 该最小边 添加到MST中 - 直到MST中的结点数量 = 图中的结点数量，此时的分量就是我们想要的MST
// 特征：算法其实依赖于一个forest对象，以及它的 unionToSameComponent()的操作
public class KruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private double weightOfMST;                        // 最小展开树的总权重
    private Queue<Edge> edgesInMSTQueue = new Queue<Edge>();  // 由 MST中的边 所构成的队列

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * 计算一个加权图的 最小展开树
     * @param weightedGraph the edge-weighted graph
     */
    public KruskalMST(EdgeWeightedGraph weightedGraph) {
        // #1 获取 加权图的所有边（以可迭代集合的形式），然后 转换为数组形式
        Edge[] edges = getEdgesIn(weightedGraph);

        // #2 根据 Edge对象compareTo()方法所定义的规则，来 对数组中的Edge对象 进行排序
        Arrays.sort(edges);

        /* #3 执行 贪心算法，遍历边集合中的每一条边 */
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (int currentEdgeCursor = 0; withinLegitRange(weightedGraph, currentEdgeCursor); currentEdgeCursor++) {
            // 对于 当前权重最小的边
            Edge currentEdge = edges[currentEdgeCursor];
            // 尝试使用它 来 构建出MST
            constructMST(forest, currentEdge);
        }

        // check optimality conditions
        assert check(weightedGraph);
    }

    private void constructMST(QuickFind forest, Edge currentMinEdge) {
        // #1 获取到 最小边的两个端点
        int oneVertex = currentMinEdge.eitherVertex();
        int theOtherVertex = currentMinEdge.theOtherVertexAgainst(oneVertex);

        // #2 如果 边的两个端点 不在同一个连通分量中，说明 添加此边到MST中不会形成环，则：
        // 原理：连接同一个连通分量中的两个顶点 会形成一个环
        if (notInSameComponent(forest, oneVertex, theOtherVertex)) {
            // ① 把两个顶点 合并到 同一个连通分量中（这个连通分量最终会扩展得到MST）
            forest.unionToSameComponent(oneVertex, theOtherVertex);     // merge oneVertex and theOtherVertex components
            // ② 使用 当前边 来 更新MST中的边 与 总权重
            updateMSTVia(currentMinEdge);
        }
    }

    private void updateMSTVia(Edge currentEdge) {
        // ① 把 这条边 添加到“MST边队列”中(最小横切边一定属于MST)
        edgesInMSTQueue.enqueue(currentEdge);     // add edge currentEdge to mst
        // ② 更新MST的权重值
        weightOfMST += currentEdge.weight();
    }

    private Edge[] getEdgesIn(EdgeWeightedGraph weightedGraph) {
        Edge[] edges = new Edge[weightedGraph.getEdgeAmount()];

        // 把 图中的每一条边 都添加到数组中
        int currentSpot = 0;
        for (Edge currentEdge : weightedGraph.edges()) {
            edges[currentSpot++] = currentEdge;
        }
        return edges;
    }

    private boolean notInSameComponent(QuickFind forest, int oneVertex, int theOtherVertex) {
        return forest.findGroupIdOf(oneVertex) != forest.findGroupIdOf(theOtherVertex);
    }

    private boolean withinLegitRange(EdgeWeightedGraph weightedGraph, int currentEdgeCursor) {
        return withinLegitAmount(weightedGraph, currentEdgeCursor) && withinLegitSize(weightedGraph);
    }

    // MST中 边的数量的有效性
    private boolean withinLegitSize(EdgeWeightedGraph weightedGraph) {
        return edgesInMSTQueue.size() < weightedGraph.getVertexAmount() - 1;
    }

    // 原始图中 边的指针大小(数量)的有效性
    private boolean withinLegitAmount(EdgeWeightedGraph weightedGraph, int currentEdgeCursor) {
        return currentEdgeCursor < weightedGraph.getEdgeAmount();
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * 以可迭代集合的方式 来 返回最小展开树中的边
     * @return the edges in a minimum spanning tree (or forest) as
     * an iterable of edges
     */
    public Iterable<Edge> edges() {
        return edgesInMSTQueue;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * 返回最小展开树中所有边的权重之和
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        return weightOfMST;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph weightedGraph) {

        // 检查MST的总权重值 - 边的权重之和 是不是 与MST的权重值相等
        if (MSTWeightNotRight()) return false;

        // 检查MST是否满足无环图的约束 - MST中不应该存在有环
        QuickFind unionedForest = getUnionedForestOfMSTVertexesIn(weightedGraph);
        if (unionedForest == null) return false;

        // 检查MST是否满足“展开树”的约束 - 原始树中的任意边的结点，都应该出现在MST中
        if (anyEdgeBreachSpanningTree(weightedGraph, unionedForest)) return false;

        // check that it is a minimal spanning unionedForestOfMSTVertexesIn (cut optimality conditions)
        // 检查是不是一个 最小生成树
        if (breachMinRestriction(weightedGraph)) return false;

        return true;
    }

    private boolean breachMinRestriction(EdgeWeightedGraph weightedGraph) {
        for (Edge currentMSTEdge : edges()) {
            if (breachMinCrossEdge(weightedGraph, currentMSTEdge))
                return true;
        }
        return false;
    }

    private boolean breachMinCrossEdge(EdgeWeightedGraph weightedGraph, Edge currentMSTEdge) {
        // #1 新建一个森林，并使用 除了当前MST边以外的其他MST边来连接它
        QuickFind unionedForest = new QuickFind(weightedGraph.getVertexAmount());
        unionAllMSTEdgesBut(unionedForest, currentMSTEdge);

        // #2 检查 currentMSTEdge 是不是 横切边中的最小权重边
        for (Edge graphsCurrentEdge : weightedGraph.edges()) {
            if (lighterThan(currentMSTEdge, graphsCurrentEdge, unionedForest))
                return true;
        }
        return false;
    }

    private boolean lighterThan(Edge currentMSTEdge, Edge graphsCurrentEdge, QuickFind unionedForest) {
        int oneVertex = graphsCurrentEdge.eitherVertex(),
            theOtherVertex = graphsCurrentEdge.theOtherVertexAgainst(oneVertex);

        if (notInSameComponent(unionedForest, oneVertex, theOtherVertex)) {
            if (graphsCurrentEdge.weight() < currentMSTEdge.weight()) {
                System.err.println("Edge " + graphsCurrentEdge + " violates cut optimality conditions");
                return true;
            }
        }
        return false;
    }

    private void unionAllMSTEdgesBut(QuickFind unionedForest, Edge currentMSTEdge) {
        for (Edge currentEdgeInMST : edgesInMSTQueue) {
            int oneVertex = currentEdgeInMST.eitherVertex(),
                theOtherVertex = currentEdgeInMST.theOtherVertexAgainst(oneVertex);

            if (currentEdgeInMST != currentMSTEdge)
                unionedForest.unionToSameComponent(oneVertex, theOtherVertex);
        }
    }

    private boolean anyEdgeBreachSpanningTree(EdgeWeightedGraph weightedGraph, QuickFind unionedForest) {
        for (Edge currentEdge : weightedGraph.edges()) {
            int oneVertex = currentEdge.eitherVertex(),
                theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            if (notInSameComponent(unionedForest, oneVertex, theOtherVertex)) {
                System.err.println("Not a spanning unionedForestOfMSTVertexesIn");
                return true;
            }
        }
        return false;
    }

    private QuickFind getUnionedForestOfMSTVertexesIn(EdgeWeightedGraph weightedGraph) {
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (Edge currentEdge : edges()) {
            int oneVertex = currentEdge.eitherVertex(),
                theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            // 如果边的两个顶点 属于同一个连通分量，则：图就不是一个森林???
            if (forest.findGroupIdOf(oneVertex) == forest.findGroupIdOf(theOtherVertex)) {
                System.err.println("Not a forest");
                return null;
            }
            forest.unionToSameComponent(oneVertex, theOtherVertex);
        }
        return forest;
    }

    private boolean MSTWeightNotRight() {
        double totalWeight = 0.0;
        for (Edge currentEdge : edges()) {
            totalWeight += currentEdge.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return true;
        }
        return false;
    }


    /**
     * Unit tests the {@code KruskalMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 从 命令行参数中，读取 图的文件
        In in = new In(args[0]);
        // 创建加权图对象
        EdgeWeightedGraph weightedGraph = new EdgeWeightedGraph(in);
        // 使用Kruskal算法，得到 加权图的MST
        KruskalMST graphsMST = new KruskalMST(weightedGraph);

        // 顺序打印MST中的边
        for (Edge currentMSTEdge : graphsMST.edges()) {
            StdOut.println(currentMSTEdge);
        }
        StdOut.printf("%.5f\n", graphsMST.weight());
    }

}