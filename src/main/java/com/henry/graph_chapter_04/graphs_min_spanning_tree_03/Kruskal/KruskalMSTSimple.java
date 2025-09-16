package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Kruskal;

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

// Kruskal算法步骤：对于 按照权重排序的边序列 中的当前边，如果 边的两个顶点 分属于 不同的分量，则：将之 添加到MST中
public class KruskalMSTSimple {

    private Queue<Edge> MSTEdgesQueue; // 由MST的边所构成的队列

    public KruskalMSTSimple(EdgeWeightedGraph weightedGraph) {
        MSTEdgesQueue = new Queue<>();
        // 把 加权图所有的边 都添加到 一个优先队列中   作用：用于方便地获取到 权重最小的边
        MinPQ<Edge> edgeMinPQ = insertEdgesIntoPQ(weightedGraph);

        // 创建一个树林，其中包含有 V个 相互独立的 树/结点/连通分量  作用：用于判断 边的两个端点 是否在同一个连通分量中
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());

        while (edgeAmountWithinLegitRange(weightedGraph, edgeMinPQ)) {
            // #1 获取到 优先队列中 当前权重最小的边，并得到 其两个端点
            Edge currentMinEdge = edgeMinPQ.delMin();
            int oneVertex = currentMinEdge.eitherVertex(),
                theOtherVertex = currentMinEdge.theOtherVertexAgainst(oneVertex);

            // #2 如果 边的两个端点 在同一个分量中，说明 添加这个边会为MST引入环。则：
            if (forest.isConnectedBetween(oneVertex, theOtherVertex)) {
                // 跳过它，处理下一条边
                continue;
            }

            // #3 如果 边的两个顶点 在不同的分量中，说明 这个边是一个 有效的横切边，则：
            // ① 把 两个顶点 各自所在的分量 连接起来    作用：作为 “MST节点”分组
            forest.unionToSameComponent(oneVertex, theOtherVertex);
            // ② 把 当前边(因为它是最小横切边，必然属于MST) 添加到 MST中
            MSTEdgesQueue.enqueue(currentMinEdge);
        }
    }

    private MinPQ<Edge> insertEdgesIntoPQ(EdgeWeightedGraph weightedGraph) {
        MinPQ<Edge> edgeMinPQ = new MinPQ();

        for (Edge currentGraphEdge : weightedGraph.edges()) {
            edgeMinPQ.insert(currentGraphEdge);
        }

        return edgeMinPQ;
    }

    // 为什么这里需要两个条件呢?
    private boolean edgeAmountWithinLegitRange(EdgeWeightedGraph weightedGraph, MinPQ<Edge> edgeMinPQ) {
        return !edgeMinPQ.isEmpty() && // 优先队列不为空，并且
                MSTEdgesQueue.size() < weightedGraph.getVertexAmount() - 1; // MST边队列中边的数量 < 图节点的数量
    }
}
