package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.execution;

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 结论：可以使用 基础版本的DFS算法 来 得到 有向图中，从“指定的起始结点”可以到达的 所有其他结点(accessibleVertexes)。
// 手段：使用一个 名叫 vertexToIsMarked的数组 来 记录“指定的结点” 是否“被标记”；
// 具体做法：对于 当前节点的所有相邻结点(可达结点)，如果它 没有被标记，则：👇
// 调用DFS 对其进行标记（直到 可达路径中的所有结点 都被标记了，递归调用 才会返回）。
public class AccessibleVertexesInDigraph {
    // 记录 顶点 -> 顶点是否被标记 的映射关系
    private boolean[] vertexToIsMarked;

    // 在构造方法中：① 初始化成员变量； ② 完成任务；
    // 接受 单个顶点 作为 起始顶点

    /**
     * 构造器方法
     * 一般作用：用于 创建 当前类的实例对象
     * 此处的具体作用：用于 在 指定的有向图 中，从 指定起点s 开始 执行BFS 以 标记 所有 由其可达的顶点
     * 特征：一般 在构造器中 完成 对成员变量的初始化
     *
     * @param digraph     指定的有向图
     * @param startVertex 指定的起始顶点s
     */
    public AccessibleVertexesInDigraph(Digraph digraph, int startVertex) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        // 任务：对图中 由传入顶点可达的 所有其他顶点 进行标记
        markAllAccessibleVertexesStartFrom(digraph, startVertex);
    }

    // 重载的构造器方法
    // 不同点在于：重载方法 接受 一个顶点集合 来 作为 起始顶点集合
    public AccessibleVertexesInDigraph(Digraph digraph, Iterable<Integer> startVertexes) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];

        for (Integer currentStartVertex : startVertexes) {
            if (isNotMarked(currentStartVertex)) {
                markAllAccessibleVertexesStartFrom(digraph, currentStartVertex);
            }
        }
    }

    private boolean isNotMarked(Integer currentStartVertex) {
        return !vertexToIsMarked[currentStartVertex];
    }

    private void markAllAccessibleVertexesStartFrom(Digraph digraph, int currentVertex) {
        // 把 当前顶点 标记为true
        vertexToIsMarked[currentVertex] = true;

        // 对于  当前顶点的所有邻居顶点...
        for (Integer currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            // 如果 该邻居顶点 还没有被标记过，说明 它还没有被访问过..
            if (isNotMarked(currentAdjacentVertex)) {
                // 则：继续递归地 对其进行标记
                markAllAccessibleVertexesStartFrom(digraph, currentAdjacentVertex);
            }
        }
    }


    /**
     * 指定的顶点 是否 由起始顶点可达？
     * 原理：在BFS执行结束后，所有 由起始顶点可达的顶点 都会被标记；
     *
     * @param vertexV 指定的顶点
     * @return 如果 它由起始顶点可达，则 返回true；否则 返回false。
     */
    public boolean isAccessibleFromStartVertex(int vertexV) {
        return vertexToIsMarked[vertexV];
    }

    /*****************************************************
     * 使用 类的构造器 + 上述的APIs 来 得到 关于有向图的一些复杂性质，
     * 比如 在图中 由起始顶点s 可达的 各个顶点
     * @param args
     */
    public static void main(String[] args) {
        // 使用 命令行参数（文件）来 创建输入流，再进一步创建 有向图
        Digraph digraph = new Digraph(new In(args[0]));

        // 创建 起始顶点的集合 并 使用 命令行参数 来 初始化 此集合
        Bag<Integer> startVertexes = new Bag<>();
        for (int currentArgSpot = 1; currentArgSpot < args.length; currentArgSpot++) {
            startVertexes.add(Integer.parseInt(args[currentArgSpot]));
        }

        // 在图中，以指定顶点作为起点 进行BFS
        AccessibleVertexesInDigraph markedDigraph = new AccessibleVertexesInDigraph(digraph, startVertexes);

        /* 打印出 所有 “由起始顶点集合可达”的顶点 */
        // 对于 有向图中的每一个顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 如果 它是 由起始顶点可达的，说明 它是我们想要的顶点，则：
            // 手段：AVID对象的API👇
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex)) {
                // 把该顶点打印出来
                StdOut.print(currentVertex + " ");
            }
        }

        StdOut.println();
    }
}
