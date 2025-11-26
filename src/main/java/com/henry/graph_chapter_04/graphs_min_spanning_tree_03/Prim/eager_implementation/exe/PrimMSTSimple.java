package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.eager_implementation.exe;

import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah.EdgeWeightedGraph;
import edu.princeton.cs.algs4.IndexMinPQ;

// Prim算法“即时实现”步骤：
// 特征：通过一些手段，避免 把无效的边 添加到优先队列中
// 算法步骤：
// #1 把当前结点，标记为“MST结点”； #2 遍历 MST节点 所关联的所有边，更新 图结点的“最小边”、“最小边的权重” - 直到索引优先队列为空
// 🐖 所谓“即时实现”，指的是 把“横切边”添加到PQ中时，就已经是 “最小的横切边”了
// 难点：怎么描述 某个图节点”到MST的最小边“呢？
// 由于 MST中的节点 是逐个添加的，因此 我们可以 在每次添加新的MST节点时：
// ① 得到 其邻居图节点 到 此新MST节点 的边的权重；
// ② 比较 该边的权重 与 先前记录的 此图节点到MST的最小边的权重，就可以 更新/描述 ”此图节点到MST的最小边的权重“ */
public class PrimMSTSimple {
    // 节点 -> 节点到MST的权重最小的边
    private Edge[] vertexToItsMinCrossEdgeToMST; // 🐖 在这个简单版的实现中，这个成员变量没啥用，因为它只有写，而没有读
    private double[] vertexToItsMinCrossEdgesWeight; // 节点 -> 节点到MST的权重最小的边的权重
    private boolean[] vertexToIsMSTVertex; // 节点 -> 节点是否是MST节点
    // 索引队列：节点 -> 节点到MST的 权重最小的边的权重（PQ形式）
    private IndexMinPQ<Double> vertexToItsMinCrossEdgeWeightPQ; // 疑问：它的作用 和vertexToItsMinEdgesWeight是不是重复了？

    public PrimMSTSimple(EdgeWeightedGraph weightedGraph) {
        vertexToItsMinCrossEdgeToMST = new Edge[weightedGraph.getVertexAmount()];
        vertexToItsMinCrossEdgesWeight = new double[weightedGraph.getVertexAmount()];
        vertexToIsMSTVertex = new boolean[weightedGraph.getVertexAmount()];

        // 最开始时，初始化 所有节点 到MST的最小横切边的权重 都为无穷大
        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++) {
            vertexToItsMinCrossEdgesWeight[currentVertex] = Double.POSITIVE_INFINITY;
        }

        vertexToItsMinCrossEdgeWeightPQ = new IndexMinPQ<>(weightedGraph.getVertexAmount());
        // 初始化时，先向索引优先队列中 添加一个键值对 vertex：0，itsMinEdgeWeightToMST：0.0；
        vertexToItsMinCrossEdgesWeight[0] = 0.0;
        vertexToItsMinCrossEdgeWeightPQ.insert(0, 0.0); // 用 顶点0 -> 顶点到MST的最小边的权重为0 来 初始化pq

        // 如果索引优先队列 不为空，说明 队列中 仍旧存在有 横切边，因此 仍旧存在有图节点 没有被添加为 MST节点，则：
        while (!vertexToItsMinCrossEdgeWeightPQ.isEmpty()) {
            // 从优先队列中，删除并获取到 当前的最小元素的索引（距离MST 权重最小的横切边的 图节点）
            int graphVertexWithMinWeightToMST = vertexToItsMinCrossEdgeWeightPQ.delMin();
            // 把 该图节点 添加为 MST节点，并 更新其所有”邻居图节点“ 到MST的最小横切边
            addVertexInMSTAndUpdateItsNeighborsMinEdgeToMST(weightedGraph, graphVertexWithMinWeightToMST);
        }
    }

    private void addVertexInMSTAndUpdateItsNeighborsMinEdgeToMST(EdgeWeightedGraph weightedGraph, int currentVertex) {
        // ① 把 当前图节点 添加为 MST节点
        vertexToIsMSTVertex[currentVertex] = true;

        /* ② 更新 这个新的MST节点的 所有邻居图节点 到MST的最小边(及其权重)   作用：保证从indexPQ中取出的节点 是最小横切边的图节点 */
        // 对于 当前图节点 所关联的所有边...
        for (Edge currentAssociatedEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            // 获取 该关联边(邻接边)的“另一个端点”
            int theOtherVertex = currentAssociatedEdge.theOtherVertexAgainst(currentVertex);

            // 如果 另一个端点 是一个 MST节点，说明 该关联边 是一个无效的边，则：跳过 该关联边
            if (vertexToIsMSTVertex[theOtherVertex]) continue;

            /* 否则 另一个端点 会是一个 图节点，说明 该关联边 是一个横切边，则：尝试更新 该图节点 到MST的最小边的权重 */
            // 如果 该横切边的权重 比起 成员变量 当前所记录的 “该图节点 到MST的最小横切边的权重“ 更小，说明 找到了此图节点的一条 距离MST更小的横切边...
            /* 原理：
                ① 对于 MST节点(包括刚刚加入MST的节点) 的 每一个邻居图节点，成员变量都为它们维护了一个 它到MST的最小边的权重
                ② currentAssociatedEdge是一条 新的横切边，而成员变量中记录的是一个 旧的横切边 */
            if (currentAssociatedEdge.weight() < vertexToItsMinCrossEdgesWeight[theOtherVertex]) {
                // 则：更新 此“非树节点” 相关的成员变量
                updatePropertiesFor(theOtherVertex, currentAssociatedEdge);
            }
        }
    }

    // 更新 图节点 到MST的最小边及其权重
    private void updatePropertiesFor(int graphVertex, Edge newMinCrossEdge) {
        // #1 更新 数组中 该图节点所对应的 到MST的最小边 && 到MST的最小边的权重
        vertexToItsMinCrossEdgeToMST[graphVertex] = newMinCrossEdge;
        vertexToItsMinCrossEdgesWeight[graphVertex] = newMinCrossEdge.weight();

        // #2 更新 优先队列中 该图节点 -> 其到MST的最小边的权重 的元素
        if (vertexToItsMinCrossEdgeWeightPQ.contains(graphVertex)) // 结点在PQ中 已经存在，则：更新
            vertexToItsMinCrossEdgeWeightPQ.changeKey(graphVertex, vertexToItsMinCrossEdgesWeight[graphVertex]);
        else // 结点在PQ中 尚未存在，则：添加
            vertexToItsMinCrossEdgeWeightPQ.insert(graphVertex, vertexToItsMinCrossEdgesWeight[graphVertex]);
    }

    public Iterable<Edge> edges() {
        return null;
    }

    public double weight() {
        return 0.0;
    }
}
