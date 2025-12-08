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

    /**
     * 构造器方法
     * 一般作用：用于 创建 当前类的实例对象
     * 具体作用：从 指定的起始顶点s 开始，在图中进行BFS 来 标记 所有 与s相连通的顶点
     *
     * @param graph       指定的图
     * @param startVertex 指定的起始顶点s
     */
    public ShortestPathToConnectedVertexesInGraph(Graph graph, int startVertex) {
        // 成员变量的初始化
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        terminalVertexToDepartVertex = new int[graph.vertexAmount()];
        this.startVertex = startVertex;

        // 使用BFS 来 标记图中 所有“由起始顶点可达”的顶点
        markVertexesAndRecordEdgesInSPViaBFS(graph, startVertex);
    }

    /**
     * 对于 指定的图，标记 其 与 指定起始顶点s 相连通的所有顶点，并 获取 到达所有顶点的最短路径
     * 手段：在图中，从 起始顶点s 开始 进行BFS
     * BFS的基本作用：标记 图中 与起始顶点s 相连通的 所有顶点；
     * BFS的衍生作用：解决 单点最短路径问题
     *
     * @param graph       指定的图
     * @param startVertex 起始顶点
     */
    private void markVertexesAndRecordEdgesInSPViaBFS(Graph graph, int startVertex) {
        // #1 把 “起始顶点” 设置为 “已标记”
        vertexToIsMarked[startVertex] = true;
        System.out.println("~~~ 标记起始节点" + startVertex + " ~~~");

        // 准备一个队列 - 用于 支持BFS中按序动态处理元素的需求
        // 为什么这里要使用队列呢? 因为 借助队列的特性(FIFO) + 控制元素的出入队时机，我们可以 按照预期的顺序 来 动态地逐个处理元素
        Queue<Integer> vertexesToProcess = new Queue<>();

        // #2 把 “起始顶点” 入队到 队列中 - BFS算法会 从它开始 进行”对图中顶点进行标记“的过程
        vertexesToProcess.enqueue(startVertex);
        System.out.println("!!! 把 起始节点" + startVertex + " 添加到 待处理节点的队列中 !!!");

        /* #3 进行 BFS的循环，直到 队列为空（说明 图中所有“能够被标记的顶点” 都已经 被标记了）*/
        // 当 队列中 存在有 任何元素 时，说明 还有未处理的连通顶点，则：
        while (!vertexesToProcess.isEmpty()) {
            // ① 出队“待处理的顶点”
            int currentVertex = vertexesToProcess.dequeue();
            System.out.println("@@@ 1 从 待处理节点队列中，出队节点" + currentVertex + " @@@");
            // ② 处理它
            process(currentVertex, graph, vertexesToProcess);
            System.out.println("@@@ 2 当前节点" + currentVertex + "处理完成 @@@");
        }
    }

    // 处理 当前节点
    private void process(int currentVertex,
                         Graph graph,
                         Queue<Integer> vertexesToProcess) {
        // 对于 当前顶点的 所有邻居顶点...
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 执行 固定的SOP三个步骤
            System.out.println("### 对于 当前节点" + currentVertex + "的邻居节点" + currentAdjacentVertex + "，如果 它还没有被标记，则： ###");
            recordPathWhileMarkIt(currentAdjacentVertex, currentVertex, vertexesToProcess);
        }
    }

    /**
     * 标记 路径中某条边 的到达顶点；记录 到达此到达顶点的边；把 此到达顶点 添加到 待处理的顶点集合 中；
     *
     * @param terminalVertex    指定的到达顶点
     * @param departVertex      该到达顶点的出发顶点
     * @param vertexesToProcess 待处理的顶点集合
     */
    private void recordPathWhileMarkIt(int terminalVertex,
                                       int departVertex,
                                       Queue<Integer> vertexesToProcess) {
        // 如果 到达顶点 还没有被标记，说明 算法 还没有访问过 此顶点，则：
        if (isNotMarked(terminalVertex)) {
            // #1 标记它
            vertexToIsMarked[terminalVertex] = true;
            System.out.println("$$$ 1 标记 到达节点" + terminalVertex + " $$$");
            // #2 记录 这条边 [核心步骤/BFS基本模板的额外步骤] 用于回溯出 从“起始顶点”到“到达顶点”的完整路径
            // 手段：把 数组index->item的映射关系 具体化为 到达顶点->出发顶点的映射关系
            terminalVertexToDepartVertex[terminalVertex] = departVertex;
            System.out.println("$$$ 2 记录下 搜索路径中的当前边(" + departVertex + " -> " + terminalVertex + ") $$$");
            // #3 把它 添加到 “待处理的顶点队列“中 - 算法 后继会 对它做同样的处理
            vertexesToProcess.enqueue(terminalVertex);
            System.out.println("$$$ 3 把 该到达节点" + terminalVertex + "添加到 待处理节点队列中，用于后继处理 $$$");
        }
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    /**
     * 回答 指定的顶点 是否 与起始顶点s相连通
     * 手段：如果 相连通，说明 在BFS执行完成后，该顶点必然会被标记，则 直接在BFS后检查节点 是否被标记 即可
     *
     * @param passedVertex 指定的顶点
     * @return 如果 与起始顶点s相连通，则 返回true；否则 返回false
     */
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        return vertexToIsMarked[passedVertex];
    }

    // 🐖 由于 BFS算法的特性，所以 这里得到的路径 是 最短路径
    // 方法的实现 与 DepthFirstPaths中的同名方法 完全相同

    /**
     * 获取 图中 到 从起始顶点s 到指定顶点的 最短路径
     *
     * @param endVertex 指定的顶点
     * @return 以 可迭代集合的形式 来 返回 该最短路径。
     */
    public Iterable<Integer> pathFromStartVertexTo(int endVertex) {
        // #1 在 获取路径 之前，先判断 是不是 已经存在 这条路径
        if (!doesStartVertexHasPathTo(endVertex)) {
            return null;
        }

        /* #2 从 terminalVertexToDepartVertex数组中 逐个回溯出 路径的节点，并 把节点添加到 栈容器中（方便 客户端使用for-each语法） */
        // 准备一个栈容器
        Stack<Integer> vertexSequence = new Stack<>();
        // 从 数组的最后一个元素 从后往前地 获取到 路径中的顶点
        for (int backwardsVertexCursor = endVertex;
             backwardsVertexCursor != startVertex;
             backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            // 把 获取到的 路径中的顶点 添加到 栈中
            vertexSequence.push(backwardsVertexCursor);
        }

        // #3 最后，把 起始结点 显式添加到 栈中 - 因为上面的代码 不会 把它添加到 栈中
        vertexSequence.push(startVertex);

        return vertexSequence;
    }

    /*****************************************************************
     * 使用 类的构造器 + 上述的APIs 来 得到 关于图的一些复杂性质，
     * 比如 从起始顶点s 所能够连通到的 各个顶点的 最短路径
     * @param args
     */
    public static void main(String[] args) {
        // #1 创建 图 并 指定 起始顶点
        Graph graph = new Graph(new In(args[0]));
        int startVertex = Integer.parseInt(args[1]);

        // #2 调用 构造器方法 来 完成BFS
        ShortestPathToConnectedVertexesInGraph markedGraph = new ShortestPathToConnectedVertexesInGraph(graph, startVertex);

        // #3 打印出 所有 起始顶点->可达顶点 的路径
        // 对于 图中的当前顶点...
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            StdOut.print("shortest path from " + startVertex + " to " + currentVertex + ": ");
            // 判断 该顶点 是不是 “由起始顶点可达的”
            if (markedGraph.doesStartVertexHasPathTo(currentVertex)) {
                // 如果是，说明 存在有 这样的路径，则：
                // ① 获取到 ”由起始顶点到达它“的最短路径 - 手段：pathFromStartVertexTo()方法
                // ② 然后 打印出 路径中的结点（起始顶点s单独打印）- 手段：for-each语法
                // 🐖 这里之所以可以使用 for-each的语法，是因为 pathFromStartVertexTo()方法 返回了 一个栈
                for (int currentVertexInPath : markedGraph.pathFromStartVertexTo(currentVertex)) {
                    if (currentVertexInPath == startVertex) StdOut.print(startVertex);
                    else StdOut.print("-" + currentVertexInPath);
                } // 最终的打印结果 是 从头到尾打印出 路径中的各个节点
            }

            StdOut.println();
        }
    }
}
