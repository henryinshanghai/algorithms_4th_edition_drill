package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Kruskal;

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

// Kruskal算法步骤：对于按照权重排序的边序列中的当前边，如果边的两个顶点分属于不同的分量，则：将之添加到MST中
public class KruskalMSTSimple {

    private Queue<Edge> mst;

    public KruskalMSTSimple(EdgeWeightedGraph weightedGraph) {
        mst = new Queue<>();
        MinPQ<Edge> pq = new MinPQ();

        for (Edge currentEdge : weightedGraph.edges()) {
            pq.insert(currentEdge);
        }

        // V个相互独立的 结点/连通分量
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());

        while (!pq.isEmpty() && mst.size() < weightedGraph.getVertexAmount() - 1) {
            // 当前权重最小的边
            Edge e = pq.delMin();
            int v = e.eitherVertex(), w = e.theOtherVertexAgainst(v);

            if (forest.isConnectedBetween(v, w)) {
                continue;
            }

            // 如果边视为两个顶点 没有相互连通，则：连接它们
            forest.unionToSameComponent(v, w);

            // 把当前边添加到 MST中
            mst.enqueue(e);
        }
    }
}
