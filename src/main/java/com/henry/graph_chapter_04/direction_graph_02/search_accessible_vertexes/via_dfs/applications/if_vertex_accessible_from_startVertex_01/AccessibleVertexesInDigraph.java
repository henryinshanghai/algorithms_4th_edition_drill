package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01;

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 结论：可以使用 基础版本的DFS算法 来 得到 有向图中，从“指定的起始结点”可以到达的 所有其他结点(accessibleVertexes)。
// 手段：使用一个 名叫 vertexToIsMarked的数组 来 记录“指定的结点” 是否“被标记”；
// 具体做法：对于 当前节点的所有相邻结点(可达结点)，如果它 没有被标记，则：👇
// 调用DFS 对其进行标记（直到 可达路径中的所有结点 都被标记了，递归调用 才会返回）。
public class AccessibleVertexesInDigraph {
    private boolean[] vertexToIsMarked;

    // 在构造方法中：① 初始化成员变量； ② 完成任务；
    // 接受 单个顶点 作为 起始顶点
    public AccessibleVertexesInDigraph(Digraph digraph, int startVertex) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        // 任务：对图中 由传入顶点可达的所有其他顶点 进行标记
        markAllAccessibleVertexesStartFrom(digraph, startVertex);
    }

    // 重载的构造器方法
    // 不同点在于：重载方法 接受 一个顶点集合 来 作为多个起始顶点
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

    // 关键API - 回答 图中的指定结点 是否“由起始结点可达”?
    public boolean isAccessibleFromStartVertex(int vertexV) {
        return vertexToIsMarked[vertexV];
    }

    public static void main(String[] args) {
        // 使用 命令行参数（文件）来 创建 有向图
        Digraph digraph = new Digraph(new In(args[0]));

        // 创建&初始化 起始顶点集合
        Bag<Integer> startVertexes = new Bag<>();
        for (int currentArgSpot = 1; currentArgSpot < args.length; currentArgSpot++) {
            startVertexes.add(Integer.parseInt(args[currentArgSpot]));
        }

        // 对 图中 “由起始顶点集合可达”的所有顶点 进行标记
        AccessibleVertexesInDigraph markedDigraph = new AccessibleVertexesInDigraph(digraph, startVertexes);

        /* 打印出 所有 “由起始顶点集合可达”的顶点 */
        // 对于 有向图中的每一个顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 如果 它是 由起始顶点可达的，说明 它是我们想要的顶点，则：
            // 手段：ACID对象的API👇
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex)) {
                // 把该顶点打印出来
                StdOut.print(currentVertex + " ");
            }
        }

        StdOut.println();
    }
}
