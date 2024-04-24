package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex.extend.if_two_vertex_access_each_other;

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex.AccessibleVertexesInDigraph;

// 验证：可以使用/通过 为每个图中的每个结点都建立一个 与其连通的结点集合的方式 来 回答“图中指定的两个顶点是否相连通”的问题；
// 结论：对于有向图中顶点之间的连通性问题，可以使用方形的结点矩阵 来 为每一个结点，建立其可以达到的结点列表；
// 手段：使用DFS算法，就能够 为指定的结点，搜索&收集到其 可以达到的所有顶点；
// 用法：把顶点对中的其中一个顶点作为startVertex，另外一个顶点作为 passedVertex，就能够 使用DFS对象的API 来 判断两个结点是否连通
public class TransitiveClosure {
    private AccessibleVertexesInDigraph[] vertexToItsAccessibleVertexes;

    public TransitiveClosure(Digraph digraph) {
        // 准备一个 vertexesAmount大小的数组
        vertexToItsAccessibleVertexes = new AccessibleVertexesInDigraph[digraph.getVertexAmount()];

        // 对于有向图中的每一个顶点
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 为它创建一个 accessibleVertexesInDigraph对象，并建立 顶点->其accessibleVertexesInDigraph对象的映射关系
            vertexToItsAccessibleVertexes[currentVertex] = new AccessibleVertexesInDigraph(digraph, currentVertex);
        }
    }

    // 原始问题：判断有向图中的 v->w之间是否连通 🐖 这是一个yes/no的问题，不需要给出具体的连通路径
    public boolean reachable(int vertexV, int vertexW) {
        // 对于顶点v，获取到有向图中 由其可达的所有顶点集合
        int startVertex = vertexV;
        AccessibleVertexesInDigraph markedDigraph = vertexToItsAccessibleVertexes[startVertex];
        // 判断顶点w，是否由v可达
        return markedDigraph.isAccessibleFromStartVertex(vertexW);
    }
}
