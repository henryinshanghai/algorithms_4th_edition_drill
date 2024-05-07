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

// 目标：获取到图的最小生成树MST
// 原理：最小横切边一定属于MST
// 步骤：#1 对边按照权重排序； #2 创建一个forest对象（每个结点都是一棵树）； #3 判断当前边是否为横切边。如果是，则：添加到MST（队列）中，并合并边的两个顶点
public class KruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private double weightOfMST;                        // 最小展开树的权重
    private Queue<Edge> edgesInMSTQueue = new Queue<Edge>();  // MST中的边所构成的队列

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * 计算一个加权图的 最小展开树
     * @param weightedGraph the edge-weighted graph
     */
    public KruskalMST(EdgeWeightedGraph weightedGraph) {

        // #1 获取 加权图的所有边（以可迭代集合的形式），然后 转换为数组形式
        Edge[] edges = getEdgesIn(weightedGraph);

        // #2 根据 Edge对象compareTo()方法的定义，来 对数组中的Edge对象 进行排序
        Arrays.sort(edges);

        // #3 执行贪心算法 遍历边集合中的每一条边
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (int currentEdgeCursor = 0; withinLegitRange(weightedGraph, currentEdgeCursor); currentEdgeCursor++) {
            // 对于当前边
            Edge currentEdge = edges[currentEdgeCursor];
            // 使用它来构建出MST
            constructMST(forest, currentEdge);
        }

        // check optimality conditions
        assert check(weightedGraph);
    }

    private void constructMST(QuickFind forest, Edge currentEdge) {
        // #1 从排序后的数组中，获取到 当前边 & 当前边的两个端点
        int oneVertex = currentEdge.eitherVertex();
        int theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

        // #2 如果 边的两个端点 不在同一个连通分量中，说明 这条边 能够把两棵树连接成一棵更大的树（这是一个横切边?），则：
        // 原理：连接同一个连通分量中的两个顶点 会形成一个环
        if (notInSameComponent(forest, oneVertex, theOtherVertex)) {
            // ① 把两个顶点 合并到 同一个连通分量中,得到一个更大的连通分量
            forest.unionToSameComponent(oneVertex, theOtherVertex);     // merge oneVertex and theOtherVertex components
            // ② 使用当前边来更新MST
            updateMSTVia(currentEdge);
        }
    }

    private void updateMSTVia(Edge currentEdge) {
        // ① 把这条边添加到“MST边队列”中(因为它连接了两个连通分量，所以它一定是一条横切边 而最小横切边一定属于MST)
        edgesInMSTQueue.enqueue(currentEdge);     // add edge currentEdge to mst
        // ② 更新MST的权重值
        weightOfMST += currentEdge.weight();
    }

    private Edge[] getEdgesIn(EdgeWeightedGraph weightedGraph) {
        Edge[] edges = new Edge[weightedGraph.getEdgeAmount()];
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

    private boolean withinLegitSize(EdgeWeightedGraph weightedGraph) {
        return edgesInMSTQueue.size() < weightedGraph.getVertexAmount() - 1;
    }

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
        // 检查是不是一个 最小生成树  todo extract method
        for (Edge currentMSTEdge : edges()) {

            // all edges in MST except currentMSTEdge
            unionedForest = new QuickFind(weightedGraph.getVertexAmount());
            for (Edge currentEdgeInMST : edgesInMSTQueue) {
                int oneVertex = currentEdgeInMST.eitherVertex(),
                    theOtherVertex = currentEdgeInMST.theOtherVertexAgainst(oneVertex);

                if (currentEdgeInMST != currentMSTEdge)
                    unionedForest.unionToSameComponent(oneVertex, theOtherVertex);
            }

            // check that currentMSTEdge is min weight edge in crossing cut
            for (Edge graphsCurrentEdge : weightedGraph.edges()) {
                int oneVertex = graphsCurrentEdge.eitherVertex(),
                    theOtherVertex = graphsCurrentEdge.theOtherVertexAgainst(oneVertex);

                if (notInSameComponent(unionedForest, oneVertex, theOtherVertex)) {
                    if (graphsCurrentEdge.weight() < currentMSTEdge.weight()) {
                        System.err.println("Edge " + graphsCurrentEdge + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
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