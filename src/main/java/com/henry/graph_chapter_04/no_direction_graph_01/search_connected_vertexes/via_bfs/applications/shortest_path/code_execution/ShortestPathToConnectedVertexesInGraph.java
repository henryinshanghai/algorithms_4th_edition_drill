package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_bfs.applications.shortest_path.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用 在图中 从指定起点开始 进行BFS（广度优先搜索）的方式 来 得到图中 从 “指定起始结点” 到 “其所有可达结点” 所对应的“最短路径”
// 广度优先搜索的应用：找到 图中，“由 起始顶点开始 到 指定目的顶点结束”的最短路径；
// 原理/BFS的特征：在 图中搜索边 时，BFS会按照 距离起始顶点的远近 来 递进地 遍历顶点。
// 核心步骤：#1 标记结点； #2 入队结点；
public class ShortestPathToConnectedVertexesInGraph {
    private boolean[] vertexToIsMarked; // 顶点 -> 顶点是否已经被标记 的映射关系
    private int[] terminalVertexToDepartVertex; // 结束顶点 -> 出发顶点 的映射关系（用于描述一条边的术语）
    private final int startVertex; // 起始顶点（用于描述一条路径的术语）

    // 作用：① 初始化成员变量； ② 执行具体的任务；
    public ShortestPathToConnectedVertexesInGraph(Graph graph, int startVertex) {
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        terminalVertexToDepartVertex = new int[graph.vertexAmount()];
        this.startVertex = startVertex;

        // 使用BFS 来 标记图中 所有“由起始顶点可达”的顶点
        markVertexesAndRecordEdgesInSPViaBFS(graph, startVertex);
    }

    // BFS - 用于标记图中 所有“由起始顶点可达的“顶点
    // 🐖 BFS的主要作用就是“标记可达结点”，其他的作用 都是 由这个主要作用 所衍生出来的 所以方法名叫做 xxxViaBFS()
    private void markVertexesAndRecordEdgesInSPViaBFS(Graph graph, int startVertex) {
        // #1 把 “起始顶点” 设置为 “已标记”
        vertexToIsMarked[startVertex] = true;
        // 准备一个队列 - 用于记录 算法所需要处理的 所有顶点
        Queue<Integer> vertexesToProcess = new Queue<>();
        // #2 把 “起始顶点” 入队到 队列中 - BFS算法会 从它开始 进行”对图中顶点进行标记“的过程
        vertexesToProcess.enqueue(startVertex);

        // #3 进行 BFS的循环，直到 图中所有“能够被标记的顶点” 都已经 被标记了
        while (!vertexesToProcess.isEmpty()) { // 循环结束的条件 - 队列中 没有任何元素（aka 图中 所有能够处理的节点 都已经处理完成）
            // #3-1 出队“待处理的顶点”
            int currentVertex = vertexesToProcess.dequeue();
            // #3-2 处理它
            process(currentVertex, graph, vertexesToProcess);
        }
    }

    // 处理步骤
    private void process(int currentVertex, Graph graph, Queue<Integer> vertexesToProcess) {
        // 对于 当前顶点的 所有邻居顶点...
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 执行固定的SOP三个步骤
            recordPathWhileMarkIt(currentAdjacentVertex, currentVertex, vertexesToProcess);
        }
    }

    private void recordPathWhileMarkIt(int terminalVertex, int departVertex, Queue<Integer> vertexesToProcess) {
        // 如果 它(当前邻居顶点) 还没有被标记，说明 算法 还没有访问过 此顶点，则：
        if (isNotMarked(terminalVertex)) {
            // #1 标记它 [基本步骤] 用于记录 顶点 是不是 已经被处理过了
            vertexToIsMarked[terminalVertex] = true;
            // #2 记录 结束顶点->出发顶点的边 [核心步骤/BFS基本模板的额外步骤] 用于回溯出 从“起始顶点”到“终止顶点”的完整路径
            terminalVertexToDepartVertex[terminalVertex] = departVertex;
            // #3 把它 添加到 “待处理的顶点队列“中 - 算法 后继会 对它做同样的处理
            vertexesToProcess.enqueue(terminalVertex);
        }
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    // public APIs
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        return vertexToIsMarked[passedVertex];
    }

    // 🐖 由于 BFS算法的特性，所以 这里得到的路径 是 最短路径
    // 方法的实现 与 DepthFirstPaths中的同名方法 完全相同
    public Iterable<Integer> pathFromStartVertexTo(int endVertex) {
        // #1 获取路径 之前，先判断 是不是 已经存在 这条路径
        if(!doesStartVertexHasPathTo(endVertex)) return null;

        // 我们使用 terminalVertexToDepartVertex 这个数组 来 存储路径，它记录的是 “结束顶点->出发顶点”的映射关系。而不是 路径中的顶点
        // #2 所以 需要想办法 让客户端能够 直接获取到 路径中的顶点👇
        // 手段：使用一个栈 来 有序地记录 路径中的所有顶点
        Stack<Integer> vertexSequence = new Stack<>();
        // 从 数组的最后一个元素 从后往前地 获取到 路径中的顶点
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex;
             backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            // 把 获取到的 路径中的顶点 添加到 栈中
            vertexSequence.push(backwardsVertexCursor);
        }

        // #3 把 起始结点 显式添加到 栈中 - 因为上面的代码 不会 把它添加到 栈中
        vertexSequence.push(startVertex);

        return vertexSequence;
    }

    public static void main(String[] args) {
        // #1 创建 图 并 指定 起始顶点
        Graph graph = new Graph(new In(args[0]));
        int startVertex = Integer.parseInt(args[1]);

        // #2 使用 BFS的方式 来 标记 图中所有“由起始顶点可达的”所有顶点，并 记录下 所有“起始顶点->可达结点(作为终止顶点)”的路径
        ShortestPathToConnectedVertexesInGraph markedGraph = new ShortestPathToConnectedVertexesInGraph(graph, startVertex);

        // #3 打印出 所有 起始顶点->可达顶点 的路径
        // 对于 图中的当前顶点...
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            StdOut.print("shortest path from " + startVertex + " to " + currentVertex + ": ");
            // #3-1 判断它 是不是 “由起始顶点可达的”
            if (markedGraph.doesStartVertexHasPathTo(currentVertex)) {
                // #3-2 如果是，说明???，则 获取到 ”由起始顶点到达它“的路径
                for (int currentVertexInPath : markedGraph.pathFromStartVertexTo(currentVertex)) {
                    // 然后 打印出 路径中的结点
                    if (currentVertexInPath == startVertex) StdOut.print(startVertex);
                    else StdOut.print("-" + currentVertexInPath);
                }
            }

            StdOut.println();
        }
    }
}
