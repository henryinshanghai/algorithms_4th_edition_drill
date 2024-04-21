package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.find_path_in_graph;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用在图中从指定起点开始进行DFS（标记结点）的方式 + terminalVertexToDepartVertex 来
// 获取到 起始结点 到 其所有可达结点的对应路径。
// 目标：从图中，获取到 从起始顶点到其所有可达顶点的 对应路径。
// 命令行参数：E:\development_project\algorithms_4th_edition_drill\src\main\java\com\henry\graph_chapter_04\no_direction_graph_01\path\dfs\go_through_graph\tinyCG 0
public class PathToAccessibleVertexesInGraph {
    /* 根据具体任务进行成员变量的设置 */
    // 当前变量有没有被标记过
    private boolean[] vertexToIsMarked;
    // 记录路径 - 手段：一个记录节点的数组
    private int[] terminalVertexToDepartVertex;
    // 起始顶点 - 为什么这里的起点s需要作为成员变量？   因为路径中需要这个顶点s，而且使用成员变量方便在方法中使用它
    private final int startVertex;

    // 构造方法的语法中不能够添加返回值类型
    public PathToAccessibleVertexesInGraph(Graph graph, int startVertex) {
        // 初始状态都是未标记
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        // 数组中所有位置的值初始都是0 -  路径的长度 不会超过 图中总节点的数量
        terminalVertexToDepartVertex = new int[graph.vertexAmount()];
        // 初始化起点s
        this.startVertex = startVertex;

        // 处理“单点路径”的任务
        markVertexesAndRecordVertexInPathViaDFS(graph, startVertex);
    }

    // 作用：标记节点 + 记录路径中的节点
    private void markVertexesAndRecordVertexInPathViaDFS(Graph graph, int currentVertex) {
        // 标记当前顶点
        vertexToIsMarked[currentVertex] = true;
        // 对于当前顶点的所有相邻节点
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 如果相邻节点还没有被标记过...
            if (isNotMarked(currentAdjacentVertex)) {
                // 记录 当前路径中的结点    手段：记录下"当前邻居节点"(terminalVertex) 到 “当前结点”(departVertex)的连接关系
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                // 对当前节点进行同样的操作
                markVertexesAndRecordVertexInPathViaDFS(graph, currentAdjacentVertex);
            }
        }
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    // APIs
    // 指定的顶点v与起点s是不是连通的
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        return vertexToIsMarked[passedVertex];
    }

    // 获取到从起始顶点 到 指定顶点v的路径 - 具体方式：从数组中得到 一个可迭代的数据
    public Iterable<Integer> pathFromStartVertexTo(int endVertex) {
        if (!doesStartVertexHasPathTo(endVertex)) return null;

        // 从数组中转化出 路径中的所有节点
        // 准备一个栈对象
        Stack<Integer> vertexSequence = new Stack<>();
        // 从路径的最后一个节点，往前回溯 从而得到整个路径（有序）
        // 手段：vertexToVertex[]中记录了 路径中，从 尾结点 到 ”其上一个结点“的连接关系
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            // 把当前节点添加到栈数据中
            vertexSequence.push(backwardsVertexCursor);
        }
        // 最后，把 “起始节点”（因为循环中没有添加它） 添加到栈结构中 - 从上往下就是路径中的顺序结点
        vertexSequence.push(startVertex);

        // 返回栈数据
        return vertexSequence;
    }

    public static void main(String[] args) {
        // 图与起点
        Graph graph = new Graph(new In(args[0]));
        int startVertex = Integer.parseInt(args[1]);

        // 创建 path对象
        PathToAccessibleVertexesInGraph markedGraph = new PathToAccessibleVertexesInGraph(graph, startVertex);

        // 对于图中的每一个顶点
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 从起点到 当前顶点的路径为...
            StdOut.print(startVertex + " -> " + currentVertex + ": ");

            // 判断从起始节点s 到当前节点是否连通
            if (markedGraph.doesStartVertexHasPathTo(currentVertex)) {
                // 获取到 路径的可迭代对象(aka 栈对象) - 手段：foreach语法
                for (int currentVertexInPath : markedGraph.pathFromStartVertexTo(currentVertex)) {
                    // 从栈结构中顺序获取到路径中的节点     形式: a - b - c - d
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
