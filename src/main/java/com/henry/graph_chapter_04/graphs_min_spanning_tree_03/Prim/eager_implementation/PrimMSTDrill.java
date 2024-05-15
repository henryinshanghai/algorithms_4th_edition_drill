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

        initVertexToMinEdgeWeightArr(graphVertexAmount);

        for (int currentVertex = 0; currentVertex < graphVertexAmount; currentVertex++) {
            if (isNotMSTVertex(currentVertex)) {
                prim(weightedGraph, currentVertex);
            }
        }
    }

    private void initVertexToMinEdgeWeightArr(int graphVertexAmount) {
        for (int currentVertex = 0; currentVertex < graphVertexAmount; currentVertex++) {
            vertexToItsMinEdgeWeightPQ.insert(currentVertex, Double.POSITIVE_INFINITY);
        }
    }

    private void prim(EdgeWeightedGraph weightedGraph, int startVertex) {
        vertexToItsMinEdgeWeightPQ.insert(startVertex, 0.0);

        while (!vertexToItsMinEdgeWeightPQ.isEmpty()) {
            int vertexWithMinWeight = vertexToItsMinEdgeWeightPQ.delMin();

            markVertexAsMSTAndUpdateNeighborGraphVertex(weightedGraph, vertexWithMinWeight);
        }
    }

    private void markVertexAsMSTAndUpdateNeighborGraphVertex(EdgeWeightedGraph weightedGraph, int currentVertex) {
        vertexToIsMSTVertex[currentVertex] = true;

        // 获取到 当前结点的所有关联的边 - 这里需要使用图对象
        for (Edge currentAssociatedGraphEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            int neighborGraphVertex = currentAssociatedGraphEdge.theOtherVertexAgainst(currentVertex);

            if (isASmallerEdge(currentAssociatedGraphEdge, neighborGraphVertex)) {
                updateGraphVertexProperties(currentAssociatedGraphEdge, neighborGraphVertex);
            }
        }
    }

    private void updateGraphVertexProperties(Edge currentAssociatedGraphEdge, int neighborGraphVertex) {
        // Ⅰ 树结点 到MST的最小边
        vertexToItsMinEdgeToMST[neighborGraphVertex] = currentAssociatedGraphEdge;

        updatePQEntryFor(currentAssociatedGraphEdge, neighborGraphVertex);
    }

    private void updatePQEntryFor(Edge currentAssociatedGraphEdge, int neighborGraphVertex) {
        if (vertexToItsMinEdgeWeightPQ.contains(neighborGraphVertex)) {
            vertexToItsMinEdgeWeightPQ.decreaseKey(neighborGraphVertex, currentAssociatedGraphEdge.weight());
        } else {
            vertexToItsMinEdgeWeightPQ.insert(neighborGraphVertex, currentAssociatedGraphEdge.weight());
        }
    }

    private boolean isASmallerEdge(Edge currentAssociatedGraphEdge, int neighborGraphVertex) {
        return currentAssociatedGraphEdge.weight() < vertexToItsMinEdgeWeightPQ.keyOf(neighborGraphVertex);
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
