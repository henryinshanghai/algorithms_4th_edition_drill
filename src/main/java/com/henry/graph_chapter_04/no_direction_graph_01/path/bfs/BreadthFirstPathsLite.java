package com.henry.graph_chapter_04.no_direction_graph_01.path.bfs;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// 广度优先搜索的应用：找到 图中，起始，到指定目的顶点结束的最短路径；
// 原理/BFS的特征：在图中搜索边时，BFS会按照距离 起始顶点的远近 来 递进地遍历顶点。
public class BreadthFirstPathsLite {
    private boolean[] vertexToIsMarked;
    private int[] terminalVertexToDepartVertex;
    private final int startVertex;

    public BreadthFirstPathsLite(Graph graph, int startVertex) {
        vertexToIsMarked = new boolean[graph.V()];
        terminalVertexToDepartVertex = new int[graph.V()];
        this.startVertex = startVertex;

        bfs(graph, startVertex);
    }

    // BFS - 先添加到数据结构中的边，先处理
    private void bfs(Graph graph, int passedVertex) {
        // 准备一个队列
        Queue<Integer> queue = new Queue<>();

        // 标记起点
        vertexToIsMarked[passedVertex] = true;

        // 入队起点
        queue.enqueue(passedVertex);

        // 进行BFS的循环，直到图中所有能标记的点都已经被标记了
        // 循环结束的条件 - 数据结构中没有节点（aka 图中所有能够处理的节点都已经处理完成）
        while (!queue.isEmpty()) {
            // 出队节点
            int currentVertex = queue.dequeue();
            // 处理当前节点的所有相邻节点
            for (int currentAdjacentVertex : graph.adj(currentVertex)) {
                if (isNotMarked(currentAdjacentVertex)) {
                    // 标记
                    vertexToIsMarked[currentAdjacentVertex] = true;
                    // 记录边
                    terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                    // 入队 - 入队之前先标记好节点
                    queue.enqueue(currentAdjacentVertex);
                }
            }
        }
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    // public APIs
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        return vertexToIsMarked[passedVertex];
    }

    // 🐖 由于BFS算法，这里得到的路径是最短路径
    // 方法的实现 与 DepthFirstPaths中的同名方法 完全相同
    public Iterable<Integer> pathFromStartVertexTo(int endVertex) {
        if(!doesStartVertexHasPathTo(endVertex)) return null;

        Stack<Integer> vertexSequence = new Stack<>();
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            vertexSequence.push(backwardsVertexCursor);
        }

        vertexSequence.push(startVertex);

        return vertexSequence;
    }

    public static void main(String[] args) {
        // 创建图 与 起点
        Graph graph = new Graph(new In(args[0]));
        int startVertex = Integer.parseInt(args[1]);

        BreadthFirstPathsLite markedGraph = new BreadthFirstPathsLite(graph, startVertex);

        for (int currentVertex = 0; currentVertex < graph.V(); currentVertex++) {
            StdOut.print("shortest path from " + startVertex + " to " + currentVertex + ": ");

            if (markedGraph.doesStartVertexHasPathTo(currentVertex)) {
                for (int currentVertexInPath : markedGraph.pathFromStartVertexTo(currentVertex)) {
                    if (currentVertexInPath == startVertex) StdOut.print(startVertex);
                    else StdOut.print("-" + currentVertexInPath);
                }
            }

            StdOut.println();
        }
    }
}
