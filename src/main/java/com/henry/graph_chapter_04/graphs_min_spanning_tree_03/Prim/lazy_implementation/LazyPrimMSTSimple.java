package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.lazy_implementation;

import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

// 结论：延迟Prim的算法步骤：
// #1 把当前节点添加为父结点； #2 把当前节点的所有横切边添加到优先队列中；
// #3 循环队列，取最小的横切边 添加到MST中；对非树节点，重复 #1，#2
public class LazyPrimMSTSimple {
    private boolean[] vertexToIsTreeVertex; // 最小生成树中的顶点
    private Queue<Edge> edgesQueueInMST; // 最小生成树中的边
    private MinPQ<Edge> crossEdgesPQ; // 横切边（包含有失效的边）

    public LazyPrimMSTSimple(EdgeWeightedGraph weightedGraph) {
        crossEdgesPQ = new MinPQ<>();
        vertexToIsTreeVertex = new boolean[weightedGraph.getVertexAmount()];
        edgesQueueInMST = new Queue<>();

        addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, 0); // 假设G是连通的

        while (!crossEdgesPQ.isEmpty()) {
            Edge minCrossEdge = crossEdgesPQ.delMin();

            if(isMSTEdge(minCrossEdge)) continue;

            edgesQueueInMST.enqueue(minCrossEdge);

            addNonMSTVertexToMST(weightedGraph, minCrossEdge);
        }

    }

    private void addNonMSTVertexToMST(EdgeWeightedGraph weightedGraph, Edge minCrossEdge) {
        int oneVertex = minCrossEdge.eitherVertex(),
            theOtherVertex = minCrossEdge.theOtherVertexAgainst(oneVertex);

        if(isNotMSTVertex(oneVertex)) addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, oneVertex);
        if(isNotMSTVertex(theOtherVertex)) addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, theOtherVertex);
    }

    private boolean isNotMSTVertex(int oneVertex) {
        return !vertexToIsTreeVertex[oneVertex];
    }

    private boolean isMSTEdge(Edge minCrossEdge) {
        int oneVertex = minCrossEdge.eitherVertex(),
            theOtherVertex = minCrossEdge.theOtherVertexAgainst(oneVertex);

        return vertexToIsTreeVertex[oneVertex] && vertexToIsTreeVertex[theOtherVertex];
    }

    private void addVertexInMSTAndAddItsCrossEdgesInPQ(EdgeWeightedGraph weightedGraph, int currentVertex) {
        vertexToIsTreeVertex[currentVertex] = true;

        for (Edge e : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            if(!vertexToIsTreeVertex[e.theOtherVertexAgainst(currentVertex)]) crossEdgesPQ.insert(e);
        }
    }

    public Iterable<Edge> edges() {
        return edgesQueueInMST;
    }
}
