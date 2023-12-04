package com.henry.graph_chapter_04.direction_graph_02.path.dfs.graph_property;

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.if_accessible.DirectedDFS;

// 有向图中，顶点对的可达性
public class TransitiveClosure {
    private DirectedDFS[] vertexToItsAccessibleVertexes;

    TransitiveClosure(Digraph digraph) {
        vertexToItsAccessibleVertexes = new DirectedDFS[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            vertexToItsAccessibleVertexes[currentVertex] = new DirectedDFS(digraph, currentVertex);
        }
    }

    boolean reachable(int vertexV, int vertexW) {
        int startVertex = vertexV;
        DirectedDFS markedDigraph = vertexToItsAccessibleVertexes[startVertex];

        return markedDigraph.isAccessibleFromStartVertex(vertexW);
    }
}
