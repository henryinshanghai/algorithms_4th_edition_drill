package com.henry.graph_chapter_04.no_direction_graph_01.graph;

import com.henry.graph_chapter_04.no_direction_graph_01.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Graph_simple_drill04 {
    private int V;
    private int E;
    private Bag<Integer>[] adj;

    public Graph_simple_drill04(int V) {
        this.V = V;
        this.E = 0;

        adj = (Bag<Integer>[]) new Bag[V]; // 初始化 并 强制转换
        for (int v = 0; v < V; v++) {
            // 单个元素的初始化
            adj[v] = new Bag<>();
        }
    }

    public Graph_simple_drill04(In in) {
        this(in.readInt());
        this.E = in.readInt();

        // 创建图中的边
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

    //APIs
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public int degree(int v) {
        return adj[v].size();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        //基本属性
        s.append(this.V + " vertices, " + E + " edges");

        // 当前节点
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
        }

        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph_simple_drill04 g = new Graph_simple_drill04(in);
        StdOut.println(g.toString());
    }
}
