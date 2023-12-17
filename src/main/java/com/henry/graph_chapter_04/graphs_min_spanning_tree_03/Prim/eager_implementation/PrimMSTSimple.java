package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.eager_implementation;

import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.IndexMinPQ;

// Prim算法即时实现步骤：
// #1 把当前结点，标记为“MST结点”； #2 遍历结点所关联的所有边，更新结点的“最小边”、“最小边的权重” - 直到索引优先队列为空
public class PrimMSTSimple {
    private Edge[] vertexToItsMinEdgeToMST;
    private double[] vertexToItsMinEdgeWeight;
    private boolean[] vertexToIsMSTVertex;
    private IndexMinPQ<Double> vertexToItsMinEdgeWeightPQ;

    public PrimMSTSimple(EdgeWeightedGraph weightedGraph) {
        vertexToItsMinEdgeToMST = new Edge[weightedGraph.getVertexAmount()];
        vertexToItsMinEdgeWeight = new double[weightedGraph.getVertexAmount()];
        vertexToIsMSTVertex = new boolean[weightedGraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < weightedGraph.getVertexAmount(); currentVertex++) {
            vertexToItsMinEdgeWeight[currentVertex] = Double.POSITIVE_INFINITY;
        }

        vertexToItsMinEdgeWeightPQ = new IndexMinPQ<>(weightedGraph.getVertexAmount());

        vertexToItsMinEdgeWeight[0] = 0.0;
        vertexToItsMinEdgeWeightPQ.insert(0, 0.0); // 用顶点0 和 权重0 来 初始化pq

        while (!vertexToItsMinEdgeWeightPQ.isEmpty()) {
            addVertexInMSTAndUpdateItsMinEdgeToMST(weightedGraph, vertexToItsMinEdgeWeightPQ.delMin()); // 把 最近的顶点 添加到树中
        }
    }

    private void addVertexInMSTAndUpdateItsMinEdgeToMST(EdgeWeightedGraph weightedGraph, int currentVertex) {
        // 把 顶点v添加到树中，更新数据
        vertexToIsMSTVertex[currentVertex] = true;

        // 更新 结点的最小边、最小边的权重 - 手段：从所有关联的边中，找到权重最小的边，并用它更新数组、队列
        for (Edge currentAssociatedEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            // 获取边的“非树节点”
            int theOtherVertex = currentAssociatedEdge.theOtherVertexAgainst(currentVertex);

            if (vertexToIsMSTVertex[theOtherVertex]) continue;

            // 如果 找到了“权重值更小的边”...
            if (currentAssociatedEdge.weight() < vertexToItsMinEdgeWeight[theOtherVertex]) {
                // #1 更新数组元素
                vertexToItsMinEdgeToMST[theOtherVertex] = currentAssociatedEdge;
                vertexToItsMinEdgeWeight[theOtherVertex] = currentAssociatedEdge.weight();

                // #2 更新优先队列中的元素
                if (vertexToItsMinEdgeWeightPQ.contains(theOtherVertex)) vertexToItsMinEdgeWeightPQ.change(theOtherVertex, vertexToItsMinEdgeWeight[theOtherVertex]);
                else vertexToItsMinEdgeWeightPQ.insert(theOtherVertex, vertexToItsMinEdgeWeight[theOtherVertex]);
            }
        }
    }

    public Iterable<Edge> edges() {
        return null;
    }

    public double weight() {
        return 0.0;
    }
}
