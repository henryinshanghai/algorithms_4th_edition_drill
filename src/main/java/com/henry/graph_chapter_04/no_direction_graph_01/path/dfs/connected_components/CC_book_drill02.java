package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.connected_components;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CC_book_drill02 {
    private boolean[] marked;
    private int[] ids;
    private int count;

    public CC_book_drill02(Graph G) {
        marked = new boolean[G.V()];
        ids = new int[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        ids[v] = count;

        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // APIs
    public boolean connected(int v, int w) {
        return ids[v] == ids[w];
    }

    public int count() {
        return count;
    }

    public int groupNumOf(int v) {
        return ids[v];
    }


    public static void main(String[] args) {
        Graph graph = new Graph(new In(args[0]));
        CC_book_drill02 cc = new CC_book_drill02(graph);

        int M = cc.count();
        StdOut.println("there are " + M + " sub-graph in G.");

        // 打印每一个子图中的节点
        // 使用Bag对象来存储子图
        Bag<Integer>[] components;
        components = new Bag[M];

        for (int i = 0; i < M; i++) {
            components[i] = new Bag<>();
        }

        // 填充每一个子图
        for (int v = 0; v < graph.V(); v++) {
            components[cc.groupNumOf(v)].add(v);
        }

        // 打印每一个子图中的节点
        for (int i = 0; i < M; i++) {
            for (int w : components[i]) {
                StdOut.print(w + " ");
            }
            StdOut.println();
        }


    }
}
