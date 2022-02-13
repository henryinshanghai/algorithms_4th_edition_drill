package com.henry.graph_chapter_04.no_direction_graph_01.dfs;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstSearch_drill03 {
    private boolean[] marked;
    private int count;

    public DepthFirstSearch_drill03(Graph G, int s) {
        // 初始化的方式 - 指定数组的尺寸大小 每个item的值都会被初始化为false
//        for (int i = 0; i < G.V(); i++) {
//            marked[i] = false;
//        }
        marked = new boolean[G.V()];

        dfs(G, s);
    }

    // 为什么这种方式能够 遍历所有与起点相连通的顶点呢？
    // 私有方法 尽量直接使用成员变量，而不要使用其他的API - 这会造成不清晰
    private void dfs(Graph G, int s) {
        marked[s] = true;
        count++;

        for (int w : G.adj(s)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // APIs
    // 节点v 与起点s是否连通
    public boolean connected(int v) {
        return marked[v];
    }

    // 与起点s所连通的所有节点的数量
    public int connectedAmount() {
        return count;
    }

    // 使用DFS的APIs 来 获取到图的一些个性质
    public static void main(String[] args) {
        Graph graph = new Graph(new In(args[0]));

//        System.out.println(args[0]);
        int s = Integer.parseInt(args[1]);

        DepthFirstSearch_drill03 dfs = new DepthFirstSearch_drill03(graph, s);

        for (int v = 0; v < graph.V(); v++) {
            if (dfs.connected(v)) {
                StdOut.print(v + " ");
            }
        }
        StdOut.println();

        // 图的性质 VS. dfs的性质
        if (graph.V() != dfs.connectedAmount()) {
            StdOut.print("NOT ");
        }
        StdOut.print("CONNECTED.");
    }

}
