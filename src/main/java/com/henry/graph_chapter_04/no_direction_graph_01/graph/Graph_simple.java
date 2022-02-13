package com.henry.graph_chapter_04.no_direction_graph_01.graph;

import com.henry.graph_chapter_04.no_direction_graph_01.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Graph_simple {
    private static final String NEWLINE = System.getProperty("line.separator");

    // 底层数据结构
    private int V;
    private int E;
    private Bag<Integer>[] adj;

    // 构造函数
    public Graph_simple(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    public Graph_simple(In in) {
        this(in.readInt());

        int E = in.readInt(); // 局部变量用来存储图中总的边的数量
//        this.E = in.readInt(); // 成员变量用来记录当前构建的图中的边的数量

        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();

            addEdge(v, w);
        }

    }

    private void addEdge(int v, int w) {
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    // APIs
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public int degree(int v) {
        return adj[v].size();
    }

    public String toString() {

        StringBuilder s = new StringBuilder();
        // 打印图的基本性质
        s.append(V + " vertices, " + E + " edges " + NEWLINE); // NEWLINE没用到呀

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
        // 从命令行参数中读取输入流
        In in = new In(args[0]);
        // 从输入流中读取图
        Graph_simple G = new Graph_simple(in);
        // 打印图的结构
        StdOut.println(G.toString());
    }
}
