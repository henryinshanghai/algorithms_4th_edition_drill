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
        int vertexAmount = weightedGraph.getVertexAmount();

        Edge[] edgeArr = new Edge[vertexAmount];
        int currentSpot = 0;
        for (Edge currentEdge : weightedGraph.edges()) {
            edgeArr[currentSpot++] = currentEdge;
        }

        // #1
        Arrays.sort(edgeArr);

        QuickFind forest = new QuickFind(weightedGraph.getVertexAmount());
        for (int currentEdgeCursor = 0; edgeAmountConditions(currentEdgeCursor, weightedGraph); currentEdgeCursor++) {
            Edge currentEdge = edgeArr[currentEdgeCursor];

            int oneVertex = currentEdge.eitherVertex();
            int theOtherVertex = currentEdge.theOtherVertexAgainst(oneVertex);

            if (isNotSameComponent(forest, oneVertex, theOtherVertex)) {
                forest.unionToSameComponent(oneVertex, theOtherVertex);
                edgesInMST.enqueue(currentEdge);
            }
        }
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
