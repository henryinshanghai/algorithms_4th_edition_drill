package com.henry.graph_chapter_04.no_direction_graph_01.dfs;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstSearch_drill02 {
    // 当前节点与起点s是否连通
    private boolean[] marked;
    // 图中被标记的节点数量 - 标记 是搜索算法的基本实现手段
    private int count;

    public DepthFirstSearch_drill02(Graph G, int s) {
        marked = new boolean[G.V()];

        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;

        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // APIs
    // 1 指定顶点v是否 从起点s可达
    private boolean marked(int v) {
        return marked[v];
    }

    // 2 图中 与起点s相连接的节点总数量
    private int count() {
        return count;
    }

    // 使用DFS的APIs 来 获取图的性质
    public static void main(String[] args) {
        // 获取输入流 创建图
        Graph G = new Graph(new In(args[0]));

        // 获取起始节点
        int s = Integer.parseInt(args[1]);

        // 创建DFS对象
        DepthFirstSearch_drill02 dfs = new DepthFirstSearch_drill02(G, s);

        // 使用DFS对象的APIs 来 获取图的性质
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) {
                StdOut.print(v + " ");
            }
        }
        StdOut.println();

        if (dfs.count() != G.V()) {
            StdOut.print("NOT ");
        }
        StdOut.print("connected");

    }
}
