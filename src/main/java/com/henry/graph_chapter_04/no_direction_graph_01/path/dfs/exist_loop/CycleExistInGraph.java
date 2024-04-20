package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.exist_loop;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;

// 验证：可以使用在图中从指定起点开始进行DFS（递归地标记结点）的方式&结点被标记的特征 来 判断给定的图中 是否存在环结构
// 特征：“当前结点” 它的所有“被标记的邻居结点”中，存在有 不是“它的出发结点”的结点，说明存在环；
// 原理：按照DFS地方式递归地标记结点时，如果是一条单向的路径，则：当前结点已经被标记的邻居结点，只可能是路径中的上一个结点。否则的话，说明存在环
public class CycleExistInGraph {
    private boolean[] vertexToIsMarked;
    private boolean hasCycle;

    public CycleExistInGraph(Graph graph) {
        // 实例化成员变量
        vertexToIsMarked = new boolean[graph.vertexAmount()];

        // 对于图中的每一个节点，判断节点所在的连通分量中是否存在环
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            if (isNotMarked(currentVertex)) {
                // 由于当前结点是“起始结点”，所以 它不存在自己的“departVertex”，这里 书上把它设置为“当前结点自身”，
                // 而网站上 把它设置为“-1”（似乎更容易理解一些）
                int departVertex = -1; // currentVertex
                markVertexAndDecideExistCycleViaDFS(graph, currentVertex, departVertex);
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 作用：判断图G中是否存在环???
    // 手段：判断 terminalVertex的 已经访问过的邻居节点 中，是不是存在 非departVertex的结点，如果是，则：必然存在环
    private void markVertexAndDecideExistCycleViaDFS(Graph graph, int terminalVertex, int departVertex) {
        // 标记当前"结束结点"
        vertexToIsMarked[terminalVertex] = true;

        // 对于当前“结束结点”的每一个邻居结点
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(terminalVertex)) {
            if (isNotMarked(currentAdjacentVertex)) { // 如果邻居结点还没有被标记
                // 则 递归地对它进行标记
                markVertexAndDecideExistCycleViaDFS(graph, currentAdjacentVertex, terminalVertex);
                // 如果“邻居结点”已经被标记...
            } else if (currentAdjacentVertex != departVertex)
                // 并且 它 还不是 DFS路径当前边中的“出发节点”，说明当前连通分量中必然存在环，则👇
                hasCycle = true;
        }
    }

    // API
    // 判断图是不是二分图
    public boolean doesExistCycle() {
        // 直接返回成员变量 hasCycle
        return hasCycle;
    }
    // 这种返回成员变量的API会要求 客户端代码按照特定的顺序调用API，才能够得到正确的结果
}
