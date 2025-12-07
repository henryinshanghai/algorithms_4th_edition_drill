package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.is_a_connected_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用 在图中 从指定起点开始 进行DFS（标记结点）的方式 来 判断 给定的一幅图 是不是连通图
// 任务：遍历 图中的每一个顶点、边，以实现 某些更具体的作用
// 命令行参数：E:\development_project\algorithms_4th_edition_drill\src\main\java\com\henry\graph_chapter_04\no_direction_graph_01\dfs\tinyG 0
public class IsConnectedGraph {
    // 用于记录 顶点 -> 顶点是否被标记 的映射关系
    private boolean[] vertexToIsMarkedArr;
    // 记录 被标记的节点数量
    private int markedVertexAmount;


    // 在 构造方法 中，调用dfs()。  作用#2：创建 graph对象 时，相互连通的顶点 就已经被标记了

    /**
     * 构造器方法
     * 一般作用：用于创建 当前类的实例对象
     * 此处的具体作用：使用DFS 来 完成 对 图中所有 与起始顶点相连通的节点 的标记
     * <p>
     * 特征：#1 一般会 在构造器中 完成 成员变量的初始化；
     *
     * @param graph       指定的图
     * @param startVertex 指定的起始顶点
     */
    public IsConnectedGraph(Graph graph, int startVertex) {
        // 初始化 boolean数组    🐖 其元素的初始值默认都为false
        vertexToIsMarkedArr = new boolean[graph.vertexAmount()];
        // 执行DFS 来 完成 对 所有 与起始顶点相连通的所有顶点 的标记
        System.out.println("~~~ 1 选择 以节点" + startVertex + "作为起始节点 来 在图中进行DFS ~~~");
        markVertexAndCountsViaDFS(graph, startVertex);
        System.out.println("~~~ 2 以 节点" + startVertex + "作为起始节点的DFS 结束并返回 ~~~");
        System.out.println();
    }

    /**
     * 对 指定的图 中的 与指定顶点相连通的 所有顶点 进行标记
     * 手段：DFS算法
     * 原理：深度优先搜索 只能够找到 图G中所有 “与顶点s相连通”的顶点集合
     * 算法步骤：一边 遍历图中的节点，一边 对 与起始顶点相连通的所有顶点 进行标记
     * 疑问：如何验证 通过这种方式 能够遍历 图中所有的顶点与边？
     *
     * @param graph         指定的图
     * @param currentVertex 指定的顶点
     */
    private void markVertexAndCountsViaDFS(Graph graph, int currentVertex) {
        // #1 首先，标记 当前顶点 “已经被访问过”
        vertexToIsMarkedArr[currentVertex] = true;
        // #2 标记 当前顶点后，更新 “被标记的节点”的数量
        markedVertexAmount++;
        System.out.println("!!! 与节点" + currentVertex + "对应的成员变量都已经被更新了 !!!");

        // #3 对于 当前顶点v 的每一个相邻顶点w
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 如果 该邻居顶点 还 没有被标记过，则：
            if (isNotMarked(currentAdjacentVertex)) {
                // 在图中，以它作为起始顶点 继续递归地进行DFS
                System.out.println("@@@ 以当前邻居节点" + currentAdjacentVertex + "作为起始节点，开始执行DFS @@@");
                markVertexAndCountsViaDFS(graph, currentAdjacentVertex);
                System.out.println("@@@ 以当前邻居节点" + currentAdjacentVertex + "作为起始节点的DFS 结束并返回 @@@");
            }
        }
    }

    private boolean isNotMarked(int vertex) {
        return !vertexToIsMarkedArr[vertex];
    }

    // APIs

    /**
     * 判断 指定的顶点w 是否 与起点s相连通
     * 手段：在执行完成DFS之后，查看 顶点w 在 数组vertexToIsMarkedArr[]数组中的值
     * 原理：DFS会对图中 所有 与起点s相连通的顶点 进行标记
     *
     * @param currentVertex 指定的顶点
     * @return 如果 与起点相连通，返回true；否则 返回false
     */
    public boolean doesConnectedWithStartVertex(int currentVertex) {
        return vertexToIsMarkedArr[currentVertex];
    }

    /**
     * 获取到 “与起点s相连通”的顶点的个数
     * 手段：在执行完成DFS之后，返回 被标记的顶点的数量
     */
    public int vertexAmountConnectToStartVertex() {
        return markedVertexAmount;
    }

    /**************************************************
     * 使用 类的构造器 + 上述的APIs 来 得到关于图的一些复杂性质，
     * 比如 判断 当前图 是不是 一个连通图
     * @param args
     */
    public static void main(String[] args) {
        // 读取 图
        Graph graph = new Graph(new In(args[0]));
        // 读取 起点
        int startVertex = Integer.parseInt(args[1]);

        // #1 创建 DFS对象
        IsConnectedGraph markedGraph = new IsConnectedGraph(graph, startVertex);

        // #2 调用API 来 获取图的性质
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 打印出 图中 所有 与顶点相连通 的顶点
            if (markedGraph.doesConnectedWithStartVertex(currentVertex)) {
                StdOut.print(currentVertex + " ");
            }
        }
        StdOut.println();

        // 如果 “与起点连通的顶点”的数量 不等于 图中所有顶点的数量
        if (markedGraph.vertexAmountConnectToStartVertex() != graph.vertexAmount()) {
            StdOut.print("NOT ");
        }
        StdOut.print("connected");
    }
}
