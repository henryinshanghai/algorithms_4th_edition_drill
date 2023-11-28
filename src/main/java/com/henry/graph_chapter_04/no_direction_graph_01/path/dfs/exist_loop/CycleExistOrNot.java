package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.exist_loop;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;

// DFS的应用：判断图中是否存在环结构
// 原理：DFS标记结点时，如果是一条单向的路径，则：当前结点已经被标记的邻居结点，只可能是路径中的上一个结点。否则的话，说明存在环
public class CycleExistOrNot {
    private boolean[] vertexToIsMarked;
    private boolean hasCycle;

    public CycleExistOrNot(Graph graph) {
        // 初始化成员变量
        vertexToIsMarked = new boolean[graph.V()];

        // 对于图中的每一个节点，判断节点所在的连通分量中是否存在环
        for (int currentVertex = 0; currentVertex < graph.V(); currentVertex++) {
            if (isNotMarked(currentVertex)) {
                dfs(graph, currentVertex, currentVertex);
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 作用：判断图G中是否存在环???
    // 手段：判断 terminalVertex的 已经访问过的邻居节点 中，是不是存在 非departVertex的结点，如果是，则：必然存在环
    private void dfs(Graph graph, int currentTerminalVertex, int currentDepartVertex) {
        // 标记当前节点
        vertexToIsMarked[currentTerminalVertex] = true;

        // 对于当前节点的每一个相邻节点
        for (int currentAdjacentVertex : graph.adj(currentTerminalVertex)) {
            if (isNotMarked(currentAdjacentVertex)) { // 对于未被标记的邻居结点...
                dfs(graph, currentAdjacentVertex, currentTerminalVertex);
            // 对于已经被标记的邻居结点...
            } else if (currentAdjacentVertex != currentDepartVertex)
                // 如果 terminalVertex的“已经被标记的邻居结点” 不是 它的“departVertex”，则：必然存在环
                hasCycle = true;
        }
    }
}
