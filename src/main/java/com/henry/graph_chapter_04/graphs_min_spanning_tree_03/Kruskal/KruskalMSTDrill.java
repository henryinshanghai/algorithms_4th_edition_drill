package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Kruskal;

import com.henry.basic_chapter_01.specific_application.implementation.primary.QuickFind;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

// 步骤：#1 对边按照权重排序； #2 准备一个结点森林（用于连接结点得到MST）； #3 如果是当前边是横切边，则：把它添加到MST（队列）中
public class KruskalMSTDrill {
    private Queue<Edge> edgesInMST;

    public KruskalMSTDrill(EdgeWeightedGraph weightedGraph) {
        // #0
        Edge[] edgeArr = initEdgeArrWith(weightedGraph);

        // #1
        Arrays.sort(edgeArr);

        // #2
        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (int currentEdgeCursor = 0; edgeAmountConditions(currentEdgeCursor, weightedGraph); currentEdgeCursor++) {
            // ①
            Edge currentEdge = edgeArr[currentEdgeCursor];
            constructMST(forest, currentEdge);
        }
    }

    private void constructMST(QuickFind forest, Edge currentEdge) {
        // ②
        int oneVertex = currentEdge.eitherVertex();
        int theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);
        // ③ 如果边的两个顶点在不同的连通分量中，说明这条边是一个横切边，则：
        if (isNotSameComponent(forest, oneVertex, theOtherVertex)) {
            // Ⅰ 连接两个不同的连通分量
            forest.unionToSameComponent(oneVertex, theOtherVertex);
            // Ⅱ 把这个最小横切边，添加到 MST边的队列中
            edgesInMST.enqueue(currentEdge);
        }
    }

    private Edge[] initEdgeArrWith(EdgeWeightedGraph weightedGraph) {
        int vertexAmount = weightedGraph.getVertexAmount();

        Edge[] edgeArr = new Edge[vertexAmount];
        int currentSpot = 0;
        for (Edge currentEdge : weightedGraph.edges()) {
            edgeArr[currentSpot++] = currentEdge;
        }
        return edgeArr;
    }

    private boolean edgeAmountConditions(int currentEdgeCursor, EdgeWeightedGraph weightedGraph) {
        int graphEdgeAmount = weightedGraph.getEdgeAmount();
        int graphVertexAmount = weightedGraph.getVertexAmount();

        // 当前边的上限：图中边的总数量   &&   最小生成树中边数量的上限：原始图中的结点数量-1
        return currentEdgeCursor < graphEdgeAmount && edgesInMST.size() < graphVertexAmount - 1;
    }

    private boolean isNotSameComponent(QuickFind forest, int oneVertex, int theOtherVertex) {
        return forest.findGroupIdOf(oneVertex) != forest.findGroupIdOf(theOtherVertex);
    }
}
