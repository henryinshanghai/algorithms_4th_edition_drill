package com.henry.graph_chapter_04.direction_graph_02.path.dfs.graph_property.if_two_vertex_accessible;

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.if_accessible_from_startVertex.DirectedDFS;

// 结论：对于有向图中顶点之间的连通性问题，可以使用方形的结点矩阵 来 为每一个结点，建立其可以达到的结点列表；
// 手段：使用DFS算法，就能够 为指定的结点，搜索&收集到其 可以达到的所有顶点；
// 用法：把顶点对中的其中一个顶点作为startVertex，另外一个顶点作为 passedVertex，就能够 使用DFS对象的API 来 判断两个结点是否连通
public class TransitiveClosure {
    private DirectedDFS[] vertexToItsAccessibleVertexes;

    TransitiveClosure(Digraph digraph) {
        vertexToItsAccessibleVertexes = new DirectedDFS[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            vertexToItsAccessibleVertexes[currentVertex] = new DirectedDFS(digraph, currentVertex);
        }
    }

    // 原始问题：判断图中的 v->w是否存在
    public boolean reachable(int vertexV, int vertexW) {
        int startVertex = vertexV;
        DirectedDFS markedDigraph = vertexToItsAccessibleVertexes[startVertex];

        return markedDigraph.isAccessibleFromStartVertex(vertexW);
    }
}
