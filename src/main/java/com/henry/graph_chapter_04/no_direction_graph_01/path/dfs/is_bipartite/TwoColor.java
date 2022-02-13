package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.is_bipartite;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;

public class TwoColor {
    private boolean[] marked;
    private boolean[] color;
    private boolean isTwoColorable = true; // 图是否能够通过两种颜色染色成为二分图？

    public TwoColor(Graph G) {
        // 初始化 成员变量
        marked = new boolean[G.V()];
        color = new boolean[G.V()];
        // 对于 图中的每一个节点...
        for (int s = 0; s < G.V(); s++) {
            // 如果节点还没有被标记过
            if (!marked[s]) {
                // 执行dfs 以/来 判断 G是不是一个二分图？
                dfs(G, s);
            }
        }
    }

    /*
        1 明确方法的作用与返回值；
        2 找到递归的终结条件——这个条件会停止递归，并开始弹出调用栈；
        3 确定本级递归需要完成的工作；（这时候要假设已经有了功能可用的API给自己调用）
     */
    // 方法作用：在图G中，标记节点v 并 处理与v连通的所有其他节点
    private void dfs(Graph G, int v) {
        // 标记节点v
        marked[v] = true;

        // 对于节点v的每一个相邻节点
        for (int w : G.adj(v)) {
            // 如果节点还没有被标记过
            if (!marked[w]) {
                // 使用 当前节点v的颜色 来 为 未被标记的节点上色
                color[w] = !color[v];
                // 对节点w 做同样的操作 - 直到图中的所有连通的节点都被标记完成
                dfs(G, w);
            } else if (color[w] == color[v]) { // 如果节点w已经被标记过 & 节点w的颜色 与父节点v的颜色一样
                // 更改成员变量 - 表示 当前图G不能够 只使用两种颜色染成二分图
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
