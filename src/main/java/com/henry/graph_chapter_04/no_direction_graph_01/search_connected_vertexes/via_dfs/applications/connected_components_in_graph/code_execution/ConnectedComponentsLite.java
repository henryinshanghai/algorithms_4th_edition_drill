package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.connected_components_in_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用在图中从指定起点开始进行DFS（递归地标记结点）的方式 来 获取到图中存在几个连通分量，以及各个连通分量中的结点
public class ConnectedComponentsLite {
    // 用于 记录节点 => 它有没有被标记过 的映射
    private boolean[] vertexToIsMarked;
    // 用于 记录节点 => 节点所属的分组/连通分量ID 的映射
    private int[] vertexToItsComponentId; // 这里的componentId 使用 componentAmount来设置
    // 记录component的数量   应用：可以用来 设置组的id（因为它是按照自然数序列递增的）
    private int componentAmount;

    // 构造器
    public ConnectedComponentsLite(Graph graph) {
        // 初始化成员变量 count的初始值为0 已经初始化完成
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        vertexToItsComponentId = new int[graph.vertexAmount()];

        // 对于 图中的每一个顶点
        for(int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 如果 当前顶点 还 没有标记过...
            if (isNotMarked(currentVertex)) {
                // 找到 当前节点所连通的所有顶点，然后成组
                markVertexAndAssignItsComponentIdViaDFS(graph, currentVertex);
                // 得到一个组 之后，把groupNum+1
                componentAmount++;
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 作用：把图G中 当前顶点v 所连通的所有顶点 成组
    private void markVertexAndAssignItsComponentIdViaDFS(Graph graph, int currentVertex) {
        // 标记 当前节点
        vertexToIsMarked[currentVertex] = true;
        // 为 当前节点 添加组名  当前节点所属的分组/子图/连通分量的ID为count - 第0组、第1组...
        vertexToItsComponentId[currentVertex] = componentAmount;

        // 对 ”当前节点邻接表中“的所有节点 递归地执行：标记 + 添加组名
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                markVertexAndAssignItsComponentIdViaDFS(graph, currentAdjacentVertex);
            }
        }
    }

    // APIs
    // 判断 顶点v 与 顶点w 是否相连通(“连通” 是 无向图中的一个概念)
    public boolean isConnectedBetween(int vertexV, int vertexW) {
        // 手段：判断 两个顶点所属的组id 是否相同
        return vertexToItsComponentId[vertexV] == vertexToItsComponentId[vertexW];
    }

    // 获取到 当前节点v 所属的分组
    public int componentIdOf(int vertexV) {
        return vertexToItsComponentId[vertexV];
    }

    // 获取 当前图中 所有子图的数量
    public int componentAmount() {
        return componentAmount;
    }


    public static void main(String[] args) {
        // #1 创建图 与 连通分量的对象
        Graph graph = new Graph(new In(args[0]));
        // 通过 类的构造方法 来 完成此任务(统计 图中的连通分量数量)
        ConnectedComponentsLite dividedGraph = new ConnectedComponentsLite(graph);

        // 使用APIs 获取图的性质👇
        // #2 图中有几个子图
        int componentAmount = dividedGraph.componentAmount();
        StdOut.println(componentAmount + " components.");

        // #3 打印 图中所有的连通分量 - 这需要 准备 邻接表数组
        // 获取到 所有连通分量的数组
        Bag<Integer>[] componentIdToComponent = getComponentsFrom(dividedGraph, graph);

        // 打印每一个连通分量中的顶点
        printVertexesInEachComponent(componentIdToComponent);
    }

    private static void printVertexesInEachComponent(Bag<Integer>[] componentIdToComponent) {
        // 对于每一个子图...
        for (int currentComponentId = 0; currentComponentId < componentIdToComponent.length; currentComponentId++) {
            Bag<Integer> currentComponent = componentIdToComponent[currentComponentId];
            // 对于子图中的每一个顶点...
            for (int currentVertex : currentComponent) {
                // 打印它
                StdOut.print(currentVertex + " ");
            }

            StdOut.println();
        }
    }

    private static Bag<Integer>[] getComponentsFrom(ConnectedComponentsLite dividedGraph, Graph graph) {
        // #1 初始化 componentId[]的大小(元素数量)
        int componentAmount = dividedGraph.componentAmount();
        Bag<Integer>[] componentIdToComponent = new Bag[componentAmount];

        // #2 实例化 components元素
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            componentIdToComponent[currentComponentId] = new Bag<Integer>();
        }

        // #3 调用API，为 components中的item 逐一赋值
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 把 节点v 添加到 它所属的分组 中👇
            // 获取 顶点的所属分组
            int componentIdOfVertex = dividedGraph.vertexToItsComponentId[currentVertex];
            // 把它 添加到 对应分组中
            componentIdToComponent[componentIdOfVertex].add(currentVertex);
        }
        return componentIdToComponent;
    }
}
// 上海 居转户
//