package com.henry.graph_chapter_04.no_direction_graph_01.graph;

import com.henry.graph_chapter_04.no_direction_graph_01.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Graph_simple_drill01 {
    // line.separator的属性似乎是内置的
    private static final String NEWLINE = System.getProperty("line.separator");

    private int V;
    private int E;
    private Bag<Integer>[] adj;

    public Graph_simple_drill01(int V) {
        this.V = V;
        this.E = 0;

        // 初始化邻接表数组
        // 初始化数组大小
        adj = (Bag<Integer>[]) new Bag[V];
        // 初始化数组元素item
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<>();
        }
    }

    public Graph_simple_drill01(In in) {
        this(in.readInt());
        // 这里使用局部变量 局部变量 VS. 成员变量
        int E = in.readInt();

        // 遍历边的数量次 - 用来创建这么多边
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();

            addEdge(v, w);
        }
    }

    private void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    // APIs
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public int degree(int v) {
        return adj[v].size();
    }

    // toString
    public String toString() {
        StringBuilder s = new StringBuilder();
        // 1 打印图的基本性质
        s.append(V + "vertices, " + E + " edges " + NEWLINE); // NEWLINE没用到呀

        // 遍历图的每一个顶点
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            // 遍历当前顶点的每一个相邻节点
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph_simple_drill01 g = new Graph_simple_drill01(in);
        StdOut.println(g.toString());
    }
}
