package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.eager_implementation;

import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Queue;

// 原理：对于MST来说，原始图中的每一个结点，都会有它的“距离MST的最小边”。
// 步骤：#1 把某一个结点的“距离MST的最小边”设置为0 & 把vertex -> itsMinEdge添加到优先队列中；#2 从队列中获取“最小的”结点；
// #3 把结点更新为“树结点” & 更新“theOtherVertex结点“ 它”距离MST的最小边”。
public class PrimMSTDrill {

    private Edge[] vertexToItsMinEdgeToMST;
    private IndexMinPQ<Double> vertexToItsMinEdgeWeightPQ;

    private boolean[] vertexToIsMSTVertex;

    public PrimMSTDrill(EdgeWeightedGraph weightedGraph) {
        int graphVertexAmount = weightedGraph.getVertexAmount();

        for (int currentVertex = 0; currentVertex < graphVertexAmount; currentVertex++) {
            vertexToItsMinEdgeWeightPQ.insert(currentVertex, Double.POSITIVE_INFINITY);
        }

        for (int currentVertex = 0; currentVertex < graphVertexAmount; currentVertex++) {
            if (isNotMSTVertex(currentVertex)) {
                prim(weightedGraph, currentVertex);
            }
        }
    }

    private void prim(EdgeWeightedGraph weightedGraph, int currentVertex) {
        vertexToItsMinEdgeWeightPQ.insert(currentVertex, 0.0);

        while (!vertexToItsMinEdgeWeightPQ.isEmpty()) {
            int vertexWithMinWeight = vertexToItsMinEdgeWeightPQ.delMin();

            addVertexInMSTAndUpdateTheOtherVertex(weightedGraph, vertexWithMinWeight);
        }
    }

    private void addVertexInMSTAndUpdateTheOtherVertex(EdgeWeightedGraph weightedGraph, int currentVertex) {
        vertexToIsMSTVertex[currentVertex] = true;

        // 获取到 当前结点的所有关联的边 - 这里需要使用图对象
        for (Edge currentAssociatedEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            int theOtherVertex = currentAssociatedEdge.theOtherVertexAgainst(currentVertex);

            if (currentAssociatedEdge.weight() < vertexToItsMinEdgeWeightPQ.keyOf(theOtherVertex)) {
                vertexToItsMinEdgeToMST[theOtherVertex] = currentAssociatedEdge;

                if (vertexToItsMinEdgeWeightPQ.contains(theOtherVertex)) {
                    vertexToItsMinEdgeWeightPQ.decreaseKey(theOtherVertex, currentAssociatedEdge.weight());
                } else {
                    vertexToItsMinEdgeWeightPQ.insert(theOtherVertex, currentAssociatedEdge.weight());
                }
            }
        }
    }

    private boolean isNotMSTVertex(int currentVertex) {
        return !vertexToIsMSTVertex[currentVertex];
    }

    public Iterable<Edge> edgesOfMST() {
        Queue<Edge> edges = new Queue<>();
        for (int currentVertex = 0; currentVertex < vertexToItsMinEdgeToMST.length; currentVertex++) {
            if (vertexToItsMinEdgeToMST[currentVertex] != null) {
                edges.enqueue(vertexToItsMinEdgeToMST[currentVertex]);
            }
        }

        return edges;
    }
}
