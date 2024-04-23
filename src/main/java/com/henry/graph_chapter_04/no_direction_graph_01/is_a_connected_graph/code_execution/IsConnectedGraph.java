package com.henry.graph_chapter_04.no_direction_graph_01.is_a_connected_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用在图中从指定起点开始进行DFS（标记结点）的方式 来 判断给定的一幅图 是不是连通图
// 任务：遍历图中的每一个顶点、边，以实现某些更具体的作用
// 命令行参数：E:\development_project\algorithms_4th_edition_drill\src\main\java\com\henry\graph_chapter_04\no_direction_graph_01\dfs\tinyG 0
public class IsConnectedGraph {
    // 记录某个顶点是否可以通过起点到达 手段：使用一个size和图中顶点数量相同的boolean类型数组
    private boolean[] vertexToIsMarkedArr;
    // 记录 被标记的节点数量
    private int markedVertexAmount;


    // 构造器 - 完成成员变量的初始化
    // 在构造方法中，调用dfs()。  作用：创建graph对象时，连通的顶点就已经被标记了
    public IsConnectedGraph(Graph graph, int startVertex) {
        // 初始化 boolean数组 起始默认为false
        vertexToIsMarkedArr = new boolean[graph.vertexAmount()];
        // 调用dfs 在遍历的同时：标记顶点、检查顶点
        markVertexAndCountsViaDFS(graph, startVertex);
    }

    // 如何验证 通过这种方式能够遍历图中所有的顶点与边？
    // 深度优先搜索 只能够找到 图G中所有 与顶点s相连通的节点集合
    // 算法步骤：一边遍历，一边标记
    private void markVertexAndCountsViaDFS(Graph graph, int currentVertex) {
        // 标记当前顶点“已经被访问过”
        vertexToIsMarkedArr[currentVertex] = true;
        // 记录“被标记的节点”的数量
        markedVertexAmount++;
        // 对于 当前节点v的每一个相邻节点w
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 如果节点w还没有被标记过，就对它进行深度搜索
            if(isNotMarked(currentAdjacentVertex)) markVertexAndCountsViaDFS(graph, currentAdjacentVertex);
        }
    }

    private boolean isNotMarked(int vertex) {
        return !vertexToIsMarkedArr[vertex];
    }

    // APIs
    // 1 判断顶点w 是否从起点s可达
    public boolean doesConnectedWithStartVertex(int currentVertex) {
        // 手段：是否已经被标记
        return vertexToIsMarkedArr[currentVertex];
    }

    // 2 获取到与起点s相连通的顶点的个数
    public int vertexAmountConnectToStartVertex() {
        return markedVertexAmount;
    }

    public static void main(String[] args) {
        // 读取图
        Graph graph = new Graph(new In(args[0]));
        // 读取起点
        int startVertex = Integer.parseInt(args[1]);

        // 1 创建 DFS对象
        IsConnectedGraph markedGraph = new IsConnectedGraph(graph, startVertex);

        // 2 调用API来获取图的性质
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 打印出图中，所有可以到达（aka 与顶点连通）的顶点
            if (markedGraph.doesConnectedWithStartVertex(currentVertex)) {
                StdOut.print(currentVertex + " ");
            }
        }
        StdOut.println();

        // 如果 与起点连通的顶点的数量 不等于 图中所有顶点的数量
        if (markedGraph.vertexAmountConnectToStartVertex() != graph.vertexAmount()) {
            StdOut.print("NOT ");
        }
        StdOut.print("connected");
    }
}
