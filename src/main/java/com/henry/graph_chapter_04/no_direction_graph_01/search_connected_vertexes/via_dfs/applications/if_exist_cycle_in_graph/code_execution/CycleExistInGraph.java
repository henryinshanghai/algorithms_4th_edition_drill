package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.if_exist_cycle_in_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;

// 验证：可以使用 ① 在图中从指定起点开始进行DFS（递归地标记结点）的方式 & ② 结点被标记的特征 来 判断 给定的图中 是否存在 环结构
// 特征：“当前结点” 它的所有“被标记的邻居结点”中，存在有 不是“它的出发结点”的结点，说明存在环；
// 原理：当 按照DFS的方式 递归地标记 顶点 时，
// ① 如果 是一条 单向的路径，则：当前顶点的 ”已经被标记的“邻居顶点，只可能会是 路径中的”上一个结点“。
// ② 否则的话，说明 路径中 存在有环
public class CycleExistInGraph {
    // #1 顶点 -> 顶点是否被标记过 的映射
    private boolean[] vertexToIsMarked;
    // #2 用于表示 是否有环
    private boolean hasCycle;

    public CycleExistInGraph(Graph graph) {
        // 实例化 成员变量
        vertexToIsMarked = new boolean[graph.vertexAmount()];

        // 对于 图中的每一个顶点，判断 顶点所在的连通分量中 是否存在环
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            if (isNotMarked(currentVertex)) {
                // 由于 当前顶点 是“起始顶点”，所以它 不存在 其自己的“departVertex”
                // 书与网站的不同：书上 把它设置为 “当前结点自身”，而 网站上 把它设置为“-1”（似乎更容易理解一些）；
                int dummyDepartVertex = -1; // currentVertex
                markVertexAndDecideExistCycleViaDFS(graph, currentVertex, dummyDepartVertex);
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 作用：判断 图G中 是否存在环???
    // 手段：判断 terminalVertex的 所有”已经访问过的“邻居节点 中，是不是存在有 ”非departVertex“的结点。如果是，则：必然 存在环
    private void markVertexAndDecideExistCycleViaDFS(Graph graph, int terminalVertex, int departVertex) {
        // 标记 当前"结束顶点"
        vertexToIsMarked[terminalVertex] = true;

        // 对于 当前“结束顶点”的每一个邻居顶点
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(terminalVertex)) {
            // 如果 该邻居顶点 还没有被标记，说明 它是路径中尚未被访问到的顶点...
            if (isNotMarked(currentAdjacentVertex)) {
                // 则 继续递归地对 当前邻居顶点 进行标记
                markVertexAndDecideExistCycleViaDFS(graph, currentAdjacentVertex, terminalVertex);
            } else if (currentAdjacentVertex != departVertex) // 如果 “该邻居结点” 已经被标记
                // 并且 它 还不是 DFS路径中”当前边“的“出发节点”，说明 当前连通分量中 必然存在环，则👇
                // 把 ”用于表示是否存在环“的 布尔变量 标记为true
                hasCycle = true;
        }
    }

    // API
    // 判断图 是不是 二分图
    public boolean doesExistCycle() {
        // 直接 返回成员变量hasCycle的值
        // 🐖 前提是 这个成员变量的值 已经是正确的
        return hasCycle;
    }
    // 👆 这种 返回成员变量的API 会要求 客户端代码 按照 特定的顺序 调用API，才能够 得到正确的结果
}
