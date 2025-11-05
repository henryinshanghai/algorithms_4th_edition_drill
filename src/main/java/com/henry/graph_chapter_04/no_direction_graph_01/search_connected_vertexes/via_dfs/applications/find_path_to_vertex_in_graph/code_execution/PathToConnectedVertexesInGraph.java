package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.find_path_to_vertex_in_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用 在图中从指定起点开始进行DFS（标记结点）的方式 + terminalVertexToDepartVertex 来
// 获取到 起始结点 到 其所有可达结点 的对应路径集合。
// 目标：从图中，获取到 从起始顶点 到 其所有可达顶点的 对应路径集合。
// 命令行参数：E:\development_project\algorithms_4th_edition_drill\src\main\java\com\henry\graph_chapter_04\no_direction_graph_01\path\dfs\go_through_graph\tinyCG 0
public class PathToConnectedVertexesInGraph {
    /* 根据 具体任务 进行 成员变量的设置 */
    // 当前节点 有没有被标记过
    private boolean[] vertexToIsMarked;
    // 记录路径 - 手段：一个记录 节点间指向关系 的数组
    private int[] terminalVertexToDepartVertex;
    // 起始顶点 - 为什么这里的 起点s 需要作为 成员变量？   因为路径中 需要 这个顶点s，而且 使用成员变量 方便在方法中直接使用它
    private final int startVertex;

    // 🐖 构造方法的语法中 不能够添加 返回值类型
    public PathToConnectedVertexesInGraph(Graph graph, int startVertex) {
        /* #1 初始化 需要的成员变量 */
        // 初始状态 都是 ”未标记“
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        // 数组中 所有位置上的值 初始都是0 -  路径的长度 不会超过 图中总节点的数量
        terminalVertexToDepartVertex = new int[graph.vertexAmount()];
        // 初始化起点s
        this.startVertex = startVertex;

        /* #2 处理“单点路径”的任务 */
        markVertexesAndRecordVertexInPathViaDFS(graph, startVertex);
    }

    // 作用：标记节点 + 记录路径中的顶点间的指向关系
    private void markVertexesAndRecordVertexInPathViaDFS(Graph graph, int currentVertex) {
        // #1 标记当前顶点
        vertexToIsMarked[currentVertex] = true;

        // 对于 当前顶点的 所有相邻顶点
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // #2 如果 该相邻节点 还没有被标记过...
            if (isNotMarked(currentAdjacentVertex)) {
                // 记录下 "当前邻居节点"(terminalVertex) 与 “当前结点”(departVertex)的连接/指向关系
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;

                // 对 该邻居节点 递归地执行 同样的操作
                markVertexesAndRecordVertexInPathViaDFS(graph, currentAdjacentVertex);
            }
        }
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    /****** APIs ******/
    /**
     * 指定的顶点v 与起点s 是不是相连通的
     */
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        return vertexToIsMarked[passedVertex];
    }

    /**
     * 获取到从 起始顶点s 到 指定顶点v的路径 - 具体方式：从数组中得到 一个可迭代的数据
     *
     * @param endVertex 指定的路径终点
     * @return 以 可迭代方式 返回 从起始顶点s开始 到 该指定的终点 的路径
     */
    public Iterable<Integer> pathFromStartVertexTo(int endVertex) {
        /* 先判断 是不是存在这样一条路径 */
        // 如果不存在，则：
        if (!doesStartVertexHasPathTo(endVertex)) {
            // 返回 null，表示 不存在这样的路径
            return null;
        }

        /* 从数组中 转化出 路径中的所有节点 */
        // 准备一个栈对象
        Stack<Integer> vertexSequence = new Stack<>();
        // 从 路径的最后一个节点 开始，往前回溯 从而得到 整个路径（有序）
        // 手段：vertexToVertex[]中 记录了 路径中 从 到达结点 到 ”其出发结点“的连接关系
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            // 把 当前节点 添加到 栈数据中
            vertexSequence.push(backwardsVertexCursor);
        }
        // 最后，把 “起始节点s”（因为 在循环中 没有添加它） 添加到 栈结构中 - 从上往下 就是 路径中的顺序结点
        vertexSequence.push(startVertex);

        // 返回栈数据
        return vertexSequence;
    }

    public static void main(String[] args) {
        // 获取 传入的图 与 起点
        Graph graph = new Graph(new In(args[0]));
        int startVertex = Integer.parseInt(args[1]);

        // 使用它们 来 创建 path对象
        PathToConnectedVertexesInGraph markedGraph = new PathToConnectedVertexesInGraph(graph, startVertex);

        // 对于图中的每一个顶点
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 从 起点s 到 当前顶点的路径为...
            StdOut.print(startVertex + " -> " + currentVertex + ": ");

            // 如果 从 起始节点s 到 当前节点 相连通，则：
            if (markedGraph.doesStartVertexHasPathTo(currentVertex)) {
                // 获取到 路径的可迭代对象(aka 栈对象)，并对之进行迭代 - 手段：foreach语法
                for (int currentVertexInPath : markedGraph.pathFromStartVertexTo(currentVertex)) {
                    // 从栈结构中 顺序获取到 路径中的节点     形式: a - b - c - d
                    if (currentVertexInPath == startVertex) {
                        StdOut.print(currentVertexInPath);
                    } else {
                        StdOut.print("-" + currentVertexInPath);
                    }
                }
            }

            StdOut.println();
        }
    }
}
