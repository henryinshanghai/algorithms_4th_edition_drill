package com.henry.graph_chapter_04.no_direction_graph_01.path.bfs;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BreadthFirstPath_drill01 {

    // 节点是否已经被标记/处理
    private boolean[] marked;
    // 用于回溯出路径的数组
    private int[] edgeTo;
    // 指定的起点顶点
    private final int s;

    public BreadthFirstPath_drill01(Graph G, int s) {
        // 初始化成员变量
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;

        // 执行任务
        bfs(G, s);
    }

    // 为了一般性 这里把顶点的名字叫做v
    // BFS - 先添加到数据结构中的边，先处理
    private void bfs(Graph G, int v) {
        // 准备一个队列
        Queue<Integer> queue = new Queue<>();

        // 标记起点
        marked[v] = true;

        // 入队起点
        queue.enqueue(v);

        // 进行BFS的循环，直到图中所有能标记的点都已经被标记了
        // 循环结束的条件 - 数据结构中没有节点（aka 图中所有能够处理的节点都已经处理完成）
        while (!queue.isEmpty()) {
            // 出队节点
            int currV =  queue.dequeue();
            // 处理当前节点的所有相邻节点
            for (int w : G.adj(currV)) {
                // 处理当前的相邻节点w
                if (!marked[w]) {
                    // 标记
                    marked[w] = true;
                    // 记录边
                    edgeTo[w] = currV;
                    // 入队 - 入队之前先标记好节点
                    queue.enqueue(w);
                }
            }
        }

    }

    // public APIs
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // pathTo
    public Iterable<Integer> shortestPathTo(int v) {
        if(!hasPathTo(v)) return null;

        Stack<Integer> pathIfExist = new Stack<>();
        // 从edgeTo[]数组中回溯出路径中的所有节点
        for (int x = v; x != s; x = edgeTo[x]) {
            pathIfExist.push(x);
        }
        pathIfExist.push(s);

        return pathIfExist;
    }

    public static void main(String[] args) {
        // 创建图 与 起点
        Graph graph = new Graph(new In(args[0]));
        int s = Integer.parseInt(args[1]);

        // 创建对象
        BreadthFirstPath_drill01 bfs = new BreadthFirstPath_drill01(graph, s);

        // 打印 以s作为起点，到它所连通的所有顶点的所有最短路径
        for (int v = 0; v < graph.V(); v++) {
            StdOut.print("shortest path from " + s + " to " + v + " is: ");

            if (bfs.hasPathTo(v)) {
                for (int w : bfs.shortestPathTo(v)) {
                    if (w == s) StdOut.print(s);
                    else StdOut.print("-" + w);
                }
            }

            StdOut.println();
        }
    }
}
