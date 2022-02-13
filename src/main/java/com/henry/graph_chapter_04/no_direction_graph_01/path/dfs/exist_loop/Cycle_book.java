package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.exist_loop;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.Cycle;

public class Cycle_book {
    private boolean[] marked;
    private boolean hasCycle;

    public Cycle_book(Graph G) {
        // 初始化成员变量
        marked = new boolean[G.V()];

        // 对于图中的每一个节点，判断节点所在的连通分量中是否存在环
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v, v);
            }
        }
    }

    // 作用：判断图G中是否存在环???
    // 手段：判断当前节点的相邻节点w中 已经访问过的节点 是不是当前节点v本身，如果不是 说明存在环
    private void dfs(Graph G, int v, int u) {
        // 标记当前节点
        marked[v] = true;

        // 对于当前节点的每一个相邻节点
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                // 当前节点为w   当前节点的父节点为v  为什么参数是 w v?
                dfs(G, w, v);
            } else if (w != u) hasCycle = true;
        }
    }
}
