package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.is_graph_a_bipartite.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;

// 验证：可以使用 在图中 从指定起点开始 进行DFS（递归地标记结点）的方式 来 判断 给定的一幅图 是不是二分图(bipartite)
// aka 是否能够 仅使用两种颜色 对图中的结点 进行着色，使得 图中任意一条边的 两个端点的颜色 都不相同？
public class DoesGraphDyeWith2Color {
    private boolean[] vertexToIsMarked;
    private boolean[] vertexToItsColor;
    private boolean isTwoColorable = true; // 图 是否能够 通过两种颜色 染色成为 二分图？

    public DoesGraphDyeWith2Color(Graph passedGraph) {
        // 初始化 成员变量
        vertexToIsMarked = new boolean[passedGraph.vertexAmount()];
        vertexToItsColor = new boolean[passedGraph.vertexAmount()];
        // 对于 图中的每一个节点...
        for (int currentVertex = 0; currentVertex < passedGraph.vertexAmount(); currentVertex++) {
            // 如果节点 还没有 被标记过
            if (isNotMarked(currentVertex)) {
                // 执行dfs 以/来 判断 G是不是一个二分图？
                markVertexAndDyeItsAdjacentToSeeIfBipartiteViaDFS(passedGraph, currentVertex);
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    /*
        1 明确 方法的作用 与 返回值；
        2 找到 递归的终结条件 —— 这个条件 会停止递归，并开始 弹出调用栈；
        3 确定 本级递归 需要完成的工作；（这时候 要假设 已经有了 功能可用的API 给自己调用）
     */
    // 方法作用：在图G中，标记节点v 并 处理 “与v连通的”所有 其他节点
    private void markVertexAndDyeItsAdjacentToSeeIfBipartiteViaDFS(Graph graph, int currentVertex) {
        // 标记节点 currentVertex
        vertexToIsMarked[currentVertex] = true;

        // 对于 节点 currentVertex 的 每一个相邻节点
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 如果 “该邻居节点” 还没有被标记过，说明???，
            if (isNotMarked(currentAdjacentVertex)) {
                // 则：① 为邻居节点 涂上 ”与当前节点不同的颜色“
                vertexToItsColor[currentAdjacentVertex] = !vertexToItsColor[currentVertex];
                // ② 继续标记 currentAdjacentVertex 的 所有邻居结点 - 直到 图中的所有连通的节点 都被标记完成
                markVertexAndDyeItsAdjacentToSeeIfBipartiteViaDFS(graph, currentAdjacentVertex);
            } else if (vertexToItsColor[currentAdjacentVertex] == vertexToItsColor[currentVertex]) {
                // 如果 “邻居节点” 已经被标记过...
                // 并且 “邻居结点” 与 其“当前结点”的颜色 相同，说明 上述“DFS染色算法” 会导致 二分图的breach，
                // 则：图本身不是二分图
                isTwoColorable = false;
            }
        }
    }

    // API
    // 判断图是不是二分图
    public boolean isBipartite() {
        // 直接返回成员变量 isTwoColorable
        return isTwoColorable;
    }
}
