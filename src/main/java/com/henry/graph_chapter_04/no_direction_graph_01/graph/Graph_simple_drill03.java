package com.henry.graph_chapter_04.no_direction_graph_01.graph;

import com.henry.graph_chapter_04.no_direction_graph_01.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Graph_simple_drill03 {
    // 顶点数量
    private int V;
    // 图中当前边的数量
    private int E;
    // 邻接表 用来表示图
    private Bag<Integer>[] adj;

    // 构造器 - 初始化所有的成员变量
    // 构造器1 - 传入图的顶点数V
    public Graph_simple_drill03(int V) {
        this.V = V;
        this.E = 0;

        // 初始化邻接表数组 - 每一个顶点的邻接表 都初始化为空的bag对象
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>(); // a bit question
        }

    }

    // 构造函数2 - 传入一个输入流 来 构建图（节点+边）
    public Graph_simple_drill03(In in) {
        // 初始化顶点数量、边的数量
        this(in.readInt());
        int E = in.readInt();

        // 初始化图 - 手段：创建 文本文件所指定的节点所连接的边
        for (int i = 0; i < E; i++) {
            // 读取边的节点
            int v = in.readInt();
            int w = in.readInt();

            // 创建边
            addEdge(v, w);
        }
    }

    // 创建连接两个顶点的边
    private void addEdge(int v, int w) {
        // 添加边 v-w
        adj[v].add(w);
        // 添加边 w-v （v-w 与 w-v 其实是一条边）
        adj[w].add(v);
        // 所以 边的数量只需要增加1
        E++;
    }

    // APIs
    // 1 获取到 节点v的所有相邻顶点 返回值类型是一个Iterable对象
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    // 2 节点v的度数 aka 从节点v引出的边的数量
    public int degree(int v) {
        return adj[v].size();
    }

    // 3 图的字符串表示方法
    public String toString() {
        // 创建 SB对象
        StringBuilder s = new StringBuilder();

        // 1 打印图的基本性质 - 顶点数量、边的数量
        s.append(V + " vertices, " + E + " edges");

        // 2 打印每一个节点 -> 此节点的所有邻接节点
        for (int v = 0; v < V; v++) {
            // 2-1 当前节点
            s.append(v + ": ");
            // 2-2 当前节点的所有邻接节点
            for (int w : adj[v]) {
                s.append(w + " ");
            }
        }

        return s.toString();
    }

    public static void main(String[] args) {
        // 1 读取 文本文件
        In in = new In(args[0]);
        // 2 创建图对象
        Graph g = new Graph(in);
        // 3 打印 图对象的字符串表示
        StdOut.print(g.toString());
    }
}
