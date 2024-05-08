package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Kruskal;

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

// Kruskal算法步骤：对于按照权重排序的边序列中的当前边，如果边的两个顶点分属于不同的分量，则：将之添加到MST中
public class KruskalMSTSimple {

    private Queue<Edge> MSTEdgesQueue;

    public KruskalMSTSimple(EdgeWeightedGraph weightedGraph) {
        MSTEdgesQueue = new Queue<>();
        MinPQ<Edge> edgeMinPQ = new MinPQ();

        for (Edge currentGraphEdge : weightedGraph.edges()) {
            edgeMinPQ.insert(currentGraphEdge);
        }

        // 创建一个树林，其中包含有V个相互独立的 树/结点/连通分量
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());

        while (edgeAmountWithinLegitRange(weightedGraph, edgeMinPQ)) {
            // #1 获取到 优先队列中当前权重最小的边
            Edge currentMinEdge = edgeMinPQ.delMin();
            int oneVertex = currentMinEdge.eitherVertex(),
                theOtherVertex = currentMinEdge.theOtherVertexAgainst(oneVertex);

            // #2 如果边的两个顶点在同一个分量中，说明这个边不是一个横切边。则：跳过它，处理下一条边
            if (forest.isConnectedBetween(oneVertex, theOtherVertex)) {
                continue;
            }

            // #3 如果边的两个顶点 在不同的分量中，说明这个边是一个横切边，则：
            // ① 把两个顶点所在的分量连接起来
            forest.unionToSameComponent(oneVertex, theOtherVertex);
            // ② 把当前边(因为它是最小横切边)添加到 MST中
            MSTEdgesQueue.enqueue(currentMinEdge);
        }
    }

    private boolean edgeAmountWithinLegitRange(EdgeWeightedGraph weightedGraph, MinPQ<Edge> edgeMinPQ) {
        return !edgeMinPQ.isEmpty() && MSTEdgesQueue.size() < weightedGraph.getVertexAmount() - 1;
    }
}
